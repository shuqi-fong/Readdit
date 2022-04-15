package com.example.readdit.ui.notes

import android.os.Parcelable
import java.util.*
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import kotlinx.android.parcel.Parcelize

@Parcelize
class NotesData (
    val title: String,
    val body: String,
    val lastEdited: Timestamp?,
    @DocumentId
    val id: String
):Parcelable{
    constructor():this("","",null,"")
}