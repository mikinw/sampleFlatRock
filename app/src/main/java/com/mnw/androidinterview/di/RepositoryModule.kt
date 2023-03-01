package com.mnw.androidinterview.di

import com.mnw.androidinterview.model.BookRepo
import com.mnw.androidinterview.repo.BookRetrofitRoom
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMovieRepo(movieRetrofitRoom: BookRetrofitRoom): BookRepo


}