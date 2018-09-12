package com.codefuelindia.wecarefarm.view

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.*
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
import com.codefuelindia.wecarefarm.db.Cart
import com.codefuelindia.wecarefarm.db.CartDatabase
import com.codefuelindia.wecarefarm.model.Category
import com.codefuelindia.wecarefarm.model.Product
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.content_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    private lateinit var getCategory: GetCategory
    private var categoryList = ArrayList<Category>()
    private var customAdapter: CustomAdapter? = null
    private var productAdapter: ProductAdapter? = null
    private val IMAGE_PATH = Constants.BASE_URL + "category/"
    private val PRODUCT_IMG_PATH = Constants.BASE_URL + "img/"
    private lateinit var getProducts: GetProducts
    private var productlist = ArrayList<Product>()
    private lateinit var dbInstance: CartDatabase
    private var id = ""
    private var mCartItemCount = 0
    private var mTextCartCounter: TextView? = null
    private var title = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        dbInstance = CartDatabase.getDatabase(applicationContext)
        val sharedPreferences = Utils.getSharedPreference(Constants.MY_PREF, this@HomeActivity)
        id = sharedPreferences.getString(Constants.ID, "")
        title = sharedPreferences.getString(Constants.NAME, "")

        getCategory = getCategoryListService()
        getProducts = getProductServices()

        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.title = "Home"

        rvCategory.layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)

        rvProductList.layoutManager = GridLayoutManager(this@HomeActivity, 2, GridLayoutManager.VERTICAL, false)

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        getCategory.getCategoryapi().enqueue(
                object : Callback<List<Category>> {
                    override fun onFailure(call: Call<List<Category>>?, t: Throwable?) {
                        progressBar.visibility = View.GONE
                        Toast.makeText(this@HomeActivity, "Connection error", Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(call: Call<List<Category>>?, response: Response<List<Category>>?) {
                        progressBar.visibility = View.GONE
                        if (response!!.isSuccessful) {
                            if (response!!.body()!!.isNotEmpty()) {
                                customAdapter = CustomAdapter(response.body() as ArrayList<Category>)
                                rvCategory.adapter = customAdapter

                                progressBar.visibility = View.VISIBLE

                                getProducts.getProductlistapi((response.body() as ArrayList<Category>)[0].id)
                                        .enqueue(object : Callback<List<Product>> {
                                            override fun onFailure(call: Call<List<Product>>?, t: Throwable?) {
                                                progressBar.visibility = View.GONE

                                            }

                                            override fun onResponse(call: Call<List<Product>>?, response: Response<List<Product>>?) {
                                                progressBar.visibility = View.GONE

                                                if (response!!.isSuccessful) {

                                                    if (response!!.body()!!.size > 0) {
                                                        productAdapter = ProductAdapter(response!!.body() as ArrayList<Product>)
                                                        rvProductList.adapter = productAdapter
                                                    } else {
                                                        productAdapter = ProductAdapter(ArrayList())
                                                        rvProductList.adapter = productAdapter
                                                    }

                                                }

                                            }

                                        })


                            } else {
                                Toast.makeText(this@HomeActivity, "No product category", Toast.LENGTH_LONG).show()
                                customAdapter = CustomAdapter(ArrayList())
                                rvCategory.adapter = customAdapter
                            }
                        }

                    }

                }
        )

        val tvTitle = nav_view.getHeaderView(0).findViewById<TextView>(R.id.tvTitleOfUser)
        tvTitle.text = title

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        val menuItem = menu.findItem(R.id.action_my_cart)
        val actionView = menuItem.actionView

        mTextCartCounter = actionView.findViewById(R.id.cart_badge)

        GetCartList().execute()

        if (mTextCartCounter != null) {


            if (mCartItemCount > 0) {

                mTextCartCounter!!.text = mCartItemCount.toString()
                mTextCartCounter!!.visibility = View.VISIBLE

            } else {
                mTextCartCounter!!.visibility = View.GONE
                mTextCartCounter!!.text = ""

            }

            if (actionView != null) {
                actionView.setOnClickListener {

                    onOptionsItemSelected(menuItem)
                }
            }


        }

        return true
    }


    override fun onResume() {
        super.onResume()

        GetCartList().execute()

        if (mTextCartCounter != null) {


            if (mCartItemCount > 0) {

                mTextCartCounter!!.text = mCartItemCount.toString()
                mTextCartCounter!!.visibility = View.VISIBLE


            } else {
                mTextCartCounter!!.visibility = View.GONE
                mTextCartCounter!!.text = ""

            }


        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_LogOut -> {

                val editor = Utils.writeToPreference(Constants.MY_PREF, this@HomeActivity)
                editor.putBoolean(Constants.IS_LOGIN, false)
                editor.putString(Constants.USER_NAME, null)
                editor.putString(Constants.NAME, null)
                editor.putString(Constants.ID, null)
                editor.putString(Constants.ROLE, null)
                editor.putBoolean(Constants.IS_REF_ADDED, false)

                editor.apply()
                DestroyCartOnLogOut().execute()


                finishAffinity()
                startActivity(Intent(this@HomeActivity, LoginActivity::class.java))

                return true
            }
            R.id.action_change_pwd -> {

                startActivity(Intent(this@HomeActivity, ChangePwdActivity::class.java))

                return true
            }
            R.id.action_my_cart -> {

                startActivity(Intent(this@HomeActivity, ViewCartActivity::class.java))

                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_offer_zone -> {
                // Handle the camera action
            }
            R.id.nav_notification -> {

            }
            R.id.nav_my_account -> {

                startActivity(Intent(this@HomeActivity, ShowTransactionActivity::class.java))

            }
            R.id.nav_my_cart -> {

            }
            R.id.nav_my_order -> {

                startActivity(Intent(this@HomeActivity, ViewOrderActivity::class.java))

            }
            R.id.nav_my_wishList -> {

            }

            R.id.nav_my_referel -> {

                //showReferelDialog()


            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


    /**
     * Category list
     *
     *
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    inner class CustomAdapter(private val dataSet: ArrayList<Category>) :
            RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
        var selected_position = 0

        /**
         * Provide a reference to the type of views that you are using (custom ViewHolder)
         */
        inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val textView: TextView
            val imgLogo: ImageView

            init {
                // Define click listener for the ViewHolder's View.
                v.setOnClickListener {
                    notifyItemChanged(selected_position)
                    selected_position = adapterPosition
                    notifyItemChanged(selected_position)

                    progressBar.visibility = View.VISIBLE

                    getProducts.getProductlistapi(dataSet[adapterPosition].id)
                            .enqueue(object : Callback<List<Product>> {
                                override fun onFailure(call: Call<List<Product>>?, t: Throwable?) {
                                    progressBar.visibility = View.GONE


                                }

                                override fun onResponse(call: Call<List<Product>>?, response: Response<List<Product>>?) {
                                    progressBar.visibility = View.GONE

                                    if (response!!.isSuccessful) {

                                        if (response!!.body()!!.size > 0) {
                                            productAdapter = ProductAdapter(response!!.body() as ArrayList<Product>)
                                            rvProductList.adapter = productAdapter
                                        } else {
                                            productAdapter = ProductAdapter(ArrayList())
                                            rvProductList.adapter = productAdapter
                                        }

                                    }

                                }

                            })


                }
                textView = v.findViewById(R.id.tvCategoryName)
                imgLogo = v.findViewById(R.id.ivCategoryLogo)
            }
        }

        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            // Create a new view.
            val v = LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.row_product_category, viewGroup, false)

            return ViewHolder(v)
        }

        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

            viewHolder.itemView.setBackgroundColor(if (selected_position == position) resources.getColor(R.color.colorPrimary) else Color.TRANSPARENT)

            viewHolder.textView.setTextColor(if (selected_position == position) Color.WHITE else Color.BLACK)


            // Get element from your dataset at this position and replace the contents of the view
            // with that element
            viewHolder.textView.text = dataSet[position].categoryName
            GlideApp.with(this@HomeActivity)
                    .load(IMAGE_PATH + dataSet[position].img)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(viewHolder.imgLogo)
        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = dataSet.size


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
    inner class ProductAdapter(private val dataSet: ArrayList<Product>) :
            RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

        /**
         * Provide a reference to the type of views that you are using (custom ViewHolder)
         */
        inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val textView: TextView
            val imgLogo: ImageView
            val productPrise: TextView
            val tvAvailableQty: TextView

            init {
                // Define click listener for the ViewHolder's View.
                v.setOnClickListener {

                    val intent = Intent(this@HomeActivity, AddToCartActivity::class.java)
                    intent.putExtra("product", dataSet[adapterPosition])
                    startActivity(intent)

                }
                textView = v.findViewById(R.id.tvProductName)
                imgLogo = v.findViewById(R.id.ivProductLogo)
                productPrise = v.findViewById(R.id.tvProductPrice)
                tvAvailableQty = v.findViewById(R.id.tvAvailableQty)
            }
        }

        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            // Create a new view.
            val v = LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.row_product_list, viewGroup, false)

            return ViewHolder(v)
        }

        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

            // Get element from your dataset at this position and replace the contents of the view
            // with that element
            viewHolder.textView.text = dataSet[position].name
            viewHolder.productPrise.text = "ભાવ: Rs ${dataSet[position].unitPrice} / ".plus(dataSet[position].unitName)
            viewHolder.productPrise.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.price_font_size))
            GlideApp.with(this@HomeActivity)
                    .load(PRODUCT_IMG_PATH + dataSet[position].productImg).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                    .placeholder(R.mipmap.ic_launcher)
                    .into(viewHolder.imgLogo)

            if (dataSet[position].qty.toDouble() > 1) {
                viewHolder.tvAvailableQty.setTextColor(Color.BLACK)
                viewHolder.tvAvailableQty.text = "Avl.Qty: ".plus(dataSet[position].qty).plus(dataSet[position].unitName)
                viewHolder.itemView.isClickable = true

            } else {
                viewHolder.tvAvailableQty.setTextColor(Color.RED)
                viewHolder.tvAvailableQty.text = "Out Of Stock"
                viewHolder.itemView.isClickable = false
            }


        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = dataSet.size


    }

    interface GetCategory {

        @POST("user/list_categoryapi")
        fun getCategoryapi(): Call<List<Category>>

    }

    fun getCategoryListService(): GetCategory {
        return RetrofiltClient.getRettofitClient(Constants.BASE_URL, "x").create(GetCategory::class.java)
    }


    interface GetProducts {

        @POST("user/list_category_productapi")
        @FormUrlEncoded
        fun getProductlistapi(@Field("id") id: String): Call<List<Product>>

    }


    fun getProductServices(): GetProducts {
        return RetrofiltClient.getRettofitClient(Constants.BASE_URL, "x").create(GetProducts::class.java)
    }


    inner class GetCartList() : AsyncTask<Void, Void, List<Cart>>() {
        override fun doInBackground(vararg params: Void?): List<Cart>? {

            var cartlist = ArrayList<Cart>()
            if (dbInstance != null) {

                cartlist = dbInstance.cartDAO().getCartList(id) as ArrayList<Cart>


            }

            return cartlist
        }

        override fun onPreExecute() {
            super.onPreExecute()

        }

        override fun onPostExecute(result: List<Cart>?) {
            super.onPostExecute(result)

            if (result != null) {
                mCartItemCount = result.size

                if (mTextCartCounter != null) {


                    if (mCartItemCount > 0) {

                        mTextCartCounter!!.text = mCartItemCount.toString()
                        mTextCartCounter!!.visibility = View.VISIBLE


                    } else {
                        mTextCartCounter!!.visibility = View.GONE
                        mTextCartCounter!!.text = ""

                    }


                }

            }

        }
    }


    inner class DestroyCartOnLogOut : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {


            dbInstance.cartDAO().deleteAllOrder()


            return null
        }

    }


    fun showReferelDialog() {


        val dialogBuilder = AlertDialog.Builder(this@HomeActivity as Context)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.row_dialog_referel, null)
        dialogBuilder.setView(dialogView)
//
//          GlideApp.with(this@HomeActivity)
//                  .load(this@HomeActivity?.getDrawable(R.drawable.loader)).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
//                  .into(dialogView.findViewById<ImageView>(R.id.ivGif))
        dialogBuilder.setCancelable(false)


        val tvReferelName = dialogView.findViewById<TextView>(R.id.tvRefererName)
        val tvReferelLable = dialogView.findViewById<TextView>(R.id.tvLableReferel)


        tvReferelName.visibility = View.GONE


        tvReferelLable.text = "Add Referer"

        val edtMobile = dialogView.findViewById<TextView>(R.id.edtMobile)

        val btnAdd = dialogView.findViewById<TextView>(R.id.btnAdd)

        val btnCancel = dialogView.findViewById<TextView>(R.id.btnCancel)
        val alertDialog = dialogBuilder.create()
        // alertDialog.window.setBackgroundDrawable(this@HomeActivity?.getDrawable(android.R.color.transparent))
        alertDialog.show()

        btnAdd.setOnClickListener {


        }

        btnCancel.setOnClickListener {

            alertDialog.dismiss()


        }


    }


}
