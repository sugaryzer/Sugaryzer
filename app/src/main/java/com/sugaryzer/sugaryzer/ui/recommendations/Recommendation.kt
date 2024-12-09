package com.sugaryzer.sugaryzer.ui.recommendations

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sugaryzer.sugaryzer.data.response.Product
import com.sugaryzer.sugaryzer.databinding.FragmentRecommendationBinding
import com.sugaryzer.sugaryzer.ui.adapter.RecommendationAdapter

class Recommendation : Fragment() {

    private var _binding: FragmentRecommendationBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerview: RecyclerView
    private lateinit var product: List<Product>
    private lateinit var recommendationAdapter: RecommendationAdapter

    private val viewModel: RecommendationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecommendationBinding.inflate(inflater, container, false)
        return binding.root

        val recommendationViewModel =
            ViewModelProvider(this).get(RecommendationViewModel::class.java)
        recyclerview = binding.rvRecommendations
        recyclerview.layoutManager = LinearLayoutManager(context)

        product = listOf(
            Product("Teh pucuk harum", "10:00 AM", "5gr"),
            Product("Kratingdaeng", "11:00 AM", "35gr"),
        )

        recommendationAdapter = RecommendationAdapter(product)
        recyclerview.adapter = recommendationAdapter
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}