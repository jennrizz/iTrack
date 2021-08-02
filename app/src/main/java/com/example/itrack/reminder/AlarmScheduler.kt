package com.example.itrack.reminder

import android.app.AlarmManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast

class AlarmScheduler {
    object Singleton {

        fun setAlarm(context: Context, alarmTime: Long, reminderTask: Uri) {
            val manager =  context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val operation = ReminderAlarmService.Singleton.getReminderPendingIntent(context,reminderTask)
            if (Build.VERSION.SDK_INT >= 23) {
                manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime, operation)
            } else if (Build.VERSION.SDK_INT >= 19) {
                manager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, operation)
            } else {
                manager.set(AlarmManager.RTC_WAKEUP, alarmTime, operation)
            }
            Toast.makeText(context, "setAlarm Set", Toast.LENGTH_SHORT).show()
        }
        fun setRepeatAlarm(context: Context, alarmTime: Long, reminderTask: Uri, RepeatTime: Long) {
            val manager = AlarmManagerProvider.Singleton().getAlarmManager(context)
            val operation = ReminderAlarmService.Singleton.getReminderPendingIntent(context, reminderTask)
            manager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, RepeatTime, operation)

        }
        fun cancelAlarm(context: Context, reminderTask: Uri) {
            val manager = AlarmManagerProvider.Singleton().getAlarmManager(context)
            val operation = ReminderAlarmService.Singleton.getReminderPendingIntent(context, reminderTask)
            manager.cancel(operation)
        }
    }
}