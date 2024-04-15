package com.mrshashankbisht.imageloader.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import com.mrshashankbisht.imageloader.ui.screen.main.view.MainScreen
import com.mrshashankbisht.imageloader.ui.screen.main.viewmodel.MainScreenViewModel
import com.mrshashankbisht.imageloader.ui.theme.ImageLoaderTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // view model
            val viewModel: MainScreenViewModel = hiltViewModel()
            enableEdgeToEdge()
            ImageLoaderTheme {
                MainScreen(viewModel.state, viewModel)
            }
        }
    }
}

