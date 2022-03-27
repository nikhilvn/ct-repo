package com.example.ct

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.work.*
import com.example.ct.ctframework.CTNotificationWorker
import com.example.ct.ctframework.TAG_WORKER
import com.example.ct.ctframework.TAG_WORKER_NAME
import com.example.ct.ctframework.helper.CTPrefs
import com.example.ct.ctframework.helper.CustomDateFormatter
import com.example.ct.ctframework.helper.requestPermission
import com.google.android.material.textfield.TextInputLayout
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity(), SensorEventListener {

    private var sensorManager: SensorManager? = null
    private val ctPrefs: CTPrefs by lazy { CTPrefs(this) }
    private var running = false
    private var totalSteps = 0f
    private var currentSteps = 0f
    private var target = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermission()

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        currentSteps = ctPrefs.getSteps().toFloat()
        target = ctPrefs.getTarget()

        val progress = (currentSteps / target) * 100
        findViewById<TextView>(R.id.steps_goal_target).text = target.toString()
        findViewById<TextView>(R.id.steps_goal_recorded).text = currentSteps.roundToInt().toString()
        findViewById<TextView>(R.id.steps_progress_text).text = ("${progress.roundToInt()}%")
        findViewById<CircularProgressBar>(R.id.steps_progress_bar).apply {
            setProgressWithAnimation(progress)
        }
        findViewById<Button>(R.id.edit_goal).setOnClickListener {
            showEditDialog()
        }

        val schedule = Calendar.getInstance()
        schedule.set(Calendar.HOUR_OF_DAY, 19)
        schedule.set(Calendar.MINUTE, 0)
        schedule.set(Calendar.SECOND, 0)
        val isSchedulePassed = Calendar.getInstance().after(schedule)

        if (isSchedulePassed) {
            schedule.add(Calendar.HOUR_OF_DAY, 24)
        }

        Log.wtf("isSchedulePassed", CustomDateFormatter.displayDate(schedule))
        Log.wtf("newSchedule", Calendar.getInstance().after(schedule).toString())

        val workManager = WorkManager.getInstance(this)
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(false)
            .build()
        val notificationWorkRequest = PeriodicWorkRequestBuilder<CTNotificationWorker>(1, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .addTag(TAG_WORKER)
            .build()
        workManager.enqueueUniquePeriodicWork(
            TAG_WORKER_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            notificationWorkRequest
        )
    }

    private fun showEditDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.edit_goal)
        dialog.show()
        val inputSteps = dialog.findViewById<TextInputLayout>(R.id.input_steps)
        val inputTarget = dialog.findViewById<TextInputLayout>(R.id.input_target)
        inputSteps.editText?.setText(currentSteps.roundToInt().toString())
        inputTarget.editText?.setText(target.toString())
        dialog.findViewById<Button>(R.id.save_goal).setOnClickListener {
            currentSteps = inputSteps.editText?.text.toString().toFloat()
            target = inputTarget.editText?.text.toString().toInt()
            val progress = (currentSteps / target) * 100
            findViewById<TextView>(R.id.steps_goal_target).text = target.toString()
            findViewById<TextView>(R.id.steps_goal_recorded).text = currentSteps.roundToInt().toString()
            findViewById<TextView>(R.id.steps_progress_text).text = ("${progress.roundToInt()}%")
            findViewById<CircularProgressBar>(R.id.steps_progress_bar).apply {
                setProgressWithAnimation(progress)
            }
            saveData()
            dialog.cancel()
        }
    }

    override fun onResume() {
        super.onResume()
        running = true
        val stepSensor: Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

        if (stepSensor == null) {
            Toast.makeText(
                this,
                "No step sensor detected in the device",
                Toast.LENGTH_LONG
            ).show()
        } else {
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_FASTEST)
        }
    }

    override fun onPause() {
        super.onPause()
        saveData()
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (running && event != null) {
            totalSteps = event.values[0]
            if (totalSteps.toInt() == 1) {
                currentSteps += totalSteps
                val progress = (currentSteps / target) * 100
                Log.wtf("currentSteps", currentSteps.toString())
                findViewById<TextView>(R.id.steps_goal_recorded).text = currentSteps.roundToInt().toString()
                findViewById<TextView>(R.id.steps_progress_text).text = ("${progress.roundToInt()}%")
                findViewById<CircularProgressBar>(R.id.steps_progress_bar).apply {
                    setProgressWithAnimation(progress)
                }
            }
        }
    }

    private fun saveData() {
        ctPrefs.setTarget(target)
        ctPrefs.setSteps(currentSteps.roundToInt())
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }
}