package com.sugaryzer.sugaryzer.ui.scan

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.camera.core.ImageCaptureException
import androidx.navigation.ui.AppBarConfiguration
import com.sugaryzer.sugaryzer.ViewModelFactory
import com.sugaryzer.sugaryzer.data.helper.createCustomTempFile
import com.sugaryzer.sugaryzer.data.helper.getRotatedBitmap
import com.sugaryzer.sugaryzer.data.helper.reduceFileImage
import com.sugaryzer.sugaryzer.databinding.ActivityScanBinding
import java.io.File
import java.io.FileOutputStream

class ScanActivity : AppCompatActivity() {
    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
    private val viewModel by viewModels<ScanViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityScanBinding
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }
        startCamera()
        binding.btnCapture.setOnClickListener { takePhoto() }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

            try {
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                Log.e("CameraX", "Use case binding failed", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = createCustomTempFile(application)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    viewModel.setImageUri(savedUri)

                    val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)

                    val rotatedBitmap = bitmap.getRotatedBitmap(photoFile)

                    val overlayRect = getOverlayBounds()
                    val croppedBitmap = cropImage(rotatedBitmap, overlayRect)

                    val croppedFile = File(externalCacheDir, "cropped_${System.currentTimeMillis()}.jpg")
                    FileOutputStream(croppedFile).use {
                        croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
                    }

                    val compressedFile = croppedFile.reduceFileImage()
                    val croppedUri = Uri.fromFile(compressedFile)

                    val intent = Intent(this@ScanActivity, ConfirmActivity::class.java)
                    intent.putExtra("IMAGE_URI", croppedUri.toString())
                    startActivity(intent)
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("CameraX", "Photo capture failed: ${exception.message}", exception)
                }
            }
        )
    }

    private fun getOverlayBounds(): Rect {
        val overlay = binding.overlay
        val location = IntArray(2)
        overlay.getLocationOnScreen(location)
        val left = location[0]
        val top = location[1]
        val right = left + overlay.width
        val bottom = top + overlay.height
        return Rect(left, top, right, bottom)
    }

    private fun cropImage(bitmap: Bitmap, overlayRect: Rect): Bitmap {
        val scaleX = bitmap.width.toFloat() / binding.previewView.width.toFloat()
        val scaleY = bitmap.height.toFloat() / binding.previewView.height.toFloat()

        val left = (overlayRect.left * scaleX).toInt().coerceAtLeast(0)
        val top = (overlayRect.top * scaleY).toInt().coerceAtLeast(0)
        val right = (overlayRect.right * scaleX).toInt().coerceAtMost(bitmap.width)
        val bottom = (overlayRect.bottom * scaleY).toInt().coerceAtMost(bitmap.height)

        val width = (right - left).coerceAtLeast(1)
        val height = (bottom - top).coerceAtLeast(1)

        return Bitmap.createBitmap(bitmap, left, top, width, height)
    }


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

}