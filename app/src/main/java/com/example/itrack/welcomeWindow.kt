package com.example.itrack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class welcomeWindow : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var fstore: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()
    }

    override fun onStart(){
        super.onStart()
        setContentView(R.layout.activity_welcome_window)
        var firebaseUserID = auth.currentUser
        if (firebaseUserID == null) {
            Handler().postDelayed({
                val intent = Intent(this, CreateAcc::class.java)
                startActivity(intent)
                finish()
            }, 4000)
        }

        else{
            Handler().postDelayed({
                val intent = Intent(this, Home::class.java)
                startActivity(intent)
                finish()
            }, 4000)
        }
    }
}