package com.example.talentium

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.talentium.Settings.ManageAccountActivity
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        backButton()
        gotoManageAccount()
    }

    fun backButton() {
        backButtonWraper.setOnClickListener {
            onBackPressed();
        }
    }

    fun gotoManageAccount() {
        ManageAccount.setOnClickListener {
            val changePage = Intent(this, ManageAccountActivity::class.java)
            startActivity(changePage, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }
    }
}