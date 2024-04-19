package com.mrshashankbisht.imageloader.ui.screen.main.view

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mrshashankbisht.imageloader.ui.screen.main.event.MainScreenEvents
import com.mrshashankbisht.imageloader.ui.screen.main.state.MainScreenState
import com.mrshashankbisht.imageloader.ui.theme.bgError
import com.mrshashankbisht.imageloader.ui.theme.bgWarningColor
import com.mrshashankbisht.imageloader.ui.theme.circularProgressColor
import com.mrshashankbisht.imageloader.utils.toDp
import com.mrshashankbisht.imageloader.utils.toPx
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
    mainScreenEvents.getPhotoImage(1)

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
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 1.dp,
                horizontalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                items(state.itemCount) { i ->
                    if (i == (state.itemCount - 2) && !state.loadingData) {
                        mainScreenEvents.getPhotoImage(state.listPageCount + 1)
                    }
                    val photo = state.mutableListOfPhotoImage[i]
                    if (photo.url != null && photo.id != null) {
                        ImageListScreen(
                            mainScreenEvents,
                            photo.id!!,
                            imageUrl = photo.url,
                            { error ->
                                if (!state.anyError) {
                                    stateFlow.value.copy(anyError = true, errorMessage = error)
                                }
                            }) {
                            // save image to db for cache.

                        }
                    }
                }
            }

            //loading data loader
            if (state.loadingData) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                        .height(Dp(50f))
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.width(30.dp),
                        color = circularProgressColor,
                        trackColor = Color.LightGray,
                    )
                }

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
fun ImageListScreen(
    mainScreenEvents: MainScreenEvents,
    id: String,
    imageUrl: String,
    onError: ((error: String) -> Unit),
    loadedSuccess: ((bitmap: Bitmap) -> Unit)
) {

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.toFloat().toPx()

    var bitmap: Bitmap? by remember { mutableStateOf(null) }
    var errorMessage: String? by remember {
        mutableStateOf(null)
    }

    bitmap = mainScreenEvents.getCacheBitmap(id)

    if (bitmap == null) {
        LaunchedEffect(imageUrl) {
            withContext(Dispatchers.IO) {
                try {
                    val inputStream = URL(imageUrl).openStream()
                    bitmap = BitmapFactory.decodeStream(inputStream)
                    inputStream.close()
                    if (bitmap != null) {
                        mainScreenEvents.saveInCache(id, bitmap!!)
                    } else {
                        errorMessage = "Loading image issue"
                    }
                } catch (e: IOException) {
                    errorMessage = "Loading image issue"
                    onError("${e.message}")
                }
            }

        }
    }
    if (bitmap != null) {
        val ration = bitmap!!.height.toFloat() / bitmap!!.width.toFloat()
        val ImageFrameHeight = ration*(screenWidth/2)
        Image(
            bitmap!!.asImageBitmap(),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .height(Dp(ImageFrameHeight.toDp()))
        )
    } else {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            if (errorMessage != null) {
                Text(
                    color = Color.Red,
                    fontSize = 14f.sp,
                    textAlign = TextAlign.Center,
                    text = errorMessage!!.toString()
                )
            } else {
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

}