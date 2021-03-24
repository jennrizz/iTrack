package com.example.itrack.reminder

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class AlarmReminderDBHelper(var context: Context?): SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION) {
    companion object{
        val TAG = AlarmReminderDBHelper::class.java.simpleName
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "reminder.db"
    }


    override fun onCreate(db: SQLiteDatabase) {
        val SQL_CREATE_REMINDER_TABLE = "CREATE TABLE IF NOT EXISTS ${AlarmReminderContract.AlermReminderEntry().TABLE_NAME}(" +
                "${AlarmReminderContract.AlermReminderEntry()._ID} INTEGER PRIMARY KEY AUTOINCREMENT, "+
                AlarmReminderContract.AlermReminderEntry().KEY_TITLE + " TEXT, "+
                AlarmReminderContract.AlermReminderEntry().KEY_DATE + " TEXT, "+
                "${AlarmReminderContract.AlermReminderEntry().KEY_REPEAT} TEXT, "+
                AlarmReminderContract.AlermReminderEntry().KEY_REPEAT_TYPE + " TEXT, "+
                AlarmReminderContract.AlermReminderEntry().KEY_REPEAT_NO + " TEXT, "+
                AlarmReminderContract.AlermReminderEntry().KEY_ACTIVE + " TEXT, "+
                AlarmReminderContract.AlermReminderEntry().KEY_TIME + " TEXT" + " );"
        db.execSQL(SQL_CREATE_REMINDER_TABLE)
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVer: Int) {}
}