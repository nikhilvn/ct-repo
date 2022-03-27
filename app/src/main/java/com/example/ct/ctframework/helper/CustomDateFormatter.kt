package com.example.ct.ctframework.helper

import java.text.SimpleDateFormat
import java.util.*

class CustomDateFormatter {
    companion object {
        private const val format = "yyyy.MM.dd G 'at' HH:mm:ss z"

        fun fromStringToDate(date: String): Calendar {
            val df = SimpleDateFormat(format, Locale.getDefault())
            val c = df.calendar
            c.time = df.parse(date)!!
            return c
        }

        fun fromDateToString(date: Calendar): String {
            val df = SimpleDateFormat(format, Locale.getDefault())
            return df.format(date.time)
        }

        fun displayDate(date: Calendar): String {
            val df = SimpleDateFormat(format, Locale.getDefault())
            return df.format(date.time)
        }

        fun isDateEqual(d1: Calendar, d2: Calendar): Boolean {
            return fromDateToString(d1) == fromDateToString(d2)
        }
    }
}