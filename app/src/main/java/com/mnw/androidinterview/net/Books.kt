package com.mnw.androidinterview.net

import com.google.gson.annotations.SerializedName

data class Books(

    @SerializedName("page")
    var page: Int,
    @SerializedName("total")
    var totalResults: Int,

    @SerializedName("books")
    var bookList: List<BookData>? = null,


    )


data class BookData(
    @SerializedName("isbn13")
    var id: String,
    @SerializedName("title")
    var title: String,
    @SerializedName("image")
    var thumbnail: String,
)
