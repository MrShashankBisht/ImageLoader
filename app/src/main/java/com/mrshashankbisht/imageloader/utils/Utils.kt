package com.mrshashankbisht.imageloader.utils

import android.content.res.Resources

/**
 * Created by Shashank on 20-04-2024
 */

fun Float.toPx() =
    (this * Resources.getSystem().displayMetrics.density)

fun Float.toDp() =
    (this / Resources.getSystem().displayMetrics.density)