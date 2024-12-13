package com.dicoding.freshfish.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DetailViewModel : ViewModel() {

    private val _imageUrl = MutableLiveData<String?>()
    val imageUrl: MutableLiveData<String?> get() = _imageUrl

    private val _title = MutableLiveData<String>()
    val title: LiveData<String> get() = _title

    private val _author = MutableLiveData<String>()
    val author: LiveData<String> get() = _author

    private val _description = MutableLiveData<String>()
    val description: LiveData<String> get() = _description


    private val _url = MutableLiveData<String?>()
    val url: MutableLiveData<String?> get() = _url

    fun setData(
        imageUrl: String?,
        title: String?,
        author: String?,
        description: String?,
        url: String?
    ) {
        _imageUrl.value = imageUrl
        _title.value = title ?: "No Title"
        _author.value = author ?: "Unknown Author"
        _description.value = description ?: "No Description"
        _url.value = url
    }
}
