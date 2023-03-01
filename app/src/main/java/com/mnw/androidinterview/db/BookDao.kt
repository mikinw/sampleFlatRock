package com.mnw.androidinterview.db



import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: BookRaw)

    @Query("DELETE FROM book WHERE id = :movieId")
    suspend fun deleteById(movieId: String)

    @Delete
    suspend fun delete(movie: BookRaw)

    @Query("SELECT * FROM book")
    fun getAll(): LiveData<List<BookRaw>>
    @Query("SELECT * FROM book WHERE id = :movie")
    fun getById(movie: Int): BookRaw

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<BookRaw>)

    @Query("DELETE FROM book WHERE id NOT IN (:freshIds)")
    fun deleteExcept(freshIds: List<Int>): Int

}