package com.example.handyjobs.services


import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_MUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.handyjobs.R
import com.example.handyjobs.activities.ServicesActivity
import com.example.handyjobs.util.ACTION_SHOW_CHAT_FRAGMENT
import com.example.handyjobs.util.CHANNEL_ID
import com.example.handyjobs.util.CHANNEL_NAME
import com.example.handyjobs.util.USER_COLLECTION
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class FirebaseMessagingService : FirebaseMessagingService() {

    //onMessage Received
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val intent = Intent(this,ServicesActivity::class.java)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification_id = Random.nextInt()


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel(notificationManager = notificationManager)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val notification = NotificationCompat.Builder(
            this,
            CHANNEL_ID
        )
            .setContentTitle(message.data["title"])
            .setContentText(message.data["message"])
            .setSmallIcon(R.drawable.handy)
            .setAutoCancel(true)
            .setContentIntent(getPendingIntent(intent))
            .build()

        notificationManager.notify(notification_id,notification)

        message.data["title"]?.let { Log.d("DEN", it) }

    }

    //notification channel

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager){
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Handy Jobs Channel Description"
        }
        notificationManager.createNotificationChannel(channel)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun getPendingIntent(intent: Intent) = PendingIntent.getActivity(
        this,
        0,
        intent.also {
            it.action = ACTION_SHOW_CHAT_FRAGMENT
        },        FLAG_UPDATE_CURRENT or FLAG_MUTABLE

    )




    //get token
    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        FirebaseAuth.getInstance().currentUser?.let {
            val uid = it.uid
            FirebaseFirestore.getInstance().collection(USER_COLLECTION).document(uid).update("token",newToken)

        }
    }


}