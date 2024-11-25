package com.sugaryzer.sugaryzer.ui.login

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sugaryzer.sugaryzer.R
import com.sugaryzer.sugaryzer.databinding.ActivityAuthBinding
import com.sugaryzer.sugaryzer.ui.login.signin.SignInFragment

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainerView.id,SignInFragment())
                .commit()
        }
    }
}