package com.sugaryzer.sugaryzer.ui.detailhistory

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sugaryzer.sugaryzer.databinding.ActivityDetailHistoryBinding

class DetailHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailHistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}