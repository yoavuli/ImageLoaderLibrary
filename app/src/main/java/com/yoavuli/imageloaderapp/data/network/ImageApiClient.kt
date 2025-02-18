package com.yoavuli.imageloaderapp.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ImageApiClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://zipoapps-storage-test.nyc3.digitaloceanspaces.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ImageApiService = retrofit.create(ImageApiService::class.java)
}