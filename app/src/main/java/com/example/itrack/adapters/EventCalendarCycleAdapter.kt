package com.example.itrack.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.example.itrack.R
import com.example.itrack.cycleEvent.EventCalendarCycle

class EventCalendarCycleAdapter (
        context: Context,
        events: Array<EventCalendarCycle>
    ) : ArrayAdapter<EventCalendarCycle>(context, 0, events) {

        @SuppressLint("ResourceAsColor")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: LayoutInflater.from(parent.context)
                    .inflate(R.layout.dialog_event, parent, false)
            val eventItem = getItem(position)

            if (eventItem != null) {
                view.findViewById<TextView>(R.id.eventNameView).text = eventItem.eventName
                view.findViewById<LinearLayout>(R.id.eventDialogRoot).setBackgroundColor(R.color.dialogbackground)
            }
            return view
        }
}