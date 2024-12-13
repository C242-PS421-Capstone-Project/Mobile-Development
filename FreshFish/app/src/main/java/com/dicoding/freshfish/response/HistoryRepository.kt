package com.dicoding.freshfish.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.freshfish.api.ApiConfig
import com.dicoding.freshfish.response.DataItem
import com.dicoding.freshfish.response.HistoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryRepository {
    private val _historyData = MutableLiveData<List<DataItem>>()
    val historyData: LiveData<List<DataItem>> = _historyData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun fetchHistory() {
        _isLoading.postValue(true)
        ApiConfig.instance.getHistory().enqueue(object : Callback<HistoryResponse> {
            override fun onResponse(call: Call<HistoryResponse>, response: Response<HistoryResponse>) {
                _isLoading.postValue(false)
                if (response.isSuccessful) {
                    response.body()?.let { historyResponse ->
                        val sortedData = historyResponse.data.sortedByDescending { it.createdAt }
                        _historyData.postValue(sortedData)
                    } ?: _historyData.postValue(emptyList())
                } else {
                    _historyData.postValue(emptyList())
                }
            }

            override fun onFailure(call: Call<HistoryResponse>, t: Throwable) {
                _isLoading.postValue(false)
                _historyData.postValue(emptyList())
            }
        })
    }
}

