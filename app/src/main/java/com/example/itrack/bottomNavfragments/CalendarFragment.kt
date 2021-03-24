package com.example.itrack.bottomNavfragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.example.itrack.DateIterator.DateProgression
import com.example.itrack.R
import com.example.itrack.adapters.EventCalendarCycleAdapter
import com.example.itrack.cycleEvent.EventCalendarCycle
import ru.cleverpumpkin.calendar.CalendarDate
import ru.cleverpumpkin.calendar.CalendarView
import ru.cleverpumpkin.calendar.extension.getColorInt
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


@Suppress("NAME_SHADOWING")
class CalendarFragment : Fragment(R.layout.fragment_calendar) {

    private var periodDate: String? = " "
    private var lperiodYear = 0
    private var lperiodDay = 0
    private var lperiodMonth = 0
    private var avgCycle = 0


    lateinit var periodCalendar: Date
    lateinit var calendarView: ru.cleverpumpkin.calendar.CalendarView
    lateinit var markedDays : ArrayList<LocalDate>
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // get bundle
        if (arguments != null) {
            periodDate = arguments!!.getString("lPeriodDate")
            lperiodYear = arguments!!.getInt("lPeriodYear")
            lperiodMonth = arguments!!.getInt("lperiod_month")
            lperiodDay = arguments!!.getInt("lperiod_Day")
            avgCycle = arguments!!.getInt("avgcycle")
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //late init

        calendarView = view.findViewById(R.id.calendar_view)

        // get calendar instance
        val calendar = Calendar.getInstance()
        calendar.set(lperiodYear, lperiodMonth, lperiodDay)
        val nextCyclePeriod = Calendar.getInstance()


        // The first day of week
        val firstDayOfWeek = Calendar.MONDAY

        if (avgCycle !=0) {
            calendarView.datesIndicators = generateCycle()
        }

        if (savedInstanceState == null) {
            calendarView.setupCalendar(selectionMode = CalendarView.SelectionMode.NONE, showYearSelectionView = false)
        }

        // Set date click callback
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
    private fun generateCycle(): List<EventCalendarCycle>{
        val eventItems = mutableListOf<EventCalendarCycle>()
        val context = requireContext()
        val months_to_Indicate = 0..10
        val ovulationDay = avgCycle - 19

        var startDate = LocalDate.parse(periodDate)
        var startDateParse = startDate

        for(i in months_to_Indicate){

            var nextPeriod_date = startDate

            var currentPeriodRange = DateProgression(startDate,startDate.plusDays(4),1)
            var currentfertileWindow = startDate.plusDays(ovulationDay.toLong())
            var currentFertileRange = DateProgression(currentfertileWindow,currentfertileWindow.plusDays(10),1)
            var ovulationDate = nextPeriod_date.minusDays(14)


            if(startDate == startDateParse ){
                eventItems += EventCalendarCycle(
                        eventName = "Last Period Day",
                        date = CalendarDate(sdf.parse(startDate.toString()).time),
                        color = context.getColorInt(R.color.red)
                )
              }
            else if (startDate != startDateParse){
                for (pDays in currentPeriodRange) {
                    if(pDays == nextPeriod_date){
                        eventItems += EventCalendarCycle(
                                eventName = "Expected Next Period Day",
                                date = CalendarDate(sdf.parse(pDays.toString()).time),
                                color = context.getColorInt(R.color.red))
                        eventItems += EventCalendarCycle(
                                eventName = " ",
                                date = CalendarDate(sdf.parse(pDays.toString()).time),
                                color = context.getColorInt(R.color.white))
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

            for (fDays in currentFertileRange step 1){
                if (fDays == ovulationDate){
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
            startDate = startDate.plusDays(avgCycle.toLong())

        }

        return eventItems
    }

    private fun showDialogWithEventsForSpecificDate(date: CalendarDate) {
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







