package com.codefuelindia.wecarefarm.view

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.codefuelindia.wecarefarm.R
import com.codefuelindia.wecarefarm.Utils.Utils
import com.codefuelindia.wecarefarm.common.RetrofiltClient
import com.codefuelindia.wecarefarm.cons.Constants
import com.codefuelindia.wecarefarm.model.AllProductList

import kotlinx.android.synthetic.main.activity_my_products_of_farmer.*
import kotlinx.android.synthetic.main.content_my_products_of_farmer.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.util.ArrayList

class MyProductsOfFarmerActivity : AppCompatActivity() {

    private var lastclick = 0L
    private lateinit var getAllProducts: GetAllProducts
    private var productListArray = ArrayList<AllProductList>()
    private var id = ""
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_products_of_farmer)
        setSupportActionBar(toolbar)
        getAllProducts = getAllProductListService(Constants.BASE_URL)

        val sharedPreferences = Utils.getSharedPreference(Constants.MY_PREF, this@MyProductsOfFarmerActivity)
        id = sharedPreferences.getString(Constants.ID, "")


        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.title = "Products"

        toolbar.setNavigationOnClickListener { finish() }

        rvFarmerProduct.layoutManager = LinearLayoutManager(this@MyProductsOfFarmerActivity, LinearLayoutManager.VERTICAL, false)


        fab.setOnClickListener { view ->

            if (SystemClock.elapsedRealtime() - lastclick < 2000) {
                return@setOnClickListener
            }
            lastclick = SystemClock.elapsedRealtime()

            startActivity(Intent(this@MyProductsOfFarmerActivity, RequestProductApprovalActivity::class.java))


        }


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

                    }

                }

            }


        })


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
            val tvStatus: TextView


            init {
                // Define click listener for the ViewHolder's View.
                tvProductName = v.findViewById<TextView>(R.id.tvFarmerProductname)
                tvQty = v.findViewById<TextView>(R.id.tvQty)
                tvStatus = v.findViewById<TextView>(R.id.tvStatus)

                tvQty.visibility = View.GONE

            }
        }

        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            // Create a new view.
            val v = LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.row_farmer_product_list, viewGroup, false)

            return ViewHolder(v)
        }

        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

            // Get element from your dataset at this position and replace the contents of the view
            // with that element

            viewHolder.tvProductName.text = dataSet[position].name
            viewHolder.tvQty.text = "Qty: ".plus(dataSet[position].qty).plus(" KG")
            if (dataSet[position].approve == "1") {
                viewHolder.tvStatus.text = "Approved"
            }


        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = dataSet.size


    }


}
