package com.example.appcliente_moviles1.repositories

import android.content.Context
import com.example.appcliente_moviles1.models.Categoria
import com.example.appcliente_moviles1.models.CitaResponse
import com.example.appcliente_moviles1.models.CrearCitaBody
import com.example.appcliente_moviles1.models.LoginResponse
import com.example.appcliente_moviles1.models.MeResponse
import com.example.appcliente_moviles1.models.RegistroResponse
import com.example.appcliente_moviles1.models.Trabajador
import com.example.appcliente_moviles1.models.chat.ChatMessage
import com.example.appcliente_moviles1.models.chat.SendMessageBody
import retrofit2.Response

object ApiRepository {
    // LOGIN
    suspend fun loginCliente(email: String, password: String): Response<LoginResponse> {
        return RetrofitRepository.publicApi.loginCliente(
            mapOf(
                "email" to email,
                "password" to password
            )
        )
    }

    // REGISTRO
    suspend fun registrarCliente(name: String, lastName: String, email: String, password: String): Response<RegistroResponse> {
        return RetrofitRepository.publicApi.registroClient(
            mapOf(
                "name" to name,
                "lastName" to lastName,
                "email" to email,
                "password" to password
            )
        )
    }
    // CATEGORÍAS
    suspend fun getCategorias(context: Context): List<Categoria> {
        return try {
            RetrofitRepository.authApi(context).getCategorias()
        } catch (e: Exception) {
            emptyList()
        }
    }
    // TRABAJADORES
    suspend fun getTrabajadoresPorCategoria(context: Context, categoriaId: Int): List<Trabajador> {
        return try {
            RetrofitRepository.authApi(context).getTrabajadoresPorCategoria(categoriaId)
        } catch (e: Exception) {
            emptyList()
        }
    }
    suspend fun getTrabajadorById(context: Context, trabajadorId: Int): Trabajador? {
        return try {
            RetrofitRepository.authApi(context).getTrabajadorPorId(trabajadorId)
        } catch (e: Exception) {
            null
        }
    }

    // CITAS
    suspend fun crearCita(context: Context, workerId: Int, categoriaId: Int): CitaResponse? {
        return try {
            RetrofitRepository.authApi(context).crearCita(
                CrearCitaBody(worker_id = workerId, category_selected_id = categoriaId)
            )
        } catch (e: Exception) {
            null
        }
    }
    // MENSAJES
    suspend fun enviarMensaje(context: Context, appointmentId: Int,
                              message: String, receiverId: Int): ChatMessage? {
        return try {
            RetrofitRepository.authApi(context).sendMessage(
                appointmentId,
                SendMessageBody(message, receiverId)
            )
        } catch (e: Exception) {
            null
        }
    }
    // OBTENER MENSAJES DEL CHAT
    suspend fun obtenerMensajesChat(context: Context, appointmentId: Int): List<ChatMessage> {
        return try {
            RetrofitRepository.authApi(context).getChatMessages(appointmentId)
        } catch (e: Exception) {
            emptyList()
        }
    }
    // OBTENER CITA POR ID
    suspend fun obtenerCita(context: Context, appointmentId: Int): CitaResponse? {
        return try {
            RetrofitRepository.authApi(context).getCitaPorId(appointmentId)
        } catch (e: Exception) {
            null
        }
    }
    // OBTENER SHO :3
    suspend fun obtenerMe(context: Context): MeResponse? {
        return try {
            RetrofitRepository.authApi(context).getMe()
        } catch (e: Exception) {
            null
        }
    }



}






