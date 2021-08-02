package com.example.itrack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import ru.cleverpumpkin.calendar.CalendarDate
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class CreateAcc : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var documentReference: DocumentReference
    lateinit var fstore : FirebaseFirestore

    lateinit var refUsers: DatabaseReference
    lateinit var username: EditText
    lateinit var mpassword: EditText
    lateinit var user: EditText

    var firebaseUserID = " "


    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_acc)

        username = findViewById(R.id.userName)
        mpassword = findViewById(R.id.password)
        user = findViewById(R.id.user)


        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()

        findViewById<Button>(R.id.create).setOnClickListener {
            registerUser()
        }
    }


    fun registerUser(){
        var usernametxt = username.text.toString().trim()
        var passtxt = mpassword.text.toString().trim()
        var usertxt = user.text.toString().trim()

        if(TextUtils.isEmpty(usertxt)){
            mpassword.setError("Username is Required")
            return
        }

        if(usertxt.length <= 6  ){
            mpassword.setError("Username Must be >= 6 Characters")
            return
        }

        if (TextUtils.isEmpty(usernametxt)){
            username.setError("Email is Required")
            return
        }
        if(usernametxt.length <= 6){
            username.setError("Email Must be >= 6 Characters")
            return
        }
        if (usernametxt.isEmailValid() == false){
            username.setError("Must be in EMAIL format example@gmail.com")
            return
        }
        if(TextUtils.isEmpty(passtxt)){
            mpassword.setError("Password is Required")
            return
        }

        if(passtxt.length <= 6){
            mpassword.setError("Password Must be >= 6 Characters")
            return
        }

        val intent = Intent(applicationContext, startInformation::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("email", usernametxt)
        intent.putExtra("pass", passtxt)
        intent.putExtra("username", usertxt)
        Log.d("Create Acc", "$usernametxt, $passtxt, $usertxt")
        startActivity(intent)

    }

    fun goTologin(view: View) {
        val intent = Intent(applicationContext, login::class.java)
        startActivity(intent)
    }
    fun String.isEmailValid():Boolean{
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }
}