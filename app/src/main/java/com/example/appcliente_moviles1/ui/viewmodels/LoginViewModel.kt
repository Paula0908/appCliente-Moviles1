package com.example.appcliente_moviles1.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcliente_moviles1.models.LoginResponse
import com.example.appcliente_moviles1.repositories.ApiRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginViewModel : ViewModel() {
    private val _loginResult = MutableLiveData<String?>()
    val loginResult: LiveData<String?> = _loginResult

    private val _accessToken = MutableLiveData<String?>()
    val accessToken: LiveData<String?> = _accessToken

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val response: Response<LoginResponse> = ApiRepository.loginCliente(email, password)
            if (response.isSuccessful && response.body()?.access_token != null) {
                _accessToken.value = response.body()?.access_token
                _loginResult.value = "ok" // señal para navegar
            } else {
                _loginResult.value = response.body()?.message ?: "Credenciales incorrectas o error desconocido"
            }
        }
    }
}

