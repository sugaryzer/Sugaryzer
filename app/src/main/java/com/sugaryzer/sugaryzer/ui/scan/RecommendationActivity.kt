package com.sugaryzer.sugaryzer.ui.scan

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.sugaryzer.sugaryzer.R
import com.sugaryzer.sugaryzer.ViewModelFactory
import com.sugaryzer.sugaryzer.data.ResultState
import com.sugaryzer.sugaryzer.databinding.ActivityProductInformationBinding
import com.sugaryzer.sugaryzer.databinding.ActivityRecommendationBinding
import com.sugaryzer.sugaryzer.ui.adapter.NewsListAdapter
import com.sugaryzer.sugaryzer.ui.adapter.RecommendationAdapter

class RecommendationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecommendationBinding
    private val viewModel by viewModels<ScanViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var recommendationAdapter: RecommendationAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecommendationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recommendationAdapter = RecommendationAdapter(this)
        binding.rvRecommendations.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recommendationAdapter
        }

        val productId = intent.getIntExtra(EXTRA_PRODUCT_ID,0)
        val productBarcode = intent.getStringExtra(EXTRA_PRODUCT_CODE)
        val productName = intent.getStringExtra(EXTRA_PRODUCT_NAME)
        val productSugar = intent.getIntExtra(EXTRA_PRODUCT_SUGAR,0)
        val personalSugar = intent.getIntExtra(EXTRA_PERSONAL_SUGAR,0)

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.loading)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val sugarMessage = "$personalSugar/30"
        val sugarLeft = (30 - personalSugar).coerceAtLeast(0)
        val sugarLeftMessage = "You have $sugarLeft gram left"

        binding.tvDetailSugar1.text = sugarMessage
        binding.tvDetailSugar3.text = sugarLeftMessage

        viewModel.getRecommendation(productBarcode.toString()).observe(this) { response ->
            when (response) {
                is ResultState.Loading -> dialog.show()
                is ResultState.Error -> {
                    dialog.dismiss()
                    AlertDialog.Builder(this).apply {
                        setTitle("Gagal memuat berita")
                        setMessage(response.message)
                        create()
                        show()
                    }
                }

                is ResultState.Success -> {
                    dialog.dismiss()
                    recommendationAdapter.submitList(response.data)
                }

                else -> dialog.dismiss()
            }
        }

        binding.btnLanjutRekap.setOnClickListener{
            val intent = Intent(this, ProductInformationActivity::class.java).apply {
                putExtra(ProductInformationActivity.EXTRA_PRODUCT_ID, productId)
                putExtra(ProductInformationActivity.EXTRA_PRODUCT_CODE, productBarcode)
                putExtra(ProductInformationActivity.EXTRA_PRODUCT_NAME, productName)
                putExtra(ProductInformationActivity.EXTRA_PRODUCT_SUGAR, productSugar)
            }
            startActivity(intent)
        }

        binding.btnKembaliKeKamera.setOnClickListener {
            val intent = Intent(this, ScanActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
    companion object {
        const val EXTRA_PRODUCT_ID = "extra_product_id"
        const val EXTRA_PRODUCT_CODE = "extra_product_code"
        const val EXTRA_PRODUCT_NAME = "extra_product_name"
        const val EXTRA_PRODUCT_SUGAR = "extra_product_sugar"
        const val EXTRA_PERSONAL_SUGAR = "extra_personal_sugar"
    }
}