package com.example.appcliente_moviles1.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcliente_moviles1.models.Trabajador
import com.example.appcliente_moviles1.models.chat.ChatMessage
import com.example.appcliente_moviles1.repositories.ApiRepository
import kotlinx.coroutines.launch
class ChatViewModel : ViewModel() {

    private val _trabajador = MutableLiveData<Trabajador?>()
    val trabajador: LiveData<Trabajador?> get() = _trabajador
    private val _mensajes = MutableLiveData<List<ChatMessage>>(emptyList())
    val mensajes: LiveData<List<ChatMessage>> get() = _mensajes

    fun cargarTrabajador(context: Context, trabajadorId: Int) {
        viewModelScope.launch {
            _trabajador.value = ApiRepository.getTrabajadorById(context, trabajadorId)
        }
    }

    fun cargarMensajes(context: Context, citaId: Int) {
        viewModelScope.launch {
            _mensajes.value = ApiRepository.obtenerMensajesChat(context, citaId)
        }
    }

    fun enviarMensaje(context: Context, citaId: Int, mensaje: String, trabajadorId: Int) {
        viewModelScope.launch {
            ApiRepository.enviarMensaje(context, citaId, mensaje, trabajadorId)
            cargarMensajes(context, citaId)
        }
    }
}
