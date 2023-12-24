package com.example.weatherapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.weatherapp.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySplashScreenBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)

        val icon=binding.loadingAnim

            icon.alpha=0f;
            icon.animate().setDuration(5000).alpha(1f).withEndAction{
                val i= Intent(this,MainActivity::class.java)
                startActivity(i)
                finish()
            }
    }
}