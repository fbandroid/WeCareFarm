package com.codefuelindia.wecarefarm.view

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.SystemClock
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.codefuelindia.wecarefarm.R
import com.codefuelindia.wecarefarm.Utils.Utils
import com.codefuelindia.wecarefarm.common.MyAdapter
import com.codefuelindia.wecarefarm.common.RetrofiltClient
import com.codefuelindia.wecarefarm.cons.Constants
import com.codefuelindia.wecarefarm.db.Cart
import com.codefuelindia.wecarefarm.db.CartDatabase
import com.codefuelindia.wecarefarm.model.MyRes
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_user_dash_board22.*
import kotlinx.android.synthetic.main.app_bar_user_dash_board22.*
import kotlinx.android.synthetic.main.content_delivery_boy_dashboard.*
import kotlinx.android.synthetic.main.content_user_dash_board22.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

class UserDashBoard22 : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


    private var id = ""
    private var title = ""
    private var mCartItemCount = 0
    private var mTextCartCounter: TextView? = null
    private lateinit var dbInstance: CartDatabase
    private val currentPage = 0
    private val XMEN = arrayOf<Int>(R.drawable.codefuellogo2, R.drawable.codefuellogo2, R.drawable.codefuellogo2, R.drawable.codefuellogo2)
    private val XMENArray = ArrayList<Int>()
    private var lastclick = 0L
    lateinit var mAdView: AdView
    private lateinit var addReferer: AddReferer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_dash_board22)
        setSupportActionBar(toolbar)

        dbInstance = CartDatabase.getDatabase(applicationContext)
        addReferer = RetrofiltClient.getRettofitClient(Constants.BASE_URL, "x").create(AddReferer::class.java)


        val sharedPreferences = Utils.getSharedPreference(Constants.MY_PREF, this@UserDashBoard22)
        id = sharedPreferences.getString(Constants.ID, "")
        title = sharedPreferences.getString(Constants.NAME, "")


        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()


        frProductList.setOnClickListener {

            if (SystemClock.elapsedRealtime() - lastclick < 2000) {
                return@setOnClickListener
            }
            lastclick = SystemClock.elapsedRealtime()

            startActivity(Intent(this@UserDashBoard22, HomeActivity::class.java))


        }

        frViewExecutedOrders.setOnClickListener {
            if (SystemClock.elapsedRealtime() - lastclick < 2000) {
                return@setOnClickListener
            }
            lastclick = SystemClock.elapsedRealtime()

            startActivity(Intent(this@UserDashBoard22, ViewOrderActivity::class.java))

        }

        frViewCurrentOrders.setOnClickListener {
            if (SystemClock.elapsedRealtime() - lastclick < 2000) {
                return@setOnClickListener
            }
            lastclick = SystemClock.elapsedRealtime()

            startActivity(Intent(this@UserDashBoard22, AprovePlaceOrderActivity::class.java))
        }



        for (i in 0 until XMEN.size)
            XMENArray.add(XMEN[i])

        viewPager.adapter = MyAdapter(this@UserDashBoard22, XMENArray)


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

                val editor = Utils.writeToPreference(Constants.MY_PREF, this@UserDashBoard22)
                editor.putBoolean(Constants.IS_LOGIN, false)
                editor.putString(Constants.USER_NAME, null)
                editor.putString(Constants.NAME, null)
                editor.putString(Constants.ID, null)
                editor.putString(Constants.ROLE, null)
                editor.apply()


                DestroyCartOnLogOut().execute()



                finishAffinity()
                startActivity(Intent(this@UserDashBoard22, LoginActivity::class.java))

                return true
            }
            R.id.action_change_pwd -> {

                startActivity(Intent(this@UserDashBoard22, ChangePwdActivity::class.java))

                return true
            }
            R.id.action_my_cart -> {

                startActivity(Intent(this@UserDashBoard22, ViewCartActivity::class.java))

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
                startActivity(Intent(this@UserDashBoard22, ShowTransactionActivity::class.java))

            }
            R.id.nav_my_cart -> {

            }
            R.id.nav_my_order -> {

                startActivity(Intent(this@UserDashBoard22, ViewOrderActivity::class.java))

            }
            R.id.nav_my_wishList -> {

            }


            R.id.nav_my_referel -> {

                showReferelDialog()


            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
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


        val dialogBuilder = AlertDialog.Builder(this@UserDashBoard22 as Context)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.row_dialog_referel, null)
        dialogBuilder.setView(dialogView)

        dialogBuilder.setCancelable(false)


        val tvReferelName = dialogView.findViewById<TextView>(R.id.tvRefererName)
        val tvReferelLable = dialogView.findViewById<TextView>(R.id.tvLableReferel)


        tvReferelName.visibility = View.GONE


        tvReferelLable.text = "Add Referer"

        val edtMobile = dialogView.findViewById<TextView>(R.id.edtMobile)

        val btnAdd = dialogView.findViewById<TextView>(R.id.btnAdd)

        val btnCancel = dialogView.findViewById<TextView>(R.id.btnCancel)

        val progressBar = dialogView.findViewById<ProgressBar>(R.id.progressBar2)
        progressBar.visibility = View.GONE

        val alertDialog = dialogBuilder.create()
        // alertDialog.window.setBackgroundDrawable(this@HomeActivity?.getDrawable(android.R.color.transparent))
        alertDialog.show()

        btnAdd.setOnClickListener {


            if (edtMobile.text.toString().trim().isNotEmpty() && edtMobile.text.toString().trim().length == 10) {

                btnAdd.isEnabled = false
                progressBar.visibility = View.VISIBLE

                addReferer.addRefererOfUser(id, edtMobile.text.toString().trim()).enqueue(
                        object : Callback<MyRes> {
                            override fun onFailure(call: Call<MyRes>?, t: Throwable?) {

                                btnAdd.isEnabled = true
                                progressBar.visibility = View.GONE

                                Toast.makeText(this@UserDashBoard22, "Connection error", Toast.LENGTH_LONG).show()
                                alertDialog.dismiss()
                            }

                            override fun onResponse(call: Call<MyRes>?, response: Response<MyRes>?) {

                                btnAdd.isEnabled = true
                                progressBar.visibility = View.GONE

                                if (response!!.isSuccessful) {

                                    when {
                                        response!!.body()!!.msg.equals("true", true) -> {
                                            Toast.makeText(this@UserDashBoard22, "Added Successfully", Toast.LENGTH_LONG).show()
                                            alertDialog.dismiss()
                                        }
                                        response!!.body()!!.msg.equals("3x", true) -> {
                                            Toast.makeText(this@UserDashBoard22, "Your mobile number is not allowed", Toast.LENGTH_LONG).show()
                                            alertDialog.dismiss()
                                        }
                                        response!!.body()!!.msg.equals("2x", true) -> {
                                            Toast.makeText(this@UserDashBoard22, "This user is not available", Toast.LENGTH_LONG).show()
                                            alertDialog.dismiss()
                                        }
                                        response!!.body()!!.msg.equals("5x", true) -> {
                                            Toast.makeText(this@UserDashBoard22, "Max Limit reached", Toast.LENGTH_LONG).show()
                                            alertDialog.dismiss()
                                        }
                                        response!!.body()!!.msg.equals("1x", true) -> {
                                            Toast.makeText(this@UserDashBoard22, "User already added", Toast.LENGTH_LONG).show()
                                            alertDialog.dismiss()
                                        }

                                    }


                                } else {
                                    Toast.makeText(this@UserDashBoard22, "Internal Server error", Toast.LENGTH_LONG).show()
                                }


                            }


                        }
                )

            } else {
                Toast.makeText(this@UserDashBoard22, "Mobile is not valid", Toast.LENGTH_LONG).show()

            }


        }

        btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }


    }


    interface AddReferer {

        @POST("user/addreference_mobile_api")
        @FormUrlEncoded
        fun addRefererOfUser(@Field("cid") cid: String, @Field("mobile") mobile: String): Call<MyRes>


    }


}
