package com.example.appcliente_moviles1.api

import com.example.appcliente_moviles1.models.Categoria
import com.example.appcliente_moviles1.models.CitaResponse
import com.example.appcliente_moviles1.models.ConcretarCitaInfo
import com.example.appcliente_moviles1.models.CrearCitaBody
import com.example.appcliente_moviles1.models.LoginResponse
import com.example.appcliente_moviles1.models.MeResponse
import com.example.appcliente_moviles1.models.RegistroResponse
import com.example.appcliente_moviles1.models.Trabajador
import com.example.appcliente_moviles1.models.chat.ChatMessage
import com.example.appcliente_moviles1.models.chat.SendMessageBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ClienteApi {

    // Endpoint para login de cliente
    @POST("client/login")
    suspend fun loginCliente(
        @Body body: Map<String, String>
    ): Response<LoginResponse>

    // Endpoint para registro de cliente
    @POST("client/register")
    suspend fun registroClient(
        @Body body: Map<String, String>
    ): Response<RegistroResponse>

    // Endpoint para obtener categorias
    @GET("categories")
    suspend fun getCategorias(): List<Categoria>

    // Endpoint para obtener los trabajadores segun su categoria
    @GET("categories/{idCategoria}/workers")
    suspend fun getTrabajadoresPorCategoria(
        @Path("idCategoria") idCategoria: Int
    ): List<Trabajador>


    // Endpoint para obtener un trabajador by id Uwu
    @GET("workers/{idTrabajador}")
    suspend fun getTrabajadorPorId(
        @Path("idTrabajador") idTrabajador: Int
    ): Trabajador

    // Endpoint para crear una cita
    @POST("appointments")
    suspend fun crearCita(
        @Body body: CrearCitaBody
    ): CitaResponse

    // Endpoint para enviar mensajes en el chat
    @POST("appointments/{appointmentId}/chats")
    suspend fun sendMessage(
        @Path("appointmentId") appointmentId: Int,
        @Body body: SendMessageBody
    ): ChatMessage

    // Endpoint para traer los mensajes del chat
    @GET("appointments/{appointmentId}/chats")
    suspend fun getChatMessages(
        @Path("appointmentId") appointmentId: Int
    ): List<ChatMessage>

    // Endpoint para obtener una cita by id
    @GET("appointments/{appointmentId}")
    suspend fun getCitaPorId(
        @Path("appointmentId") appointmentId: Int
    ): CitaResponse

    // Endpoint para obtener mis datos :3
    @GET("me")
    suspend fun getMe(): MeResponse

    // Endpoint para obtener mis citas
    @GET("appointments")
    suspend fun getMisCitas(): List<CitaResponse>

    // Endpoint para concretar una cita
    @POST("appointments/{appointmentId}/make")
    suspend fun concretarCita(
        @Path("appointmentId") appointmentId: Int,
        @Body datos: ConcretarCitaInfo
    ): CitaResponse

}


