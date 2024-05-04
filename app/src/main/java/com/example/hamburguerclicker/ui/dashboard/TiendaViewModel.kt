package com.example.hamburguerclicker.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TiendaViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Tiendas"
    }
    val text: LiveData<String> = _text
}