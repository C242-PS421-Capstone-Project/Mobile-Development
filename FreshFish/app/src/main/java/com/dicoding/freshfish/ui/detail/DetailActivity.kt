package com.dicoding.freshfish.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.freshfish.R
import com.dicoding.freshfish.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        val imageUrl = intent.getStringExtra(EXTRA_IMAGE)
        val title = intent.getStringExtra(EXTRA_TITLE)
        val author = intent.getStringExtra(EXTRA_AUTHOR)
        val description = intent.getStringExtra(EXTRA_DESCRIPTION)
        val url = intent.getStringExtra(EXTRA_URL)

        detailViewModel.setData(imageUrl, title, author, description, url)

        detailViewModel.imageUrl.observe(this) { image ->
            Glide.with(this)
                .load(image)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(binding.detailImage)
        }

        detailViewModel.title.observe(this) { binding.detailTitle.text = it }
        detailViewModel.author.observe(this) { binding.detailAuthor.text = it }
        detailViewModel.description.observe(this) { binding.detailDescription.text = it }
        detailViewModel.url.observe(this) { articleUrl ->
            binding.openUrlButton.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(articleUrl))
                startActivity(intent)
            }
        }
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            title = "Detail Article"
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_IMAGE = "extra_image"
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_AUTHOR = "extra_author"
        const val EXTRA_DESCRIPTION = "extra_description"
        const val EXTRA_URL = "extra_url"
    }
}
