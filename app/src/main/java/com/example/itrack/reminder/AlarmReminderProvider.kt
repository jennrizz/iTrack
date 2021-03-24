package com.example.itrack.reminder

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.CancellationSignal
import android.util.Log

class AlarmReminderProvider: ContentProvider() {
    lateinit var mDbhelper: AlarmReminderDBHelper
    companion object {
        val LOG_TAG = AlarmReminderProvider.javaClass.simpleName
        val REMINDER = 100
        val REMINDER_ID = 101
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        fun matcher() {
            sUriMatcher.addURI(AlarmReminderContract.CONTENT_AUTHORITY, AlarmReminderContract.PATH_VEHICLE, REMINDER)
            sUriMatcher.addURI(AlarmReminderContract.CONTENT_AUTHORITY, AlarmReminderContract.PATH_VEHICLE + "/#", REMINDER_ID)
        }
    }

    override fun onCreate(): Boolean {
        mDbhelper = AlarmReminderDBHelper(getContext())
        matcher()
        return true
    }


    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        val matcher = sUriMatcher.match(p0)
        when (matcher) {
            REMINDER -> return insertReminder(p0, p1!!)
            else-> return throw java.lang.IllegalArgumentException("Insertion is not supported for: " + p0)
        }
    }

    fun insertReminder(uri: Uri, contentValues: ContentValues): Uri? {
        val db = mDbhelper.writableDatabase
        val id = db.insert(AlarmReminderContract.AlermReminderEntry().TABLE_NAME, null, contentValues)
        if (id.equals(-1)) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri)
            return null
        }
        context!!.contentResolver!!.notifyChange(uri, null)
        return ContentUris.withAppendedId(uri, id)
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? {
        val db = mDbhelper.readableDatabase
        var cursor: Cursor
        val match = sUriMatcher.match(uri)
        when (match) {
            REMINDER -> {
                cursor = db.query(AlarmReminderContract.AlermReminderEntry().TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder)
            }
            REMINDER_ID -> {
                var selection = AlarmReminderContract.AlermReminderEntry()._ID + "=?"
                var selectionArgs = arrayOf(ContentUris.parseId(uri).toString())
                cursor = db.query(AlarmReminderContract.AlermReminderEntry().TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder)
            }
            else ->
                return throw IllegalArgumentException("Cannot query, unknown URI " + uri)

        }
        cursor.setNotificationUri(context!!.contentResolver, uri)
        return cursor
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        val match = sUriMatcher.match(p0)
        when(match){
            REMINDER ->{
                return updateReminder(p0,p1!!,p2,p3)
            }
            REMINDER_ID ->{
                var selection = AlarmReminderContract.AlermReminderEntry()._ID+"=?"
                var selectionArgs = arrayOf(ContentUris.parseId(p0).toString())
                return updateReminder(p0,p1!!,selection,selectionArgs)
            }
            else -> throw java.lang.IllegalArgumentException("Update is not supported for "+p0)
        }

    }
    fun updateReminder(uri: Uri,contentValues: ContentValues,selection: String?,selectionArgs: Array<out String>?): Int {
        if (contentValues.size() == 0){
            return 0
        }
        val db = mDbhelper.writableDatabase
        var rowUpdated = db.update(AlarmReminderContract.AlermReminderEntry().TABLE_NAME,contentValues,selection,selectionArgs)

        if(rowUpdated != 0 ){
            context!!.contentResolver.notifyChange(uri, null)
        }
        return rowUpdated
    }
    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        val db = mDbhelper.writableDatabase
        var rowsDeleted: Int
        val match = sUriMatcher.match(p0)

        when(match){
            REMINDER->{
                rowsDeleted=db.delete(AlarmReminderContract.AlermReminderEntry().TABLE_NAME,p1,p2)
            }
            REMINDER_ID->{
                var selection = AlarmReminderContract.AlermReminderEntry()._ID+ "=?"
                var selectionArgs = arrayOf(ContentUris.parseId(p0).toString())
                rowsDeleted = db.delete(AlarmReminderContract.AlermReminderEntry().TABLE_NAME,selection,selectionArgs)
            }
            else -> throw java.lang.IllegalArgumentException("Deletion is not supported for "+ p0)
        }
        if (rowsDeleted !=0){
            context!!.contentResolver.notifyChange(p0,null)
        }
        return rowsDeleted
    }

    override fun getType(p0: Uri): String? {
       val match = sUriMatcher.match(p0)
       when(match){
           REMINDER ->return AlarmReminderContract.AlermReminderEntry().CONTENT_LIST_TYPE
           REMINDER_ID -> return AlarmReminderContract.AlermReminderEntry().CONTENT_ITEM_TYPE
           else-> throw IllegalStateException("Unkown URI: "+p0+" with match: "+ match)
       }
    }
}
