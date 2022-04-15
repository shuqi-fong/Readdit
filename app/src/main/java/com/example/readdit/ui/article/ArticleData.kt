package com.example.readdit.ui.article

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import kotlinx.android.parcel.Parcelize

data class ArticleData(

    val audio: String,
    val body: String,
    val coverImage: String,
    val datePosted: Timestamp?,
    val durationMin: Int,
    val readCount: Int,
    val title: String,
    val topic: String,
    @DocumentId
    val id: String
) {
    constructor():this("","","",null,0,0,"","","")
}