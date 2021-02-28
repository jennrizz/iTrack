package com.example.itrack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
/*import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser*/
import ru.cleverpumpkin.calendar.CalendarDate
import java.text.SimpleDateFormat
import java.util.*


class EnterUser : AppCompatActivity() {
   /* private lateinit var auth: FirebaseAuth*/
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


        setContentView(R.layout.activity_enter_user)
        lperiod_Year = intent.getIntExtra("lperiod_Year", 0)
        lperiod_Month = intent.getIntExtra("lperiod_month", 0)
        lperiod_Day = intent.getIntExtra("lperiod_Day", 0)
        avgcycle = intent.getIntExtra("avgcycle", 0)
        lPeriodDate = intent.getStringExtra("lPeriodDate").toString()

       findViewById<Button>(R.id.login).setOnClickListener{Login1()}

    }

    fun createAccount(view : View){
        val homeIntent = Intent(applicationContext, LogIn::class.java)
        homeIntent.putExtra("lPeriodYear", lperiod_Year)
        homeIntent.putExtra("lperiod_month", lperiod_Month)
        homeIntent.putExtra("lperiod_Day", lperiod_Day)
        homeIntent.putExtra("lPeriodDate", lPeriodDate)
        homeIntent.putExtra("avgcycle", avgcycle)
        startActivity(homeIntent)
        finish()
    }
    fun Login1() {
        val loginIntent = Intent(applicationContext,Home::class.java)
        loginIntent.putExtra("lPeriodYear", lperiod_Year)
        loginIntent.putExtra("lperiod_month", lperiod_Month)
        loginIntent.putExtra("lperiod_Day", lperiod_Day)
        loginIntent.putExtra("lPeriodDate", lPeriodDate)
        loginIntent.putExtra("avgcycle", avgcycle)
        startActivity(loginIntent)
    }

}