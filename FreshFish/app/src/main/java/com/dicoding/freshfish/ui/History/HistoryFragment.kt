package com.dicoding.freshfish.ui.History

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.freshfish.adapter.HistoryAdapter
import com.dicoding.freshfish.databinding.FragmentHistoryBinding
import com.dicoding.freshfish.repository.HistoryRepository
import com.dicoding.freshfish.viewmodel.HistoryViewModel
import com.dicoding.freshfish.viewmodel.HistoryViewModelFactory

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)

        val repository = HistoryRepository()
        val factory = HistoryViewModelFactory(repository)
        historyViewModel = ViewModelProvider(this, factory).get(HistoryViewModel::class.java)

        setupRecyclerView()
        observeData()
        historyViewModel.loadHistory()

        return binding.root
    }

    private fun setupRecyclerView() {
        historyAdapter = HistoryAdapter()
        binding.recyclerViewHistory.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = historyAdapter
        }
    }

    private fun observeData() {
        historyViewModel.historyData.observe(viewLifecycleOwner) { historyList ->
            historyAdapter.setHistoryList(historyList)
        }

        historyViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.lottieAnimationViewHistory.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.recyclerViewHistory.visibility = if (isLoading) View.GONE else View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

