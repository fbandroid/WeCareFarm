package com.codefuelindia.wecarefarm.view

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.codefuelindia.wecarefarm.R
import com.codefuelindia.wecarefarm.Utils.Utils
import com.codefuelindia.wecarefarm.cons.Constants

import kotlinx.android.synthetic.main.activity_delivery_boy_dashboard.*
import kotlinx.android.synthetic.main.content_delivery_boy_dashboard.*

class DeliveryBoyDashboard : AppCompatActivity() {


    private var lastclick = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_boy_dashboard)
        setSupportActionBar(toolbar)

        val sharedPreferences = Utils.getSharedPreference(Constants.MY_PREF, this@DeliveryBoyDashboard)
        val userName = sharedPreferences.getString(Constants.NAME, "")

        tvUserName.text = "Welcome ".plus(userName.toUpperCase())

        cvTodayDelivery.setOnClickListener {

            if (SystemClock.elapsedRealtime() - lastclick < 2000) {
                return@setOnClickListener
            }

            lastclick = SystemClock.elapsedRealtime()

            val intent = Intent(this@DeliveryBoyDashboard, TodaysDeliveryActivity::class.java)
            intent.putExtra("from", "agent")
            startActivity(intent)


        }

        cvSearchOrder.setOnClickListener {
            if (SystemClock.elapsedRealtime() - lastclick < 2000) {
                return@setOnClickListener
            }

            lastclick = SystemClock.elapsedRealtime()

            val intent = Intent(this@DeliveryBoyDashboard, AgentOrderHistory::class.java)
            intent.putExtra("from", "all")
            startActivity(intent)

        }

        cvPaymentHistory.setOnClickListener {
            if (SystemClock.elapsedRealtime() - lastclick < 2000) {
                return@setOnClickListener
            }

            lastclick = SystemClock.elapsedRealtime()

            val intent = Intent(this@DeliveryBoyDashboard, AgentOrderHistory::class.java)
            intent.putExtra("from", "pay")
            startActivity(intent)

        }

        cvTodayCollection.setOnClickListener {
            if (SystemClock.elapsedRealtime() - lastclick < 2000) {
                return@setOnClickListener
            }

            lastclick = SystemClock.elapsedRealtime()


            val intent = Intent(this@DeliveryBoyDashboard, TodayColectionActivity::class.java)
            startActivity(intent)


        }

        cvCollectOrder.setOnClickListener {
            if (SystemClock.elapsedRealtime() - lastclick < 2000) {
                return@setOnClickListener
            }

            lastclick = SystemClock.elapsedRealtime()

            val intent = Intent(this@DeliveryBoyDashboard, CollectOrderByDeliveryAgent::class.java)
            startActivity(intent)

        }

        cvMyAccount.setOnClickListener {
            if (SystemClock.elapsedRealtime() - lastclick < 2000) {
                return@setOnClickListener
            }

            lastclick = SystemClock.elapsedRealtime()

            val intent = Intent(this@DeliveryBoyDashboard, MyAccountAgActivity::class.java)
            startActivity(intent)
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

                val editor = Utils.writeToPreference(Constants.MY_PREF, this@DeliveryBoyDashboard)
                editor.putBoolean(Constants.IS_LOGIN, false)
                editor.putString(Constants.USER_NAME, null)
                editor.putString(Constants.NAME, null)
                editor.putString(Constants.ID, null)
                editor.putString(Constants.ROLE, null)
                editor.apply()


                finishAffinity()
                startActivity(Intent(this@DeliveryBoyDashboard, LoginActivity::class.java))

                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}
