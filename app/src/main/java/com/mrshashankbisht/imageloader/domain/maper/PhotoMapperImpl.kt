package com.mrshashankbisht.imageloader.domain.maper

import com.mrshashankbisht.imageloader.data.model.network.PhotosDetails
import com.mrshashankbisht.imageloader.domain.entity.PhotoImage

/**
 * Created by Shashank on 15-04-2024
 */
class PhotoMapperImpl: PhotosImageMapper<PhotosDetails, PhotoImage> {

    override fun mapToPhotoImage(model: PhotosDetails): PhotoImage {
        return PhotoImage(model.id, model.urls?.small)
    }


    fun mapToListOfPhotoImage(list: List<PhotosDetails>): List<PhotoImage> {
        val listOfPhotoImage: MutableList<PhotoImage> = mutableListOf()
        list.map {
            listOfPhotoImage.add(mapToPhotoImage(it))
        }
        return listOfPhotoImage
    }


}