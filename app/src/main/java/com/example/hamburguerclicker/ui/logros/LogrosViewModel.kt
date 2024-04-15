package com.example.hamburguerclicker.ui.logros

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LogrosViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Logros"
    }
    val text: LiveData<String> = _text
}