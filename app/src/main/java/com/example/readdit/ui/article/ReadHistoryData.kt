package com.example.readdit.ui.article

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class ReadHistoryData(
    @DocumentId
    val id: String,
    val article: String,
    @field:JvmField
    val isBookmarked: Boolean,
    @field:JvmField
    val isRead: Boolean,
    val readDate: Timestamp?,
    val user: String,

){
    constructor():this("","",false,false, null,"")
}
