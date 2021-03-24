package com.example.itrack.models

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class userData (var username : String, var pass: String, var email :String, var age : String, var periodDate : String, var cycleLength : String)
    {

    lateinit var auth : FirebaseAuth
    lateinit var fstore : FirebaseFirestore
    lateinit var docref : DocumentReference

    init {
        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()
    }

    fun createUser(){

    }

}