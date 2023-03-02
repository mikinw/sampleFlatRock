package com.mnw.androidinterview.di

import android.app.Application
import androidx.room.Room
import com.mnw.androidinterview.db.BookDao
import com.mnw.androidinterview.db.BookDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): BookDatabase {
        return Room.databaseBuilder(application, BookDatabase::class.java, "books.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideArtworkDao(db: BookDatabase): BookDao {
        return db.bookDao()
    }
}

