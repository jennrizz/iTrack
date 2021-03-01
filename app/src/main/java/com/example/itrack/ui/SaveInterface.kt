package com.example.itrack.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.example.itrack.MoodTracker
import com.example.itrack.R
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.ui.AppBarConfiguration
import com.example.itrack.bottomNavfragments.CalendarFragment
import com.example.itrack.bottomNavfragments.communityFragment
import com.example.itrack.bottomNavfragments.graphDataFragment
import com.example.itrack.ui.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class SaveInterface : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_interface)
        val save =  findViewById<ImageButton>(R.id.saveBTN)
        save.setOnClickListener {
            val purposeIntent = Intent(applicationContext, MoodTracker::class.java)
            startActivity(purposeIntent)
        }


    }


}



