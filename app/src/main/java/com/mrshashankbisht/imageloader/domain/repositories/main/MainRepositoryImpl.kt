package com.mrshashankbisht.imageloader.domain.repositories.main

import com.mrshashankbisht.imageloader.data.source.network.MainScreenApiService
import com.mrshashankbisht.imageloader.domain.entity.PhotoImage
import com.mrshashankbisht.imageloader.domain.maper.PhotoMapperImpl
import com.mrshashankbisht.imageloader.domain.maper.PhotosImageMapper
import javax.inject.Inject

/**
 * Created by Shashank on 15-04-2024
 */
class MainRepositoryImpl (
    private val mainScreenApiService: MainScreenApiService,
    private val photoMapperImpl: PhotoMapperImpl
): MainRepository {

    override suspend fun getPhotos(pageNum: Int): List<PhotoImage> {
        try{
            return photoMapperImpl.mapToListOfPhotoImage(mainScreenApiService.getPaginatedPhotos(pageNum))
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }
    }

    override suspend fun getPhotosFromLocal(pageNum: Int): List<PhotoImage> {
        return listOf()
    }

    override suspend fun saveImageToDb(
        id: String,
        path: String,
        error: (errorMsg: String) -> Unit,
        success: () -> Unit
    ) {

    }
}