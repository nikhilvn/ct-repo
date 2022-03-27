package com.example.ct.ctframework.helper

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ct.R

const val PERMISSION_REQUEST_ACTIVITY_RECOGNITION = 1000

fun Activity.requestPermission() {
  if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
    != PackageManager.PERMISSION_GRANTED) {
    val arrayOfPermission: Array<String> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      arrayOf(Manifest.permission.ACTIVITY_RECOGNITION)
    }else{
      arrayOf("com.android.gms.permission.ACTIVITY_RECOGNITION")
    }
    ActivityCompat.requestPermissions(this, arrayOfPermission, PERMISSION_REQUEST_ACTIVITY_RECOGNITION)
  }
}

fun Activity.isPermissionGranted(): Boolean {
  val isAndroidQOrLater: Boolean =
      android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q

  return if (isAndroidQOrLater.not()) {
    true
  } else {
    PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACTIVITY_RECOGNITION
    )
  }
}

private fun showRationalDialog(activity: Activity) {
  AlertDialog.Builder(activity).apply {
    setTitle(R.string.permission_rational_dialog_title)
    setMessage(R.string.permission_rational_dialog_message)
    setPositiveButton(R.string.permission_rational_dialog_positive_button_text) { _, _ ->
      ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
          PERMISSION_REQUEST_ACTIVITY_RECOGNITION)
    }
    setNegativeButton(R.string.permission_rational_dialog_negative_button_text){ dialog, _ ->
      dialog.dismiss()
    }
  }.run {
    create()
    show()
  }
}