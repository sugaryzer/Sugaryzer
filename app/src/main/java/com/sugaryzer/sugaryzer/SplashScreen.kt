package com.sugaryzer.sugaryzer

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager.LayoutParams.*
import androidx.appcompat.app.AppCompatActivity

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            FLAG_FULLSCREEN,
            FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splash_screen)
        android.os.Handler().postDelayed(
        {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        },
            SPLASH_TIMER.toLong()
        )
    }

    companion object{
        private const val SPLASH_TIMER = 2000
    }
}