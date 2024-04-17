package com.mrshashankbisht.imageloader.ui.screen.main.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrshashankbisht.imageloader.domain.repositories.main.MainRepository
import com.mrshashankbisht.imageloader.ui.screen.main.event.MainScreenEvents
import com.mrshashankbisht.imageloader.ui.screen.main.state.MainScreenState
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


    override fun getPhotoImage() {
        viewModelScope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                // checking the internet if available then load from serve else load from db cache
                if(state.value.internetAvailable) {
                    _state.value.copy(loadingData = true)
                    val photos = mainRepository.getPhotos(1)

                    _state.update {
                        it.copy(mutableListOfPhotoImage = photos.toMutableList())
                    }
                    _state.value.copy(loadingData = false)
                } else {
                    // from local db
                    var data = _state.value.mutableListOfPhotoImage
                    data.addAll(mainRepository.getPhotos(1))
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
}