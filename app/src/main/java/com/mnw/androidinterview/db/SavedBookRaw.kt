package com.mnw.androidinterview.db

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "savedbook")
data class SavedBookRaw (
    @PrimaryKey
    val id: String,
)
