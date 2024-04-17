package com.mrshashankbisht.imageloader.ui.screen.main.view

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mrshashankbisht.imageloader.ui.screen.main.event.MainScreenEvents
import com.mrshashankbisht.imageloader.ui.screen.main.state.MainScreenState
import com.mrshashankbisht.imageloader.ui.theme.bgError
import com.mrshashankbisht.imageloader.ui.theme.bgWarningColor
import com.mrshashankbisht.imageloader.ui.theme.circularProgressColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL


/**
 * Created by Shashank on 15-04-2024
 */

@Composable
fun MainScreen(stateFlow: StateFlow<MainScreenState>, mainScreenEvents: MainScreenEvents) {

    val state by stateFlow.collectAsState()
    mainScreenEvents.getPhotoImage()

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            // top bar visible when no internet
            if (!state.internetAvailable) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dp(40f))
                        .background(bgWarningColor)
                ) {
                    Text(
                        modifier = Modifier.padding(Dp(7f)),
                        textAlign = TextAlign.Center,
                        fontSize = 16.sp,
                        color = Color.White,
                        text = state.internetNotAvailableMessage,
                    )
                }
            }
            // dialog for any issue or problem.
            if (state.anyError) {
                ShowHideUIWithDelay(state.errorMessage)
            }
            // main body.
            LazyColumn {
                items(state.mutableListOfPhotoImage) {
                    if(it.url != null) {
                        ImageListScreen(imageUrl = it.url, { error ->
                            stateFlow.value.copy(anyError = true, errorMessage = error)
                        }) {
                            // save image to db for cache.

                        }
                    }
                }
            }

            //loading data loader
            if (state.loadingData) {
                CircularProgressIndicator(
                    modifier = Modifier.width(30.dp),
                    color = circularProgressColor,
                    trackColor = Color.LightGray,
                )
            }

        }
    }
}

@Composable
fun ShowHideUIWithDelay(errorMessage: String) {
    var showUI by remember { mutableStateOf(true) }

    // Launch a coroutine when the composable is first composed
    LaunchedEffect(true) {
        // Delay for 5 seconds
        delay(5000)
        // Hide the UI after the delay
        showUI = false
    }

    // Your UI components
    if (showUI) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dp(40f))
                .background(bgError),
        ) {
            Text(
                modifier = Modifier.padding(Dp(7f)),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = Color.White,
                text = errorMessage,
            )
        }
    }
}

@Composable
fun ImageListScreen(imageUrl: String, onError: ((error: String) -> Unit), loadedSuccess: ((bitmap: Bitmap) -> Unit)) {

    // Step 3: Placeholder
    var bitmap: Bitmap? by remember { mutableStateOf(null) }

    // Step 4: Load Images
    if (bitmap == null) {
        LaunchedEffect(imageUrl) {
            try {
                withContext(Dispatchers.IO) {
                    val inputStream = URL(imageUrl).openStream()
                    bitmap = BitmapFactory.decodeStream(inputStream)
                    inputStream.close()
                }
            } catch (e: IOException) {
                // Handle error
            }
        }
    }

    // Display image once loaded
    if (bitmap != null) {
        Image(
            bitmap!!.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
    } else {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .width(30.dp)
                    .align(Alignment.Center),
                color = circularProgressColor,
                trackColor = Color.LightGray,
            )
        }
    }

}