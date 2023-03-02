package com.mnw.androidinterview.model


data class Book (
    val id: String,
    val title: String,
    val authors: String? = null,
    val publisher: String? = null,
    val rating: String? = null,
    val year: Int? = null,
    val description: String? = null,
    val thumbnail: String? = null
)