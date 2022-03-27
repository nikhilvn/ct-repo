package com.example.ct.ctframework

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.ct.ctframework.ctcontext.CTContextManager
import com.example.ct.ctframework.helper.CTPrefs
import kotlin.math.roundToInt

const val TAG_WORKER = "CT_NOTIFICATION_WORKER_TAG"
const val TAG_WORKER_NAME = "CT_NOTIFICATION_WORKER_TAG_NAME"

class CTNotificationWorker(
    private val context: Context,
    workerParameters: WorkerParameters
): Worker(context, workerParameters) {

    private val ctPrefs: CTPrefs by lazy { CTPrefs(context) }

    override fun doWork(): Result {
        val ctNotificationManager = CTNotificationManager(context)
        val ctContextManager = CTContextManager(context)
        val triggerManager = TriggerManager(context, ctContextManager)
        val triggers = triggerManager.getTriggers()

        Log.d("WorkManager_works", "works")

        return when {
            (triggers.contains(Triggers.GOAL_NEARLY_COMPLETE)) -> {
                val progress = (ctPrefs.getSteps().toFloat() / ctPrefs.getTarget().toFloat()) * 100
                ctNotificationManager.notifyGoalNearlyComplete(progress.roundToInt())
                Result.success()
            }
            else -> {
                Result.success()
            }
        }
    }

}