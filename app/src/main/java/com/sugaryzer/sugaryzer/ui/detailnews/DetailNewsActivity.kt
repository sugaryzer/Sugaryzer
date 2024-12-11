package com.sugaryzer.sugaryzer.ui.detailnews

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sugaryzer.sugaryzer.databinding.ActivityDetailNewsBinding
import com.sugaryzer.sugaryzer.ui.news.NewsFragment

class DetailNewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener{
            val intent = Intent(this, NewsFragment::class.java)
            startActivity(intent)
            finish()
        }
    }
}