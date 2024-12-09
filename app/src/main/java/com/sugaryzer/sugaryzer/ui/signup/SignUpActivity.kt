package com.sugaryzer.sugaryzer.ui.signup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sugaryzer.sugaryzer.databinding.ActivitySignUpBinding
import com.sugaryzer.sugaryzer.ui.signin.SignInActivity

class SignUpActivity : AppCompatActivity() {

    private var _binding : ActivitySignUpBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignUp.setOnClickListener{
            val signUpActivity = Intent(this, SignInActivity::class.java)
            startActivity(signUpActivity)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}