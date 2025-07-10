package com.example.appcliente_moviles1.ui.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcliente_moviles1.models.Trabajador
import com.example.appcliente_moviles1.repositories.ApiRepository // Cambia el nombre si tienes otro repo
import kotlinx.coroutines.launch

class TrabajadoresViewModel : ViewModel() {
    private val _trabajadores = MutableLiveData<List<Trabajador>>()
    val trabajadores: LiveData<List<Trabajador>> = _trabajadores

    fun cargarTrabajadoresPorCategoria(context: Context, categoriaId: Int) {
        viewModelScope.launch {
            val lista = ApiRepository.getTrabajadoresPorCategoria(context, categoriaId)
            _trabajadores.value = lista
        }
    }
}
