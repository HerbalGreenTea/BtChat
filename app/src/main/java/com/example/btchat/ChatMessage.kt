package com.example.btchat

data class ChatMessage(
        val textMessage: String,
        val typeMessage: TypeMessage
)

enum class TypeMessage {
    IN, OUT
}