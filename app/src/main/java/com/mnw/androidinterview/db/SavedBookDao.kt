package com.mnw.androidinterview.db

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface SavedBookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(book: SavedBookRaw)
    @Delete
    suspend fun delete(book: SavedBookRaw)
    @Query("SELECT * FROM savedbook")
    fun getAll(): LiveData<List<SavedBookRaw>>
}