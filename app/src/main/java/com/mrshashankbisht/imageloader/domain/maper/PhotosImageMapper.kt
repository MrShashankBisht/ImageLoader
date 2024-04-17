package com.mrshashankbisht.imageloader.domain.maper

/**
 * Created by Shashank on 15-04-2024
 */
interface PhotosImageMapper<T, PhotosImageMapper> {
    fun mapToPhotoImage(model: T): PhotosImageMapper
}