package com.example.itrack.reminder


import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import java.lang.IllegalStateException


var sManager : AlarmManager? = null
class AlarmManagerProvider {
    private val TAG = AlarmManagerProvider::class.simpleName

    object Singleton{
        fun injectAlarmManager(alarmManager: AlarmManager) {
            if (sManager != null) {
                throw IllegalStateException("Alarm Manager Already Set")
            }
            sManager = alarmManager
        }

        fun getAlarmManager(context: Context): AlarmManager {
            if (sManager == null) {
                sManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            }
            return sManager!!
        }
    }
}