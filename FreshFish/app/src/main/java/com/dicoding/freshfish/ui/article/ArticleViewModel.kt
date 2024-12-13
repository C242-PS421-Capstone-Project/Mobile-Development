package com.dicoding.freshfish.ui.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.freshfish.response.ArticlesItem
import com.dicoding.freshfish.response.ArticleRepository

class ArticleViewModel(private val articleRepository: ArticleRepository) : ViewModel() {

    private val _articles = MutableLiveData<List<ArticlesItem>?>()
    val articles: LiveData<List<ArticlesItem>?> = _articles

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> = _searchQuery

    fun fetchArticles(query: String = "fish AND ocean") {
        _isLoading.value = true
        val searchIn = "title,description"
        val apiKey = "40eaf5820b104279976695598d73f0ca"

        articleRepository.getArticles(query, searchIn, apiKey) { articles ->
            _isLoading.value = false
            _articles.value = articles
        }
    }
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        fetchArticles(query)
    }
}

