package com.codefuelindia.wecarefarm.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.codefuelindia.wecarefarm.R
import com.codefuelindia.wecarefarm.Utils.UIUtils
import com.codefuelindia.wecarefarm.common.RetrofiltClient
import com.codefuelindia.wecarefarm.cons.Constants
import com.codefuelindia.wecarefarm.model.MyOrder
import com.codefuelindia.wecarefarm.model.MyRes
import com.codefuelindia.wecarefarm.model.TransactionDetail

import kotlinx.android.synthetic.main.activity_order_detail_view.*
import kotlinx.android.synthetic.main.content_order_detail_view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

class OrderDetailView : AppCompatActivity() {

    lateinit var orderData: MyOrder
    private lateinit var getDetailedOrder: GetDetailedOrder
    private var lastclick = 0L
    private lateinit var cancelOrder: CancelOrder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail_view)
        setSupportActionBar(toolbar)
        cancelOrder = cancelOrderServiceCreator()

        getDetailedOrder = orderTransactionService()

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.title = "Order detail"

        toolbar.setNavigationOnClickListener { finish() }

        if (intent != null) {
            orderData = intent.getParcelableExtra("order_data")
        }

        tvOrderId.text = "Order No:- ".plus(orderData.order_no)
        tvOrderDate.text = orderData.timestamp

        when (orderData.status) {

            "1" -> {
                tvStatus.text = "Order placed"
            }
            "2" -> {
                tvStatus.text = "Order Approved"

            }
            "3" -> {
                tvStatus.text = "Order Canceled"
                btnOrderCancel.visibility = View.GONE
            }
            "4" -> {
                tvStatus.text = "Order packed"
                btnOrderCancel.visibility = View.GONE

            }
            "5" -> {
                tvStatus.text = "In Delivery"
                btnOrderCancel.visibility = View.GONE

            }
            "6" -> {
                tvStatus.text = "Delivered"
                btnOrderCancel.visibility = View.GONE

            }
            "7" -> {
                tvStatus.text = "Competed"
                btnOrderCancel.visibility = View.GONE

            }
        }


        /**
         *    Cancel the order by user
         *
         */
        btnOrderCancel.setOnClickListener {

            if (SystemClock.elapsedRealtime() - lastclick < 2000) {
                return@setOnClickListener
            }

            lastclick = SystemClock.elapsedRealtime()

            val loader = UIUtils.showLoader(this@OrderDetailView, layoutInflater)

            // <----- alert dialog
            val builder = AlertDialog.Builder(this@OrderDetailView)
            builder.setTitle("Order Cancellation")
            builder.setMessage("Are you want to cancel order?")
            builder.setPositiveButton("YES") { dialog, which ->

                /**
                 *   Call cancel order rest service
                 *
                 */

                loader.show()

                cancelOrder.cancelOrderOfUser(orderData.order_no).enqueue(object : Callback<MyRes> {
                    override fun onFailure(call: Call<MyRes>?, t: Throwable?) {

                        loader.dismiss()
                        Toast.makeText(this@OrderDetailView, "Connection error", Toast.LENGTH_LONG).show()
                        finish()

                    }

                    override fun onResponse(call: Call<MyRes>?, response: Response<MyRes>?) {

                        loader.dismiss()

                        if (response!!.isSuccessful) {

                            if (response!!.body()!!.msg.equals("true", true)) {
                                Toast.makeText(this@OrderDetailView, "Order canceled successfully", Toast.LENGTH_LONG).show()
                                val intent = Intent(this@OrderDetailView, HomeActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                startActivity(intent)


                            } else {
                                Toast.makeText(this@OrderDetailView, "Error Occurred", Toast.LENGTH_LONG).show()
                                finish()
                            }
                        } else {
                            Toast.makeText(this@OrderDetailView, "Connection error", Toast.LENGTH_LONG).show()
                            finish()
                        }


                    }


                })

                /**
                 *  End Call cancel order rest service
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


        getDetailedOrder.orderTransactionDetail(orderData.order_no).enqueue(object : Callback<List<TransactionDetail>> {
            override fun onFailure(call: Call<List<TransactionDetail>>?, t: Throwable?) {
                progressBar.visibility = View.GONE

                Toast.makeText(this@OrderDetailView, "Error occurred", Toast.LENGTH_LONG).show()

            }

            override fun onResponse(call: Call<List<TransactionDetail>>?, response: Response<List<TransactionDetail>>?) {
                progressBar.visibility = View.GONE

                if (response!!.isSuccessful) {

                    val orderListTransaction = response!!.body()

                    if (orderListTransaction!!.isNotEmpty()) {

                        rvOrderDetailList.layoutManager = LinearLayoutManager(this@OrderDetailView,
                                LinearLayoutManager.VERTICAL, false)
                        val customAdapter = CustomAdapter(orderListTransaction as ArrayList<TransactionDetail>)
                        rvOrderDetailList.adapter = customAdapter

                        tvTotalAmount.text = "Grand Total: ".plus(orderListTransaction[0].grandTotal)


                    } else {
                        val customAdapter = CustomAdapter(orderListTransaction as ArrayList<TransactionDetail>)
                        rvOrderDetailList.adapter = customAdapter
                        Toast.makeText(this@OrderDetailView, "No history found", Toast.LENGTH_LONG).show()

                    }

                } else {
                    Toast.makeText(this@OrderDetailView, "Error occurred", Toast.LENGTH_LONG).show()

                }

            }


        })


    }


    interface GetDetailedOrder {

        @POST("user/orderdetail_api/")
        @FormUrlEncoded
        fun orderTransactionDetail(@Field("id") id: String): Call<List<TransactionDetail>>

    }

    fun orderTransactionService(): GetDetailedOrder {
        return RetrofiltClient.getRettofitClient(Constants.BASE_URL, "x").create(GetDetailedOrder::class.java)
    }


    /**
     * Caregory list
     *
     *
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    inner class CustomAdapter(private val dataSet: ArrayList<TransactionDetail>) :
            RecyclerView.Adapter<CustomAdapter.ViewHolder>() {


        /**
         * Provide a reference to the type of views that you are using (custom ViewHolder)
         */
        inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val tvProductName: TextView
            val orderTime: TextView
            val qty: TextView
            val totalAmount: TextView

            init {
                // Define click listener for the ViewHolder's View.

                tvProductName = v.findViewById(R.id.tvProductName)
                orderTime = v.findViewById(R.id.tvOrderDate)
                qty = v.findViewById(R.id.tvQty)
                totalAmount = v.findViewById(R.id.tvTotalPrice)

            }
        }

        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            // Create a new view.
            val v = LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.row_view_order, viewGroup, false)

            return ViewHolder(v)
        }

        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {


            // Get element from your dataset at this position and replace the contents of the view
            // with that element

            viewHolder.tvProductName.text = dataSet[position].name
            viewHolder.totalAmount.text = "Total: ".plus(dataSet[position].amount)
            viewHolder.orderTime.text = "Unit Price: Rs ".plus(dataSet[position].unitPrice)
            viewHolder.qty.text = "Qty: ".plus(dataSet[position].qty).plus(" " + dataSet[position].unitName)


        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = dataSet.size


    }


    interface CancelOrder {

        @POST("user/cancel_order/")
        @FormUrlEncoded
        fun cancelOrderOfUser(@Field("order_id") order_id: String): Call<MyRes>
    }

    fun cancelOrderServiceCreator(): CancelOrder {
        return RetrofiltClient.getRettofitClient(Constants.BASE_URL, "x").create(CancelOrder::class.java)
    }

}
