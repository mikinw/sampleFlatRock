package com.mnw.androidinterview.db


import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [BookRaw::class, SavedBookRaw::class], version = 5)
abstract class BookDatabase: RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun savedBookDao(): SavedBookDao
}