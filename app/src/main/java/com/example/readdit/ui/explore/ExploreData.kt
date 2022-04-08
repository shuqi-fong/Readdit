package com.example.readdit.ui.explore

import android.net.Uri

data class ExploreData (
    val topicImage: String,
    val topicName : String
){
    constructor():this("","")
}
