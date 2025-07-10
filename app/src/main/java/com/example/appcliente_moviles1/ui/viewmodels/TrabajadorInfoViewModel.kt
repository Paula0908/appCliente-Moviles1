package com.example.appcliente_moviles1.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcliente_moviles1.models.CitaResponse
import com.example.appcliente_moviles1.models.Trabajador
import com.example.appcliente_moviles1.repositories.ApiRepository
import com.google.android.gms.common.api.Api
import kotlinx.coroutines.launch


class TrabajadorInfoViewModel : ViewModel() {

    private val _trabajador = MutableLiveData<Trabajador?>()
    val trabajador: LiveData<Trabajador?> get() = _trabajador
    private val _misCitas = MutableLiveData<CitaResponse?>()
    val misCitas = _misCitas

    fun cargarTrabajador(context: Context, trabajadorId: Int) {
        viewModelScope.launch {
            _trabajador.value = ApiRepository.getTrabajadorById(context, trabajadorId)
        }
    }

    fun crearCita(context: Context, trabajadorId: Int, categoriaId: Int, onResult: (CitaResponse?) -> Unit) {
        viewModelScope.launch {
            val cita = ApiRepository.crearCita(context, trabajadorId, categoriaId)
            onResult(cita)
        }
    }
    fun getMisCitas(context: Context){
        viewModelScope.launch {
            _misCitas.value = ApiRepository.getMisCitas(context) as CitaResponse?
        }
    }
    fun obtenerCitaExistente(
        context: Context,
        trabajadorId: Int,
        categoriaId: Int,
        onResult: (CitaResponse?) -> Unit
    ) {
        viewModelScope.launch {
            val misCitas = try {
                ApiRepository.getMisCitas(context) ?: emptyList()
            } catch (e: Exception) {
                emptyList()
            }
            val citaExistente = misCitas
                .filter { it.worker_id == trabajadorId && it.category_selected_id == categoriaId }
                .maxByOrNull { it.id }
            onResult(citaExistente)
        }
    }

}
