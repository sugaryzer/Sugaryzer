package com.sugaryzer.sugaryzer.ui.scan

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sugaryzer.sugaryzer.R
import com.sugaryzer.sugaryzer.ViewModelFactory
import com.sugaryzer.sugaryzer.data.ResultState
import com.sugaryzer.sugaryzer.data.helper.reduceFileImage
import com.sugaryzer.sugaryzer.data.helper.uriToFile
import com.sugaryzer.sugaryzer.databinding.ActivityConfirmBinding
import com.sugaryzer.sugaryzer.ui.profile.EditProfileActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

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

        val imageFile = uriToFile(imageUri!!, this)
        val compressedImage = imageFile.reduceFileImage()
        val photoRequestBody = compressedImage.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val photoMultipart =
            MultipartBody.Part.createFormData(
                "file",
                compressedImage.name,
                photoRequestBody
            )

        binding.btnBack.setOnClickListener{
            finish()
        }
        binding.btnConfirm.setOnClickListener {
            viewModel.uploadStory(photoMultipart)
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.loading)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            viewModel.uploadResult.observe(this){ responseProduct ->
                when (responseProduct) {
                    is ResultState.Loading -> dialog.show()
                    is ResultState.Error -> {
                        dialog.dismiss()
                        AlertDialog.Builder(this).apply {
                            setTitle("Gagal mendeteksi gambar")
                            setMessage(responseProduct.message)
                            create()
                            show()
                        }
                    }

                    is ResultState.Success -> {
                        dialog.dismiss()
                        viewModel.getConsume.observe(this){responseConsume ->
                            when (responseConsume) {
                                is ResultState.Loading -> dialog.show()
                                is ResultState.Error -> {
                                    dialog.dismiss()
                                    AlertDialog.Builder(this).apply {
                                        setTitle("Gagal mendeteksi gambar")
                                        setMessage(responseConsume.message)
                                        create()
                                        show()
                                    }
                                }

                                is ResultState.Success -> {
                                    dialog.dismiss()
                                    val personalSugar = responseConsume.data.totalConsume
                                    val productSugar = responseProduct.data.result.amountOfSugar

                                    if ((productSugar ?: 0) > 30 || (personalSugar ?: 0) > 30) {
                                        val intent = Intent(this, RecommendationActivity::class.java).apply {
                                            putExtra(RecommendationActivity.EXTRA_PRODUCT_ID, responseProduct.data.result.id)
                                            putExtra(RecommendationActivity.EXTRA_PRODUCT_CODE, responseProduct.data.result.code)
                                            putExtra(RecommendationActivity.EXTRA_PRODUCT_NAME, responseProduct.data.result.name)
                                            putExtra(RecommendationActivity.EXTRA_PRODUCT_SUGAR, productSugar)
                                            putExtra(RecommendationActivity.EXTRA_PERSONAL_SUGAR, personalSugar)
                                        }
                                        startActivity(intent)
                                    } else{
                                        val intent = Intent(this, ProductInformationActivity::class.java).apply {
                                            putExtra(ProductInformationActivity.EXTRA_PRODUCT_ID, responseProduct.data.result.id)
                                            putExtra(ProductInformationActivity.EXTRA_PRODUCT_CODE, responseProduct.data.result.code)
                                            putExtra(ProductInformationActivity.EXTRA_PRODUCT_NAME, responseProduct.data.result.name)
                                            putExtra(ProductInformationActivity.EXTRA_PRODUCT_SUGAR, responseProduct.data.result.amountOfSugar)
                                        }
                                        startActivity(intent)
                                    }
                                }

                                else -> dialog.dismiss()
                            }
                        }
                    }

                    else -> dialog.dismiss()
                }

            }
//            val intent = Intent(this, ProductInformationActivity::class.java).apply {
//                putExtra(ProductInformationActivity.EXTRA_PRODUCT_ID, 3)
//                putExtra(ProductInformationActivity.EXTRA_PRODUCT_NAME, "Fibe Mini Botol 100 ml")
//                putExtra(ProductInformationActivity.EXTRA_PRODUCT_SUGAR, 9)
//            }
//            startActivity(intent)
        }
    }
}