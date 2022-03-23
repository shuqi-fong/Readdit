package com.example.readdit.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_quote_table")
data class DailyQuote (
    @PrimaryKey(autoGenerate = true)
    val id: Long,

    @ColumnInfo(name = "quote")
    var quote: String = "",

    @ColumnInfo(name = "author")
    var author: String = ""
)