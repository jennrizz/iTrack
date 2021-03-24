package com.example.itrack.cycleEvent


import ru.cleverpumpkin.calendar.CalendarDate
import ru.cleverpumpkin.calendar.CalendarView


// color indicator
class EventCalendarCycle(
        override val date: CalendarDate,
        override val color: Int,
        val eventName: String

) : CalendarView.DateIndicator