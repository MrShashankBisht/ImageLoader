package com.mrshashankbisht.imageloader.data.model.network

import com.google.gson.annotations.SerializedName


data class Urls(
    @SerializedName(value = "raw") var raw: String? = null,
    @SerializedName(value = "full") var full: String? = null,
    @SerializedName(value = "regular") var regular: String? = null,
    @SerializedName(value = "small") var small: String? = null,
    @SerializedName(value = "thumb") var thumb: String? = null,
    @SerializedName(value = "small_s3") var small_s3: String? = null
)