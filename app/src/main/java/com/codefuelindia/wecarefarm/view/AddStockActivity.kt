package com.codefuelindia.wecarefarm.view

import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

import kotlinx.android.synthetic.main.activity_add_stock.*
import kotlinx.android.synthetic.main.content_add_stock.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.util.ArrayList

class AddStockActivity : AppCompatActivity() {

    private var lastclick = 0L
    private lateinit var getAllProducts: GetAllProducts
    private var productListArray = ArrayList<AllProductList>()
    private var id = ""
    private lateinit var productAdapter: ProductAdapter
    private lateinit var addStockByFarmer: AddStockByFarmer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_stock)
        setSupportActionBar(toolbar)

        addStockByFarmer = addStockServices()

        getAllProducts = getAllProductListService(Constants.BASE_URL)

        val sharedPreferences = Utils.getSharedPreference(Constants.MY_PREF, this@AddStockActivity)
        id = sharedPreferences.getString(Constants.ID, "")


        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.title = "Add Stock"
        toolbar.setNavigationOnClickListener { finish() }

        rvFarmerProduct.layoutManager = LinearLayoutManager(this@AddStockActivity, LinearLayoutManager.VERTICAL, false)

        btnAddStock.visibility = View.GONE

        getAllProducts.getAllProductListApi(id).enqueue(object : Callback<List<AllProductList>> {

            override fun onFailure(call: Call<List<AllProductList>>?, t: Throwable?) {
                progressBar.visibility = View.GONE
            }

            override fun onResponse(call: Call<List<AllProductList>>?, response: Response<List<AllProductList>>?) {
                progressBar.visibility = View.GONE

                if (response!!.isSuccessful) {

                    productListArray = response!!.body() as ArrayList<AllProductList>

                    if (productListArray.isNotEmpty()) {
                        productAdapter = ProductAdapter(productListArray)
                        rvFarmerProduct.adapter = productAdapter
                        btnAddStock.visibility = View.VISIBLE

                    }

                }

            }


        })


        btnAddStock.setOnClickListener {
            if (SystemClock.elapsedRealtime() - lastclick < 2000) {
                return@setOnClickListener
            }
            lastclick = SystemClock.elapsedRealtime()
            var isStockAdded = false


            for (items in productListArray) {

                if (items.addStockQty != null && items.addStockQty.trim().isNotEmpty() && items.addStockQty.trim()
                                .toString().toDouble() > 0) {

                    isStockAdded = true
                    break
                }

            }


            if (isStockAdded) {
                // its ok

                val stockSelectedArray = ArrayList<AllProductList>()

                for (items in productListArray) {
                    if (items.addStockQty != null && items.addStockQty.trim().isNotEmpty() && items.addStockQty.trim().toString().toDouble() > 0) {

                        stockSelectedArray.add(items)
                    }
                }


                val addStock = AddStock()
                addStock.allProductLists = stockSelectedArray

                Log.e("stock array", Gson().toJson(addStock))


                val loader = showLoader()


                addStockByFarmer.addStockToGodown(addStock).enqueue(object : Callback<MyRes> {
                    override fun onFailure(call: Call<MyRes>?, t: Throwable?) {
                        loader.dismiss()
                        Toast.makeText(this@AddStockActivity, "Connection error", Toast.LENGTH_LONG).show()
                        finish()


                    }

                    override fun onResponse(call: Call<MyRes>?, response: Response<MyRes>?) {
                        loader.dismiss()

                        if (response!!.isSuccessful) {

                            if (response!!.body()!!.msg.equals("true", true)) {

                                Toast.makeText(this@AddStockActivity, "SuccessFully Added!", Toast.LENGTH_LONG).show()
                                finish()


                            } else {
                                Toast.makeText(this@AddStockActivity, "Error occurred!", Toast.LENGTH_LONG).show()
                                finish()
                            }


                        } else {
                            Toast.makeText(this@AddStockActivity, "Connection error", Toast.LENGTH_LONG).show()
                            finish()
                        }


                    }


                })


            } else {
                Toast.makeText(this@AddStockActivity, "Please enter quantity of product", Toast.LENGTH_LONG).show()
            }


        }


    }


    internal interface GetAllProducts {
        @POST("user/farmer_productapi/")
        @FormUrlEncoded
        fun getAllProductListApi(@Field("id") id: String): Call<List<AllProductList>>
    }


    internal fun getAllProductListService(baseUrl: String): GetAllProducts {
        return RetrofiltClient.getRettofitClient(baseUrl, "x")?.create(GetAllProducts::class.java)!!
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
            val tvQty: TextView
            val edtAddStockQty: TextInputEditText
            val edtApproxQty: TextInputEditText


            init {
                // Define click listener for the ViewHolder's View.
                tvProductName = v.findViewById<TextView>(R.id.tvFarmerProductName)
                tvQty = v.findViewById<TextView>(R.id.tvApprovedQty)
                edtAddStockQty = v.findViewById<TextInputEditText>(R.id.edtStockAddQty)
                edtApproxQty = v.findViewById(R.id.edtApproxStockAddQty)

                tvQty.visibility = View.GONE

                edtApproxQty.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {

                        if (s.toString().trim().isNotEmpty() && s.toString().trim().toDouble() > 0) {
                            dataSet[adapterPosition].approx_qty = s.toString()

                        } else {
                            dataSet[adapterPosition].approx_qty = " "

                        }


                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                    }
                })


                edtAddStockQty.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {

                        if (s.toString().trim().isNotEmpty() && s.toString().trim().toDouble() > 0) {
                            dataSet[adapterPosition].addStockQty = s.toString()

                        } else {
                            dataSet[adapterPosition].addStockQty = " "

                        }

                        var totalQty = 0

                        for (items in productListArray) {

                            if (items.addStockQty != null && items.addStockQty.trim().isNotEmpty() && items.addStockQty.trim().toDouble() > 0) {
                                totalQty += items.addStockQty.toInt()


                            }

                        }

                        tvTotalQty.text = "Total Qty: ".plus(totalQty.toString()).plus(" બોરી ")


                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {


                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


                    }
                })


            }
        }

        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            // Create a new view.
            val v = LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.row_add_new_stock, viewGroup, false)

            return ViewHolder(v)
        }

        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

            // Get element from your dataset at this position and replace the contents of the view
            // with that element

            viewHolder.edtAddStockQty.setText(dataSet[position].addStockQty)
            viewHolder.edtApproxQty.setText(dataSet[position].approx_qty)

            viewHolder.tvProductName.text = dataSet[position].name
            viewHolder.tvQty.text = "Available Qty: ".plus(dataSet[position].qty).plus(" " + dataSet[position].unitName)
            dataSet[position].addStockQty = viewHolder.edtAddStockQty.text.toString()
            dataSet[position].approx_qty = viewHolder.edtApproxQty.text.toString()


        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = dataSet.size


    }


    fun showLoader(): AlertDialog {

        val dialogBuilder = AlertDialog.Builder(this@AddStockActivity as Context)
// ...Irrelevant code for customizing the buttons and title
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.alert_label_editor, null)
        dialogBuilder.setView(dialogView)

        GlideApp.with(this@AddStockActivity)
                .load(this@AddStockActivity?.getDrawable(R.drawable.loader)).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(dialogView.findViewById<ImageView>(R.id.ivGif))
        dialogBuilder.setCancelable(false)
        val alertDialog = dialogBuilder.create()
        alertDialog.window.setBackgroundDrawable(this@AddStockActivity?.getDrawable(android.R.color.transparent))
        alertDialog.show()

        return alertDialog


    }


    private interface AddStockByFarmer {

        @POST("user/addreqqty_transaction/")
        fun addStockToGodown(@Body stock: AddStock): Call<MyRes>

    }


    private fun addStockServices(): AddStockByFarmer {
        return RetrofiltClient.getRettofitClient(Constants.BASE_URL, "x").create(AddStockByFarmer::class.java)
    }


}
