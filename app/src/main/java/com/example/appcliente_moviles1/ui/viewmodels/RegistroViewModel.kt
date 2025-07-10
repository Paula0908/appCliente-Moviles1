package com.example.appcliente_moviles1.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.appcliente_moviles1.repositories.ApiRepository
class RegistroViewModel : ViewModel() {
    private val _registroResult = MutableLiveData<Boolean>()
    val registroResult: LiveData<Boolean> = _registroResult

    private val _mensajeError = MutableLiveData<String>()
    val mensajeError: LiveData<String> = _mensajeError

    fun registrar(nombre: String, apellido: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = ApiRepository.registrarCliente(nombre, apellido, email, password)
                if (response.isSuccessful) {
                    _registroResult.value = true
                } else {

                    val errorMsg = response.errorBody()?.string() ?: "Error desconocido"
                    _mensajeError.value = errorMsg
                    _registroResult.value = false
                }
            } catch (e: Exception) {
                _mensajeError.value = e.message ?: "Error desconocido"
                _registroResult.value = false
            }
        }
    }
}
