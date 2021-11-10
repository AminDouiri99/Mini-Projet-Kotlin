package com.example.talentium

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentContainerView
import com.example.talentium.R.anim
import com.example.talentium.R.id.buttonBacktoLogin
import com.example.talentium.R.id.fragmentContainerView2
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var  backToLogin :AppCompatButton

    lateinit var  buttonRegister :AppCompatButton
     var iamLogin : Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        val preferences: SharedPreferences =
            this.getSharedPreferences("pref", Context.MODE_PRIVATE)

        if(!preferences.getString("token",null).toString().equals("")){
            val intent = Intent (this, LandingActivity::class.java)
            startActivity(intent)
        }
            super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }
    fun init(){
        val preferences: SharedPreferences =
            this.getSharedPreferences("pref", Context.MODE_PRIVATE)

        if(!preferences.getString("token",null).toString().equals("")){
            val intent = Intent (this, LandingActivity::class.java)
            startActivity(intent)
        }
        iamLogin=true

        backToLogin=findViewById(R.id.buttonBacktoLogin)

        if(iamLogin){
            backToLogin.isVisible=false
        }
        gotoReg()
        gotoLogin()
        buttonEffect(buttonRegister)
        buttonEffect(backToLogin)
        buttonEffect(circleInsta)
        buttonEffect(circleFB)
        buttonEffect(circleGmail)

    }
    fun gotoReg(){
        buttonRegister=findViewById(R.id.buttonRegister)

        buttonRegister.setOnClickListener {

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView2, RegisterFragment())
                .setCustomAnimations(
                     R.anim.enter_from_left,
            R.anim.exit_to_left
            )
                .commit()

            backToLogin.isVisible=true
            buttonRegister.isVisible=false
        iamLogin=false}
    }
    fun gotoLogin(){
        backToLogin.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView2, LoginFragment()).setCustomAnimations(
                    R.anim.enter_from_left,
                    R.anim.exit_to_left
                ).commit()

            backToLogin.isVisible=false
            buttonRegister.isVisible=true
        iamLogin=true
        }
    }

    fun buttonEffect(button: View) {
        button.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.background.setColorFilter(-0x1f0b8adf, PorterDuff.Mode.SRC_ATOP)
                    v.invalidate()
                }
                MotionEvent.ACTION_UP -> {
                    v.background.clearColorFilter()
                    v.invalidate()
                }
            }
            false
        }
    }
}