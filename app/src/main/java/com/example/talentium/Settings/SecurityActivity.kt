package com.example.talentium.Settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import com.example.talentium.R
import kotlinx.android.synthetic.main.activity_manage_account.*
import kotlinx.android.synthetic.main.activity_security.*

class SecurityActivity : AppCompatActivity() {
    var UpdatePasswordLayout: Boolean = true;
    var UpdatePrivacyLayout: Boolean = true;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_security)
        goBack()
        UpdatePasswordLayout = true
        UpdatePrivacyLayout = true
        openUpdatePasswordLayout()
        openPrivacyLayout()
    }
    fun openPrivacyLayout(){
        updateprivacyemenu.setOnClickListener {
            if (UpdatePrivacyLayout == false) {
                updateprivacylayout.visibility = View.GONE
                updateprivacyarrow.animate().rotationBy(-90f).setDuration(300)
                    .setInterpolator(LinearInterpolator()).start();
                UpdatePrivacyLayout = true
            }else {
                updatepasswordarrow.rotation = 0f
                UpdatePrivacyLayout = false
                updateprivacylayout.visibility = View.VISIBLE
                updateprivacyarrow.animate().rotationBy(90f).setDuration(300)
                    .setInterpolator(LinearInterpolator()).start();

            }
        }
    }
    fun openUpdatePasswordLayout() {
        updatepasswordmenu.setOnClickListener {
            if (UpdatePasswordLayout == false) {
                updatepasswordlayout.visibility = View.GONE
                updatepasswordarrow.animate().rotationBy(-90f).setDuration(300)
                    .setInterpolator(LinearInterpolator()).start();
                UpdatePasswordLayout = true
            }else {
                updatepasswordarrow.rotation = 0f
                UpdatePasswordLayout = false
                updatepasswordlayout.visibility = View.VISIBLE
                updatepasswordarrow.animate().rotationBy(90f).setDuration(300)
                    .setInterpolator(LinearInterpolator()).start();

            }
    }
    }
        fun goBack(){
        backButtonWraperofSecurity.setOnClickListener {
            onBackPressed()
        }
    }
}