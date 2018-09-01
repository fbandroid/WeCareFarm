package com.codefuelindia.wecarefarm.view

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity;
import android.view.View
import android.widget.Toast
import com.codefuelindia.wecarefarm.R
import com.codefuelindia.wecarefarm.Utils.UIUtils
import com.codefuelindia.wecarefarm.Utils.Utils
import com.codefuelindia.wecarefarm.common.RetrofiltClient
import com.codefuelindia.wecarefarm.cons.Constants
import com.codefuelindia.wecarefarm.model.MyRes
import com.codefuelindia.wecarefarm.model.TodayAgentDelivery

import kotlinx.android.synthetic.main.activity_approve_delivery_by_agent.*
import kotlinx.android.synthetic.main.content_approve_delivery_by_agent.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

class ApproveDeliveryByAgent : AppCompatActivity() {

    private var lastclick = 0L
    private lateinit var approveDelivery: ApproveDelivery
    private var id = ""
    var status = " "
    lateinit var orderDetail: TodayAgentDelivery

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_approve_delivery_by_agent)
        setSupportActionBar(toolbar)

        approveDelivery = approveOrderServiceCreator()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val sharedPreferences = Utils.getSharedPreference(Constants.MY_PREF, this@ApproveDeliveryByAgent)
        id = sharedPreferences.getString(Constants.ID, "")

        if (intent != null) {

            orderDetail = intent.getParcelableExtra("data")

        }



        if (orderDetail != null) {

            val addr = orderDetail.houseNo.plus(", ").plus(orderDetail.societyName)
                    .plus(", ").plus(orderDetail.landmark).plus(", ")
                    .plus(orderDetail.city).plus(", ").plus(orderDetail.district)

            tvAddr.text = addr
            tvName.text = orderDetail.name
            tvOrderId.text = "Order ID: ".plus(orderDetail.orderNo)
            tvTotalPrice.text = "Total: ".plus(orderDetail.grandTotal)
            tvMobile.text = "Mobile: ".plus(orderDetail.mobile)

            when (orderDetail.status) {

                "1" -> {

                }
                "2" -> {
                    tvStatus.text = "Order Approved"
                }
                "3" -> {
                    tvStatus.text = "Order canceled"
                    btnCancelDelivery.visibility = View.GONE
                    txtInReceivedBy.visibility = View.GONE
                    btnMakeDelivery.visibility = View.GONE
                    txtInReceivedAmount.visibility = View.GONE


                }
                "4" -> {
                    tvStatus.text = "Order Packed"
                }
                "5" -> {
                    tvStatus.text = "In Delivery"
                }
                "6" -> {

                    tvStatus.text = "Order Delivered"

                    btnMakeDelivery.visibility = View.GONE
                    btnCancelDelivery.visibility = View.GONE
                    txtInReceivedBy.visibility = View.GONE
                    txtInReceivedAmount.visibility = View.GONE

                }
                "7" -> {
                    tvStatus.text = "Order Completed"
                    btnMakeDelivery.visibility = View.GONE
                    btnCancelDelivery.visibility = View.GONE
                    txtInReceivedBy.visibility = View.GONE
                    txtInReceivedAmount.visibility = View.GONE

                }


            }


        }


        toolbar.title = "Approve Delivery"

        toolbar.setNavigationOnClickListener { finish() }

        btnMakeDelivery.setOnClickListener {

            if (SystemClock.elapsedRealtime() - lastclick < 2000) {
                return@setOnClickListener
            }

            lastclick = SystemClock.elapsedRealtime()
            status = "6"


            if (edtReceivedAmount.text.toString().trim().isEmpty()) {
                Toast.makeText(this@ApproveDeliveryByAgent, "Enter received Amount", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }


            val loader = UIUtils.showLoader(this@ApproveDeliveryByAgent, layoutInflater)

            // <----- alert dialog
            val builder = AlertDialog.Builder(this@ApproveDeliveryByAgent)
            builder.setTitle("Approve Delivery")
            builder.setMessage("Are you want to Deliver Order?")
            builder.setPositiveButton("YES") { dialog, which ->

                /**
                 *   Call Deliver order rest service
                 *   status:- 6
                 */

                loader.show()

                approveDelivery.approveDeliveryOfUser(id, orderDetail.orderNo, edtReceivedBy.text.toString().trim(), "6",
                        edtReceivedAmount.text.toString().trim())
                        .enqueue(object : Callback<MyRes> {
                            override fun onFailure(call: Call<MyRes>?, t: Throwable?) {
                                loader.dismiss()

                                Toast.makeText(this@ApproveDeliveryByAgent, "Connection error", Toast.LENGTH_LONG).show()
                                finish()

                            }

                            override fun onResponse(call: Call<MyRes>?, response: Response<MyRes>?) {
                                loader.dismiss()


                                if (response!!.isSuccessful) {

                                    if (response!!.body()!!.msg.equals("true", true)) {
                                        Toast.makeText(this@ApproveDeliveryByAgent, "Successfully Delivered", Toast.LENGTH_LONG).show()

                                        val intent = Intent(this@ApproveDeliveryByAgent, DeliveryBoyDashboard::class.java)
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                        startActivity(intent)


                                    } else {

                                    }


                                } else {
                                    Toast.makeText(this@ApproveDeliveryByAgent, "Connection error", Toast.LENGTH_LONG).show()
                                    finish()

                                }

                            }


                        })


                /**
                 *  End Call Deliver order rest service
                 *
                 */


            }
            builder.setNegativeButton("No") { dialog, which ->


            }

            val dialog: AlertDialog = builder.create()
            dialog.show()
            // ---> end alert dialog

        }
        /**
         *   end Deliver order
         *
         */


        /**
         *    Start Order cancel
         *
         *    status- 3
         */

        btnCancelDelivery.setOnClickListener {

            if (SystemClock.elapsedRealtime() - lastclick < 2000) {
                return@setOnClickListener
            }

            status = "3"

            lastclick = SystemClock.elapsedRealtime()

            if (edtReceivedBy.text.toString().trim().isEmpty()) {

                edtReceivedBy.requestFocus()
                Toast.makeText(this@ApproveDeliveryByAgent, "Enter remark", Toast.LENGTH_LONG).show()

                return@setOnClickListener
            }


            val loader = UIUtils.showLoader(this@ApproveDeliveryByAgent, layoutInflater)

            // <----- alert dialog
            val builder = AlertDialog.Builder(this@ApproveDeliveryByAgent)
            builder.setTitle("Cancel Delivery")
            builder.setMessage("Are you want to Cancel Order?")
            builder.setPositiveButton("YES") { dialog, which ->

                /**
                 *   Call Cancel order rest service
                 *
                 */

                loader.show()



                approveDelivery.approveDeliveryOfUser(id, orderDetail.orderNo, edtReceivedBy.text.toString().trim(), "3", "0")
                        .enqueue(object : Callback<MyRes> {
                            override fun onFailure(call: Call<MyRes>?, t: Throwable?) {
                                loader.dismiss()

                                Toast.makeText(this@ApproveDeliveryByAgent, "Connection error", Toast.LENGTH_LONG).show()
                                finish()

                            }

                            override fun onResponse(call: Call<MyRes>?, response: Response<MyRes>?) {
                                loader.dismiss()


                                if (response!!.isSuccessful) {

                                    if (response!!.body()!!.msg.equals("true", true)) {
                                        Toast.makeText(this@ApproveDeliveryByAgent, "Successfully Canceled", Toast.LENGTH_LONG).show()

                                        val intent = Intent(this@ApproveDeliveryByAgent, DeliveryBoyDashboard::class.java)
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                        startActivity(intent)


                                    } else {

                                    }


                                } else {
                                    Toast.makeText(this@ApproveDeliveryByAgent, "Connection error", Toast.LENGTH_LONG).show()
                                    finish()

                                }

                            }


                        })


                /**
                 *  End Call Cancel order rest service
                 *
                 */


            }
            builder.setNegativeButton("No") { dialog, which ->


            }

            val dialog: AlertDialog = builder.create()
            dialog.show()
            // ---> end alert dialog

        }
        /**
         *   end cancel order
         *
         */


    }


    interface ApproveDelivery {

        @POST("user/agent_order_status/")
        @FormUrlEncoded
        fun approveDeliveryOfUser(@Field("id") id: String, @Field("orderno") orderno: String
                                  , @Field("remark") remark: String, @Field("status") status: String,
                                  @Field("received_amount") received_amount: String
        )
                : Call<MyRes>
    }

    private fun approveOrderServiceCreator(): ApproveDelivery {
        return RetrofiltClient.getRettofitClient(Constants.BASE_URL, "x").create(ApproveDelivery::class.java)
    }


}







