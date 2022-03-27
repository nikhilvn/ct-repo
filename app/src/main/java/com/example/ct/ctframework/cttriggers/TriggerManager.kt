package com.example.ct.ctframework

import android.content.Context
import com.example.ct.ctframework.ctcontext.CTContextManager
import com.example.ct.ctframework.helper.CTPrefs
import java.util.*

enum class Triggers {
    GOAL_NEARLY_COMPLETE,
    WALKING
}

class TriggerManager(
    private val context: Context,
    private val ctContextManager: CTContextManager
) {

    private val ctPrefs: CTPrefs by lazy { CTPrefs(context) }

    fun getTriggers(): List<Triggers> {
        val triggers: MutableList<Triggers> = emptyList<Triggers>().toMutableList()
        when {
            (checkGoalNearlyComplete()) -> {
                triggers.add(Triggers.GOAL_NEARLY_COMPLETE)
            }
        }
        return triggers
    }

    private fun checkGoalNearlyComplete(): Boolean {
        val isGoalNearlyComplete = ctContextManager.isGoalNearlyComplete()
        if (!isGoalNearlyComplete) {
            return isGoalNearlyComplete
        }
        val schedule = ctPrefs.getGoalNearlyCompletionSchedule()
        val isSchedulePassed = Calendar.getInstance().after(schedule)
        if (isSchedulePassed) {
            schedule.add(Calendar.HOUR_OF_DAY, 24)
            ctPrefs.setGoalNearlyCompletionSchedule(schedule)
        }
        return isSchedulePassed
    }
}