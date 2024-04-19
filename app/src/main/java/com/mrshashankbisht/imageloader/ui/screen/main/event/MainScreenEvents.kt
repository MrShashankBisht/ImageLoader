package com.mrshashankbisht.imageloader.ui.screen.main.event

import android.graphics.Bitmap

/**
 * Created by Shashank on 15-04-2024
 */
interface MainScreenEvents {
    fun getPhotoImage(page: Int = 0)
    fun savePhotoInCache(success: (bitmap: Bitmap) -> Unit)
    fun savePhotoInDb(id: String, loaclPath: String, success: (() -> Unit), failure: ((errorMessage: String) -> Unit))
    fun getCacheBitmap(id: String): Bitmap?
    fun saveInCache(id: String, bitmap: Bitmap)
}