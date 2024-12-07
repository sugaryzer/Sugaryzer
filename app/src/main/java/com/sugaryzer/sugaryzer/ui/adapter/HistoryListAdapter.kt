package com.sugaryzer.sugaryzer.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sugaryzer.sugaryzer.data.response.DataItemHistory
import com.sugaryzer.sugaryzer.databinding.ItemHistoryBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
class HistoryListAdapter(private val context: Context) :
    ListAdapter<DataItemHistory, HistoryListAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val historyItem = getItem(position)
        holder.bind(historyItem)
    }

    class MyViewHolder(private val binding: ItemHistoryBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(history: DataItemHistory) {
            binding.tvProductName.text = history.product?.name ?: "Unknown Product"
            binding.tvDetailSugar.text = "${history.product?.amountOfSugar ?: 0} Gram"
            binding.tvTime.text = formatTime(history.createdAt ?: "")
        }

        private fun formatTime(inputDate: String): String {
            return try {
                val inputFormat = DateTimeFormatter.ISO_OFFSET_DATE_TIME
                val outputFormat = DateTimeFormatter.ofPattern("HH:mm")
                LocalDateTime.parse(inputDate, inputFormat).format(outputFormat)
            } catch (e: Exception) {
                ""
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItemHistory>() {
            override fun areItemsTheSame(oldItem: DataItemHistory, newItem: DataItemHistory): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: DataItemHistory, newItem: DataItemHistory): Boolean {
                return oldItem == newItem
            }
        }
    }
}