package com.codefuelindia.wecarefarm.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.codefuelindia.wecarefarm.R
import com.codefuelindia.wecarefarm.Utils.Utils
import com.codefuelindia.wecarefarm.common.RetrofiltClient
import com.codefuelindia.wecarefarm.cons.Constants
import com.codefuelindia.wecarefarm.model.MyOrder

import kotlinx.android.synthetic.main.activity_view_order.*
import kotlinx.android.synthetic.main.content_view_order.*
import kotlinx.android.synthetic.main.fragment_verify_otp.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

class ViewOrderActivity : AppCompatActivity() {

    private var id = ""
    private lateinit var myOrderList: MyOrderList
    private var orderlist: ArrayList<MyOrder> = ArrayList()
    private lateinit var customAdapter: CustomAdapter
    private var filteredOrderList: ArrayList<MyOrder> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_order)
        setSupportActionBar(toolbar)

        myOrderList = myOrderListService()

        val sharedPreferences = Utils.getSharedPreference(Constants.MY_PREF, this@ViewOrderActivity)
        id = sharedPreferences.getString(Constants.ID, "")

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.title = "My Order"
        toolbar.setNavigationOnClickListener { finish() }

        rvMyOrderList.layoutManager = LinearLayoutManager(this@ViewOrderActivity, LinearLayoutManager.VERTICAL, false)


        spFilterBy.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {


            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                when (position) {

                    0 -> { // All

                        filteredOrderList = orderlist
                        rvMyOrderList.adapter = CustomAdapter(filteredOrderList)


                    }

                    1 -> { // order placed


                        filteredOrderList = (orderlist.filter { it.status == "1" }) as ArrayList<MyOrder>
                        rvMyOrderList.adapter = CustomAdapter(filteredOrderList)


                    }

                    2 -> { // order canceled
                        filteredOrderList = (orderlist.filter { it.status == "3" }) as ArrayList<MyOrder>
                        rvMyOrderList.adapter = CustomAdapter(filteredOrderList)


                    }

                    3 -> { // order approve
                        filteredOrderList = (orderlist.filter { it.status == "2" }) as ArrayList<MyOrder>
                        rvMyOrderList.adapter = CustomAdapter(filteredOrderList)


                    }

                    4 -> { // order deliver
                        filteredOrderList = (orderlist.filter { it.status == "6" }) as ArrayList<MyOrder>
                        rvMyOrderList.adapter = CustomAdapter(filteredOrderList)


                    }

                    5 -> { // order completed
                        filteredOrderList = (orderlist.filter { it.status == "7" }) as ArrayList<MyOrder>
                        rvMyOrderList.adapter = CustomAdapter(filteredOrderList)


                    }


                }


            }


        }




        myOrderList.getMyOrderListApi(id).enqueue(object : Callback<List<MyOrder>> {
            override fun onFailure(call: Call<List<MyOrder>>?, t: Throwable?) {
                progressBar.visibility = View.GONE


            }

            override fun onResponse(call: Call<List<MyOrder>>?, response: Response<List<MyOrder>>?) {
                progressBar.visibility = View.GONE

                if (response!!.isSuccessful) {

                    if (response.body()!!.isNotEmpty()) {

                        orderlist = response.body() as ArrayList<MyOrder>
                        customAdapter = CustomAdapter(response.body() as ArrayList<MyOrder>)
                        rvMyOrderList.adapter = customAdapter


                    } else {
                        Toast.makeText(this@ViewOrderActivity, "No order history", Toast.LENGTH_LONG).show()

                    }


                }


            }

        })

    }


    interface MyOrderList {

        @POST("user/customer_ordersapi/")
        @FormUrlEncoded
        fun getMyOrderListApi(@Field("id") id: String): Call<List<MyOrder>>

    }

    fun myOrderListService(): MyOrderList {
        return RetrofiltClient.getRettofitClient(Constants.BASE_URL, "x").create(MyOrderList::class.java)
    }


    /**
     * Caregory list
     *
     *
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    inner class CustomAdapter(private val dataSet: ArrayList<MyOrder>) :
            RecyclerView.Adapter<CustomAdapter.ViewHolder>() {


        /**
         * Provide a reference to the type of views that you are using (custom ViewHolder)
         */
        inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val tvProductName: TextView
            val orderTime: TextView
            val qty: TextView
            val totalAmount: TextView
            val tvStaus: TextView

            init {
                // Define click listener for the ViewHolder's View.
                v.setOnClickListener {

                    val intent = Intent(this@ViewOrderActivity, OrderDetailView::class.java)
                    intent.putExtra("order_data", dataSet[adapterPosition])
                    startActivity(intent)

                }
                tvProductName = v.findViewById(R.id.tvProductName)
                tvProductName.visibility = View.GONE
                orderTime = v.findViewById(R.id.tvOrderDate)
                qty = v.findViewById(R.id.tvQty)
                totalAmount = v.findViewById(R.id.tvTotalPrice)
                tvStaus = v.findViewById(R.id.tvStatus)

                qty.setTextColor(Color.BLACK)

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

            // viewHolder.tvProductName.text = dataSet[position].name
            viewHolder.totalAmount.text = "Total: ".plus(dataSet[position].grand_total)
            viewHolder.orderTime.text = "Order date: ".plus(dataSet[position].timestamp)
            viewHolder.qty.text = "Order No: ".plus(dataSet[position].order_no)

            when (dataSet[position].status) {

                "1" -> {
                    viewHolder.tvStaus.text = "Order placed"
                }
                "2" -> {
                    viewHolder.tvStaus.text = "Order Approved"

                }
                "3" -> {
                    viewHolder.tvStaus.text = "Order Canceled"

                }
                "4" -> {
                    viewHolder.tvStaus.text = "Order packed"

                }
                "5" -> {
                    viewHolder.tvStaus.text = "In Delivery"

                }
                "6" -> {
                    viewHolder.tvStaus.text = "Delivered"

                }
                "7" -> {
                    viewHolder.tvStaus.text = "Competed"

                }


            }


        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = dataSet.size


    }

}
