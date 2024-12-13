package com.dicoding.freshfish.response

import com.dicoding.freshfish.api.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArticleRepository(private val apiService: ApiService) {

    fun getArticles(query: String, searchIn: String, apiKey: String, callback: (List<ArticlesItem>?) -> Unit) {
        apiService.getArticles(query, searchIn, apiKey).enqueue(object : Callback<ArticleResponse> {
            override fun onResponse(call: Call<ArticleResponse>, response: Response<ArticleResponse>) {
                if (response.isSuccessful) {
                    callback(response.body()?.articles)
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<ArticleResponse>, t: Throwable) {
                callback(null)
            }
        })
    }
}
