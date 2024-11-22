package co.tz.mtangoo.devfestdar24messenger.data

import java.util.Date

data class ChatMessage(
    val id: String,
    val text: String,
    val sender: String,
    val time: Date = Date()
)
