package com.example.readdit.ui


import com.google.firebase.firestore.DocumentId

data class QuoteData(

    val author: String,
    val quote: String,
    @DocumentId
    val id: String
) {
    constructor():this("","","")
}
