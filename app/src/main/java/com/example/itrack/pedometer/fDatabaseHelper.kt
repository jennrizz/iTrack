package com.example.itrack.pedometer

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class fDatabaseHelper {
    lateinit var fDb : FirebaseDatabase
    lateinit var docRef : DatabaseReference
    lateinit var auth : FirebaseAuth
    lateinit var userid :String
    init{
        auth = FirebaseAuth.getInstance()
        userid = auth.currentUser!!.uid
        fDb = FirebaseDatabase.getInstance()
        docRef = fDb.getReference("PedoChart-$userid")
    }
}