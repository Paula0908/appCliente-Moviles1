package com.example.appcliente_moviles1.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcliente_moviles1.models.CitaResponse
import com.example.appcliente_moviles1.models.Trabajador
import com.example.appcliente_moviles1.repositories.ApiRepository
import kotlinx.coroutines.launch


class TrabajadorInfoViewModel : ViewModel() {

    private val _trabajador = MutableLiveData<Trabajador?>()
    val trabajador: LiveData<Trabajador?> get() = _trabajador

    fun cargarTrabajador(context: Context, trabajadorId: Int) {
        viewModelScope.launch {
            _trabajador.value = ApiRepository.getTrabajadorById(context, trabajadorId)
        }
    }

    fun crearCita(context: Context, workerId: Int, categoriaId: Int, onResult: (CitaResponse?) -> Unit) {
        viewModelScope.launch {
            val cita = ApiRepository.crearCita(context, workerId, categoriaId)
            onResult(cita)
        }
    }

}
