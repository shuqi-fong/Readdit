package com.example.readdit.database

import android.provider.MediaStore
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "username")
    var username: String = "",

    @ColumnInfo(name = "email_address")
    var emailAddress: String = "",

    @ColumnInfo(name = "password")
    var password: String = "",

    @ColumnInfo(name = "profile_photo")
    var profilePhoto: MediaStore.Images?,

    @ColumnInfo(name = "total_read_minutes")
    var totalReadMinutes: Int = 0,

    @ColumnInfo(name = "total_read_article_count")
    var totalReadArticleCount: Int = 0,

    @ColumnInfo(name = "is_reminder_on")
    var isReminderOn: Boolean = false,

    @ColumnInfo(name = "is_article_notif_on")
    var isArticleNotifOn: Boolean = false
)