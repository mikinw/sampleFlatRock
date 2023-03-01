package com.mnw.androidinterview.db

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "book")
data class BookRaw(
    @PrimaryKey
    val id: String,
    val title: String,
    val authors: String?,
    val publisher: String?,
    val rating: Int?,
    val year: Int?,
    val description: String?,
    val thumbnail: String?
)