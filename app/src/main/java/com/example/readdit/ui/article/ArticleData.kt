package com.example.readdit.ui.article

import android.net.Uri
import com.google.type.DateTime
import java.util.*

data class ArticleData (
    val audio: String,
    val body: String,
    val coverImage: String,
    val datePosted: String,
    val description: String,
    val duration: String,
    val readCount: String,
    val title: String,
    val topic: String
){
    constructor():this("","","","","","","","","")
}