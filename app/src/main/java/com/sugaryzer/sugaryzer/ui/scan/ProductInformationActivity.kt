package com.sugaryzer.sugaryzer.ui.scan

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sugaryzer.sugaryzer.R
import com.sugaryzer.sugaryzer.ViewModelFactory
import com.sugaryzer.sugaryzer.data.ResultState
import com.sugaryzer.sugaryzer.databinding.ActivityConfirmBinding
import com.sugaryzer.sugaryzer.databinding.ActivityProductInformationBinding
import com.sugaryzer.sugaryzer.ui.main.MainActivity

class ProductInformationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductInformationBinding
    private val viewModel by viewModels<ScanViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var selectedSugarAmount = 0.0
        var selectedButton = false
        val productId = intent.getIntExtra(EXTRA_PRODUCT_ID,0)
        val productCode = intent.getStringExtra(EXTRA_PRODUCT_CODE)
        val productName = intent.getStringExtra(EXTRA_PRODUCT_NAME)
        val productSugar = intent.getIntExtra(EXTRA_PRODUCT_SUGAR,0)
        val quarterButton = findViewById<Button>(R.id.btnQuarter)
        val halfButton = findViewById<Button>(R.id.btnHalf)
        val threeQuarterButton = findViewById<Button>(R.id.btnThreeQuarter)
        val fullButton = findViewById<Button>(R.id.btnFull)

        binding.tvNamaProduk.text = productName
        binding.tvKandunganGula.text = "${productSugar} gram"

        quarterButton.setOnClickListener {
            selectedButton = true
            val selectedSugarAmount = 0.25 * productSugar
            Toast.makeText(this, "Anda memilih 1/4 Botol", Toast.LENGTH_SHORT).show()
        }

        halfButton.setOnClickListener {
            selectedButton = true
            selectedSugarAmount = 0.5 * productSugar
            Toast.makeText(this, "Anda memilih 1/2 Botol", Toast.LENGTH_SHORT).show()
        }

        threeQuarterButton.setOnClickListener {
            selectedButton = true
            selectedSugarAmount = 0.75 * productSugar
            Toast.makeText(this, "Anda memilih 3/4 Botol", Toast.LENGTH_SHORT).show()
        }

        fullButton.setOnClickListener {
            selectedButton = true
            selectedSugarAmount = productSugar.toDouble()
            Toast.makeText(this, "Anda memilih 1 Botol", Toast.LENGTH_SHORT).show()
        }


        binding.btnBack.setOnClickListener{
            val intent = Intent(this, ScanActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        binding.btnTambahkanKeKonsumsi.setOnClickListener{
            if (selectedButton == false) {
                Toast.makeText(this, "Silakan pilih jumlah konsumsi terlebih dahulu", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.uploadScan(productCode?:"", selectedSugarAmount)
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setView(R.layout.loading)
                val dialog: AlertDialog = builder.create()
                viewModel.scannedResult.observe(this){ response->
                    when (response) {
                        is ResultState.Loading -> dialog.show()
                        is ResultState.Error -> {
                            dialog.dismiss()
                            AlertDialog.Builder(this).apply {
                                setTitle("Gagal menyimpan history")
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
                                setTitle(getString(R.string.success_save_history))
                                setMessage(getString(R.string.success_save_history))
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
        }
    }

    companion object {
        const val EXTRA_PRODUCT_ID = "extra_product_id"
        const val EXTRA_PRODUCT_CODE = "extra_product_code"
        const val EXTRA_PRODUCT_NAME = "extra_product_name"
        const val EXTRA_PRODUCT_SUGAR = "extra_product_sugar"
    }
}