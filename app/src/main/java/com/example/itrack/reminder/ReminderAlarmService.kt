package com.example.itrack.reminder

import android.app.IntentService
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.itrack.R


val NOTIFICATION_ID = 42

class ReminderAlarmService: IntentService("ReminderAlarmService") {

    open val TAG = AlarmManagerProvider::class.simpleName

    object Singleton{

    fun getReminderPendingIntent(context: Context, uri: Uri): PendingIntent{
        val action = Intent(context, ReminderAlarmService::class.java)
        action.setData(uri)
        return  PendingIntent.getService(context,0,action,PendingIntent.FLAG_UPDATE_CURRENT)
    }
    }


    override fun onHandleIntent(p0: Intent?) {
        var manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val uri = p0!!.getData()

        val action = Intent(this, reminderAdd::class.java)
        action.setData(uri)

        var operation = TaskStackBuilder.create(this)
                .addNextIntentWithParentStack(action)
                .getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT)

        val cursor= contentResolver.query(uri!!,null,null,null,null)!!
        var desc = ""
        try{
            if(cursor !=null && cursor.moveToFirst()){
                desc = AlarmReminderContract.getColumnString(cursor, AlarmReminderContract.AlermReminderEntry().KEY_TITLE)
            }
        }finally {
            if(cursor != null){
                cursor.close()
            }
        }
        var longArr = longArrayOf(1000,1000,1000,1000,1000)
        var note = NotificationCompat.Builder(this)
                .setContentTitle("iTrack Reminder")
                .setContentText(desc)
                .setSmallIcon(R.drawable.ic_action_notif2)
                .setVibrate(longArr)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(operation)
                .setAutoCancel(true)
                .build()
        manager.notify(NOTIFICATION_ID,note)
    }

}