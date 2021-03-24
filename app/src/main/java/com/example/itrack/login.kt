package com.example.itrack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.log


class login : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var fstore: FirebaseFirestore
    lateinit var logBtn : Button
    lateinit var passTV : TextView
    lateinit var emailTV : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()
        logBtn = findViewById(R.id.loginButton)
        passTV = findViewById(R.id.loginpassword)
        emailTV = findViewById(R.id.loginEmail)


        logBtn.setOnClickListener {
            login()
        }
    }
    fun login(){

        val passtxt = passTV.text.toString()
        val emailtxt = emailTV.text.toString()

        if(TextUtils.isEmpty(passtxt)){
            passTV.setError("Password is Required")
            return
        }
        if(TextUtils.isEmpty(emailtxt)){
            emailTV.setError("Email is Required")
            return
        }

        auth.signInWithEmailAndPassword(emailtxt, passtxt)
            .addOnCompleteListener{
                    task ->
                if(task.isSuccessful){
                    val intent = Intent(applicationContext, Home::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this, "${task.exception!!.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}