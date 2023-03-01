package com.mnw.androidinterview.model


import androidx.lifecycle.LiveData


interface BookRepo {

    val books: LiveData<List<Book>>

    suspend fun refreshAll()

    suspend fun getDetails(id: String)

}
