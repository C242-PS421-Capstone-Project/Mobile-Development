package com.dicoding.freshfish.ui.article

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.freshfish.adapter.ArticleAdapter
import com.dicoding.freshfish.databinding.FragmentArticleBinding
import com.dicoding.freshfish.api.ApiService
import com.dicoding.freshfish.response.ArticleRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.core.widget.doAfterTextChanged

class ArticleFragment : Fragment() {

    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!

    private lateinit var articleViewModel: ArticleViewModel
    private lateinit var articleAdapter: ArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val apiService = Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        val articleRepository = ArticleRepository(apiService)
        articleViewModel = ViewModelProvider(this, ArticleViewModelFactory(articleRepository))
            .get(ArticleViewModel::class.java)

        _binding = FragmentArticleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        articleAdapter = ArticleAdapter(emptyList())
        binding.recyclerViewArticles.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewArticles.adapter = articleAdapter

        articleViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.linearLayoutLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.recyclerViewArticles.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        articleViewModel.articles.observe(viewLifecycleOwner) { articles ->
            if (articles != null) {
                articleAdapter.updateArticles(articles)
            }
        }

        binding.searchView.doAfterTextChanged { text ->
            articleViewModel.updateSearchQuery(text.toString())
        }

        articleViewModel.fetchArticles()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}