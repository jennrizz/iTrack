package com.example.itrack.reminder

import android.content.ContentUris
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns._ID
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.example.itrack.R
import com.example.itrack.reminder.AlarmReminderContract
import com.example.itrack.reminder.reminderAdd
import com.google.android.material.floatingactionbutton.FloatingActionButton

import  com.example.itrack.reminder.AlarmReminderDBHelper

class drawerReminder() : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor>{
    lateinit var addButton: FloatingActionButton
    lateinit var mCursor: alarmCursorAdapter
    lateinit var listView: ListView
    lateinit var emptyview: View
    lateinit var reminderText: TextView
    val alarmReminderDBHelper = AlarmReminderDBHelper
    val REMINDER_LOADER = 0
   companion object {
       private var alarmTitle = ""
   }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer_reminder)
        setTitle("iTrack Reminder")
        // initiate
        addButton = findViewById(R.id.reminder_add_button)
        listView = findViewById(R.id.reminder_LV)
        emptyview = findViewById(R.id.reminder_empty_view)
        listView.setEmptyView(emptyview)
        reminderText = findViewById(R.id.reminder_text_top)
        mCursor = alarmCursorAdapter(this, null)
        listView.adapter = mCursor

        listView.setOnItemClickListener { adapterView, view, i, id ->
            val intent = Intent(this, reminderAdd::class.java)
            val currentReminderUri = ContentUris.withAppendedId(Uri.parse("${AlarmReminderContract.AlermReminderEntry().CONTENT_URI}/# ${AlarmReminderContract.AlermReminderEntry()._ID}"), id)
            intent.setData(currentReminderUri)
            startActivity(intent)
        }
        addButton.setOnClickListener { v->
            //val intent = Intent(v.getContext(), reminderAdd::class.java)
            //startActivity(intent)
            addReminderTittle()
        }
        supportLoaderManager.initLoader(REMINDER_LOADER,null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val projection = arrayOf(
                AlarmReminderContract.AlermReminderEntry()._ID,
                AlarmReminderContract.AlermReminderEntry().KEY_TITLE,
                AlarmReminderContract.AlermReminderEntry().KEY_DATE,
                AlarmReminderContract.AlermReminderEntry().KEY_TIME,
                AlarmReminderContract.AlermReminderEntry().KEY_REPEAT,
                AlarmReminderContract.AlermReminderEntry().KEY_REPEAT_NO,
                AlarmReminderContract.AlermReminderEntry().KEY_REPEAT_TYPE,
                AlarmReminderContract.AlermReminderEntry().KEY_ACTIVE,
                )
        return CursorLoader(this,
                AlarmReminderContract.AlermReminderEntry().CONTENT_URI,
                projection,
                null,
                null,
                null)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        mCursor.swapCursor(data)
        if (data!!.getCount() > 0){
            reminderText.setVisibility(View.VISIBLE)
        }else{
            reminderText.setVisibility(View.INVISIBLE)
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        mCursor.swapCursor(null)
    }

    fun addReminderTittle(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Set Reminder Title")

        val input = EditText(this)
        input.setInputType(InputType.TYPE_CLASS_TEXT)
        builder.setView(input)

        builder.setPositiveButton("OK", DialogInterface.OnClickListener{dialogInterface, i ->
            if(input.text.toString().isEmpty()){
                return@OnClickListener
            }
            alarmTitle = input.text.toString()
            val values = ContentValues()

            values.put(AlarmReminderContract.AlermReminderEntry().KEY_TITLE, alarmTitle)

            val newUri = contentResolver.insert(AlarmReminderContract.AlermReminderEntry().CONTENT_URI, values)

            restartLoader()

            if(newUri == null){
                Toast.makeText(this, "Setting Reminder Title Failed", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Title set successfully", Toast.LENGTH_SHORT).show()
            }
        })

        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener{dialogInterface, i ->
            dialogInterface.cancel()
        })
        builder.show()
    }
    fun restartLoader(){
        getSupportLoaderManager().restartLoader(REMINDER_LOADER,null,this)
    }
    object singleton {
        fun alarmtitle(): String {
            if(alarmTitle != null){
                return alarmTitle
            }else{
                return throw ExceptionInInitializerError()
            }
        }
    }
}