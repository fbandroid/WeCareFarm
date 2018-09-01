package com.codefuelindia.wecarefarm.view

import android.content.Context
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
import com.codefuelindia.wecarefarm.model.AddStock
import com.codefuelindia.wecarefarm.model.AllProductList
import com.codefuelindia.wecarefarm.model.MyRes
import com.google.gson.Gson

import kotlinx.android.synthetic.main.activity_request_product_approval.*
import kotlinx.android.synthetic.main.content_delivery_boy_dashboard.*
import kotlinx.android.synthetic.main.content_request_product_approval.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.util.ArrayList

class RequestProductApprovalActivity : AppCompatActivity() {

    private var id = ""
    private lateinit var getProductListForRequest: GetProductListForRequest
    private lateinit var addProductRequestByFarmer: AddProductRequestByFarmer
    private var productListArray: ArrayList<AllProductList> = ArrayList()
    private var lastclick = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_product_approval)
        setSupportActionBar(toolbar)


        getProductListForRequest = RetrofiltClient.getRettofitClient(Constants.BASE_URL, "x").create(GetProductListForRequest::class.java)
        addProductRequestByFarmer = addProductRequestServices()


        val sharedPreferences = Utils.getSharedPreference(Constants.MY_PREF, this@RequestProductApprovalActivity)
        id = sharedPreferences.getString(Constants.ID, "")

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar.title = "Request Product"

        toolbar.setNavigationOnClickListener { finish() }


        getProductListForRequest.productListForRequest(id)


        btnRequestProduct.visibility = View.GONE

        btnRequestProduct.setOnClickListener {

            if (SystemClock.elapsedRealtime() - lastclick < 2000) {
                return@setOnClickListener
            }

            lastclick = SystemClock.elapsedRealtime()


            var isStockAdded = false


            for (items in productListArray) {

                if (items.isChecked) {

                    isStockAdded = true
                    break
                }

            }


            if (isStockAdded) {
                val stockSelectedArray = ArrayList<AllProductList>()


                for (items in productListArray) {
                    if (items.isChecked) {
                        stockSelectedArray.add(items)

                    }
                }


                val addStock = AddStock()
                addStock.fid = id
                addStock.allProductLists = stockSelectedArray

                val loader = showLoader()

                addProductRequestByFarmer.addStockToGodown(addStock).enqueue(object : Callback<MyRes> {
                    override fun onFailure(call: Call<MyRes>?, t: Throwable?) {
                        loader.dismiss()
                        Toast.makeText(this@RequestProductApprovalActivity, "Connection error", Toast.LENGTH_LONG).show()
                        finish()


                    }

                    override fun onResponse(call: Call<MyRes>?, response: Response<MyRes>?) {
                        loader.dismiss()
                        if (response!!.isSuccessful) {

                            if (response!!.body()!!.msg.equals("true", true)) {
                                Toast.makeText(this@RequestProductApprovalActivity, "SuccessFully Requested!", Toast.LENGTH_LONG).show()
                                finish()


                            } else {
                                Toast.makeText(this@RequestProductApprovalActivity, "Error occurred!", Toast.LENGTH_LONG).show()
                                finish()
                            }


                        } else {
                            Toast.makeText(this@RequestProductApprovalActivity, "Connection error", Toast.LENGTH_LONG).show()
                            finish()
                        }

                    }


                })

                Log.e("request product list", Gson().toJson(addStock))


            } else {
                Toast.makeText(this@RequestProductApprovalActivity, "Select product", Toast.LENGTH_LONG).show()
            }

        }


        getProductListForRequest.productListForRequest(id).enqueue(object : Callback<List<AllProductList>> {
            override fun onFailure(call: Call<List<AllProductList>>?, t: Throwable?) {

                progressBar.visibility = View.GONE

            }

            override fun onResponse(call: Call<List<AllProductList>>?, response: Response<List<AllProductList>>?) {

                progressBar.visibility = View.GONE

                if (response!!.isSuccessful) {

                    if (response.body()!!.isNotEmpty()) {

                        btnRequestProduct.visibility = View.VISIBLE

                        productListArray = response.body()!! as ArrayList<AllProductList>

                        rvProductList.layoutManager = LinearLayoutManager(this@RequestProductApprovalActivity,
                                LinearLayoutManager.VERTICAL, false)

                        rvProductList.adapter = ProductAdapter(response.body() as ArrayList<AllProductList>)

                    }


                }


            }


        })


    }


    interface GetProductListForRequest {

        @POST("user/farmer_product_req_api/")
        @FormUrlEncoded
        fun productListForRequest(@Field("fid") fid: String): Call<List<AllProductList>>


    }

    /**
     *    Products list
     *
     *
     */

    /**
     * Provide views to RecyclerView with data from dataSet.
     *
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    inner class ProductAdapter(private val dataSet: ArrayList<AllProductList>) :
            RecyclerView.Adapter<ProductAdapter.ViewHolder>() {


        /**
         * Provide a reference to the type of views that you are using (custom ViewHolder)
         */
        inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {


            val tvProductName: TextView
            val chkRequestProduct: CheckBox


            init {
                // Define click listener for the ViewHolder's View.
                tvProductName = v.findViewById<TextView>(R.id.tvProductName)
                chkRequestProduct = v.findViewById(R.id.chkRequest)


                chkRequestProduct.setOnCheckedChangeListener { buttonView, isChecked ->

                    dataSet[adapterPosition].isChecked = isChecked

                }

            }
        }

        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            // Create a new view.
            val v = LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.row_request_product, viewGroup, false)

            return ViewHolder(v)
        }

        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

            // Get element from your dataset at this position and replace the contents of the view
            // with that element

            viewHolder.chkRequestProduct.isChecked = dataSet[position].isChecked
            viewHolder.tvProductName.text = dataSet[position].name


        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = dataSet.size


    }


    private interface AddProductRequestByFarmer {

        @POST("user/add_unapprove_product_insert/")
        fun addStockToGodown(@Body stock: AddStock): Call<MyRes>

    }


    private fun addProductRequestServices(): AddProductRequestByFarmer {
        return RetrofiltClient.getRettofitClient(Constants.BASE_URL, "x").create(AddProductRequestByFarmer::class.java)
    }


    fun showLoader(): AlertDialog {

        val dialogBuilder = AlertDialog.Builder(this@RequestProductApprovalActivity as Context)
// ...Irrelevant code for customizing the buttons and title
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.alert_label_editor, null)
        dialogBuilder.setView(dialogView)

        GlideApp.with(this@RequestProductApprovalActivity)
                .load(this@RequestProductApprovalActivity?.getDrawable(R.drawable.loader)).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(dialogView.findViewById<ImageView>(R.id.ivGif))
        dialogBuilder.setCancelable(false)
        val alertDialog = dialogBuilder.create()
        alertDialog.window.setBackgroundDrawable(this@RequestProductApprovalActivity?.getDrawable(android.R.color.transparent))
        alertDialog.show()

        return alertDialog


    }


}
