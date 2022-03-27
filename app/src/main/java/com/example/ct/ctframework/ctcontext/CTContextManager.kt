package com.example.ct.ctframework.ctcontext

import android.content.Context
import com.example.ct.ctframework.helper.CTPrefs
import kotlin.math.roundToInt

class CTContextManager(context: Context) {
    private val ctPrefs: CTPrefs by lazy { CTPrefs(context) }

    fun isGoalNearlyComplete(): Boolean {
        val steps = ctPrefs.getSteps()
        val target = ctPrefs.getTarget()
        val progress = (steps.toFloat() / target.toFloat()) * 100
        return progress.roundToInt() in 90..99
    }

}