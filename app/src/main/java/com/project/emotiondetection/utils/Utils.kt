package com.project.learnmlkit.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat



fun Context.hasPermission(permission:String):Boolean{
    return ContextCompat.checkSelfPermission(this,permission) == PackageManager.PERMISSION_GRANTED
}

inline fun Context.onDeniedCameraPermissionRequest(crossinline positive:()->Unit){
    AlertDialog.Builder(this)
        .setTitle("Camera Permission Required")
        .setMessage("Without accessing the camera it is not possible to Scan QR Code.")
        .setPositiveButton("Grant"){ dialog, _ ->
            positive.invoke()
            dialog.dismiss()
        }
        .setNegativeButton("Cancel"){ dialog, _ ->
            dialog.dismiss()
        }
        .setCancelable(false)
        .show()
}

fun Context.openAppPermissionSettings(){
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package",packageName,null)
    ).also(::startActivity)
}
