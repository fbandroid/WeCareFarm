package com.codefuelindia.wecarefarm.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.codefuelindia.wecarefarm.R
import com.codefuelindia.wecarefarm.Utils.GlideApp
import com.codefuelindia.wecarefarm.Utils.Utils
import com.codefuelindia.wecarefarm.common.RetrofiltClient
import com.codefuelindia.wecarefarm.cons.Constants
import com.codefuelindia.wecarefarm.model.CollectedOrder
import com.codefuelindia.wecarefarm.model.MyRes
import com.codefuelindia.wecarefarm.model.TodayAgentDelivery
import com.google.gson.Gson

import kotlinx.android.synthetic.main.activity_collect_order_by_delivery_agent.*
import kotlinx.android.synthetic.main.content_collect_order_by_delivery_agent.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

class CollectOrderByDeliveryAgent : AppCompatActivity() {


    private var id: String = ""
    private lateinit var getOrderListForDelivery: GetOrderListForDelivery
    private lateinit var postCollectedOrder: PostCollectedOrder
    var lastclick = 0L
    var collectOrderList: ArrayList<TodayAgentDelivery> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collect_order_by_delivery_agent)
        setSupportActionBar(toolbar)

        getOrderListForDelivery = RetrofiltClient.getRettofitClient(Constants.BASE_URL, "x").create(GetOrderListForDelivery::class.java)
        postCollectedOrder = RetrofiltClient.getRettofitClient(Constants.BASE_URL, "x").create(PostCollectedOrder::class.java)


        val sharedPreferences = Utils.getSharedPreference(Constants.MY_PREF, this@CollectOrderByDeliveryAgent)
        id = sharedPreferences.getString(Constants.ID, "")

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar.title = "Order Collection"

        toolbar.setNavigationOnClickListener { finish() }

        btnCollect.visibility = View.GONE

        getOrderListForDelivery.getOrderList(id).enqueue(object : Callback<List<TodayAgentDelivery>> {
            override fun onFailure(call: Call<List<TodayAgentDelivery>>?, t: Throwable?) {
                progressBar.visibility = View.GONE

            }

            override fun onResponse(call: Call<List<TodayAgentDelivery>>?, response: Response<List<TodayAgentDelivery>>?) {
                progressBar.visibility = View.GONE

                if (response!!.isSuccessful) {

                    if (response!!.body()!!.isNotEmpty()) {
                        btnCollect.visibility = View.VISIBLE

                        collectOrderList = response!!.body() as ArrayList<TodayAgentDelivery>

                        rvCollectedOrderList.layoutManager = LinearLayoutManager(this@CollectOrderByDeliveryAgent, LinearLayoutManager.VERTICAL, false)

                        val customAdapter = CustomAdapter(response!!.body() as ArrayList<TodayAgentDelivery>)
                        rvCollectedOrderList.adapter = customAdapter


                    } else {

                        Toast.makeText(this@CollectOrderByDeliveryAgent, "No delivery found", Toast.LENGTH_LONG).show()

                    }


                } else {
                    Toast.makeText(this@CollectOrderByDeliveryAgent, "No delivery found", Toast.LENGTH_LONG).show()

                }

            }


        })


        btnCollect.setOnClickListener {

            var isChecked = false

            if (SystemClock.elapsedRealtime() - lastclick < 2000) {
                return@setOnClickListener
            }
            lastclick = SystemClock.elapsedRealtime()

            var collectedOrder = CollectedOrder()
            var collectedList: ArrayList<TodayAgentDelivery> = ArrayList()
            collectedOrder.id = id

            for (items in collectOrderList) {

                if (items.isChecked) {
                    isChecked = true
                    break
                }

            }

            for (items in collectOrderList) {
                if (items.isChecked) {
                    collectedList.add(items)
                }
            }

            collectedOrder.todayAgentDeliveries = collectedList

            if (isChecked) {


                val loader = showLoader()
                Log.e("collected order", Gson().toJson(collectedOrder))

                postCollectedOrder.postcollectionOfOrder(collectedOrder).enqueue(object : Callback<MyRes> {
                    override fun onFailure(call: Call<MyRes>?, t: Throwable?) {
                        Toast.makeText(this@CollectOrderByDeliveryAgent, "Error occurred", Toast.LENGTH_LONG).show()

                        if (loader.isShowing) {
                            loader.dismiss()
                        }

                    }

                    override fun onResponse(call: Call<MyRes>?, response: Response<MyRes>?) {

                        if (loader.isShowing) {
                            loader.dismiss()
                        }

                        if (response!!.isSuccessful) {

                            if (response!!.body()!!.msg.equals("true", true)) {

                                Toast.makeText(this@CollectOrderByDeliveryAgent, "Successfully Collected", Toast.LENGTH_LONG).show()
                                finish()
                            } else {
                                Toast.makeText(this@CollectOrderByDeliveryAgent, "Not Successfully Collected", Toast.LENGTH_LONG).show()

                            }


                        } else {
                            Toast.makeText(this@CollectOrderByDeliveryAgent, "Not Successfully Collected", Toast.LENGTH_LONG).show()

                        }


                    }

                })

            } else {
                Toast.makeText(this@CollectOrderByDeliveryAgent, "Select any order", Toast.LENGTH_LONG).show()
            }


        }


    }

    interface GetOrderListForDelivery {

        @POST("user/agent_tick_orderapi/")
        @FormUrlEncoded
        fun getOrderList(@Field("id") id: String): Call<List<TodayAgentDelivery>>


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
            val cbSelect: CheckBox

            init {
                // Define click listener for the ViewHolder's View.


                tvOrderId = v.findViewById(R.id.tvOrderId)
                tvUserName = v.findViewById(R.id.tvPersonName)
                tvTotalPrice = v.findViewById(R.id.tvTotalPrice)
                tvAddr = v.findViewById(R.id.tvAddress)
                cbSelect = v.findViewById(R.id.cbSelect)

                cbSelect.visibility = View.VISIBLE

                cbSelect.setOnCheckedChangeListener { buttonView, isChecked ->

                    dataSet[adapterPosition].isChecked = isChecked


                }
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

            // viewHolder.itemView.isClickable = from != "all"


            viewHolder.cbSelect.isChecked = dataSet[position].isChecked

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


    interface PostCollectedOrder {

        @POST("user/agent_tick_orderapi/")
        fun postcollectionOfOrder(@Body collected_order: CollectedOrder): Call<MyRes>


    }


    fun showLoader(): AlertDialog {

        val dialogBuilder = AlertDialog.Builder(this@CollectOrderByDeliveryAgent as Context)


        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.alert_label_editor, null)
        dialogBuilder.setView(dialogView)

        GlideApp.with(this@CollectOrderByDeliveryAgent)
                .load(this@CollectOrderByDeliveryAgent?.getDrawable(R.drawable.loader)).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(dialogView.findViewById<ImageView>(R.id.ivGif))
        dialogBuilder.setCancelable(false)
        val alertDialog = dialogBuilder.create()
        alertDialog.window.setBackgroundDrawable(this@CollectOrderByDeliveryAgent?.getDrawable(android.R.color.transparent))
        alertDialog.show()

        return alertDialog


    }

}
