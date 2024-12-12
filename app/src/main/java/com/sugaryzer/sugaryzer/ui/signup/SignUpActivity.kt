package com.sugaryzer.sugaryzer.ui.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.sugaryzer.sugaryzer.R
import com.sugaryzer.sugaryzer.ViewModelFactory
import com.sugaryzer.sugaryzer.data.ResultState
import com.sugaryzer.sugaryzer.databinding.ActivitySignUpBinding
import com.sugaryzer.sugaryzer.ui.signin.SignInActivity

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    private val signUpViewModel: SignUpViewModel by viewModels<SignUpViewModel> {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()

        binding.signIn.setOnClickListener {
            val signInActivity = Intent(this, SignInActivity::class.java)
            startActivity(signInActivity)
            finish()
        }
    }

    private fun playAnimation() {
        val views = listOf(
            binding.edtInputEmail,
            binding.edtInputName,
            binding.edtInputPassword,
            binding.edtInputWeight,
            binding.edtInputHeight,
            binding.edtInputAge,
            binding.signUp,
            binding.signIn,
            binding.btnSignUp
        )

        AnimatorSet().apply {
            playSequentially(views.map { ObjectAnimator.ofFloat(it, View.ALPHA, 1f).setDuration(100) })
            start()
        }
    }

    private fun setupAction() {
        binding.edtInputEmail.addTextChangedListener(createTextWatcher {
            val email = it.toString().trim()
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() to getString(R.string.error_invalid_email)
        })

        binding.edtInputPassword.addTextChangedListener(createTextWatcher {
            val password = it.toString()
            (password.length < 8) to getString(R.string.error_password_length)
        })

        binding.btnSignUp.setOnClickListener {
            val email = binding.edtInputEmail.text.toString().trim()
            val name = binding.edtInputName.text.toString().trim()
            val password = binding.edtInputPassword.text.toString().trim()
            val weight = binding.edtInputWeight.text.toString().toIntOrNull()
            val height = binding.edtInputHeight.text.toString().toIntOrNull()
            val age = binding.edtInputAge.text.toString().toIntOrNull()

            if (validateInput(email, name, password, weight, height, age)) {
                signUpViewModel.signUp(
                    email = email,
                    name = name,
                    password = password,
                    weight = weight!!,
                    height = height!!,
                    age = age!!
                )
                showMessageDialog()
            }
        }
    }

    private fun createTextWatcher(validator: (CharSequence?) -> Pair<Boolean, String>): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val (hasError, errorMessage) = validator(s)
                binding.apply {
                    when (s.hashCode()) {
                        edtInputEmail.text.hashCode() -> edtInputEmail.error = if (hasError) errorMessage else null
                        edtInputPassword.text.hashCode() -> edtInputPassword.error = if (hasError) errorMessage else null
                    }
                }
            }
        }
    }

    private fun showMessageDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.loading)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val signInActivity = Intent(this, SignInActivity::class.java)
        signUpViewModel.responseResult.observe(this) { response ->
            when (response) {
                is ResultState.Loading -> dialog.show()
                is ResultState.Error -> {
                    dialog.dismiss()
                    AlertDialog.Builder(this).apply {
                        setTitle(getString(R.string.failed_register))
                        setMessage(response.message)
                        create()
                        show()
                    }
                }

                is ResultState.Success -> {
                    dialog.dismiss()
                    AlertDialog.Builder(this).apply {
                        setTitle("Berhasil mendaftar")
                        setMessage(response.data.message)
                        setPositiveButton(getString(R.string.dialog_button_continue)) { _, _ ->
                            startActivity(signInActivity)
                            finish()
                        }
                        create()
                        show()
                    }
                }

                else -> dialog.dismiss()
            }
        }
    }

    private fun validateInput(email: String, name: String, password: String, weight: Int?, height: Int?, age: Int?): Boolean {
        return when {
            email.isEmpty() -> {
                binding.edtInputEmail.error = getString(R.string.error_email_empty)
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.edtInputEmail.error = getString(R.string.error_invalid_email)
                false
            }
            name.isEmpty() -> {
                binding.edtInputName.error = getString(R.string.error_name_empty)
                false
            }
            password.isEmpty() -> {
                binding.edtInputPassword.error = getString(R.string.error_password_empty)
                false
            }
            weight == null || weight <= 0 -> {
                binding.edtInputWeight.error = getString(R.string.error_invalid_weight)
                false
            }
            height == null || height <= 0 -> {
                binding.edtInputHeight.error = getString(R.string.error_invalid_height)
                false
            }
            age == null || age <= 0 -> {
                binding.edtInputAge.error = getString(R.string.error_invalid_age)
                false
            }
            else -> {
                clearErrors()
                true
            }
        }
    }

    private fun clearErrors() {
        binding.edtInputEmail.error = null
        binding.edtInputName.error = null
        binding.edtInputPassword.error = null
        binding.edtInputWeight.error = null
        binding.edtInputHeight.error = null
        binding.edtInputAge.error = null
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
