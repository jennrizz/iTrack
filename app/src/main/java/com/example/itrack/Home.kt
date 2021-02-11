package com.example.itrack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

class Home : AppCompatActivity() {
    lateinit var drawLayout: DrawerLayout
    lateinit var toolbar: Toolbar
    lateinit var bottomNav: BottomNavigationView
    lateinit var calendarFragment: CalendarFragment
    lateinit var communityFragment: communityFragment
    lateinit var graphDataFragment: graphDataFragment
    lateinit var fragmentManager: FragmentManager
    lateinit var fragmentTransaction: FragmentTransaction
    private lateinit var appBarConfiguration: AppBarConfiguration

    var lPeriodDate = " "
    var lperiod_Year = 0
    var lperiod_Month = 0
    var lperiod_Day = 0
    var avgcycle = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // set toolbar
        toolbar = findViewById<Toolbar>(R.id.nav_toolbar)
        // setSupportActionBar(toolbar)

        //late init
        drawLayout = findViewById(R.id.draw_layout)
        bottomNav = findViewById(R.id.bottomNav)
        calendarFragment = CalendarFragment()
        communityFragment = communityFragment()
        graphDataFragment = graphDataFragment()

        //call for navigationDrawer
        actionBarDrawer()

        //get intent data
        lPeriodDate = intent.getStringExtra("lPeriodDate").toString()
        lperiod_Year = intent.getIntExtra("lPeriodYear", 0)
        lperiod_Month = intent.getIntExtra("lperiod_month", 0)
        lperiod_Day = intent.getIntExtra("lperiod_Day", 0)
        avgcycle = intent.getIntExtra("avgcycle", 0)


        //bottom fragment commits
        currenBottomFragment(calendarFragment)
        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.bottomNav_calendar -> currenBottomFragment(calendarFragment)
                R.id.bottomNave_community -> currenBottomFragment(communityFragment)
                R.id.bottomNav_graphData -> currenBottomFragment(graphDataFragment)
            }
            true
        }


    }

    // toggle drawer
    private fun actionBarDrawer() {
        val toggle = ActionBarDrawerToggle(this, drawLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close)
        drawLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun onBackPressed() {
        if (drawLayout.isDrawerOpen(GravityCompat.START)) {
            drawLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    // bottom navigation fragment
    private fun currenBottomFragment(fragment: Fragment) {
        if (fragment == calendarFragment) {
            val bundle = Bundle()
            bundle.putString("lPeriodDate", lPeriodDate)
            bundle.putInt("lperiod_month", lperiod_Month)
            bundle.putInt("lperiod_Day", lperiod_Day)
            bundle.putInt("lPeriodYear", lperiod_Year)
            bundle.putInt("avgcycle", avgcycle)

            fragment.arguments = bundle
        }
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            commit()
        }
    }

    // send data to fragment
    private fun sendData() {
        //var bundle : new Bundle
        //bundle.putString("lPeriodDate", lPeriodDate)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.drawer_menu, menu)
        return true
    }





}


