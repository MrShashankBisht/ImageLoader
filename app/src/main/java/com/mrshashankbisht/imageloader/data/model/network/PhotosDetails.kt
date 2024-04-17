package com.mrshashankbisht.imageloader.data.model.network

import com.google.gson.annotations.SerializedName



data class PhotosDetails(
    @SerializedName(value= "id") var id: String? = null,
    @SerializedName(value="description")var description: String? = null,
    @SerializedName(value= "urls")var urls: Urls? = Urls(),
)