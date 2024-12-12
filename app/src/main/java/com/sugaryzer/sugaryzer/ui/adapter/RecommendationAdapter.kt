package com.sugaryzer.sugaryzer.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sugaryzer.sugaryzer.R
import com.sugaryzer.sugaryzer.data.response.DataItemRecommendation
import com.sugaryzer.sugaryzer.databinding.ItemRecommendationBinding
import com.sugaryzer.sugaryzer.ui.scan.ProductInformationActivity
import com.sugaryzer.sugaryzer.ui.signin.SignInActivity

class RecommendationAdapter(private val context: Context) : ListAdapter<DataItemRecommendation, RecommendationAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding = ItemRecommendationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val newsList = getItem(position)
        holder.bind(newsList)
    }

    class MyViewHolder(val binding: ItemRecommendationBinding, val context: Context): RecyclerView.ViewHolder(binding.root) {
        fun bind(recommendation: DataItemRecommendation) {
            val sugar = recommendation.altProduct.amountOfSugar
            val sugarText = "$sugar gr"
            binding.recommendationTitle.text = recommendation.altProduct.name
            binding.tvDetailSugar.text = sugarText
            Glide.with(binding.root.context)
                .load(recommendation.altProduct.image)
                .into(binding.recommendationPhoto)

            binding.root.setOnClickListener {
                AlertDialog.Builder(context).apply {
                    setTitle("Are you sure you want to choose this recommended product?")
                    setMessage("Are you sure you want to choose this recommended product?")
                    setPositiveButton("Continue") { _, _ ->
                        val intent = Intent(context, ProductInformationActivity::class.java).apply {
                            putExtra(ProductInformationActivity.EXTRA_PRODUCT_ID, recommendation.altProduct.id)
                            putExtra(ProductInformationActivity.EXTRA_PRODUCT_CODE, recommendation.altProduct.code)
                            putExtra(ProductInformationActivity.EXTRA_PRODUCT_NAME, recommendation.altProduct.name)
                            putExtra(ProductInformationActivity.EXTRA_PRODUCT_SUGAR, recommendation.altProduct.amountOfSugar)
                        }
                        context.startActivity(intent)
                    }
                    create()
                    show()
                }
            }
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItemRecommendation>() {
            override fun areItemsTheSame(oldItem: DataItemRecommendation, newItem: DataItemRecommendation): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: DataItemRecommendation, newItem: DataItemRecommendation): Boolean {
                return oldItem == newItem
            }
        }
    }
}