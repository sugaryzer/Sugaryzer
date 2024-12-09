package com.sugaryzer.sugaryzer.ui.signin

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.sugaryzer.sugaryzer.R
import com.sugaryzer.sugaryzer.ViewModelFactory
import com.sugaryzer.sugaryzer.data.ResultState
import com.sugaryzer.sugaryzer.databinding.ActivitySignInBinding
import com.sugaryzer.sugaryzer.ui.main.MainActivity
import com.sugaryzer.sugaryzer.ui.signup.SignUpActivity

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    private val loginViewModel: SignInViewModel by viewModels<SignInViewModel>{
        factory
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()
        binding.signUp.setOnClickListener{
            val signUpActivity = Intent(this, SignUpActivity::class.java)
            startActivity(signUpActivity)
            finish()
        }

    }

    private fun playAnimation() {

        val views = listOf(
            binding.emailEditTextLayout,
            binding.passwordEditTextLayout,
            binding.signIn,
            binding.signUp,
            binding.btnSignIn
        )

        AnimatorSet().apply {
            playSequentially(views.map { ObjectAnimator.ofFloat(it, View.ALPHA, 1f).setDuration(100) })
            start()
        }
    }

    private fun setupAction() {
        binding.inputEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val email = s.toString().trim()
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    binding.emailEditTextLayout.error = getString(R.string.error_invalid_email)
                } else {
                    binding.emailEditTextLayout.error = null
                }
            }
        })

        binding.inputPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()
                if (password.length < 8) {
                    binding.passwordEditTextLayout.error = getString(R.string.error_password_length)
                } else {
                    binding.passwordEditTextLayout.error = null
                }
            }
        })
        binding.btnSignIn.setOnClickListener {
            val email = binding.inputEmail.text.toString().trim()
            val password = binding.inputPassword.text.toString().trim()

            if (validateInput(email, password)) {
                loginViewModel.signin(
                    email = email,
                    password = password
                )
                showMessageDialog()
            }
        }
    }



    private fun showMessageDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setView(R.layout.loading)
        val dialog: AlertDialog = builder.create()

        loginViewModel.responseResult.observe(this) {
                response ->
            when (response) {
                is ResultState.Loading -> dialog.show()
                is ResultState.Error -> {
                    dialog.dismiss()
                    AlertDialog.Builder(this).apply {
                        setTitle(getString(R.string.failed_login))
                        setMessage(response.message)
                        create()
                        show()
                    }
                }

                is ResultState.Success -> {
                    dialog.dismiss()
                    val mainActivity = Intent(this, MainActivity::class.java)
                    startActivity(mainActivity)
                    finish()
                }

                else -> dialog.dismiss()
            }
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        return when {
            email.isEmpty() -> {
                binding.emailEditTextLayout.error = getString(R.string.error_email_empty)
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.emailEditTextLayout.error = getString(R.string.error_invalid_email)
                false
            }
            password.isEmpty() -> {
                binding.passwordEditTextLayout.error = getString(R.string.error_password_empty)
                false
            }
            else -> {
                clearErrors()
                true
            }
        }
    }

    private fun clearErrors() {
        binding.emailEditTextLayout.error = null
        binding.passwordEditTextLayout.error = null
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}