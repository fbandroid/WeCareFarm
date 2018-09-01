package com.codefuelindia.wecarefarm.view

import android.app.DatePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.os.AsyncTask
import android.os.Bundle
import android.os.SystemClock
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity;
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
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
import com.codefuelindia.wecarefarm.model.MyRes
import com.codefuelindia.wecarefarm.model.Product

import kotlinx.android.synthetic.main.activity_add_to_cart.*
import kotlinx.android.synthetic.main.content_add_to_cart.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

class AddToCartActivity : AppCompatActivity() {
    private val PRODUCT_IMG_PATH = Constants.BASE_URL + "img/"
    private lateinit var buyNowProduct: BuyNowProduct

    lateinit var product: Product
    private var totalPrice: Double = 0.0
    private var lastClick = 0L
    private var id = ""
    private lateinit var dbInstance: CartDatabase
    private val startCalender: java.util.Calendar = java.util.Calendar.getInstance()
    private val endCalendar: java.util.Calendar = java.util.Calendar.getInstance()
    private var startDay: Int = 0
    private var startMonth: Int = 0
    private var startYear: Int = 0
    private var endDay: Int = 0
    private var endMonth: Int = 0
    private var endYear: Int = 0
    private var processOrderMethodCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_to_cart)
        setSupportActionBar(toolbar)

        dbInstance = CartDatabase.getDatabase(applicationContext)

        endDay = endCalendar.get(java.util.Calendar.DAY_OF_MONTH)
        endMonth = endCalendar.get(java.util.Calendar.MONTH)
        endYear = endCalendar.get(java.util.Calendar.YEAR)

        startDay = startCalender.get(java.util.Calendar.DAY_OF_MONTH)
        startMonth = startCalender.get(java.util.Calendar.MONTH)
        startYear = startCalender.get(java.util.Calendar.YEAR)

        val sharedPreferences = Utils.getSharedPreference(Constants.MY_PREF, this@AddToCartActivity)
        id = sharedPreferences.getString(Constants.ID, "")

        buyNowProduct = buyNowProductService()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar.title = "Buy Product"
        toolbar.setNavigationOnClickListener { finish() }

        if (intent != null) {

            product = intent.getParcelableExtra("product")


        }

        tvDatePickTo.isEnabled = false
        tvDatePickTo.isClickable = false

        rgOrderType.setOnCheckedChangeListener { group, checkedId ->

            if (group.checkedRadioButtonId == R.id.rbNormal) {
                llRepeatOrderDateContainer.visibility = View.GONE
            } else if (group.checkedRadioButtonId == R.id.rbRepeat) {
                llRepeatOrderDateContainer.visibility = View.VISIBLE



                tvDatePickFrom.setOnClickListener {
                    //From

                    showStartCalender(startCalender)

                }

                tvDatePickTo.setOnClickListener {
                    //To

                    showEndCalender(endCalendar)

                }


            }

        }


        if (product != null) {

            tvProductName.text = product.name
            tvProductPrice.text = "Price:   " + product.unitPrice + " per " + product.unitName
            tvDesc.text = product.shortDescription//Show loader
            tvAvlQty.text = "Avl Qty- ".plus(product.qty).plus(" ").plus(product.unitName)


            // Display a negative button on alert dialog
//        alertDialog.window.setBackgroundDrawable(this@AddToCartActivity?.getDrawable(android.R.color.transparent))


            // Display a message on alert dialog


            // Set a positive button and its click listener on alert dialog
            when {
                product.unitName.equals("KG", true) -> {
                    edtQuantity.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL //for decimal numbers

                }
                product.unitName.equals("PIECE", true) -> {
                    edtQuantity.inputType = InputType.TYPE_CLASS_NUMBER

                }
                product.unitName.equals("DOZEN", true) -> {
                    edtQuantity.inputType = InputType.TYPE_CLASS_NUMBER

                }
            }

            GlideApp.with(this@AddToCartActivity)
                    .load(PRODUCT_IMG_PATH + product.productImg)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(ivProductLogo)

            edtQuantity.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


                    try {
                        if (s.toString().trim().isEmpty()) {
                            totalPrice = 0.0
                            tvTotalPrice.text = ""

                        } else {
                            totalPrice = edtQuantity.text.toString().trim().toDouble() * product.unitPrice.toDouble()
                            tvTotalPrice.text = totalPrice.toString()
                        }
                    } catch (e: NumberFormatException) {

                    }


                }
            })

            btnBuyNow.setOnClickListener {

                if (SystemClock.elapsedRealtime() - lastClick < 2000) {
                    return@setOnClickListener
                }
                lastClick = SystemClock.elapsedRealtime()

                if (edtQuantity.text.toString().trim().isNotEmpty() && edtQuantity.text.toString().toDouble() > 0.0) {

                    if (processOrderMethodCount == 1) {

                        throw IllegalStateException("Method called more than once")

                    }

                    processOrderMethodCount++
                    btnBuyNow.isEnabled = false

                    val dialogBuilder = AlertDialog.Builder(this@AddToCartActivity as Context)
                    dialogBuilder.setCancelable(false)
                    val inflater = this.layoutInflater
                    val dialogView = inflater.inflate(R.layout.row_alert_order, null)
                    dialogBuilder.setView(dialogView)
                    val tvName = dialogView.findViewById<TextView>(R.id.tvProductName)
                    val qty = dialogView.findViewById<TextView>(R.id.tvQuantity)
                    val tvtotalPrice = dialogView.findViewById<TextView>(R.id.tvTotalPrice)

                    tvName.text = product.name
                    qty.text = "Qty: ".plus(edtQuantity.text.toString().trim())
                    tvtotalPrice.text = "Total: ".plus(totalPrice.toString())

                    // Display a message on alert dialog
                    dialogBuilder.setMessage("Are you sure to buy?")


                    // Set a positive button and its click listener on alert dialog
                    dialogBuilder.setPositiveButton("YES") { dialog, which ->

                        //Show loader
                        val loader = showLoader()

                        buyNowProduct.buyNowProductapi(product.pid,
                                id, edtQuantity.text.toString().trim(), "pre")
                                .enqueue(object : Callback<MyRes> {
                                    override fun onFailure(call: Call<MyRes>?, t: Throwable?) {

                                        btnBuyNow.isEnabled = true
                                        loader.dismiss()
                                        Toast.makeText(this@AddToCartActivity, "Connection Error", Toast.LENGTH_LONG).show()
                                        finish()

                                        processOrderMethodCount = 0

                                    }

                                    override fun onResponse(call: Call<MyRes>?, response: Response<MyRes>?) {
                                        btnBuyNow.isEnabled = true
                                        loader.dismiss()

                                        processOrderMethodCount = 0
                                        if (response!!.isSuccessful) {
                                            if (response!!.body()!!.msg.equals("true", true)) {
                                                Toast.makeText(this@AddToCartActivity, "Successfully Order Placed..", Toast.LENGTH_LONG).show()
                                                finish()
                                            } else {
                                                Toast.makeText(this@AddToCartActivity, "Connection Error", Toast.LENGTH_LONG).show()
                                                finish()
                                            }

                                        } else {
                                            Toast.makeText(this@AddToCartActivity, "Connection Error", Toast.LENGTH_LONG).show()
                                            finish()

                                        }

                                    }


                                })


                    }


                    // Display a negative button on alert dialog
                    dialogBuilder.setNegativeButton("No") { dialog, which ->
                        dialog.dismiss()
                        btnBuyNow.isEnabled = true
                        processOrderMethodCount = 0
                    }

                    val alertDialog = dialogBuilder.create()
//        alertDialog.window.setBackgroundDrawable(this@AddToCartActivity?.getDrawable(android.R.color.transparent))
                    alertDialog.show()


                } else {
                    Toast.makeText(this@AddToCartActivity, "Quantity must be greater than zero", Toast.LENGTH_LONG).show()
                }

            }

            btnAddToCart.setOnClickListener {

                if (SystemClock.elapsedRealtime() - lastClick < 2000) {
                    return@setOnClickListener
                }

                lastClick = SystemClock.elapsedRealtime()

                try {
                    if (edtQuantity.text.toString().trim().isNotEmpty() && edtQuantity.text.toString().toDouble() > 0.0
                    ) {

                        if (edtQuantity.text.toString().toDouble() <= product.qty.toDouble()) {
                            AddCart().execute()
                        } else {
                            Toast.makeText(this@AddToCartActivity, "Quantity must be less than available Qty", Toast.LENGTH_LONG).show()
                        }


                    } else {
                        Toast.makeText(this@AddToCartActivity, "Quantity must be greater than zero", Toast.LENGTH_LONG).show()
                    }
                } catch (e: NumberFormatException) {

                }


            }

        }


    }


    interface BuyNowProduct {
        @POST("user/buynoworderapi")
        @FormUrlEncoded
        fun buyNowProductapi(@Field("pid") pid: String,
                             @Field("cid") cid: String,
                             @Field("qty") qty: String,
                             @Field("order_type") order_type: String): Call<MyRes>
    }

    fun buyNowProductService(): BuyNowProduct {
        return RetrofiltClient.getRettofitClient(Constants.BASE_URL, "x").create(BuyNowProduct::class.java)
    }


    fun showLoader(): AlertDialog {

        val dialogBuilder = AlertDialog.Builder(this@AddToCartActivity as Context)


        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.alert_label_editor, null)
        dialogBuilder.setView(dialogView)

        GlideApp.with(this@AddToCartActivity)
                .load(this@AddToCartActivity?.getDrawable(R.drawable.loader)).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(dialogView.findViewById<ImageView>(R.id.ivGif))
        dialogBuilder.setCancelable(false)
        val alertDialog = dialogBuilder.create()
        alertDialog.window.setBackgroundDrawable(this@AddToCartActivity?.getDrawable(android.R.color.transparent))
        alertDialog.show()

        return alertDialog


    }

    inner class AddCart() : AsyncTask<Void, Void, Long>() {
        override fun doInBackground(vararg params: Void?): Long {

            var insertid = -1L


            var noOfProducts = dbInstance.cartDAO().getCartListByProduct(product.pid)


            if (noOfProducts.size > 0 && dbInstance != null) {

                insertid = dbInstance.cartDAO().insertIfExist(noOfProducts[0].pid,edtQuantity.text.toString().trim().toDouble())
            } else {
                if (dbInstance != null) {

                    val cart = Cart()
                    cart.cid = id
                    cart.pid = product.pid
                    cart.orderDate = SystemClock.elapsedRealtime().toString()
                    cart.status = 1
                    cart.productName = product.name
                    cart.qty = edtQuantity.text.toString().trim().toDouble()
                    cart.unitPrice = product.unitPrice.toDouble()
                    cart.unitName = product.unitName
                    cart.avlQty = product.qty.toDouble()
                    cart.gid = product.gid

                    insertid = dbInstance.cartDAO().insertCart(cart)

                }
            }
            return insertid
        }

        override fun onPreExecute() {
            super.onPreExecute()

        }

        override fun onPostExecute(result: Long) {
            super.onPostExecute(result)

            if (result > 0) {
                finish()
            } else {
                Toast.makeText(this@AddToCartActivity, "Error in card add", Toast.LENGTH_LONG).show()
                finish()
            }


        }
    }


    fun showStartCalender(startCalendar: java.util.Calendar) {

        val startDatePickerDialog = DatePickerDialog(this@AddToCartActivity, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

            tvDatePickFrom.text = dayOfMonth.toString() + "-" + (month + 1).toString() + "-" + year.toString()

            startDay = dayOfMonth
            startMonth = month
            startYear = year
            if (tvDatePickFrom.text.toString().trim().isEmpty()) {
                tvDatePickTo.isClickable = false
                tvDatePickTo.isEnabled = false

            } else {
                tvDatePickTo.isClickable = true
                tvDatePickTo.isEnabled = true
            }

        }, startYear, startMonth, startDay)

        val tempCalendar = java.util.Calendar.getInstance()
        tempCalendar.set(java.util.Calendar.DAY_OF_MONTH, endDay - 1)
        tempCalendar.set(java.util.Calendar.MONTH, endMonth)
        tempCalendar.set(java.util.Calendar.YEAR, endYear)
        startDatePickerDialog.datePicker.minDate = System.currentTimeMillis() - 10000

        if (tvDatePickTo.text.toString().trim().isEmpty()) {

        } else {
            startDatePickerDialog.datePicker.maxDate = tempCalendar.timeInMillis
        }

        startDatePickerDialog.show()

    }

    fun showEndCalender(endCalendar: java.util.Calendar) {

        val endCalendarDatePicker = DatePickerDialog(this@AddToCartActivity, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            tvDatePickTo.text = dayOfMonth.toString() + "-" + (month + 1).toString() + "-" + year.toString()
            endDay = dayOfMonth
            endMonth = month
            endYear = year

        }, endYear, endMonth, endDay)

        val tempCalendar = java.util.Calendar.getInstance()
        tempCalendar.set(java.util.Calendar.DAY_OF_MONTH, startDay + 1)
        tempCalendar.set(java.util.Calendar.MONTH, startMonth)
        tempCalendar.set(java.util.Calendar.YEAR, startYear)

        endCalendarDatePicker.datePicker.minDate = tempCalendar.timeInMillis
        endCalendarDatePicker.show()

    }


}
