package com.mnw.androidinterview.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mnw.androidinterview.model.Book


@Entity(tableName = "savedbook")
data class SavedBookRaw (
    @PrimaryKey
    val id: String,
    val title: String,
    val authors: String?,
    val publisher: String?,
    val rating: String?,
    val year: Int?,
    val description: String?,
    val thumbnail: String?
) {
    constructor(book: Book) : this(book.id, book.title, book.authors, book.publisher, book.rating, book.year, book.description, book.thumbnail)
}
