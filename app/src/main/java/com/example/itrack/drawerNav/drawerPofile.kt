package com.example.itrack.drawerNav

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.example.itrack.Home
import com.example.itrack.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


class drawerPofile : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var fstore: FirebaseFirestore
    lateinit var docRef : DocumentReference
    lateinit var cycleSpinner: Spinner
    lateinit var periodSpinner: Spinner
    lateinit var usernameTV : TextView
    lateinit var emailTV: TextView
    lateinit var ageTV : TextView
    lateinit var menspatTextView: TextView
    lateinit var cyclelengthTextView: TextView


    var userid = " "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer_pofile)

        // initialize
        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()
        userid = auth.currentUser!!.uid
        docRef = fstore.collection("userData").document(userid)
        usernameTV = findViewById(R.id.prof_username)
        emailTV = findViewById(R.id.prof_email)
        ageTV = findViewById(R.id.profageTV)
        menspatTextView = findViewById(R.id.profmenspatTV)
        cyclelengthTextView = findViewById(R.id.proflengthTV)
        cycleSpinner = findViewById(R.id.prof_avgCycleSpin)
        periodSpinner = findViewById(R.id.prof_periodLspin)

        var cyclespinranges = arrayListOf<String>()
        var periodranges = arrayListOf<String>()

        cyclespinranges.add("Change Cycle Length")
        periodranges.add("Change Period Length")

        for (i in 21..45 step 1) {
            cyclespinranges.add(i.toString())
        }
        for (i in 1..10 step 1) {
            periodranges.add(i.toString())
        }

        getUser()

        cycleSpinner.adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cyclespinranges)
        periodSpinner.adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, periodranges)


        cycleSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {


            override fun onItemSelected(parent: AdapterView<*>, p1: View?, position: Int, p3: Long) {
                var item = cyclelengthTextView.text.toString()
                val userData = HashMap<String, Any>()

                if(parent.getItemAtPosition(position).equals("Change Cycle Length")) {
                    item = cyclelengthTextView.text.toString()
                }else{
                    item = parent.getItemAtPosition(position).toString()
                    userData.put("avgCycle", item)
                    cyclelengthTextView.text = item
                    docRef.update(userData)
                }
                Toast.makeText(
                        applicationContext,
                        "This action autmatically change values",
                        Toast.LENGTH_SHORT
                ).show()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Toast.makeText(
                    applicationContext,
                    "This action autmatically change values",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        periodSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
            var item= cyclelengthTextView.text.toString()
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(parent: AdapterView<*>, p1: View?, position: Int, p3: Long) {
                if(parent.getItemAtPosition(position).equals("Change Period Length")) {
                    item = cyclelengthTextView.text.toString()
                }else{
                    item = parent.getItemAtPosition(position).toString()
                    val userData = HashMap<String, Any>()
                    userData.put("periodLength", item.toString())
                    docRef.update(userData)
                }
                Toast.makeText(
                        applicationContext,
                        "This action autmatically change values",
                        Toast.LENGTH_SHORT
                ).show()
            }
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Toast.makeText(
                    applicationContext,
                    "This action autmatically change values",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    //get value
    fun getUser(){
        docRef.addSnapshotListener {
                    documentSnapshot, e ->
                        var usernametxt = documentSnapshot!!.getString("username")
                        var emailtxt = documentSnapshot!!.getString("email")
                        usernameTV.text = usernametxt
                        emailTV.text = emailtxt
                        ageTV.text = documentSnapshot!!.getString("age").toString()
                        menspatTextView.text = documentSnapshot!!.getString("menstrual pattern").toString()
                        cyclelengthTextView.text = documentSnapshot!!.getString("avgCycle").toString()

                }
    }
}