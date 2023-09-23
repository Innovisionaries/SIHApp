package com.example.sihapp

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.sihapp.databinding.ActivityMainBinding
import java.io.InputStream

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    companion object {
        const val CAMERA_PERMISSION_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        val getImage = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback { uri ->
                uri?.let {
                    // Load the selected image into the ImageView
                    val inputStream: InputStream? = contentResolver.openInputStream(uri)
                    val selectedBitmap: Bitmap? = BitmapFactory.decodeStream(inputStream)
                    inputStream?.close()

                    selectedBitmap?.let {
                        binding.imageView.setImageBitmap(it)
                    }
                }
            }
        )

        val takePicture = registerForActivityResult(
            ActivityResultContracts.TakePicturePreview(),
            ActivityResultCallback { bitmap: Bitmap? ->
                bitmap?.let {
                    binding.imageView.setImageBitmap(it)
                }
            }
        )

        binding.insertImageButton.setOnClickListener {
            getImage.launch("image/*")
        }

        binding.cameraButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                takePicture.launch(null)
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_CODE
                )
            }
        }
    }
}
