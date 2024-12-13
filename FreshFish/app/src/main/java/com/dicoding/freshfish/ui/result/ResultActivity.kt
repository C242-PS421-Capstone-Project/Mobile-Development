package com.dicoding.freshfish.ui.result

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.dicoding.freshfish.databinding.ActivityResultBinding
import com.dicoding.freshfish.response.FreshFishRepository
import java.io.File
import java.io.FileOutputStream

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    private val viewModel: ResultViewModel by viewModels {
        ResultViewModelFactory(FreshFishRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        val imageUriString = intent.getStringExtra("imageUri")
        val imageUri = imageUriString?.let { Uri.parse(it) }

        if (imageUri == null) {
            Log.e("ResultActivity", "Image URI is null")
            showResultFragment("fail", "No image provided")
            return
        }

        Glide.with(this)
            .load(imageUri)
            .into(binding.placeholder)

        viewModel.predictionResult.observe(this, Observer { response ->
            binding.lottieAnimationView.visibility = View.GONE

            val message = response.data.massage
            val result = response.data.result
            showResultFragment(message, result)
        })

        viewModel.error.observe(this, Observer { error ->
            binding.lottieAnimationView.visibility = View.GONE
            showResultFragment("Error", error ?: "Unknown error occurred")
        })

        binding.btnAnalyze.setOnClickListener {
            binding.lottieAnimationView.visibility = View.VISIBLE
            analyzeImageFromPlaceholder()
        }
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            title = "Hasil Pindai"
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun analyzeImageFromPlaceholder() {
        val drawable = binding.placeholder.drawable
        if (drawable == null) {
            showResultFragment("Error", "No image in placeholder")
            return
        }

        val bitmap = (drawable as BitmapDrawable).bitmap
        val imageFile = saveBitmapToFile(bitmap)

        if (imageFile != null && imageFile.exists()) {
            viewModel.analyzeImage(imageFile)
        } else {
            showResultFragment("Error", "Failed to create image file")
        }
    }

    private fun saveBitmapToFile(bitmap: Bitmap): File? {
        return try {
            val file = File(cacheDir, "image.jpg")
            FileOutputStream(file).use { fos ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                fos.flush()
            }
            file
        } catch (e: Exception) {
            Log.e("File Error", "Failed to save bitmap: ${e.message}")
            null
        }
    }

    private fun showResultFragment(message: String, result: String) {
        val fragment = ResultFragment.newInstance(message, result)
        fragment.show(supportFragmentManager, "ResultFragment")
    }
}
