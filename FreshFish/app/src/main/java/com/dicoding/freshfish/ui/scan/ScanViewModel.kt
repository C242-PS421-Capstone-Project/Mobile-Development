package com.dicoding.freshfish.ui.scan

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScanViewModel : ViewModel() {

    private val _imageUri = MutableLiveData<Uri?>()
    val imageUri: LiveData<Uri?> get() = _imageUri

    private val _isImageSelected = MutableLiveData<Boolean>(false)
    val isImageSelected: LiveData<Boolean> get() = _isImageSelected

    fun updateImageUri(uri: Uri?) {
        _imageUri.value = uri
        _isImageSelected.value = uri != null
    }
}
