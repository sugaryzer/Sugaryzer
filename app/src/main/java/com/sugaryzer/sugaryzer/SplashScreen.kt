package com.sugaryzer.sugaryzer

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.sugaryzer.sugaryzer.databinding.ActivitySplashScreenBinding
import com.sugaryzer.sugaryzer.ui.main.MainActivity
import com.sugaryzer.sugaryzer.ui.main.MainViewModel
import com.sugaryzer.sugaryzer.ui.signin.SignInActivity

class SplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.splashTitle.alpha = 0f

        ObjectAnimator.ofFloat(binding.splashTitle, "alpha", 0f, 1f).apply {
            duration = 2000
            interpolator = DecelerateInterpolator()
            start()
        }

        animateSplashImage()

        navigateToNextActivity()
    }

    private fun animateSplashImage() {
        ObjectAnimator.ofFloat(binding.splashimage, "alpha", 0f, 1f).apply {
            duration = 1500
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
        ObjectAnimator.ofFloat(binding.splashTitle, "alpha", 0f, 1f).apply {
            duration = 2000
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
    }

    private fun navigateToNextActivity() {
        val splashDuration = 3000L // 3 detik
        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.getSession().observe(this) { token ->
                val nextActivity = if (token.isNullOrEmpty()) {
                    SignInActivity::class.java
                } else {
                    MainActivity::class.java
                }
                startActivity(Intent(this, nextActivity))
                finish()
            }
        }, splashDuration)
    }

}