package com.mrshashankbisht.imageloader.di

import com.mrshashankbisht.imageloader.data.source.network.MainScreenApiService
import com.mrshashankbisht.imageloader.domain.maper.PhotoMapperImpl
import com.mrshashankbisht.imageloader.domain.repositories.main.MainRepository
import com.mrshashankbisht.imageloader.domain.repositories.main.MainRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Shashank on 15-04-2024
 */
@Module
@InstallIn(SingletonComponent ::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRecipeMapper(): PhotoMapperImpl {
        return PhotoMapperImpl()
    }

    @Provides
    @Singleton
    fun providesMainRepository(
        mainScreenApiService : MainScreenApiService,
        photoMapperImpl : PhotoMapperImpl
    ): MainRepository {
        return MainRepositoryImpl(
            mainScreenApiService = mainScreenApiService,
            photoMapperImpl = photoMapperImpl
        )
    }
}