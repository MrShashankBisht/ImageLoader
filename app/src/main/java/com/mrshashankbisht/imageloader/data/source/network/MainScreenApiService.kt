package com.mrshashankbisht.imageloader.data.source.network

import com.mrshashankbisht.imageloader.data.model.network.PhotosDetails
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Shashank on 15-04-2024
 */
interface MainScreenApiService {

    @GET("/photos")
    suspend fun getPaginatedPhotos(@Query("page") page: Int): List<PhotosDetails>
}