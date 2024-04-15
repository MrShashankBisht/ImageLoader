package com.mrshashankbisht.imageloader.ui.screen.main.viewmodel

import androidx.lifecycle.ViewModel
import com.mrshashankbisht.imageloader.ui.screen.main.event.MainScreenEvents
import com.mrshashankbisht.imageloader.ui.screen.main.state.MainScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Created by Shashank on 15-04-2024
 */
@HiltViewModel
class MainScreenViewModel : ViewModel(), MainScreenEvents {
    private val _state = MutableStateFlow(MainScreenState())
    val state = _state.asStateFlow()


}