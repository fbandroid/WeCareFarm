package com.codefuelindia.wecarefarm.view

import android.content.Intent
import android.content.SyncAdapterType
import android.os.Bundle
import android.os.SystemClock
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import com.codefuelindia.wecarefarm.R
import com.codefuelindia.wecarefarm.common.MyAdapter

import kotlinx.android.synthetic.main.activity_user_dashboard1.*
import kotlinx.android.synthetic.main.content_user_dashboard1.*

class UserDashboard1 : AppCompatActivity() {

    private val currentPage = 0
    private val XMEN = arrayOf<Int>(R.drawable.codefuellogo2, R.drawable.codefuellogo2, R.drawable.codefuellogo2, R.drawable.codefuellogo2)
    private val XMENArray = ArrayList<Int>()
    private var lastclick = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_dashboard1)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.title = "Dashboard"
        toolbar.setNavigationOnClickListener { finish() }


        frProductList.setOnClickListener {

            if (SystemClock.elapsedRealtime() - lastclick < 2000) {
                return@setOnClickListener
            }
            lastclick = SystemClock.elapsedRealtime()

            startActivity(Intent(this@UserDashboard1, HomeActivity::class.java))


        }

        frViewExecutedOrders.setOnClickListener {
            if (SystemClock.elapsedRealtime() - lastclick < 2000) {
                return@setOnClickListener
            }
            lastclick = SystemClock.elapsedRealtime()

            startActivity(Intent(this@UserDashboard1, ViewOrderActivity::class.java))

        }






    }

}
