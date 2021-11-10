package com.example.talentium

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_landing.*

class LandingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.feedPage -> {
                    // Respond to navigation item 1 click
                    true
                }
                R.id.livePage -> {
                    // Respond to navigation item 2 click
                    true
                }
                R.id.profilePage->{

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView, RegisterFragment())
                        .setCustomAnimations(
                            R.anim.enter_from_left,
                            R.anim.exit_to_left
                        )
                        .commit()

                    true
                }
                else -> false
            }
        }
    }
}