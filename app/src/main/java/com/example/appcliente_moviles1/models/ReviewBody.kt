package com.example.appcliente_moviles1.models

data class ReviewBody(
    val rating: Int,
    val comment: String?,  // puede ser null :3
    val is_done: Int
)