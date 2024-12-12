package com.sugaryzer.sugaryzer.ui.home

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sugaryzer.sugaryzer.R
import com.sugaryzer.sugaryzer.ViewModelFactory
import com.sugaryzer.sugaryzer.data.ResultState
import com.sugaryzer.sugaryzer.databinding.FragmentHomeBinding
import com.sugaryzer.sugaryzer.ui.scan.ScanActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment() {
    private val binding get() = checkNotNull(_binding) {""}
    private var _binding: FragmentHomeBinding? = null
    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentDate = Date()
        val formatter = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())
        val formattedDate = formatter.format(currentDate)
        binding.tvDate.text = formattedDate

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.loading)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        viewModel.getConsume.observe(viewLifecycleOwner){ response->
            when (response) {
                is ResultState.Loading -> dialog.show()
                is ResultState.Error -> {
                    dialog.dismiss()
                    AlertDialog.Builder(requireContext()).apply {
                        setTitle("Gagal memuat data")
                        setMessage(response.message)
                        create()
                        show()
                    }
                }

                is ResultState.Success -> {
                    dialog.dismiss()
                    val sugarConsume = response.data.totalConsume
                    val sugarLeft = (30 - sugarConsume).coerceAtLeast(0)
                    val sugarPercentage = ((sugarConsume.toFloat() / 30) * 100).toInt()
                    val sugarConsumeMessage = "${sugarConsume}/30 Gram"
                    val sugarLeftMessage = "You have $sugarLeft gr left"
                    val sugarPercentageMessage = "$sugarPercentage%"

                    binding.tvName.text = response.data.userProfile.name
                    binding.tvGramSugar.text = sugarConsumeMessage
                    binding.tvSugarLeft.text = sugarLeftMessage
                    binding.tvPersentage.text = sugarPercentageMessage

                    viewModel.getAnalytics(sugarConsume).observe(viewLifecycleOwner){response->
                        when (response) {
                            is ResultState.Loading -> dialog.show()
                            is ResultState.Error -> {
                                dialog.dismiss()
                                AlertDialog.Builder(requireContext()).apply {
                                    setTitle("Gagal memuat data")
                                    setMessage(response.message)
                                    create()
                                    show()
                                }
                            }

                            is ResultState.Success -> {
                                dialog.dismiss()
                                val category = response.data.category

                                binding.tvSugarIntake.text = category

                                val (color, categoryText, adviceText) = when (category) {
                                    "merah" -> Triple(
                                        ContextCompat.getColor(requireContext(), R.color.red),
                                        "Red",
                                        "Konsumsi gula harian berlebihan. Sebaiknya kurangi konsumsi gula untuk mencegah risiko kesehatan."
                                    )
                                    "kuning" -> Triple(
                                        ContextCompat.getColor(requireContext(), R.color.yellow),
                                        "Yellow",
                                        "Konsumsi gula harian normal. Tetap perhatikan pola makan untuk menjaga keseimbangan gula darah."
                                    )
                                    "hijau" -> Triple(
                                        ContextCompat.getColor(requireContext(), R.color.green),
                                        "Green",
                                        "Konsumsi gula harian di bawah normal. Sangat baik, teruskan kebiasaan konsumsi gula yang sehat."
                                    )
                                    else -> Triple(
                                        ContextCompat.getColor(requireContext(), R.color.gray),
                                        "Not Detected",
                                        "Data konsumsi gula tidak terdeteksi. Pastikan untuk memindai produk dengan benar."
                                    )
                                }

                                binding.tvSugarIntake.text = categoryText
                                binding.advice.text = adviceText
                                binding.tvSugarIntake.setTextColor(color)
                                binding.tvPersentage.setTextColor(color)
                            }

                            else -> dialog.dismiss()
                        }
                    }

                    viewModel.getProfile.observe(viewLifecycleOwner){response->
                        when (response) {
                            is ResultState.Loading -> dialog.show()
                            is ResultState.Error -> {
                                dialog.dismiss()
                                AlertDialog.Builder(requireContext()).apply {
                                    setTitle("Gagal memuat data")
                                    setMessage(response.message)
                                    create()
                                    show()
                                }
                            }

                            is ResultState.Success -> {
                                dialog.dismiss()
                                val height = response.data[0].height
                                val weight = response.data[0].weight
                                val heightInMeters = height / 100.0

                                val bmi = weight / (heightInMeters * heightInMeters)

                                val statusText = when {
                                    bmi < 18.5 -> "You are underweight"
                                    bmi in 18.5..24.9 -> "You have normal weight"
                                    bmi in 25.0..29.9 -> "You are overweight"
                                    else -> "You have obesity"
                                }
                                binding.tvUserWeight.text = "$weight kg"
                                binding.tvWeightStatus.text = statusText
                            }

                            else -> dialog.dismiss()
                        }
                    }
                }

                else -> dialog.dismiss()
            }
        }
        binding.addRecord.setOnClickListener {
            val addStoryActivity = Intent(requireContext(), ScanActivity::class.java)
            startActivity(addStoryActivity)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}