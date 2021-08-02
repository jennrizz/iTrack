package com.example.itrack.bottomNavfragments

import android.content.res.AssetFileDescriptor
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.example.itrack.DateIterator.DateProgression
import com.example.itrack.R
import com.example.itrack.adapters.EventCalendarCycleAdapter
import com.example.itrack.cycleEvent.EventCalendarCycle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import org.tensorflow.lite.Interpreter
import ru.cleverpumpkin.calendar.CalendarDate
import ru.cleverpumpkin.calendar.CalendarView
import ru.cleverpumpkin.calendar.extension.getColorInt
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter.ofPattern
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


@Suppress("NAME_SHADOWING")
class CalendarFragment : Fragment(R.layout.fragment_calendar) {

    private var user_age = 0.0f
    private var user_pattern = " "
    private var user_avg_cycle = 0.0f
    var user_last_period_date = 0
    private var user_period_length = 0

    var isValidLastPeriodValid = true

    lateinit var tflite: Interpreter
    var model_output: Float = 0.0f
    lateinit var periodCalendar: Date
    lateinit var calendarView: ru.cleverpumpkin.calendar.CalendarView
    lateinit var markedDays : ArrayList<LocalDate>


    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    lateinit var auth: FirebaseAuth
    lateinit var fstore: FirebaseFirestore
    lateinit var docRef : DocumentReference

    var userid = " "

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()
        userid = auth.currentUser!!.uid
        docRef = fstore.collection("userData").document(userid)
        getUser()

        try {
            tflite = Interpreter(loadModelFile())
        }catch (e: Exception){
            e.printStackTrace()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calendarView = view.findViewById(R.id.calendar_view)

        if (savedInstanceState == null) {
            calendarView.setupCalendar(selectionMode = CalendarView.SelectionMode.NONE, showYearSelectionView = false)
        }

        calendarView.onDateClickListener = { date ->
            // Do something ...
            // for example get list of selected dates
            showDialogWithEventsForSpecificDate(date)

        }
        // Set date long click callback
        calendarView.onDateLongClickListener = { date ->
            // Do something ...
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    //retrieve firebase data
    fun getUser(){
        var date: LocalDate

        docRef.addSnapshotListener { documentSnapshot, e ->
            if(documentSnapshot != null) {
                var usernametxt = documentSnapshot!!.getString("username")
                var emailtxt = documentSnapshot!!.getString("email")
                user_age = documentSnapshot!!.getString("age").toString().toFloat()
                user_pattern = documentSnapshot!!.getString("menstrual pattern").toString()
                user_avg_cycle = documentSnapshot!!.getString("avgCycle").toString().toFloat()
                user_period_length = documentSnapshot!!.getString("periodLength").toString().toFloat().toInt()
                var dateRet = documentSnapshot!!.getString("lperiodday").toString()

                if (dateRet == "I don't remember"){
                    isValidLastPeriodValid = false
                }else{
                    date = LocalDate.parse(dateRet, ofPattern("yyyy-MM-dd"))
                    Toast.makeText(context, "$date", Toast.LENGTH_SHORT).show()
                    if (user_avg_cycle > 0 && isValidLastPeriodValid) {
                    calendarView.datesIndicators = generateCycle(date,user_period_length)
                    }
                }
            }
        }

    }
    //load tflite model
    private fun loadModelFile(): MappedByteBuffer{
        val fileDescriptor: AssetFileDescriptor = context!!.getAssets().openFd("model.tflite")
        val fileInputStream  = FileInputStream(fileDescriptor.getFileDescriptor())
        val fileChannel = fileInputStream.channel
        val startOffSets = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY,startOffSets,declaredLength)
    }

    //infer using model
    @RequiresApi(Build.VERSION_CODES.O)
    private fun inference(age:Float, avgCycle:Float, lDay:Float):Float{
        var inputVal =  Array(1,{FloatArray(3)})
        var inputArr = floatArrayOf(age,avgCycle,lDay)
        inputVal[0] = inputArr

        var outputVal =  Array(1,{FloatArray(1)})
        tflite.run(inputVal,outputVal)
        return outputVal[0][0]
    }

    //generate marks
    @RequiresApi(Build.VERSION_CODES.O)
    private fun generateCycle(user_period_date: LocalDate,userPeriodLength: Int): List<EventCalendarCycle>{
        val eventItems = mutableListOf<EventCalendarCycle>()
        val context = requireContext()
        val months_to_Indicate = 0..4

        var NEXTPERIODDAY = user_period_date


        for(i in months_to_Indicate){
            var PERIODRANGE = DateProgression(NEXTPERIODDAY, NEXTPERIODDAY.plusDays(userPeriodLength.toLong()),1)
            var OVULATIONDAY = NEXTPERIODDAY.minusDays(14)
            var FIRSTFERTILEDAY = NEXTPERIODDAY.minusDays( 18.toLong())
            var LASTFERTILEDAY = NEXTPERIODDAY.minusDays(11.toLong())
            var FERTILERANGE = DateProgression(FIRSTFERTILEDAY, LASTFERTILEDAY,1)

            if(NEXTPERIODDAY == user_period_date){
                eventItems += EventCalendarCycle(
                        eventName = "Last Period Day",
                        date = CalendarDate(sdf.parse(NEXTPERIODDAY.toString()).time),
                        color = context.getColorInt(R.color.red)
                )
            }
            else if (NEXTPERIODDAY != user_period_date){
                for (pDays in PERIODRANGE) {
                    if(pDays == NEXTPERIODDAY){
                        eventItems += EventCalendarCycle(
                                eventName = "Expected Next Period Day",
                                date = CalendarDate(sdf.parse(pDays.toString()).time),
                                color = context.getColorInt(R.color.red))
                        eventItems += EventCalendarCycle(
                                eventName = " ",
                                date = CalendarDate(sdf.parse(pDays.toString()).time),
                                color = context.getColorInt(R.color.red))
                    }
                    else {
                        eventItems += EventCalendarCycle(
                                eventName = "Expected Period Day",
                                date = CalendarDate(sdf.parse(pDays.toString()).time),
                                color = context.getColorInt(R.color.red)
                        )
                    }
                }
            }

            for (fDays in FERTILERANGE step 1){
                if(NEXTPERIODDAY == user_period_date){
                    break
                }
                if (fDays == OVULATIONDAY){
                    eventItems += EventCalendarCycle(
                            eventName = "Ovulation Day",
                            date = CalendarDate(sdf.parse(fDays.toString()).time),
                            color = context.getColorInt(R.color.ferttileColor)
                    )
                    eventItems += EventCalendarCycle(
                            eventName = " ",
                            date = CalendarDate(sdf.parse(fDays.toString()).time),
                            color = context.getColorInt(R.color.ferttileColor)
                    )
                }
                else {
                    eventItems += EventCalendarCycle(
                            eventName = "High Chance of Getting Pregnant",
                            date = CalendarDate(sdf.parse(fDays.toString()).time),
                            color = context.getColorInt(R.color.ferttileColor)
                    )
                }
            }
            var nextMonth = NEXTPERIODDAY.plusMonths(1).month
            var prediction = abs(inference(user_age,user_avg_cycle, NEXTPERIODDAY.dayOfMonth.toFloat()))
            if (prediction > 30){
                prediction = prediction - 30
                nextMonth = nextMonth.plus(1.toLong())
            }
            NEXTPERIODDAY = LocalDate.of(2021, nextMonth, prediction.toInt())
        }
        return eventItems
    }

    private fun showDialogWithEventsForSpecificDate(date: CalendarDate){
        val eventItems = calendarView.getDateIndicators(date)
                .filterIsInstance<EventCalendarCycle>()
                .toTypedArray()

        if (eventItems.isNotEmpty()) {
            val adapter = EventCalendarCycleAdapter(requireContext(), eventItems)

            val builder = AlertDialog.Builder(requireContext())
                    .setTitle("${sdf.format(date.date)}")
                    .setAdapter(adapter, null)

            val dialog = builder.create()
            dialog.show()
        }
    }
    private  fun isWithinRange(date: LocalDate): Boolean{
        return true
    }
    operator fun LocalDate.rangeTo(other:LocalDate) = DateProgression(this,other)
}







