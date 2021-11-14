package com.example.talentium

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_landing.*

class LandingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
makeCurrentFragment(FeedFragment());
        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {

                R.id.feedPage -> {

                    // Respond to navigation item 1 click
                    Log.i("feed","feed")
                    makeCurrentFragment(FeedFragment());

                }
                R.id.livePage -> {
                    // Respond to navigation item 2 click
                    true
                }
                R.id.profilePage->{

                    makeCurrentFragment(ProfileFragment());


                }

            }
            true
        }
    }

    private fun makeCurrentFragment(fragment:Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView,fragment).setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right).commit()

        }
    }
}