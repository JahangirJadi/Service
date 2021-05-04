package com.kassim.foregroundservice

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import java.util.*

val CHANNEL_ID = "ForegroundService Kotlin"
private lateinit var contextt: Context

class ExampleService : Service() {



private val timer = Timer()

    companion object {
        fun startService(context: Context, message: String) {
            contextt = context
            val startIntent = Intent(context, ExampleService::class.java)
            startIntent.putExtra("inputExtra", message)
            ContextCompat.startForegroundService(context, startIntent)
        }
        fun stopService(context: Context) {
            contextt = context
            val stopIntent = Intent(context, ExampleService::class.java)
            context.stopService(stopIntent)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service Kotlin Example")
            .setContentText("input")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)
        timer.scheduleAtFixedRate(DisplayTask(this), 0,10000)

        return START_STICKY
    }
}

private fun createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val serviceChannel = NotificationChannel(CHANNEL_ID, "Foreground Service Channel",
            NotificationManager.IMPORTANCE_DEFAULT)
        val manager = contextt.getSystemService(NotificationManager::class.java)
        manager!!.createNotificationChannel(serviceChannel)
    }
}


class DisplayTask(private val context: Context) : TimerTask(){
    val handler = Handler(Looper.getMainLooper())
    override fun run() {

        handler.post{
            Toast.makeText(context, "Service is running", Toast.LENGTH_SHORT).show()

        }

    }


}