package com.example.ct.ctframework

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

class CTNotificationService: Service() {

    inner class LocalBinder : Binder() {

        val serverInstance: CTNotificationService
            get() = this@CTNotificationService
    }

    override fun onBind(p0: Intent?): IBinder = LocalBinder()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}