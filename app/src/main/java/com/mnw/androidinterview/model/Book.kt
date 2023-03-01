package com.mnw.androidinterview.model

import android.graphics.Bitmap


data class Book (
    val id: String,
    val title: String,
    val authors: String? = null,
    val publisher: String? = null,
    val rating: Int? = null,
    val year: Int? = null,
    val description: String? = null,
    val thumbnail: Bitmap? = null
)