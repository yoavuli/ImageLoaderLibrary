package com.yoavuli.imageloaderapp.data.network

import com.yoavuli.imageloaderapp.data.ImageModel
import retrofit2.http.GET

interface ImageApiService {

    @GET("/image_list.json")
    suspend fun getImages(): List<ImageModel>



}