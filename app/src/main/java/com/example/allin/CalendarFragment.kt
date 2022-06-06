package com.example.allin

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.android.synthetic.main.fragment_calendar.view.*
import java.text.SimpleDateFormat
import java.util.*


class CalendarFragment : Fragment() {

    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    var dateSelected = GregorianCalendar(year, month, day).time


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)
        view.current_date.text = toSimpleString(dateSelected)

        val date = view.calendarView.setOnDateChangeListener { calendarView, year: Int, month: Int, day: Int ->

            dateSelected = GregorianCalendar(year, month, day).time
            view.current_date.text = toSimpleString(dateSelected)
        }
        return view
    }


    @SuppressLint("SimpleDateFormat")
    private fun toSimpleString(date: Date?) = with(date ?: Date()) {
        /**
         * This format can be changed. Use the link below to see the docs.
         * Scroll down to "Date and Time Pattern" Table
         * https://developer.android.com/reference/kotlin/java/text/SimpleDateFormat
         */
        SimpleDateFormat("EEE, MMM d, yyyy").format(this)
    }
}