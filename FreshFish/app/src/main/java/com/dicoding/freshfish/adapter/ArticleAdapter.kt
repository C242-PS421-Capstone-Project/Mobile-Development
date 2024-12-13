package com.dicoding.freshfish.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.freshfish.R
import com.dicoding.freshfish.databinding.ListArticleBinding
import com.dicoding.freshfish.response.ArticlesItem
import com.dicoding.freshfish.ui.detail.DetailActivity

class ArticleAdapter(
    private var articles: List<ArticlesItem>
) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(private val binding: ListArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(article: ArticlesItem) {
            binding.articleTitle.text = article.title
            Glide.with(binding.articleImage.context)
                .load(article.urlToImage)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(binding.articleImage)

            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.EXTRA_IMAGE, article.urlToImage)
                    putExtra(DetailActivity.EXTRA_TITLE, article.title)
                    putExtra(DetailActivity.EXTRA_AUTHOR, article.author)
                    putExtra(DetailActivity.EXTRA_DESCRIPTION, article.description)
                    putExtra(DetailActivity.EXTRA_URL, article.url)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ListArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount(): Int = articles.size

    fun updateArticles(newArticles: List<ArticlesItem>) {
        articles = newArticles
        notifyDataSetChanged()
    }
}
