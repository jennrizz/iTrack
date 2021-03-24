package com.example.itrack.pedometer

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.itrack.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class pedometerMain : AppCompatActivity(), SensorEventListener{

    lateinit var lineChart: LineChart
    var sensorManager: SensorManager? = null
    lateinit var tv_calPedo: TextView
    lateinit var pedoSwitch: Switch
    lateinit var llayout: LinearLayout
    private val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)

    lateinit var auth : FirebaseAuth
    lateinit var fstore: FirebaseDatabase
    lateinit var docRef: DatabaseReference
    var lineC = LineDataSet(null, null)
    lateinit var arraylist : ArrayList<LineDataSet>
    lateinit var lineData: LineData
    var mRecordingTime: Long? = null


    lateinit var recordingCalendar: Calendar
    var steps = false
    var totalStep = 0
    var previousTotalStep = 0
    var currentSteps = 0
    var userid = " "
    var mPedoActive = " "
    var userInput = " "
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pedometer_main)

        lineChart = findViewById(R.id.pedolineChart)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        tv_calPedo = findViewById(R.id.pedoCalTV)
        pedoSwitch  = findViewById(R.id.pedoSwitch)
        llayout = findViewById(R.id.pedo_main_view)
        tv_calPedo.text = sdf.format(Calendar.getInstance().time)

        auth = FirebaseAuth.getInstance()
        fstore = FirebaseDatabase.getInstance()
        userid = auth.currentUser!!.uid
        docRef = fstore.getReference("PedoChart-$userid")

        lineChart.setScaleEnabled(false)


        if(pedoSwitch.isChecked == true){
            llayout.visibility = View.VISIBLE
            pedoSwitch.isChecked=true
        }else{
            llayout.visibility = View.INVISIBLE
            pedoSwitch.isChecked = true
        }

        findViewById<ImageView>(R.id.pedoEditIcon).setOnClickListener{
                editStepGoal()
        }
    }

    override fun onResume() {
        super.onResume()
        steps = true
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor == null) {
            Toast.makeText(this, "No sensor detected on this device", Toast.LENGTH_LONG).show()
    }
        else{
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        pedoSwitch.setOnCheckedChangeListener{ compoundButton, onSwitch ->
            if (onSwitch == true) {
                Toast.makeText(this, "Pedometer Enable", Toast.LENGTH_SHORT).show()
                llayout.visibility = View.VISIBLE
                if (steps) {
                    totalStep = event!!.values[0].toInt()
                    currentSteps = totalStep - previousTotalStep.toInt()
                    if(currentSteps !=0) {
                        recordingCalendar = Calendar.getInstance()
                        mRecordingTime = recordingCalendar.timeInMillis
                        findViewById<TextView>(R.id.stepTaken_text).text = ("$currentSteps")
                    }else{
                        findViewById<TextView>(R.id.stepTaken_text).text = ("$currentSteps")

                    }
                }
            }else{
                Toast.makeText(this, "Pedometer Disable", Toast.LENGTH_SHORT).show()
                llayout.visibility = View.GONE
            }
        }
    }

    fun insertData(){
        if(pedoSwitch.isChecked == true){
            if(currentSteps !=0){
                val id = docRef.push().key!!
                val xDateValue = recordingCalendar.toString()
                val yDataValue = currentSteps
                val datapoint = pedodataPoints(xValue = xDateValue, yValue = yDataValue.toString())
                datapoint.id = id
                docRef.child(id).setValue(datapoint)
            }
        }
        retrieveData()
    }
    fun retrieveData(){
        fDatabaseHelper().docRef.addValueEventListener(object : ValueEventListener{
            var arrayData : ArrayList<Entry>? = null
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach{ it ->
                    var datapoint = it!!.getValue(pedodataPoints:: class.java)
                    arrayData!!.add(Entry(datapoint!!.xValue, datapoint!!.yValue))
                }
                showChart(arrayData)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun Entry(x: String, yValue: String): Entry {
        return Entry(x, yValue)
    }
    private fun showChart(dateVal: ArrayList<Entry>?) {
        lineC.setValues(dateVal)
        arraylist.clear()


    }

    override fun onStart() {
        super.onStart()
        fDatabaseHelper().docRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach{ it ->
                    val datapoint = it.getValue(pedodataPoints:: class.java)
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun resetSteps(){
        val cal = Calendar.getInstance()
        val todayTime = cal.timeInMillis
        if(mRecordingTime != todayTime){
            findViewById<TextView>(R.id.stepTaken_text).text = ("0")
        }
    }

    fun editStepGoal(){
        val editTV = findViewById<TextView>(R.id.editStepGoal)
        val editIMG = findViewById<View>(R.id.pedoEditIcon)

        editIMG.setOnClickListener{
            val dialogView = LayoutInflater.from(this).inflate(R.layout.edit_step_goal,null)
            val dialogBuilder = AlertDialog.Builder(this)
                    .setView(dialogView)
                    .setTitle("Edit Step Goal")
            val alertDialog = dialogBuilder.show()
            dialogView.findViewById<Button>(R.id.pedodialogDone).setOnClickListener {
                alertDialog.dismiss()

                if(dialogView.findViewById<EditText>(R.id.newStepET).text.toString() != null) {
                    editTV.text = dialogView.findViewById<EditText>(R.id.newStepET).text.toString()
                }
            }
        }
    }
}


