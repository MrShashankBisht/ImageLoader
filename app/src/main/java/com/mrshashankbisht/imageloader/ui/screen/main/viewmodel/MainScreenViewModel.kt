package com.mrshashankbisht.imageloader.ui.screen.main.viewmodel

import android.graphics.Bitmap
import android.graphics.pdf.PdfDocument.Page
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrshashankbisht.imageloader.domain.repositories.main.MainRepository
import com.mrshashankbisht.imageloader.ui.screen.main.event.MainScreenEvents
import com.mrshashankbisht.imageloader.ui.screen.main.state.MainScreenState
import com.mrshashankbisht.imageloader.utils.ImageCacheManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Shashank on 15-04-2024
 */
@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val mainRepository: MainRepository,
) : ViewModel(), MainScreenEvents {
    private val _state = MutableStateFlow(MainScreenState())
    val state = _state.asStateFlow()
    val imageCacheManager = ImageCacheManager()


    override fun getPhotoImage(page: Int) {
        _state.value.copy(loadingData = true)
        viewModelScope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                // checking the internet if available then load from serve else load from db cache
                if(state.value.internetAvailable) {

                    var photos = state.value.mutableListOfPhotoImage
                    Log.d("calling API", "getPhotoImage: ${page}")
                    photos.addAll(mainRepository.getPhotos(page))
                    Log.d("calling API", "getPhotoImage: photos size ${photos.count()}")

                    _state.update {
                        it.copy(listPageCount = page, loadingData = false, mutableListOfPhotoImage = photos, itemCount = photos.count())
                    }
                } else {
                    // from local db
                    var data = _state.value.mutableListOfPhotoImage
                    data.addAll(mainRepository.getPhotos(page))
                    // from server
                    _state.update {
                        it.copy(mutableListOfPhotoImage = data)
                    }
                }
            }
        }
    }

    override fun savePhotoInCache(success: (bitmap: Bitmap) -> Unit) {

    }

    override fun savePhotoInDb(
        id: String,
        loaclPath: String,
        success: () -> Unit,
        failure: (errorMessage: String) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            mainRepository.saveImageToDb(id, loaclPath, failure, success)
        }
    }

    override fun getCacheBitmap(id: String): Bitmap? {
        return imageCacheManager.getImage(id)
    }

    override fun saveInCache(id: String, bitmap: Bitmap) {
        imageCacheManager.saveImageToCache(id, bitmap)
    }
}