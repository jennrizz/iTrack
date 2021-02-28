package com.example.itrack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.View
import android.widget.EditText


import ru.cleverpumpkin.calendar.CalendarDate
import java.text.SimpleDateFormat
import java.util.*


class LogIn : AppCompatActivity() {

    var age = 0
    var lperiod_Year = 0
    var lperiod_Month = 0
    var lperiod_Day = 0
    var avgcycle = 0
    var lPeriodDate = " "

    lateinit var expFertileDate : List<CalendarDate>
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)


        lperiod_Year = intent.getIntExtra("lperiod_Year", 0)
        lperiod_Month = intent.getIntExtra("lperiod_month", 0)
        lperiod_Day = intent.getIntExtra("lperiod_Day", 0)
        avgcycle = intent.getIntExtra("avgcycle", 0)
        lPeriodDate = intent.getStringExtra("lPeriodDate").toString()

    }



    fun createAccount(view : View){

        val homeIntent = Intent(applicationContext, Home::class.java)
        homeIntent.putExtra("lPeriodYear", lperiod_Year)
        homeIntent.putExtra("lperiod_month", lperiod_Month)
        homeIntent.putExtra("lperiod_Day", lperiod_Day)
        homeIntent.putExtra("lPeriodDate", lPeriodDate)
        homeIntent.putExtra("avgcycle", avgcycle)
        startActivity(homeIntent)
    }
}