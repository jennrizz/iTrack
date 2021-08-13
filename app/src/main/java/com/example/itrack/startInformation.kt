 package com.example.itrack

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.DialogFragment
import com.example.itrack.models.last_period_info
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import ru.cleverpumpkin.calendar.CalendarDate
import java.text.SimpleDateFormat
import java.time.Year
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

 class startInformation : AppCompatActivity() {
    //firebase
    lateinit var auth: FirebaseAuth
    lateinit var fstore : FirebaseFirestore
    lateinit var documentRefrence: DocumentReference
    //date format
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    //Initial userData
    lateinit var ageEditText: EditText
    lateinit var lperiodEditText: TextView
    lateinit var avgCycleSpinner: Spinner
    lateinit var lperiodTV : TextView
    lateinit var radioGroup: RadioGroup
    lateinit var checkBox: CheckBox
    lateinit var nextButton: Button


     var userId = " "
    lateinit var avgCycle : Any
    var menstrualPattern = " "
    var checkBoxtext = " "
    lateinit var cycleRanges : ArrayList<String>
    lateinit var cyclePostion : Any


    var username = " "
    var pass = " "
    var email = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_information)

        //intitialize
        ageEditText = findViewById(R.id.userage)
        lperiodEditText = findViewById(R.id.userlastperiod)
        lperiodTV = findViewById(R.id.userlastperiod)
        avgCycleSpinner = findViewById(R.id.useravgCycle)
        checkBox = findViewById(R.id.checkBox)
        radioGroup = findViewById(R.id.rGroup)
        nextButton =findViewById(R.id.proceed)

        cycleRanges = ArrayList()

        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()

        pass = intent.getStringExtra("pass").toString()
        email = intent.getStringExtra("email").toString()
        username = intent.getStringExtra("username").toString()

        // spinner dropDown elements
        cycleRanges.add("None")
        val ranges_number = 21..45 step 1
        for (i in ranges_number) {
            cycleRanges.add(i.toString())
        }
        avgCycle = cycleRanges[0]
        avgCycleSpinner.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cycleRanges)
        avgCycleSpinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
            override fun onItemSelected(view: AdapterView<*>?, p1: View?, position: Int, id: Long) {
                if (cycleRanges[position]== "None"){
                    showRadioGroup()
                    cyclePostion = "None"
                    val selectedButtonID = radioGroup.checkedRadioButtonId
                    if(selectedButtonID != -1){
                        if(findViewById<RadioButton>(R.id.userirreg).isChecked){
                            avgCycleSpinner.setSelection(0)
                            Toast.makeText(applicationContext, "No calendar prediction", Toast.LENGTH_LONG).show()
                            avgCycle = 0
                            menstrualPattern = "irregular"

                        }else if(findViewById<RadioButton>(R.id.usernoremember).isChecked){
                            avgCycleSpinner.setSelection(9)
                            Toast.makeText(applicationContext, "Average cycle will be default 28", Toast.LENGTH_LONG).show()
                            avgCycle = 28
                            menstrualPattern = "regular"
                        }
                    }else{
                        avgCycleSpinner.visibility = View.VISIBLE
                    }
                }else{
                    hideRadioGroup()
                    menstrualPattern = "regular"
                    avgCycle = cycleRanges[position].toInt()
                }
            }
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                TODO("Not yet implemented")
            }
        }

        //  button next
        nextButton.setOnClickListener {
            saveDatatoFirestore()
        }

        //method calls
        lastPeriod()
    }

        // show datePickeDialog for the last period input
    fun lastPeriod() {
            checkBox.setOnClickListener {
                if (checkBox.isChecked) {
                    lperiodTV.visibility = View.INVISIBLE
                    checkBoxtext = "I don't remember"
                }
                else if (checkBox.isChecked == false){
                    lperiodTV.visibility = View.VISIBLE}
            }
            lperiodTV.setOnClickListener {
                val cal = Calendar.getInstance()
                val datePick = DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    // instance of calendar picker
                    val sDate = Calendar.getInstance()
                    sDate.set(Calendar.YEAR, mYear)
                    sDate.set(Calendar.DAY_OF_MONTH, mDay)
                    sDate.set(Calendar.MONTH, mMonth)

                    // save instances
                    var lperiod_Year = mYear
                    var lperiod_Month = mMonth
                    var lperiod_Day = mDay

                    // set format for calendar to display
                    var lPeriodDate = sdf.format(sDate.time)
                    lperiodTV.text = lPeriodDate
                    checkBoxtext = lPeriodDate
                    Toast.makeText(this, "$checkBoxtext", Toast.LENGTH_SHORT).show()
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
                datePick.show()
            }
        }


    fun hideRadioGroup(){
        val rGroup = findViewById<RadioGroup>(R.id.rGroup)
        rGroup.visibility = View.INVISIBLE
    }
    fun showRadioGroup(){
        val rGroup = findViewById<RadioGroup>(R.id.rGroup)
        rGroup.visibility = View.VISIBLE
    }

     // function save data to firestore
    fun saveDatatoFirestore(){
        var lperiodtext = lperiodTV.text.toString()
        // check if fields are null
        if(TextUtils.isEmpty(checkBoxtext) && checkBox.isChecked == false){
            lperiodTV.setError("Check checkbox if you don't remember your last period date else enter date")
            return
        }
        if(TextUtils.isEmpty(ageEditText.text.toString())){
            ageEditText.setError("Age is Required")
            return
        }
        if (radioGroup.checkedRadioButtonId == -1 && avgCycle == "None"){
            Toast.makeText(applicationContext, "You selected $cyclePostion choose one from the provided choices", Toast.LENGTH_SHORT).show()
            return
        }
         if (avgCycle == "None"){
             avgCycle = 0
         }

         auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener{task ->
             if(task.isSuccessful){
                 val userId = auth.currentUser!!.uid
                 val firebaseUser = auth.currentUser!!
                 firebaseUser.sendEmailVerification().addOnSuccessListener {

                 }

                 Toast.makeText(this, "User Created", Toast.LENGTH_SHORT).show()
                 documentRefrence = fstore.collection("userData").document(userId)
                 val userData = HashMap<String, Any>()
                 userData.put("email", email)
                 userData.put("username", username)
                 userData.put("lperiodday", checkBoxtext)
                 userData.put("age", ageEditText.text.toString())
                 userData.put("menstrual pattern", menstrualPattern)
                 userData.put("avgCycle", avgCycle.toString())
                 userData.put("periodLength", "5")
                 documentRefrence.set(userData)


                 val intent = Intent(this, Home::class.java)
                 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                 startActivity(intent)
             }

             else{
                 Toast.makeText(this, "Error Message: " + task.exception!!.message.toString(), Toast.LENGTH_SHORT).show()
             }
         }
    }
}

