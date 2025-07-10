package com.example.appcliente_moviles1.ui.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcliente_moviles1.models.CitaResponse
import com.example.appcliente_moviles1.repositories.ApiRepository
import kotlinx.coroutines.launch

class MisCitasViewModel : ViewModel() {
    private val _misCitas = MutableLiveData<List<CitaResponse>>()
    val misCitas: LiveData<List<CitaResponse>> get() = _misCitas

    fun getMisCitas(context: Context) {
        viewModelScope.launch {
            val citas = ApiRepository.getMisCitas(context) ?: emptyList()
            _misCitas.postValue(citas)
        }
    }
}
