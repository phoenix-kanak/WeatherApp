package com.example.weatherapp

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import com.example.weatherapp.databinding.ActivityMainBinding
import com.google.android.material.search.SearchBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

//c37c9ecb324d86a061af4708fe92a939
class MainActivity : AppCompatActivity() {

    lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        fetchWeatherData("Srinagar")
        searchCity()
    }

    private fun searchCity() {
        binding.searchBar.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    fetchWeatherData(query)
                }
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                return true
            }

        })
    }

    private fun fetchWeatherData(cityName:String) {

        val retrofit=Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)

        val response=retrofit.getWeatherData(cityName,"c37c9ecb324d86a061af4708fe92a939","metric")
        response.enqueue(object :Callback<WeatherApp>{
            override fun onResponse(call: Call<WeatherApp>, response: Response<WeatherApp>) {
                val responseBody=response.body()
                if(response.isSuccessful && responseBody!=null){
                    val temperature= responseBody.main.temp
                    val humidity=responseBody.main.humidity
                    val windspeed=responseBody.wind.speed
                    val sunrise=responseBody.sys.sunrise
                    val sunset=responseBody.sys.sunset
                    val sealevel=responseBody.main.pressure
                    val conditions= responseBody.weather.firstOrNull()?.main?:"unknown"
                    val mintemp=responseBody.main.temp_min
                    val maxtemp=responseBody.main.temp_max
                    binding.temp.text="$temperature °C"
                    binding.valHumidity.text="$humidity%"
                    binding.valWind.text="$windspeed m/s"
                    val sunrise_time: Int =sunrise*1000
                    val sunset_time:Int=sunset*1000
                    binding.valSunrise.text=formatTime(sunrise_time)
                    binding.valSunset.text=formatTime(sunset_time)
                    binding.valSea.text="$sealevel hPa"
//                    binding.minTemp.text="Min: $mintemp °C"
//                    binding.maxTemp.text="Max: $maxtemp °C"
                    binding.valConditions.text="$conditions"
                    binding.SUNNY.text="$conditions"
                    binding.day.text=dayName(System.currentTimeMillis())
                    binding.date.text=dateName()
                    binding.textWithLocation.text="$cityName"
                    changeBackgroundImage(conditions)
                }
            }

            override fun onFailure(call: Call<WeatherApp>, t: Throwable) {
                Toast.makeText(this@MainActivity,"Something went wrong",Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun changeBackgroundImage(condition:String) {
        when(condition){
            "Clear" , "Clear Sky" , "Sunny" ->{
                binding.root.setBackgroundResource(R.drawable.sunny_background)
              //  binding.lottieAnimationView.setAnimation(R.raw.sun_anim)
            }
            "Rain" , "Light Rain" , "Drizzle" , "Heavy Rain" , "Showers" ->{
                binding.root.setBackgroundResource(R.drawable.rain_background)
             //   binding.lottieAnimationView.setAnimation(R.raw.rain_anim)
            }
            "Clouds" , "Partial Clouds" , "Overcast" ->{
                binding.root.setBackgroundResource(R.drawable.colud_background)
             //   binding.lottieAnimationView.setAnimation(R.raw.clouds_anim)
            }
            "Snow" , "Light Snow" ,"Partial Snow" , "Blizzard" ->{
                binding.root.setBackgroundResource(R.drawable.snow_background)
             //   binding.lottieAnimationView.setAnimation(R.raw.snow_anim)
            }
            "Smoke" , "Mist" , "Fume" , "Foggy" , "Haze"->{
                binding.root.setBackgroundResource(R.drawable.smoke_background)
             //   binding.lottieAnimationView.setAnimation(R.raw.smog_anim)
            }
            else -> {
                binding.root.setBackgroundResource(R.drawable.sunny_background)
             //   binding.lottieAnimationView.setAnimation(R.raw.sun_anim)
            }
        }
     //   binding.lottieAnimationView.playAnimation()
    }

    private fun dateName(): CharSequence? {
        val day=SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return day.format(Date())
    }

    private fun dayName(time:Long):String {
        val date=SimpleDateFormat("EEEE", Locale.getDefault())
        return date.format(Date())
    }
    private fun formatTime(date: Int): String {
        val sdf = SimpleDateFormat("HH:mm")
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(date)
    }
}