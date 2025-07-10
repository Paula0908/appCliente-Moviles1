package com.example.appcliente_moviles1.ui.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcliente_moviles1.models.Categoria
import com.example.appcliente_moviles1.repositories.ApiRepository
import kotlinx.coroutines.launch

class CategoriasViewModel : ViewModel() {
    private val _categorias = MutableLiveData<List<Categoria>>()
    val categorias: LiveData<List<Categoria>> get() = _categorias

    fun cargarCategorias(context: Context) {
        viewModelScope.launch {
            try {
                _categorias.value = ApiRepository.getCategorias(context)
            } catch (e: Exception) {
                Log.e("CategoriasViewModel", "Error: ${e.message}", e)
            }
        }
    }

}
