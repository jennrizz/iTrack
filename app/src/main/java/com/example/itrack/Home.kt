package com.example.itrack

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.itrack.bottomNavfragments.CalendarFragment
import com.example.itrack.bottomNavfragments.communityFragment
import com.example.itrack.bottomNavfragments.graphDataFragment
import com.example.itrack.drawerNav.drawerPofile
import com.example.itrack.drawerNav.drawer_signout
import com.example.itrack.drawerNav.drawershare
import com.example.itrack.reminder.drawerReminder
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class Home() : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var drawLayout : DrawerLayout
    lateinit var toolbar : Toolbar
    lateinit var drawerNav : NavigationView
    lateinit var bottomNav  : BottomNavigationView
    lateinit var calendarFragment : CalendarFragment
    lateinit var communityFragment: communityFragment
    lateinit var graphDataFragment: graphDataFragment
    lateinit var fragmentManager: FragmentManager
    lateinit var fragmentTransaction: FragmentTransaction
    lateinit var usernameText: TextView
    lateinit var auth: FirebaseAuth
    lateinit var fstore: FirebaseFirestore
    lateinit var docRef:DocumentReference

    private var user_age = 0.0f
    private var user_pattern = " "
    private var user_avg_cycle = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)



        // set toolbar
        toolbar = findViewById<Toolbar>(R.id.nav_toolbar)
        setSupportActionBar(toolbar)

        //late init
        auth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()

        val user = auth.currentUser!!
        val userid = user.uid


        docRef = fstore.collection("userData").document(userid)

        drawLayout = findViewById(R.id.draw_layout)
        drawerNav = findViewById(R.id.nav_view)
        bottomNav = findViewById(R.id.bottomNav)
        calendarFragment = CalendarFragment()
        communityFragment = communityFragment()
        graphDataFragment = graphDataFragment()

        //handling of navigation drawer
        actionBarDrawer()
        drawerNav.setNavigationItemSelectedListener(this)
        val navheader = drawerNav.getHeaderView(0)
        usernameText = navheader.findViewById(R.id.home_username)

        docRef.addSnapshotListener {
                documentSnapshot, e ->
            if(documentSnapshot !=null) {
                var usernametxt = documentSnapshot!!.getString("username")
                usernameText.text = usernametxt
                var user_age = documentSnapshot!!.getString("age").toString().toFloat()
                var user_pattern = documentSnapshot!!.getString("menstrual pattern").toString()
                var user_avg_cycle = documentSnapshot!!.getString("avgCycle").toString().toFloat()
                var user_period_length = documentSnapshot!!.getString("periodLength").toString().toInt()
                var user_last_period_date =  documentSnapshot!!.getString("lperiodday").toString()
            }
            else{
                usernameText.text = " "
            }
        }

        //bottom fragment commits
        currenBottomFragment(calendarFragment)

        bottomNav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.bottomNav_calendar -> currenBottomFragment(calendarFragment)
                R.id.bottomNave_community -> currenBottomFragment(communityFragment)
                R.id.bottomNav_graphData -> currenBottomFragment(graphDataFragment)
            }
            true
        }


    }
    // toggle drawer
    private fun actionBarDrawer(){
        val toggle = ActionBarDrawerToggle(this, drawLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close)
        drawLayout.addDrawerListener(toggle)
        toggle.syncState()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_logOut -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, drawer_signout()).commit()
            }
            R.id.nav_prof ->{
                var intent = Intent(applicationContext, drawerPofile::class.java)
                startActivity(intent)
            }
            R.id.nav_share -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container,drawershare()).commit()
            }
            R.id.nav_reminders ->{
                startActivity(Intent(applicationContext, drawerReminder::class.java))
            }
        }


        return true
    }

    override fun onBackPressed() {
        if (drawLayout.isDrawerOpen(GravityCompat.START)) {
            drawLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    // bottom navigation fragment
    private fun currenBottomFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            commit()
        }
    }

}