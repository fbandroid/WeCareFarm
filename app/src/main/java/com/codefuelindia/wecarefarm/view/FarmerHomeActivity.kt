package com.codefuelindia.wecarefarm.view

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.codefuelindia.wecarefarm.R
import com.codefuelindia.wecarefarm.Utils.Utils
import com.codefuelindia.wecarefarm.cons.Constants
import kotlinx.android.synthetic.main.activity_farmer_home.*
import kotlinx.android.synthetic.main.app_bar_farmer_home.*
import kotlinx.android.synthetic.main.content_farmer_home.*

class FarmerHomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var lastclick = 0L

    private var title = ""
    private var id = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_farmer_home)
        setSupportActionBar(toolbar)

        val sharedPreferences = Utils.getSharedPreference(Constants.MY_PREF, this@FarmerHomeActivity)
        id = sharedPreferences.getString(Constants.ID, "")
        title = sharedPreferences.getString(Constants.NAME, "")

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar.title = "Farmer Home"
        toolbar.setNavigationOnClickListener { finish() }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        val tvTitle = nav_view.getHeaderView(0).findViewById<TextView>(R.id.tvTitleOfUser)
        tvTitle.text = title

        cvMyProduct.setOnClickListener {
            if (SystemClock.elapsedRealtime() - lastclick < 2000) {
                return@setOnClickListener
            }
            lastclick = SystemClock.elapsedRealtime()


            startActivity(Intent(this@FarmerHomeActivity, MyProductsOfFarmerActivity::class.java))

        }


        cvManageStock.setOnClickListener {
            if (SystemClock.elapsedRealtime() - lastclick < 2000) {
                return@setOnClickListener
            }
            lastclick = SystemClock.elapsedRealtime()


            startActivity(Intent(this@FarmerHomeActivity, AddStockActivity::class.java))

        }


        cvViewStock.setOnClickListener {

            if (SystemClock.elapsedRealtime() - lastclick < 2000) {
                return@setOnClickListener
            }
            lastclick = SystemClock.elapsedRealtime()


            startActivity(Intent(this@FarmerHomeActivity, FarmerProductActivity::class.java))

        }

        cvFarmerTransaction.setOnClickListener {
            if (SystemClock.elapsedRealtime() - lastclick < 2000) {
                return@setOnClickListener
            }
            lastclick = SystemClock.elapsedRealtime()

            startActivity(Intent(this@FarmerHomeActivity, FarmerAccDetailsActivity::class.java))


        }


    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.farmer_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_LogOut -> {

                val editor = Utils.writeToPreference(Constants.MY_PREF, this@FarmerHomeActivity)
                editor.putBoolean(Constants.IS_LOGIN, false)
                editor.putString(Constants.USER_NAME, null)
                editor.putString(Constants.NAME, null)
                editor.putString(Constants.ID, null)
                editor.putString(Constants.ROLE, null)
                editor.apply()


                finishAffinity()
                startActivity(Intent(this@FarmerHomeActivity, LoginActivity::class.java))





                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_offer_zone -> {
                // Handle the camera action
            }
            R.id.nav_notification -> {

            }
            R.id.nav_my_account -> {

            }

            R.id.nav_my_order -> {


            }
            R.id.nav_my_wishList -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
