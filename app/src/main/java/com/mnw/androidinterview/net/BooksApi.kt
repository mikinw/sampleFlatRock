package com.mnw.androidinterview.net

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface BooksApi {

    @GET("/1.0/search/{search}")
    suspend fun searchBooks(
        @Path("search") search: String
    ): Response<Books>
}