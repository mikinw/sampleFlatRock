package com.mnw.androidinterview.db


import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [BookRaw::class], version = 2)
abstract class BookDatabase: RoomDatabase() {
    abstract fun bookDao(): BookDao
}