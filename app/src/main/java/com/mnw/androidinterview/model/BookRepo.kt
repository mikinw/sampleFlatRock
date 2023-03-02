package com.mnw.androidinterview.model


import androidx.lifecycle.LiveData


interface BookRepo {

    val books: LiveData<List<Book>>

    suspend fun refreshAll(query: String)

    suspend fun getDetails(id: String): Book
    suspend fun save(book: Book)
    suspend fun unsave(book: Book)

    val saved: LiveData<List<Book>>
    suspend fun loadMore(query: String)
}
