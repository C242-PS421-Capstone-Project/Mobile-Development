package com.dicoding.freshfish.ui.result

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.dicoding.freshfish.R
import com.dicoding.freshfish.databinding.FragmentResultBinding
import android.view.WindowManager
import androidx.navigation.fragment.findNavController

class ResultFragment : DialogFragment() {

    private var message: String? = null
    private var result: String? = null
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            message = it.getString(ARG_MESSAGE)
            result = it.getString(ARG_RESULT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentResultBinding.inflate(inflater, container, false)

        binding.hasilPesan.text = message ?: "Prediction message unavailable"
        binding.hasilResult.text = result ?: "Prediction result unavailable"

        binding.buttonFinish.setOnClickListener {
            findNavController().navigate(R.id.navigation_scan)
            activity?.finish()
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_MESSAGE = "message"
        private const val ARG_RESULT = "result"

        fun newInstance(message: String, result: String) =
            ResultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_MESSAGE, message)
                    putString(ARG_RESULT, result)
                }
            }
    }
}

