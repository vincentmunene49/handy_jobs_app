package com.example.handyjobs.data

data class Message(
    val id: String,
    val fromId: String,
    val toId: String,
    val text: TextMessage,
    val timestamp:Long
)


data class TextMessage(
    val text: String? = "",
    val file: String? = null
)

