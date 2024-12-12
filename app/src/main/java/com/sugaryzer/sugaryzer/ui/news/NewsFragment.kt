package com.sugaryzer.sugaryzer.ui.news

import android.app.Dialog
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sugaryzer.sugaryzer.R
import com.sugaryzer.sugaryzer.ViewModelFactory
import com.sugaryzer.sugaryzer.data.ResultState
import com.sugaryzer.sugaryzer.databinding.FragmentNewsBinding
import com.sugaryzer.sugaryzer.ui.adapter.NewsListAdapter

class NewsFragment : Fragment() {
    private val viewModel by viewModels<NewsViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }
    private val binding get() = checkNotNull(_binding) { "Binding should not be accessed before onCreateView or after onDestroyView." }
    private var _binding: FragmentNewsBinding? = null
    private lateinit var newsAdapter: NewsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =  FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newsAdapter = NewsListAdapter(requireContext())
        binding.rvNews.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
        }

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.loading)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        viewModel.getNews.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ResultState.Loading -> dialog.show()
                is ResultState.Error -> {
                    dialog.dismiss()
                    AlertDialog.Builder(requireContext()).apply {
                        setTitle("Gagal memuat berita")
                        setMessage(response.message)
                        create()
                        show()
                    }
                }

                is ResultState.Success -> {
                    dialog.dismiss()
                    newsAdapter.submitList(response.data)
                }

                else -> dialog.dismiss()
            }
        }
    }
}