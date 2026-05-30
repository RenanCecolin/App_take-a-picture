package com.example.app_take_a_picture.network

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    @Multipart
    @POST("api/fridge/upload-image/")
    suspend fun uploadImage(
        @Header("X-Device-Token") deviceToken: String,
        @Part image: MultipartBody.Part
    ): Response<UploadResponse>
}

data class UploadResponse(
    val message: String? = null,
    val total_detectado: Int = 0,
    val itens: List<ItemGeladeira> = emptyList()
)

data class ItemGeladeira(
    val id: Int,
    val name: String,
    val quantity: Double,
    val category: String
)
