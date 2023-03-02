package com.mnw.androidinterview.net

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface BooksApi {

    @GET("/1.0/search/{search}")
    suspend fun searchBooks(
        @Path("search") search: String,
        @Query("page") page: Int = 1
    ): Response<Books>

    @GET("/1.0/books/{isbn}")
    suspend fun getDetails(@Path("isbn") isbn: String): Response<BookData>

}