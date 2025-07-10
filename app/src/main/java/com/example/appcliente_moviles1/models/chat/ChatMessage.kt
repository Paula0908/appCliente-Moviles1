package com.example.appcliente_moviles1.models.chat

import com.example.appcliente_moviles1.models.chat.ChatUser

data class ChatMessage(
    val id: Int,
    val appointment_id: Int,
    val sender_id: Int,
    val receiver_id: Int,
    val date_sent: String,
    val message: String,
    val sender: ChatUser,
    val receiver: ChatUser

)