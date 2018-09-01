package com.codefuelindia.wecarefarm.view

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import com.codefuelindia.wecarefarm.R
import com.codefuelindia.wecarefarm.model.TodayAgentDelivery

import kotlinx.android.synthetic.main.activity_agent_order_history_detail.*
import kotlinx.android.synthetic.main.content_agent_order_history_detail.*

class AgentOrderHistoryDetail : AppCompatActivity() {


    var orderHistory: TodayAgentDelivery? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agent_order_history_detail)
        setSupportActionBar(toolbar)

        if (intent != null) {
            orderHistory = intent.getParcelableExtra("data")
        }

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        toolbar.title = "Order detail"
        toolbar.setNavigationOnClickListener { finish() }


        if (orderHistory != null) {

            tvName.text = orderHistory!!.name
            tvDate.text = orderHistory!!.timestamp
            tvOrderNo.text = orderHistory!!.orderNo
            tvTotalAmount.text = orderHistory!!.grandTotal
            tvReceivedAmount.text = orderHistory!!.receivedByPayment


            when {
                orderHistory!!.status == "6" -> tvStatus.text = "Delivered"
                orderHistory!!.status == "7" -> tvStatus.text = "Completed"
                orderHistory!!.status == "3" -> tvStatus.text = "Canceled"
            }


        }


    }

}
