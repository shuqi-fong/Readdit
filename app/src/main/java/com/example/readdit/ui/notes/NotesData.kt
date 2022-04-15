package com.example.readdit.ui.notes

import java.util.*
import com.google.firebase.Timestamp

class NotesData (
    val title: String,
    val body: String,
    val lastEdited: Timestamp?,
){
    constructor():this("","",null)
}