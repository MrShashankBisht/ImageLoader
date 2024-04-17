package com.mrshashankbisht.imageloader.di

import com.google.gson.GsonBuilder
import com.mrshashankbisht.imageloader.data.source.network.MainScreenApiService
import com.mrshashankbisht.imageloader.domain.maper.PhotoMapperImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by Shashank on 15-04-2024
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideMainScreenApiInterface(@Named("auth_token") authToken: String): MainScreenApiService {
        return Retrofit.Builder()
            .baseUrl("https://api.unsplash.com")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(OkHttpClient.Builder().addInterceptor { chain ->
                val request = chain.request().newBuilder().addHeader("Authorization", authToken).build()
                chain.proceed(request)
            }.build())
            .build()
            .create(MainScreenApiService::class.java)
    }

    @Singleton
    @Provides
    @Named("auth_token")
    fun provideAuthToken(): String {
        return "Client-ID HtXV6mCdtCk59KliSpaI5-6lfAIam3IC0gFTOQNDDd4"
    }

}