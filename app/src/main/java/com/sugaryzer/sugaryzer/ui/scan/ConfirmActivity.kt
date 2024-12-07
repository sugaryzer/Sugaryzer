package com.sugaryzer.sugaryzer.ui.scan

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sugaryzer.sugaryzer.R
import com.sugaryzer.sugaryzer.ViewModelFactory
import com.sugaryzer.sugaryzer.databinding.ActivityConfirmBinding

class ConfirmActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmBinding
    private val viewModel by viewModels<ScanViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUriString = intent.getStringExtra("IMAGE_URI")
        val imageUri = imageUriString?.let { Uri.parse(it) }

        if (imageUri != null) {
            Log.d("Image URI", "showImage: $imageUri")
            binding.previewImageView.setImageURI(imageUri)
        } else {
            Log.e("Image URI", "URI is null!")
        }

        binding.btnBack.setOnClickListener{
            finish()
        }
        binding.btnConfirm.setOnClickListener {
            Toast.makeText(this, "Image confirmed", Toast.LENGTH_SHORT).show()
        }
    }
}