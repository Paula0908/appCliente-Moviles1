package com.example.appcliente_moviles1.models

data class Trabajador(
    val id: Int,
    val user_id: Int,
    val picture_url: String?,
    val average_rating: String?,
    val reviews_count: Int,
    val user: Usuario,
    val categories: List<Categoria>?,
    val reviews: List<Review>?
)
