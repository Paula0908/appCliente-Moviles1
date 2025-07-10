package com.example.appcliente_moviles1.models

data class Review(
    val id: Int,
    val reviewerName: String,//lo mas probable es que sea user o user_id :p
    val content: String
)
