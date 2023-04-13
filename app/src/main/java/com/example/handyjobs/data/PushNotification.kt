package com.example.handyjobs.data

data class NotificationData(
    val title:String,
    val message: String
)
data class PushNotification(
    val data:NotificationData,
    val to:String
)