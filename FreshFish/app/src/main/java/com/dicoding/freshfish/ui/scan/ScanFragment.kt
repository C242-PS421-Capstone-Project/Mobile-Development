package com.dicoding.freshfish.ui.scan

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dicoding.freshfish.databinding.FragmentScanBinding
import com.dicoding.freshfish.ui.result.ResultActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import java.io.File

class ScanFragment : Fragment() {

    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!

    private var cameraImageUri: Uri? = null

    private lateinit var scanViewModel: ScanViewModel

    private val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            scanViewModel.updateImageUri(it)
            navigateToResult(it)
        }
    }

    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && cameraImageUri != null) {
            scanViewModel.updateImageUri(cameraImageUri)
            navigateToResult(cameraImageUri!!)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        scanViewModel = ViewModelProvider(this, ScanViewModelFactory()).get(ScanViewModel::class.java)

        _binding = FragmentScanBinding.inflate(inflater, container, false)
        val root: View = binding.root

        scanViewModel.imageUri.observe(viewLifecycleOwner) { uri ->
            if (uri != null) {
                binding.warningText.visibility = View.GONE
            } else {
                binding.warningText.visibility = View.VISIBLE
            }
        }

        binding.buttonSelectImage.setOnClickListener {
            getImage.launch("image/*")
        }

        binding.buttonSelectCamera.setOnClickListener {
            launchCamera()
        }

        return root
    }

    private fun launchCamera() {
        val photoFile = createImageFile()
        cameraImageUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            photoFile
        )
        takePicture.launch(cameraImageUri)
    }

    private fun createImageFile(): File {
        val storageDir = requireContext().cacheDir
        return File.createTempFile("photo_", ".jpg", storageDir)
    }

    private fun navigateToResult(imageUri: Uri) {
        val intent = Intent(requireContext(), ResultActivity::class.java)
        intent.putExtra("imageUri", imageUri.toString())
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
