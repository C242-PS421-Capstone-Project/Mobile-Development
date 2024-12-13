package com.dicoding.freshfish.ui.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.freshfish.response.FreshFishRepository
import com.dicoding.freshfish.response.PredictResponse
import java.io.File

class ResultViewModel(private val repository: FreshFishRepository) : ViewModel() {
    private val _predictionResult = MutableLiveData<PredictResponse>()
    val predictionResult: LiveData<PredictResponse> = _predictionResult

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun analyzeImage(file: File) {
        repository.uploadImage(
            file = file,
            onSuccess = { response: PredictResponse ->
                _predictionResult.postValue(response)
            },
            onError = { errorMessage: String ->
                _error.postValue(errorMessage)
            }
        )
    }
}

