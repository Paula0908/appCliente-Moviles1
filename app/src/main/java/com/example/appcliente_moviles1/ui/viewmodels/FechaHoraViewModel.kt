package com.example.appcliente_moviles1.ui.viewmodels

import com.example.appcliente_moviles1.models.CitaResponse
import com.example.appcliente_moviles1.models.ConcretarCitaInfo
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcliente_moviles1.repositories.ApiRepository
import kotlinx.coroutines.launch

class FechaHoraViewModel : ViewModel() {

    private val _fecha = MutableLiveData<String?>()
    val fecha: LiveData<String?> get() = _fecha

    private val _hora = MutableLiveData<String?>()
    val hora: LiveData<String?> get() = _hora

    private val _citaResponse = MutableLiveData<CitaResponse?>()
    val citaResponse: LiveData<CitaResponse?> get() = _citaResponse

    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> get() = _loading
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun setFecha(fecha: String) { _fecha.value = fecha }
    fun setHora(hora: String) { _hora.value = hora }

    fun concretarCita(
        context: Context,
        appointmentId: Int,
        appointmentDate: String,
        appointmentTime: String,
        latitude: String,
        longitude: String
    ) {
        _loading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                val datos = ConcretarCitaInfo(
                    appointment_date = appointmentDate,
                    appointment_time = appointmentTime,
                    latitude = latitude,
                    longitude = longitude
                )

                val response = ApiRepository.concretarCita(context, appointmentId, datos)
                _citaResponse.value = response
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }
}
