package com.example.readdit.ui

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreRepository {

    var db = FirebaseFirestore.getInstance()
    var user = FirebaseAuth.getInstance().currentUser

    fun getReadHistory(): CollectionReference {
        var collectionReference = db.collection("read_history")
        return collectionReference
    }

    fun getArticle(): CollectionReference {
        var collectionReference = db.collection("article")
        return collectionReference
    }

    fun getExplore(): CollectionReference {
        var collectionReference = db.collection("explore")
        return collectionReference
    }

    fun getQuote(): CollectionReference {
        var collectionReference = db.collection("daily_quote")
        return collectionReference
    }

    fun getNotes(): CollectionReference {
        var collectionReference =   db.collection("user").document("user001").collection("notes")
        return collectionReference
    }
}