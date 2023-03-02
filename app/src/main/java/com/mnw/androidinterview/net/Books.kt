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
    @SerializedName("authors")
    val authors: String? = null,
    @SerializedName("publisher")
    val publisher: String? = null,
    @SerializedName("rating")
    val rating: String? = null,
    @SerializedName("year")
    val year: String? = null,
    @SerializedName("desc")
    val description: String? = null,

)
