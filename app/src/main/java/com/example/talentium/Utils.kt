package com.example.talentium

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import android.provider.SyncStateContract


import android.view.View
import com.google.android.material.snackbar.Snackbar
import java.io.File
val outputPath: String
    get() {
        val path = Environment.getExternalStorageDirectory().toString() + File.separator + "com.example.talentium" + File.separator

        val folder = File(path)
        if (!folder.exists())
            folder.mkdirs()

        return path
    }
fun View.snackbar(message: String) {
    Snackbar.make(this, "No Image selected", Snackbar.LENGTH_SHORT).also { snackbar ->
        snackbar.setAction("Ok") {
            snackbar.dismiss()
        }
    }.show()
}
fun ContentResolver.getFileName(uri: Uri): String {
    var name = ""
    val cursor = query(uri, null, null, null, null)
    cursor?.use {
        it.moveToFirst()
        name = cursor.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
    }
    return name
}