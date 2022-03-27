package com.example.ct.ctframework.helper

import android.content.Context
import android.content.SharedPreferences
import java.util.*

open class CTPrefs(context: Context) {
    private val prefsFileKey = "sharedPref"
    private val targetKey = "target"
    private val stepsKey = "steps"
    private val goalNearlyCompletionScheduleKey = "goalNearlyCompletionSchedule"

    private val preferences: SharedPreferences =
        context.getSharedPreferences(prefsFileKey, Context.MODE_PRIVATE)

    fun getTarget(): Int {
        return preferences.getInt(targetKey, 2000)
    }
    fun setTarget(value: Int) {
        preferences.edit().putInt(targetKey, value).apply()
    }

    fun getSteps(): Int {
        return preferences.getInt(stepsKey, 0)
    }
    fun setSteps(value: Int) {
        preferences.edit().putInt(stepsKey, value).apply()
    }

    fun getGoalNearlyCompletionSchedule(): Calendar {
        val defaultCalendar = Calendar.getInstance()
        defaultCalendar.set(Calendar.HOUR_OF_DAY, 19)
        defaultCalendar.set(Calendar.MINUTE, 0)
        defaultCalendar.set(Calendar.SECOND, 0)
        val cal = preferences.getString(
            goalNearlyCompletionScheduleKey,
            CustomDateFormatter.fromDateToString(defaultCalendar)
        )
        return CustomDateFormatter.fromStringToDate(cal!!)
    }
    fun setGoalNearlyCompletionSchedule(cal: Calendar) {
        preferences
            .edit()
            .putString(goalNearlyCompletionScheduleKey, CustomDateFormatter.fromDateToString(cal))
            .apply()
    }
}