package com.sugaryzer.sugaryzer.ui.detailhistory

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sugaryzer.sugaryzer.databinding.ActivityDetailHistoryBinding
import com.sugaryzer.sugaryzer.ui.scan.RecommendationActivity
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DetailHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailHistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val productId = intent.getIntExtra(EXTRA_PRODUCT_ID,0)
        val productBarcode = intent.getStringExtra(EXTRA_PRODUCT_CODE)
        val productName = intent.getStringExtra(EXTRA_PRODUCT_NAME)
        val productSugar = intent.getIntExtra(EXTRA_PRODUCT_SUGAR,0)
        val productDate = intent.getStringExtra(EXTRA_PRODUCT_DATE)

        val productSugarMessage = "$productSugar gram"

        binding.tvNamaProduk.text = productName
        binding.tvKandunganGula.text = productSugarMessage
        binding.tvTanggalScan2.text = formatTime(productDate.toString())

        binding.btnBack.setOnClickListener {
            finish()
        }

    }

    private fun formatTime(inputDate: String): String {
        return try {
            val inputFormat = DateTimeFormatter.ISO_OFFSET_DATE_TIME
            val utcDateTime = LocalDateTime.parse(inputDate, inputFormat)

            val utcZonedDateTime = utcDateTime.atZone(ZoneId.of("UTC"))
            val wibZonedDateTime = utcZonedDateTime.withZoneSameInstant(ZoneId.of("Asia/Jakarta"))

            val outputFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
            wibZonedDateTime.format(outputFormat)

        } catch (e: Exception) {
            ""
        }
    }
    companion object {
        const val EXTRA_PRODUCT_ID = "extra_product_id"
        const val EXTRA_PRODUCT_CODE = "extra_product_code"
        const val EXTRA_PRODUCT_NAME = "extra_product_name"
        const val EXTRA_PRODUCT_SUGAR = "extra_product_sugar"
        const val EXTRA_PRODUCT_DATE = "extra_product_date"
    }
}