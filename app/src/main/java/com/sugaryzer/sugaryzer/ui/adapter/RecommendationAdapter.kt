package com.sugaryzer.sugaryzer.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sugaryzer.sugaryzer.data.response.Product
import com.sugaryzer.sugaryzer.databinding.ItemHistoryBinding

class RecommendationAdapter(private val product: List<Product>) : RecyclerView.Adapter<RecommendationAdapter.ProductViewHolder>() {

    class ProductViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(product : Product){
            binding.tvProductName.text = product.productName
            binding.tvTime.text = product.time
            binding.tvDetailSugar.text = product.detailSugar
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding =  ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return product.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product  = product[position]
        holder.bind(product)
    }
}