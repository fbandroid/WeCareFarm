package com.codefuelindia.wecarefarm.view

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import com.codefuelindia.wecarefarm.R
import com.codefuelindia.wecarefarm.Utils.Utils
import com.codefuelindia.wecarefarm.common.RetrofiltClient
import com.codefuelindia.wecarefarm.cons.Constants
import com.codefuelindia.wecarefarm.model.TodayCollectionModel
import kotlinx.android.synthetic.main.activity_today_colection.*
import kotlinx.android.synthetic.main.content_delivery_boy_dashboard.*
import kotlinx.android.synthetic.main.row_todays_delivery.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

class TodayColectionActivity : AppCompatActivity() {


    private var id = " "
    private lateinit var getTodayCollection: GetTodayCollection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_today_colection)

        setSupportActionBar(toolbar)


        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        toolbar.title = "Today's Collection"

        toolbar.setNavigationOnClickListener { finish() }

        getTodayCollection = RetrofiltClient.getRettofitClient(Constants.BASE_URL, "x").create(GetTodayCollection::class.java)

        id = Utils.getSharedPreference(Constants.MY_PREF, this@TodayColectionActivity).getString(Constants.ID, "")

        rvTodayCollectionList.layoutManager = LinearLayoutManager(this@TodayColectionActivity, LinearLayoutManager.VERTICAL, false)


        getTodayCollection.todayCollectionapi(id).enqueue(object : Callback<List<TodayCollectionModel>> {
            override fun onFailure(call: Call<List<TodayCollectionModel>>?, t: Throwable?) {

                progressBar.visibility = View.GONE
                Toast.makeText(this@TodayColectionActivity, "Connection error", Toast.LENGTH_LONG).show()


            }

            override fun onResponse(call: Call<List<TodayCollectionModel>>?, response: Response<List<TodayCollectionModel>>?) {

                progressBar.visibility = View.GONE

                if (response!!.isSuccessful) {

                    if (response!!.body()!!.isNotEmpty()) {

                        rvTodayCollectionList.adapter = CustomAdapter(response!!.body()!! as ArrayList<TodayCollectionModel>)

                        var sum = 0.0
                        for (items in response!!.body()!! as ArrayList<TodayCollectionModel>) {

                            sum += items.receivedByPayment.toDouble()


                        }

                        tvTotalCollection.text = " Received Amount:- ".plus(sum.toString())


                    } else {
                        Toast.makeText(this@TodayColectionActivity, "No data found", Toast.LENGTH_LONG).show()
                    }

                } else {
                    Toast.makeText(this@TodayColectionActivity, "Connection error", Toast.LENGTH_LONG).show()

                }


            }


        })


    }


    interface GetTodayCollection {

        @POST("user/today_delivery_product_listapi/")
        @FormUrlEncoded
        fun todayCollectionapi(@Field("aid") aid: String): Call<List<TodayCollectionModel>>

    }


    /**
     * Provide views to RecyclerView with data from dataSet.
     *
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    inner class CustomAdapter(private val dataSet: ArrayList<TodayCollectionModel>) :
            RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

        /**
         * Provide a reference to the type of views that you are using (custom ViewHolder)
         */
        inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

            val tvOrderId: TextView
            val tvUserName: TextView
            val tvTotalPrice: TextView
            val tvAddr: TextView
            val tvShippingDate: TextView
            val tvPaymentStatus: TextView
            val tvReceivedAmount: TextView

            init {
                // Define click listener for the ViewHolder's View.
                v.setOnClickListener {


                }

                tvOrderId = v.findViewById(R.id.tvOrderId)
                tvUserName = v.findViewById(R.id.tvPersonName)
                tvTotalPrice = v.findViewById(R.id.tvTotalPrice)
                tvAddr = v.findViewById(R.id.tvAddress)
                tvPaymentStatus = v.findViewById(R.id.tvPaymentStatus)
                tvShippingDate = v.findViewById(R.id.tvOrderDate)
                tvReceivedAmount = v.findViewById(R.id.tvReceivedAmount)


                tvUserName.visibility = View.GONE
                tvAddr.visibility = View.GONE
                tvPaymentStatus.visibility = View.VISIBLE
                tvReceivedAmount.visibility = View.VISIBLE
            }
        }

        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            // Create a new view.
            val v = LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.row_todays_delivery, viewGroup, false)

            return ViewHolder(v)
        }

        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

            // Get element from your dataset at this position and replace the contents of the view
            // with that element


            viewHolder.tvTotalPrice.text = "Total:- ".plus(dataSet[position].grandTotal)
            viewHolder.tvOrderId.text = "Order No:- ".plus(dataSet[position].orderNo)
            viewHolder.tvShippingDate.text = "Order packed Date:-".plus(dataSet[position].shippingDate)
            viewHolder.tvReceivedAmount.text = "Received:-  ".plus(dataSet[position].receivedByPayment)


            when {
                dataSet[position].receivedByPayment == "0" -> {

                    viewHolder.tvPaymentStatus.text = "Payment Not Approved"
                    viewHolder.tvPaymentStatus.setTextColor(Color.RED)

                }
                dataSet[position].receivedByPayment == "1" -> {
                    viewHolder.tvPaymentStatus.text = "Payment Approved"
                    viewHolder.tvPaymentStatus.setTextColor(Color.GREEN)


                }
                dataSet[position].receivedByPayment == "2" -> {
                    viewHolder.tvPaymentStatus.text = "Payment Approved"
                    viewHolder.tvPaymentStatus.setTextColor(Color.GREEN)


                }
            }


        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = dataSet.size


    }


}
