package com.sugaryzer.sugaryzer.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.sugaryzer.sugaryzer.ViewModelFactory
import com.sugaryzer.sugaryzer.databinding.ActivityAuthBinding
import com.sugaryzer.sugaryzer.ui.auth.signin.SignInFragment
import com.sugaryzer.sugaryzer.ui.main.MainActivity

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private val viewModel by viewModels<AuthViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this){ token ->
            if(token == null){
                supportFragmentManager.beginTransaction()
                    .replace(binding.fragmentContainerView.id,SignInFragment())
                    .commit()
            } else {
                val main = Intent(this, MainActivity::class.java)
                startActivity(main)
            }
        }
    }
}