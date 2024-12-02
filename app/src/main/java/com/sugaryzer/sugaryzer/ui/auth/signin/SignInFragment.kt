package com.sugaryzer.sugaryzer.ui.auth.signin

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.sugaryzer.sugaryzer.R
import com.sugaryzer.sugaryzer.ViewModelFactory
import com.sugaryzer.sugaryzer.data.ResultState
import com.sugaryzer.sugaryzer.databinding.FragmentSignInBinding
import com.sugaryzer.sugaryzer.ui.auth.signup.SignUpFragment
import com.sugaryzer.sugaryzer.ui.main.MainActivity

class SignInFragment : Fragment() {

    private val signInViewModel: SignInViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext()) // Panggilan aman
    }

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
        binding.tvSignup.setOnClickListener{
            val nextFragment = SignUpFragment() // Replace with your target fragment class
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, nextFragment)
                .addToBackStack(null)
                .commit()
        }
    }


    private fun setupAction() {
//        binding.inputEmail.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//
//            override fun afterTextChanged(s: Editable?) {
//                val email = s.toString().trim()
//                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//                    binding.emailEditTextLayout.error = getString(R.string.error_invalid_email)
//                } else {
//                    binding.emailEditTextLayout.error = null
//                }
//            }
//        })
//
//        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//
//            override fun afterTextChanged(s: Editable?) {
//                val password = s.toString()
//                if (password.length < 8) {
//                    binding.passwordEditTextLayout.error = getString(R.string.error_password_length)
//                } else {
//                    binding.passwordEditTextLayout.error = null
//                }
//            }
//        })
        binding.btnSignIn.setOnClickListener {
            val email = binding.inputEmail.text.toString().trim()
            val password = binding.inputPassword.text.toString().trim()
            signInViewModel.signin(
                email = email,
                password = password
            )
            showMessageDialog()

//            if (validateInput(email, password)) {
//                signInViewModel.signin(
//                    email = email,
//                    password = password
//                )
//                showMessageDialog()
//            }
        }
    }

//    private fun validateInput(email: String, password: String): Boolean {
//        return when {
//            email.isEmpty() -> {
//                binding.emailEditTextLayout.error = getString(R.string.error_email_empty)
//                false
//            }
//            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
//                binding.emailEditTextLayout.error = getString(R.string.error_invalid_email)
//                false
//            }
//            password.isEmpty() -> {
//                binding.passwordEditTextLayout.error = getString(R.string.error_password_empty)
//                false
//            }
//            else -> {
//                clearErrors()
//                true
//            }
//        }
//    }
//
//    private fun clearErrors() {
//        binding.emailEditTextLayout.error = null
//        binding.passwordEditTextLayout.error = null
//    }

    private fun showMessageDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setView(R.layout.loading)
        val dialog: AlertDialog = builder.create()

        signInViewModel.responseResult.observe(viewLifecycleOwner) {
                response ->
            when (response) {
                is ResultState.Loading -> dialog.show()
                is ResultState.Error -> {
                    dialog.dismiss()
                    AlertDialog.Builder(requireContext()).apply {
                        setTitle(getString(R.string.failed_login))
                        setMessage(response.message)
                        create()
                        show()
                    }
                }

                is ResultState.Success -> {
                    dialog.dismiss()
                    val main = Intent(requireContext(), MainActivity::class.java)
                    startActivity(main)
                }

                else -> dialog.dismiss()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}




