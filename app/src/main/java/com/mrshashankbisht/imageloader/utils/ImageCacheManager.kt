package com.mrshashankbisht.imageloader.utils


import android.content.Context
import android.graphics.Bitmap
import androidx.collection.LruCache
/**
 * Created by Shashank on 17-04-2024
 */
class ImageCacheManager {
    // Memory cache
    private val memoryCache = LruCache<String, Bitmap>(maxMemory / 8)


    fun saveImageToCache(id: String, bitmap: Bitmap) {
        if (memoryCache.get(id) == null) {
            memoryCache.put(id, bitmap)
        }
    }

    fun getImage(id: String): Bitmap? {
        return memoryCache.get(id)
    }


    companion object {
        private const val DISK_CACHE_SIZE: Long = 10 * 1024 * 1024 // 10 MB
        private val maxMemory = Runtime.getRuntime().maxMemory().toInt()
    }
}
