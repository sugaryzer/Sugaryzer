package com.sugaryzer.sugaryzer.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sugaryzer.sugaryzer.R
import com.sugaryzer.sugaryzer.data.response.Product

class HomeAdapter(private val productList: List<Product>) : RecyclerView.Adapter<HomeAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvProductName: TextView = itemView.findViewById(R.id.tvProductName)
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        val tvDetailSugar: TextView = itemView.findViewById(R.id.tvDetailSugar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.tvProductName.text = product.productName
        holder.tvTime.text = product.time
        holder.tvDetailSugar.text = product.detailSugar
    }

    override fun getItemCount(): Int {
        return productList.size
    }
}
