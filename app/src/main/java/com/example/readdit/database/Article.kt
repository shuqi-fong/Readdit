package com.example.readdit.database

import android.provider.MediaStore
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "article_table")
data class Article(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "headline")
    var headline: String = "",

    @ColumnInfo(name = "topic")
    var topic: TopicEnum,

    @ColumnInfo(name = "body")
    var body: List<Paragraph>?,

    @ColumnInfo(name = "read_count")
    var readCount: Int = 0,

    @ColumnInfo(name = "audio")
    var audio: MediaStore.Audio?,

    @ColumnInfo(name = "date_posted")
    val datePosted: Date
)