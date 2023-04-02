package com.example.handyjobs.data

data class Message(
    val id: String,
    val fromId: String,
    val toId: String,
    val text: TextMessage,
    val timestamp:Long
){
    constructor():this("","","",TextMessage(),0)
}


data class TextMessage(
    val text: String? = "",
    val file: String? = null
){
    constructor():this("",null)
}

