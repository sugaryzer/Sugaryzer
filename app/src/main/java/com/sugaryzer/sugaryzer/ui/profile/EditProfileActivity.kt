package com.sugaryzer.sugaryzer.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.sugaryzer.sugaryzer.R
import com.sugaryzer.sugaryzer.ViewModelFactory
import com.sugaryzer.sugaryzer.data.ResultState
import com.sugaryzer.sugaryzer.databinding.ActivityEditProfileBinding
import com.sugaryzer.sugaryzer.ui.main.MainActivity
import com.sugaryzer.sugaryzer.ui.signin.SignInActivity
import com.sugaryzer.sugaryzer.ui.signin.SignInViewModel

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    private val profileViewModel: ProfileViewModel by viewModels<ProfileViewModel>{
        factory
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra(EXTRA_PROFILE_NAME)
        val age = intent.getIntExtra(EXTRA_PROFILE_AGE, 0) // Default 0 jika null
        val height = intent.getIntExtra(EXTRA_PROFILE_HEIGHT, 0)
        val weight = intent.getIntExtra(EXTRA_PROFILE_WEIGHT, 0)

        binding.nameEditText.setText(name)
        binding.ageEditText.setText(age.toString())
        binding.heightEditText.setText(height.toString())
        binding.weightEditText.setText(weight.toString())

        binding.btnBack.setOnClickListener{
            finish()
        }

        binding.submitButton.setOnClickListener{
            val name = binding.nameEditText.text.toString().trim()
            val weight = binding.weightEditText.text.toString().trim().toIntOrNull()
            val age = binding.ageEditText.text.toString().trim().toIntOrNull()
            val height = binding.heightEditText.text.toString().trim().toIntOrNull()

            profileViewModel.editProfile(
                name = name,
                weight = weight!!,
                age = age!!,
                height = height!!
            )
            showMessageDialog()
        }
    }

    private fun showMessageDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setView(R.layout.loading)
        val dialog: AlertDialog = builder.create()

        profileViewModel.editProfileResult.observe(this) {
                response ->
            when (response) {
                is ResultState.Loading -> dialog.show()
                is ResultState.Error -> {
                    dialog.dismiss()
                    AlertDialog.Builder(this).apply {
                        setTitle(getString(R.string.failed_update_data))
                        setMessage(response.message)
                        create()
                        show()
                    }
                }

                is ResultState.Success -> {
                    dialog.dismiss()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    AlertDialog.Builder(this).apply {
                        setTitle(getString(R.string.success_update_profile))
                        setMessage(getString(R.string.success_update_profile))
                        setPositiveButton(getString(R.string.dialog_button_continue)) { _, _ ->
                            startActivity(intent)
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

    companion object {
        const val EXTRA_PROFILE_NAME = "extra_profile_name"
        const val EXTRA_PROFILE_AGE = "extra_profile_age"
        const val EXTRA_PROFILE_HEIGHT = "extra_profile_height"
        const val EXTRA_PROFILE_WEIGHT = "extra_profile_weight"
    }
}