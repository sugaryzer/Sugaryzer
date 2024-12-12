package com.sugaryzer.sugaryzer.ui.profile

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.sugaryzer.sugaryzer.R
import com.sugaryzer.sugaryzer.ViewModelFactory
import com.sugaryzer.sugaryzer.data.ResultState
import com.sugaryzer.sugaryzer.databinding.FragmentProfileBinding
import com.sugaryzer.sugaryzer.ui.scan.ProductInformationActivity
import com.sugaryzer.sugaryzer.ui.signin.SignInActivity
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =  FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editProfile: LinearLayout = view.findViewById(R.id.editAccount)
        val materialSwitch = binding.switchMode
        materialSwitch.isChecked = false
        materialSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveThemeSetting(isChecked)
        }

        binding.logoutButton.setOnClickListener{
            lifecycleScope.launch {
                AlertDialog.Builder(requireContext()).apply {
                    setTitle(getString(R.string.logout_confirmation))
                    setMessage(getString(R.string.logout_confirmation))
                    setPositiveButton(getString(R.string.dialog_button_continue)) { _, _ ->
                        viewModel.logout()
                        val intent = Intent(requireContext(), SignInActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                    create()
                    show()
                }
            }
        }

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.loading)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        viewModel.getProfile.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ResultState.Loading -> dialog.show()
                is ResultState.Error -> {
                    dialog.dismiss()
                    AlertDialog.Builder(requireContext()).apply {
                        setTitle("Gagal memuat profile")
                        setMessage(response.message)
                        create()
                        show()
                    }
                }

                is ResultState.Success -> {
                    dialog.dismiss()
                    binding.profileName.text = response.data[0].name
                    editProfile.setOnClickListener{
                        val intent = Intent(requireContext(), EditProfileActivity::class.java).apply {
                            putExtra(EditProfileActivity.EXTRA_PROFILE_NAME, response.data[0].name)
                            putExtra(EditProfileActivity.EXTRA_PROFILE_AGE, response.data[0].age)
                            putExtra(EditProfileActivity.EXTRA_PROFILE_HEIGHT, response.data[0].height)
                            putExtra(EditProfileActivity.EXTRA_PROFILE_WEIGHT, response.data[0].weight)
                            putExtra(EditProfileActivity.EXTRA_PROFILE_WEIGHT, response.data[0].weight)
                        }
                        startActivity(intent)
                    }
                }

                else -> dialog.dismiss()
            }
            dialog.dismiss()
        }

        viewModel.getThemeSetting().observe(viewLifecycleOwner){isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                materialSwitch.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                materialSwitch.isChecked = false
            }
        }
    }
}