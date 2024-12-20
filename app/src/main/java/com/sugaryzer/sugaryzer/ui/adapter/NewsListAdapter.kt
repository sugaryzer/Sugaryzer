package com.sugaryzer.sugaryzer.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sugaryzer.sugaryzer.data.response.DataItemNews
import com.sugaryzer.sugaryzer.databinding.ItemNewsBinding
import com.sugaryzer.sugaryzer.ui.detailnews.DetailNewsActivity

class NewsListAdapter (private val context: Context) : ListAdapter<DataItemNews, NewsListAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val newsList = getItem(position)
        holder.bind(newsList)
    }

    class MyViewHolder(val binding: ItemNewsBinding, val context: Context): RecyclerView.ViewHolder(binding.root) {
        fun bind(news: DataItemNews) {
            binding.newsName.text = news.title
            binding.newsDescription.text = news.description
            Glide.with(binding.root.context)
                .load(news.image)
                .into(binding.newsPhoto)

            binding.root.setOnClickListener {
                val intent = Intent(context, DetailNewsActivity::class.java).apply {
                    putExtra(DetailNewsActivity.EXTRA_NEWS_NAME, news.title)
                    putExtra(DetailNewsActivity.EXTRA_NEWS_DESCRIPTION, news.description)
                    putExtra(DetailNewsActivity.EXTRA_NEWS_IMAGE, news.image)
                }
                context.startActivity(intent)
            }
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItemNews>() {
            override fun areItemsTheSame(oldItem: DataItemNews, newItem: DataItemNews): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: DataItemNews, newItem: DataItemNews): Boolean {
                return oldItem == newItem
            }
        }
    }
}