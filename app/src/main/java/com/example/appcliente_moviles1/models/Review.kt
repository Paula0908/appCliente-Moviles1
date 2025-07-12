package com.example.appcliente_moviles1.models

data class Review(
    val id: Int,
    val worker_id: Int,
    val user_id: Int,
    val appointment_id: Int,
    val rating: Int,
    val comment: String?,
    val is_done: Int,
    val user: Profile
)
