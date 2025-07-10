package com.example.appcliente_moviles1.models

data class CitaResponse(
    val id: Int,
    val worker_id: Int,
    val user_id: Int,
    val appointment_date: String?,
    val appointment_time: String?,
    val category_selected_id: Int,
    val latitude: Double?,
    val longitude: Double?,
    val status: Int,
    // Puedes agregar worker, category, client si los usas luego
)
