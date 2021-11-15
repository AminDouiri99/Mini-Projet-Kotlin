package com.example.talentium

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns


import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.snackbar(message:String){
    Snackbar.make(this,"No Image selected", Snackbar.LENGTH_SHORT).also {
            snackbar ->
        snackbar.setAction("Ok"){
            snackbar.dismiss()
        }
    }.show()
}
fun ContentResolver.getFileName(uri : Uri):String{
    var name= ""
    val cursor = query(uri,null,null,null,null)
    cursor?.use {
        it.moveToFirst()
        name= cursor.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
    }
    return name
}