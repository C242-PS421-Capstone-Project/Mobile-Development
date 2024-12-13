package com.dicoding.freshfish.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.freshfish.repository.HistoryRepository
import com.dicoding.freshfish.response.DataItem

class HistoryViewModel(private val repository: HistoryRepository) : ViewModel() {
    val historyData: LiveData<List<DataItem>> = repository.historyData
    val isLoading: LiveData<Boolean> = repository.isLoading

    fun loadHistory() {
        repository.fetchHistory()
    }
}

