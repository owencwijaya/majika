package com.example.majika.utils

import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class ManagePermission(private val activity: Activity, val permissionArray : Array<String>, private val code : Int) {

    // check permission
    fun checkPermissions() : Boolean {
//        will call request permission, if permission is not granted
        if (allPermissionsGranted()) return true
        requestPermission()
        return false
    }


    private fun deniedPermission(): String {
        for (permission in permissionArray) {
            if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_DENIED)
                return permission
        }
        return ""
    }
    private fun allPermissionsGranted() = permissionArray.all {
        ContextCompat.checkSelfPermission(activity, it) == PackageManager.PERMISSION_GRANTED
    }
    private fun requestPermission() {
        val permission = deniedPermission()
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            Toast.makeText(activity, "Please grant permission", Toast.LENGTH_SHORT).show()
        }
        println("permission: $permission")

        ActivityCompat.requestPermissions(activity, permissionArray, code)
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