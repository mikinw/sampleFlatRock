package com.mnw.androidinterview.db



import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface BookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(book: BookRaw)

    @Update
    fun update(book: BookRaw)

    @Query("DELETE FROM book WHERE id = :bookId")
    suspend fun deleteById(bookId: String)

    @Delete
    suspend fun delete(book: BookRaw)

    @Query("SELECT * FROM book")
    fun getAll(): LiveData<List<BookRaw>>
    @Query("SELECT * FROM book WHERE id = :bookId")
    fun getById(bookId: String): BookRaw
    @Query("SELECT * FROM book WHERE id IN (SELECT * FROM savedbook)")
    fun getAllSaved(): LiveData<List<BookRaw>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<BookRaw>)

    @Query("DELETE FROM book WHERE id NOT IN (:freshIds)")
    fun deleteExcept(freshIds: List<String>): Int

}