package com.mrshashankbisht.imageloader.domain.repositories.main

import com.mrshashankbisht.imageloader.domain.entity.PhotoImage

/**
 * Created by Shashank on 15-04-2024
 */
interface MainRepository {
    suspend fun getPhotos(pageNum: Int = 0): List<PhotoImage>
    suspend fun getPhotosFromLocal(pageNum: Int = 0): List<PhotoImage>
    suspend fun saveImageToDb(id: String, path: String, error: ((errorMsg: String) -> Unit), success: (() -> Unit))
}