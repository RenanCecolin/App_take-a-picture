package com.example.app_take_a_picture

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.app_take_a_picture.network.RetrofitClient
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var btnTakePicture: Button

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->

            if (bitmap != null) {

                imageView.setImageBitmap(bitmap)

                uploadImage(bitmap)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.imageView)
        btnTakePicture = findViewById(R.id.btnTakePicture)

        checkCameraPermission()

        btnTakePicture.setOnClickListener {
            cameraLauncher.launch(null)
        }
    }

    private fun checkCameraPermission() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                100
            )
        }
    }

    private fun uploadImage(bitmap: Bitmap) {

        lifecycleScope.launch {

            try {

                val file = bitmapToFile(bitmap)

                val requestFile =
                    file.asRequestBody("image/jpeg".toMediaTypeOrNull())

                val body =
                    MultipartBody.Part.createFormData(
                        "image",
                        file.name,
                        requestFile
                    )

                val response = RetrofitClient.api.uploadImage(body)

                if (response.isSuccessful) {

                    Toast.makeText(
                        this@MainActivity,
                        "Imagem enviada com sucesso!",
                        Toast.LENGTH_LONG
                    ).show()

                    val uploadResponse = response.body()

                    if (uploadResponse?.itens?.isEmpty() == true) {

                        Toast.makeText(
                            this@MainActivity,
                            "Nenhum alimento reconhecido na foto. Tente novamente!",
                            Toast.LENGTH_LONG
                        ).show()

                    } else {

                        Toast.makeText(
                            this@MainActivity,
                            "Alimentos salvos na geladeira!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {

                    Toast.makeText(
                        this@MainActivity,
                        "Erro: ${response.code()}",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (e: Exception) {

                Toast.makeText(
                    this@MainActivity,
                    e.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun bitmapToFile(bitmap: Bitmap): File {

        val file = File(cacheDir, "image.jpg")

        file.createNewFile()

        val outputStream = FileOutputStream(file)

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

        outputStream.flush()
        outputStream.close()

        return file
    }
}
