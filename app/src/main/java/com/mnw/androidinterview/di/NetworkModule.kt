package com.mnw.androidinterview.di

import com.mnw.androidinterview.net.BooksApi
import com.mnw.androidinterview.net.EndpointClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideNetworkApi(): BooksApi {
        return EndpointClient.getInstance().create(BooksApi::class.java)
    }
}