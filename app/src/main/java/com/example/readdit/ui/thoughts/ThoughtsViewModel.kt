package com.example.readdit.ui.thoughts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ThoughtsViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is thoughts Fragment"
    }
    val text: LiveData<String> = _text
}