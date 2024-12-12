package com.sugaryzer.sugaryzer.ui.detailnews

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.sugaryzer.sugaryzer.databinding.ActivityDetailNewsBinding
import com.sugaryzer.sugaryzer.ui.detailhistory.DetailHistoryActivity.Companion.EXTRA_PRODUCT_DATE
import com.sugaryzer.sugaryzer.ui.detailhistory.DetailHistoryActivity.Companion.EXTRA_PRODUCT_NAME
import com.sugaryzer.sugaryzer.ui.detailhistory.DetailHistoryActivity.Companion.EXTRA_PRODUCT_SUGAR
import com.sugaryzer.sugaryzer.ui.news.NewsFragment

class DetailNewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val newsName = intent.getStringExtra(EXTRA_NEWS_NAME)
        val newsDescription = intent.getStringExtra(EXTRA_NEWS_DESCRIPTION)
        val newsImage = intent.getStringExtra(EXTRA_NEWS_IMAGE)

        binding.tvTitle.text = newsName
        binding.tvDescription.text = newsDescription
        Glide.with(binding.root.context)
            .load(newsImage)
            .into(binding.imgNews)

        binding.btnBack.setOnClickListener{
            finish()
        }
    }

    companion object {
        const val EXTRA_NEWS_NAME = "extra_news_name"
        const val EXTRA_NEWS_DESCRIPTION = "extra_news_description"
        const val EXTRA_NEWS_IMAGE = "extra_news_image"
    }
}