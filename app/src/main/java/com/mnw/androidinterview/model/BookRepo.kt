package com.mnw.androidinterview.model


import androidx.lifecycle.LiveData


interface BookRepo {

    val books: LiveData<List<Book>>

    suspend fun refreshAll(query: String)

    suspend fun getDetails(id: String): Book

    val saved: LiveData<List<Book>>
}
