package com.dicoding.freshfish.response

import com.dicoding.freshfish.api.ApiConfig
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class FreshFishRepository {

    fun uploadImage(file: File, onSuccess: (PredictResponse) -> Unit, onError: (String) -> Unit) {
        val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
        val imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)

        val apiService = ApiConfig.instance
        apiService.uploadImage(imagePart).enqueue(object : Callback<PredictResponse> {
            override fun onResponse(call: Call<PredictResponse>, response: Response<PredictResponse>) {
                when {
                    response.isSuccessful -> {
                        response.body()?.let { onSuccess(it) }
                    }
                    response.code() == 400 -> {
                        val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                        onError(parseErrorMessage(errorMessage))
                    }
                    else -> {
                        onError("Unexpected error: ${response.code()}")
                    }
                }
            }

            override fun onFailure(call: Call<PredictResponse>, t: Throwable) {
                onError("Network error: ${t.message}")
            }
        })
    }

    private fun parseErrorMessage(errorBody: String): String {
        return when {
            errorBody.contains("No image uploaded", ignoreCase = true) -> "No image uploaded"
            errorBody.contains("An error occurred in making a prediction", ignoreCase = true) -> "An error occurred in making a prediction"
            else -> "Unknown error"
        }
    }
}
