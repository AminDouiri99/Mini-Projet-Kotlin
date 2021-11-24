package com.example.talentium.constants

import android.Manifest


object VideoConstants {
    const val TAG="cameraX"
    const val FILE_NAME_FORMAT="yy-MM-dd-HH-mm-ss-SSS"
    const val REQUEST_CODE_PERMISSION=123
    val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO)
}