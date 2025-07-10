package com.example.appcliente_moviles1.models.chat

data class SendMessageBody(
    val message: String,
    val receiver_id: Int
)