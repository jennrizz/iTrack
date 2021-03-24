package com.example.itrack.reminder

import android.content.ContentResolver
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

lateinit var auth: FirebaseAuth
lateinit var fstore: FirebaseFirestore
lateinit var docRef: DocumentReference


class AlarmReminderContract {
    constructor(){
    }
    companion object{
        val CONTENT_AUTHORITY = "com.example.itrack.reminder"
        val BASE_CONTENT_URI = Uri.parse("content://"+ CONTENT_AUTHORITY)
        val PATH_VEHICLE = "reminder_path"

        fun getColumnString(cursor: Cursor, columnName: String): String{
            return cursor.getString(cursor.getColumnIndex(columnName))
        }
    }
    class AlermReminderEntry: BaseColumns {
            val CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_VEHICLE)
            val CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VEHICLE
            val CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VEHICLE
            val TABLE_NAME = "ALARMS"
            val _ID = BaseColumns._ID
            val KEY_TITLE = "KEY_TITLE"
            val KEY_DATE = "KEY_DATE"
            val KEY_TIME = "KEY_TIME"
            val KEY_REPEAT = "KEY_REPEAT"
            val KEY_REPEAT_NO = "KEY_REPEAT_NO"
            val KEY_REPEAT_TYPE = "KEY_REPEAT_TYPE"
            val KEY_ACTIVE = "KEY_ACTIVE"
    }

}