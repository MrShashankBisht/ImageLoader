package com.mrshashankbisht.imageloader.ui.screen.main.state

import com.mrshashankbisht.imageloader.domain.entity.PhotoImage

/**
 * Created by Shashank on 15-04-2024
 */
data class MainScreenState(
    var internetAvailable: Boolean = true,
    var internetNotAvailableMessage: String = "No internet please connect to internet !",
    var loadingData:Boolean = false,
    var anyError: Boolean = true,
    var errorMessage: String = "there is some problem while loading :( ",
    var mutableListOfPhotoImage: MutableList<PhotoImage> = mutableListOf()
)
