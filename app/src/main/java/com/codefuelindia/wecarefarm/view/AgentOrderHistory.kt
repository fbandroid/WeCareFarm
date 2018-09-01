package com.codefuelindia.wecarefarm.view

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
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

import kotlinx.android.synthetic.main.activity_agent_order_history.*
import kotlinx.android.synthetic.main.content_agent_order_history.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class AgentOrderHistory : AppCompatActivity() {
    var id = ""
    var from = " "

    private lateinit var getAgentOrderHistory: GetAgentOrderHistory
    private val startCalender: java.util.Calendar = java.util.Calendar.getInstance()
    private var startDay: Int = 0
    private var startMonth: Int = 0
    private var startYear: Int = 0
    private var todaysDeliveryList: ArrayList<TodayAgentDelivery> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agent_order_history)
        setSupportActionBar(toolbar)

        startDay = startCalender.get(java.util.Calendar.DAY_OF_MONTH)
        startMonth = startCalender.get(java.util.Calendar.MONTH)
        startYear = startCalender.get(java.util.Calendar.YEAR)

        if (intent != null) {
            from = intent.getStringExtra("from")
        }


        if (from == "pay") {
            tvTotalPayment.visibility = View.VISIBLE
        }

        val sharedPreferences = Utils.getSharedPreference(Constants.MY_PREF, this@AgentOrderHistory)
        id = sharedPreferences.getString(Constants.ID, "")

        getAgentOrderHistory = RetrofiltClient.getRettofitClient(Constants.BASE_URL, "x").create(GetAgentOrderHistory::class.java)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)



        if (from == "pay") {
            toolbar.title = "Payment History"
        } else {
            toolbar.title = "History"
        }

        toolbar.setNavigationOnClickListener { finish() }
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())

        tvDateFilter.text = " Date: " + currentDate

        tvDateFilter.setOnClickListener {

            showStartCalender(startCalender)


        }


        ivRefresh.setOnClickListener {



            rvAgentOrderHistory.adapter = CustomAdapter(todaysDeliveryList)

            var sum = 0.0
            for (items in todaysDeliveryList) {

                sum += items.receivedByPayment.toDouble()


            }

            tvTotalPayment.visibility = View.VISIBLE
            tvTotalPayment.text = " Collected Payment: " + sum.toString()


        }


        getAgentOrderHistory.getDeliveryHistory(id).enqueue(object : Callback<List<TodayAgentDelivery>> {
            override fun onFailure(call: Call<List<TodayAgentDelivery>>?, t: Throwable?) {

                progressBar.visibility = View.GONE


            }

            override fun onResponse(call: Call<List<TodayAgentDelivery>>?, response: Response<List<TodayAgentDelivery>>?) {

                progressBar.visibility = View.GONE

                if (response!!.isSuccessful) {

                    if (response!!.body()!!.isNotEmpty()) {

                        todaysDeliveryList = response!!.body()!! as ArrayList<TodayAgentDelivery>

                        rvAgentOrderHistory.layoutManager = LinearLayoutManager(this@AgentOrderHistory, LinearLayoutManager.VERTICAL, false)


                        var sum = 0.0
                        for (items in response!!.body() as ArrayList<TodayAgentDelivery>) {

                            sum += items.receivedByPayment.toDouble()


                        }

                        tvTotalPayment.text = " Collected Payment: " + sum.toString()


                        val customAdapter = CustomAdapter(response!!.body() as ArrayList<TodayAgentDelivery>)
                        rvAgentOrderHistory.adapter = customAdapter


                    } else {

                        Toast.makeText(this@AgentOrderHistory, "No delivery found", Toast.LENGTH_LONG).show()

                    }


                } else {
                    Toast.makeText(this@AgentOrderHistory, "No delivery found", Toast.LENGTH_LONG).show()

                }


            }


        })


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
            // val tvAddr: TextView
            val tvDate: TextView
            val status: TextView
            val tvPaymentStatus: TextView

            init {
                // Define click listener for the ViewHolder's View.
                v.setOnClickListener {

                    val intent = Intent(this@AgentOrderHistory, AgentOrderHistoryDetail::class.java)
                    intent.putExtra("data", dataSet[adapterPosition])
                    startActivity(intent)

                }

                tvOrderId = v.findViewById(R.id.tvOrderNo)
                tvUserName = v.findViewById(R.id.tvPersonName)
                tvTotalPrice = v.findViewById(R.id.tvTotalPrice)
                //tvAddr = v.findViewById(R.id.tvAddress)
                tvDate = v.findViewById(R.id.tvDeliveredDate)
                status = v.findViewById(R.id.tvStatus)
                tvPaymentStatus = v.findViewById(R.id.tvPaymentStatus)
            }
        }

        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            // Create a new view.
            val v = LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.row_agent_order_history, viewGroup, false)

            return ViewHolder(v)
        }

        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

            // Get element from your dataset at this position and replace the contents of the view
            // with that element


            val addr = dataSet[position].houseNo.plus(", ").plus(dataSet[position].societyName)
                    .plus(", ").plus(dataSet[position].landmark).plus(", ")
                    .plus(dataSet[position].city).plus(", ").plus(dataSet[position].district)

            //viewHolder.tvAddr.text = addr
            viewHolder.tvTotalPrice.text = "Total:- ".plus(dataSet[position].grandTotal)
            viewHolder.tvUserName.text = dataSet[position].name
            viewHolder.tvOrderId.text = "Order No:- ".plus(dataSet[position].orderNo)
            viewHolder.tvDate.text = "Date: ".plus(dataSet[position].timestamp)

            when {
                dataSet[position].status == "6" -> viewHolder.status.text = "Delivered"
                dataSet[position].status == "7" -> viewHolder.status.text = "Completed"
                dataSet[position].status == "3" -> viewHolder.status.text = "Canceled"
            }

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


    interface GetAgentOrderHistory {

        @POST("user/agent_order_history/")
        @FormUrlEncoded
        fun getDeliveryHistory(@Field("id") id: String): Call<List<TodayAgentDelivery>>


    }


    fun showStartCalender(startCalendar: java.util.Calendar) {

        val startDatePickerDialog = DatePickerDialog(this@AgentOrderHistory, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

            tvDateFilter.text = "Date: " + dayOfMonth.toString() + "-" + (month + 1).toString() + "-" + year.toString()

            val selectedDate = dayOfMonth.toString() + "-" + (month + 1).toString() + "-" + year.toString()

            val formattedDate = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(selectedDate)


            startDay = dayOfMonth
            startMonth = month
            startYear = year


            val filteredList = todaysDeliveryList.filter { formattedDate == getYMDDate(it.timestamp) }

            rvAgentOrderHistory.adapter = CustomAdapter(filteredList as ArrayList<TodayAgentDelivery>)

            if (filteredList.isNotEmpty()) {
                tvTotalPayment.visibility = View.VISIBLE
                var sum = 0.0
                for (items in filteredList) {

                    sum += items.receivedByPayment.toDouble()


                }

                tvTotalPayment.text = " Collected Payment: " + sum.toString()
            } else {
                tvTotalPayment.visibility = View.GONE
            }


        }, startYear, startMonth, startDay)


        startDatePickerDialog.datePicker.maxDate = Calendar.getInstance().timeInMillis
        startDatePickerDialog.show()

    }


    fun getYMDDate(dateString: String): Date {


//        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
//        val date = format.parse(dateString)

        val dateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(dateString)
        val newstring = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(dateTime)

        Log.e("formated date", newstring)

        return SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(newstring)


    }


}
