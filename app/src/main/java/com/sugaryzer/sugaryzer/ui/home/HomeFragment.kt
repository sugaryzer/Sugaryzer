package com.sugaryzer.sugaryzer.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sugaryzer.sugaryzer.data.response.Product
import com.sugaryzer.sugaryzer.databinding.FragmentHomeBinding
import com.sugaryzer.sugaryzer.ui.adapter.HomeAdapter
import com.sugaryzer.sugaryzer.ui.scan.ScanActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var productList: List<Product> // Assuming Product is a data class


    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recyclerView = binding.rvFragmentHome
        recyclerView.layoutManager = LinearLayoutManager(context)

        productList = listOf(
            Product("Teh pucuk harum", "10:00 AM", "5gr"),
            Product("Kratingdaeng", "11:00 AM", "35gr",),
        )

        homeAdapter = HomeAdapter(productList)
        recyclerView.adapter = homeAdapter

        val currentDate = Date()
        val formatter = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())
        val formattedDate = formatter.format(currentDate)


        binding.tvDate.text = formattedDate

        binding.addRecord.setOnClickListener {
            val addStoryActivity = Intent(requireContext(), ScanActivity::class.java)
            startActivity(addStoryActivity)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}