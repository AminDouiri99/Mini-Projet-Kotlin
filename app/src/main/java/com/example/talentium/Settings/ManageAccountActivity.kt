package com.example.talentium.Settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.view.isVisible
import com.example.talentium.R
import kotlinx.android.synthetic.main.activity_manage_account.*
import kotlin.properties.Delegates

class ManageAccountActivity : AppCompatActivity() {
    var UpdatePicutreLayout: Boolean = true;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_account)
        //openUpdatePicutreLayout()
        UpdatePicutreLayout = true
        openUpdatePicutreLayout()
    }
    fun openUpdateInfoLayout(){

    }   
    fun openUpdatePicutreLayout() {
        updatepicturemenu.setOnClickListener {
            Log.i("lennaaa", "tenzelt")
            if (UpdatePicutreLayout == false) {

                updatephotoLayout.visibility = View.GONE
                updatepicarrow.animate().rotationBy(-90f).setDuration(300).setInterpolator( LinearInterpolator()).start();

                UpdatePicutreLayout = true
            } else {
                updatepicarrow.rotation = 0f

                UpdatePicutreLayout = false
                updatephotoLayout.visibility = View.VISIBLE
                updatepicarrow.animate().rotationBy(90f).setDuration(300).setInterpolator( LinearInterpolator()).start();

            }
        }

    }
}