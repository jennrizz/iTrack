package com.example.itrack.ui

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.example.itrack.R
import java.text.SimpleDateFormat
import java.util.*

class Goodinterface : AppCompatActivity() {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    var lperiod_Year = 0
    var lperiod_Month = 0
    var lperiod_Day = 0
    var lPeriodDate = " "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goodinterface)
        val Smile =  findViewById<ImageButton>(R.id.smile)
        val Good = findViewById<ImageButton>(R.id.good)
        val Meh =findViewById<ImageButton>(R.id.meh)
        val Sad = findViewById<ImageButton>(R.id.sad)
        val Awful = findViewById<ImageButton>(R.id.awful)
        val next = findViewById<ImageButton>(R.id.next)
        Smile.setOnClickListener {
            val purposeIntent = Intent(applicationContext, SmileInterface::class.java)
            startActivity(purposeIntent)
        }
        Good.setOnClickListener {
            val purposeIntent = Intent(applicationContext, Goodinterface::class.java)
            startActivity(purposeIntent)
        }
        Meh.setOnClickListener {
            val purposeIntent = Intent(applicationContext,Mehinterface::class.java)
            startActivity(purposeIntent)
        }
        Sad.setOnClickListener {
            val purposeIntent = Intent(applicationContext,Sadinterface::class.java)
            startActivity(purposeIntent)
        }
        Awful.setOnClickListener {
            val purposeIntent = Intent(applicationContext,Awfulinterface::class.java)
            startActivity(purposeIntent)
        }
        next.setOnClickListener {
            val Next = Intent(applicationContext,Upto_interface::class.java)
            startActivity(Next)
        }

        fun lastPeriod(view: View) {


        }}

    fun lastPeriod(view: View) { var lastperiodText = findViewById<TextView>(R.id.datebutton)
        val cal = Calendar.getInstance()
        val datePick = DatePickerDialog(
            this,
            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
            DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                // instance of calendar picker
                val sDate = Calendar.getInstance()
                sDate.set(Calendar.YEAR, mYear)
                sDate.set(Calendar.DAY_OF_MONTH, mDay)
                sDate.set(Calendar.MONTH, mMonth)

                // save instances
                lperiod_Year = mYear
                lperiod_Month = mMonth
                lperiod_Day = mDay

                // set format for calendar to display
                lPeriodDate = sdf.format(sDate.time)

                lastperiodText.text = lPeriodDate
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )
        datePick.show()}


}