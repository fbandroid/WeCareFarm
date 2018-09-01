package com.codefuelindia.wecarefarm.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.codefuelindia.wecarefarm.R
import com.codefuelindia.wecarefarm.Utils.Utils
import com.codefuelindia.wecarefarm.common.RetrofiltClient
import com.codefuelindia.wecarefarm.cons.Constants
import com.codefuelindia.wecarefarm.model.TodayAgentDelivery

import kotlinx.android.synthetic.main.activity_todays_delivery.*
import kotlinx.android.synthetic.main.content_todays_delivery.*

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

class TodaysDeliveryActivity : AppCompatActivity() {

    var id = ""
    private lateinit var getTodaysDeliveryList: GetTodaysDeliveryList

    private var from = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todays_delivery)
        setSupportActionBar(toolbar)

        getTodaysDeliveryList = todayDeliveryListServiceCreator()

        val sharedPreferences = Utils.getSharedPreference(Constants.MY_PREF, this@TodaysDeliveryActivity)
        id = sharedPreferences.getString(Constants.ID, "")

        if (intent != null) {

            from = intent.getStringExtra("from")

        }


        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        toolbar.setNavigationOnClickListener {
            finish()
        }

        if (from == "all") {
            toolbar.title = "View Order"
            getTodaysDeliveryList.getToadayDeliveryOfAgent(" ").enqueue(object : Callback<List<TodayAgentDelivery>> {
                override fun onFailure(call: Call<List<TodayAgentDelivery>>?, t: Throwable?) {
                    progressBar.visibility = View.GONE

                }

                override fun onResponse(call: Call<List<TodayAgentDelivery>>?, response: Response<List<TodayAgentDelivery>>?) {
                    progressBar.visibility = View.GONE

                    if (response!!.isSuccessful) {

                        if (response!!.body()!!.isNotEmpty()) {

                            rvTodayDeliveryList.layoutManager = LinearLayoutManager(this@TodaysDeliveryActivity, LinearLayoutManager.VERTICAL, false)

                            val customAdapter = CustomAdapter(response!!.body() as ArrayList<TodayAgentDelivery>)
                            rvTodayDeliveryList.adapter = customAdapter


                        } else {

                            Toast.makeText(this@TodaysDeliveryActivity, "No delivery found", Toast.LENGTH_LONG).show()

                        }


                    } else {
                        Toast.makeText(this@TodaysDeliveryActivity, "No delivery found", Toast.LENGTH_LONG).show()

                    }

                }


            })


        } else {

            toolbar.title = "Today's Your Delivery"
            getTodaysDeliveryList.getToadayDeliveryOfAgent(id).enqueue(object : Callback<List<TodayAgentDelivery>> {
                override fun onFailure(call: Call<List<TodayAgentDelivery>>?, t: Throwable?) {
                    progressBar.visibility = View.GONE

                }

                override fun onResponse(call: Call<List<TodayAgentDelivery>>?, response: Response<List<TodayAgentDelivery>>?) {
                    progressBar.visibility = View.GONE

                    if (response!!.isSuccessful) {

                        if (response!!.body()!!.isNotEmpty()) {

                            rvTodayDeliveryList.layoutManager = LinearLayoutManager(this@TodaysDeliveryActivity, LinearLayoutManager.VERTICAL, false)

                            val customAdapter = CustomAdapter(response!!.body() as ArrayList<TodayAgentDelivery>)
                            rvTodayDeliveryList.adapter = customAdapter


                        } else {

                            Toast.makeText(this@TodaysDeliveryActivity, "No delivery found", Toast.LENGTH_LONG).show()

                        }


                    } else {
                        Toast.makeText(this@TodaysDeliveryActivity, "No delivery found", Toast.LENGTH_LONG).show()

                    }

                }


            })

        }


    }


    /**
     * Provide views to RecyclerView with data from dataSet.
     *
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    inner class CustomAdapter(private val dataSet: ArrayList<TodayAgentDelivery>) :
            RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

        /**
         * Provide a reference to the type of views that you are using (custom ViewHolder)
         */
        inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

            val tvOrderId: TextView
            val tvUserName: TextView
            val tvTotalPrice: TextView
            val tvAddr: TextView

            init {
                // Define click listener for the ViewHolder's View.
                v.setOnClickListener {

                    val intent = Intent(this@TodaysDeliveryActivity, ApproveDeliveryByAgent::class.java)
                    intent.putExtra("data", dataSet[adapterPosition])
                    startActivity(intent)

                }

                tvOrderId = v.findViewById(R.id.tvOrderId)
                tvUserName = v.findViewById(R.id.tvPersonName)
                tvTotalPrice = v.findViewById(R.id.tvTotalPrice)
                tvAddr = v.findViewById(R.id.tvAddress)
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

            viewHolder.itemView.isClickable = from != "all"


            val addr = dataSet[position].houseNo.plus(", ").plus(dataSet[position].societyName)
                    .plus(", ").plus(dataSet[position].landmark).plus(", ")
                    .plus(dataSet[position].city).plus(", ").plus(dataSet[position].district)

            viewHolder.tvAddr.text = addr
            viewHolder.tvTotalPrice.text = "Total:- ".plus(dataSet[position].grandTotal)
            viewHolder.tvUserName.text = dataSet[position].name
            viewHolder.tvOrderId.text = "Order No:- ".plus(dataSet[position].orderNo)


        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = dataSet.size


    }


    interface GetTodaysDeliveryList {

        @POST("user/get_agent_order/")
        @FormUrlEncoded
        fun getToadayDeliveryOfAgent(@Field("id") id: String): Call<List<TodayAgentDelivery>>


    }

    fun todayDeliveryListServiceCreator(): GetTodaysDeliveryList {
        return RetrofiltClient.getRettofitClient(Constants.BASE_URL, "x").create(GetTodaysDeliveryList::class.java)
    }


}
