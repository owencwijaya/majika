package com.example.majika.utils

import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.majika.MainActivity

class ManagePermission(val activity: Activity, val list: List<String>, val code: Int) {

    // check permission
    fun checkPermissions(){
        if (isPermissionGranted() != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(activity, "Please grant the permission request", Toast.LENGTH_SHORT).show()
        }
    }

    // check permission house
    private fun isPermissionGranted() : Int {
        var counter = 0
        for (permission in list) {
            counter += ContextCompat.checkSelfPermission(activity, permission)
        }
        return counter
    }

    private fun deniedPermission(): String {
        for (permission in list) {
            if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_DENIED)
                return permission
        }
        return ""
    }

    private fun requestPermission() {
        val permission = deniedPermission()
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            Toast.makeText(activity, "Please grant permission", Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.requestPermissions(activity, list.toTypedArray(), code)
        }
    }

    fun processPermissionResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) : Boolean {
        var result = 0
        if (grantResults.isNotEmpty()) {
            for (item in grantResults) {
                result += item
            }
        }
        if (result == PackageManager.PERMISSION_GRANTED) return true
        return false
    }

}