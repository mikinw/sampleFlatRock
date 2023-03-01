package com.mnw.androidinterview.net

import com.google.gson.annotations.SerializedName

data class Books(

    @SerializedName("page")
    val page: Int,
    @SerializedName("total")
    val totalResults: Int,

    @SerializedName("books")
    val bookList: List<BookData>? = null,


    )


data class BookData(
    @SerializedName("isbn13")
    val id: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("image")
    val thumbnail: String,
)
