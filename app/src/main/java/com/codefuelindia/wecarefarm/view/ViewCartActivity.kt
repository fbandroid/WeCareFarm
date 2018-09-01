package com.codefuelindia.wecarefarm.view

import android.content.Context
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
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
import com.codefuelindia.wecarefarm.db.Cart
import com.codefuelindia.wecarefarm.db.CartDatabase
import com.codefuelindia.wecarefarm.model.MyRes
import com.codefuelindia.wecarefarm.model.Order
import com.codefuelindia.wecarefarm.model.OrderList
import kotlinx.android.synthetic.main.activity_view_cart.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import java.lang.NumberFormatException

class ViewCartActivity : AppCompatActivity() {

    private var cartList = ArrayList<Cart>()
    private var id = ""
    private lateinit var dbInstance: CartDatabase
    private var customAdapter: CustomAdapter? = null
    private lateinit var bunchProductOrder: BunchProductOrder
    private var lastclick = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_cart)

        bunchProductOrder = bunchOrderListService()

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar?.title = "My Cart"

        toolbar.setNavigationOnClickListener { finish() }


        dbInstance = CartDatabase.getDatabase(applicationContext)
        val sharedPreferences = Utils.getSharedPreference(Constants.MY_PREF, this@ViewCartActivity)
        id = sharedPreferences.getString(Constants.ID, "")

        GetCartList().execute()

        btnCheckOut.setOnClickListener {

            if (SystemClock.elapsedRealtime() - lastclick < 2000) {
                return@setOnClickListener
            }

            lastclick = SystemClock.elapsedRealtime()


            for (items in cartList) {

                if (items.qty != 0.0 && items.qty > items.avlQty) {
                    return@setOnClickListener
                }

            }


            /**
             *   Add multiple products
             */

            val orderList = ArrayList<Order>()
            if (cartList.size > 0) {

                for (items in cartList) {

                    if (items.qty != 0.0 && items.qty <= items.avlQty) {
                        val order = Order()
                        order.cid = items.cid
                        order.pid = items.pid
                        order.qty = items.qty.toString()
                        order.orderType = "pre"
                        order.qid = items.gid
                        orderList.add(order)
                    } else {

                    }


                }

                /**
                 *   Add multiple products ends
                 */

                if (orderList.size > 0) {

                    val finalListOrder = OrderList()
                    finalListOrder.order = orderList

                    /**
                     *   loader start
                     *
                     */

                    val dialogBuilder = AlertDialog.Builder(this@ViewCartActivity as Context)
                    val inflater = this.layoutInflater
                    val dialogView = inflater.inflate(R.layout.alert_label_editor, null)
                    dialogBuilder.setView(dialogView)

                    GlideApp.with(this@ViewCartActivity)
                            .load(this@ViewCartActivity?.getDrawable(R.drawable.loader)).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                            .into(dialogView.findViewById<ImageView>(R.id.ivGif))
                    dialogBuilder.setCancelable(false)
                    val alertDialog = dialogBuilder.create()
                    alertDialog.window.setBackgroundDrawable(this@ViewCartActivity?.getDrawable(android.R.color.transparent))
                    alertDialog.show()

                    /**
                     *  loader end
                     */

                    bunchProductOrder.bunchProductOrder(finalListOrder).enqueue(
                            object : Callback<MyRes> {
                                override fun onFailure(call: Call<MyRes>?, t: Throwable?) {
                                    alertDialog.dismiss()
                                    Toast.makeText(this@ViewCartActivity, "Error occurred", Toast.LENGTH_LONG).show()
                                    finish()

                                }

                                override fun onResponse(call: Call<MyRes>?, response: Response<MyRes>?) {
                                    alertDialog.dismiss()

                                    if (response!!.isSuccessful) {

                                        if (response!!.body()!!.msg.equals("true", true)) {
                                            Toast.makeText(this@ViewCartActivity, "Successfully order placed..", Toast.LENGTH_LONG).show()


                                            AsyncTask.execute {
                                                if (dbInstance != null) {
                                                    dbInstance.cartDAO().deleteAllOrder()
                                                }
                                            }

                                            finish()

                                        } else {
                                            Toast.makeText(this@ViewCartActivity, "Error occurred", Toast.LENGTH_LONG).show()
                                            finish()
                                        }

                                    } else {
                                        Toast.makeText(this@ViewCartActivity, "Error occurred", Toast.LENGTH_LONG).show()
                                        finish()
                                    }
                                }


                            }
                    )


                } else {


                }


            }


        }

    }

    inner class GetCartList() : AsyncTask<Void, Void, List<Cart>>() {
        override fun doInBackground(vararg params: Void?): List<Cart>? {


            if (dbInstance != null) {

                cartList = dbInstance.cartDAO().getCartList(id) as ArrayList<Cart>


            }

            return cartList
        }

        override fun onPreExecute() {
            super.onPreExecute()

        }

        override fun onPostExecute(result: List<Cart>?) {
            super.onPostExecute(result)

            if (result != null && result.size > 0) {
                customAdapter = CustomAdapter(result as ArrayList<Cart>)
                rvViewCart.layoutManager = LinearLayoutManager(this@ViewCartActivity, LinearLayoutManager.VERTICAL, false)
                rvViewCart.adapter = customAdapter
            } else {
                btnCheckOut.visibility = View.GONE
            }

        }
    }

    inner class DeleteCart() : AsyncTask<String, Void, Int>() {

        var cart: Cart? = null

        override fun doInBackground(vararg params: String): Int {

            var deleteRowId = -1

            if (dbInstance != null) {

                for (items in cartList) {
                    if (items.pid == params[0]) {

                        cart = items

                        break
                    }
                }

                if (cart != null) {
                    deleteRowId = dbInstance.cartDAO().deleteCart(cart)

                }


            }

            return deleteRowId
        }

        override fun onPreExecute() {
            super.onPreExecute()

        }

        override fun onPostExecute(result: Int) {
            super.onPostExecute(result)

            if (result > 0) {
                cartList.remove(cart)
                customAdapter!!.notifyDataSetChanged()

                if (cartList.size == 0) {
                    btnCheckOut.visibility = View.GONE
                    tvGrandTotal.visibility = View.GONE
                }

            }


        }
    }


    /**
     * Caregory list
     *
     *
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    inner class CustomAdapter(private val dataSet: ArrayList<Cart>) :
            RecyclerView.Adapter<CustomAdapter.ViewHolder>() {


        /**
         * Provide a reference to the type of views that you are using (custom ViewHolder)
         */
        inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val tvProductName: TextView
            val ivDelete: ImageView
            val edtQty: TextInputEditText
            val tvPrice: TextView
            val tvTotalPrice: TextView
            val tvAvlQty: TextView

            init {
                // Define click listener for the ViewHolder's View.
                v.setOnClickListener {


                }
                tvProductName = v.findViewById(R.id.tvProductName)
                ivDelete = v.findViewById(R.id.ivDeleteCart)
                edtQty = v.findViewById(R.id.edtQty)
                tvPrice = v.findViewById(R.id.tvPrice)
                tvTotalPrice = v.findViewById(R.id.tvTotal)

                tvAvlQty = v.findViewById(R.id.tvAvlQty)

                ivDelete.setOnClickListener {

                    if (dataSet.size > 0)
                        DeleteCart().execute(dataSet[adapterPosition].pid)

                }

                edtQty.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                        if (s.toString().trim().isNotEmpty()) {


                            try {
                                dataSet[adapterPosition].qty = s.toString().toDouble()
                                var grandTotal = 0.0
                                for (items in dataSet) {
                                    grandTotal += (items.qty * items.unitPrice).toDouble()


                                }

                                if (edtQty.text.toString().trim().isNotEmpty()) {

                                    if (edtQty.text.toString().trim().toDouble() > dataSet[adapterPosition].avlQty) {
                                        edtQty.error = "Invalid Quantity"
                                    } else {
                                        edtQty.error = null
                                    }


                                    tvTotalPrice.text = "Total: ".plus((edtQty.text.toString().toDouble() * dataSet[adapterPosition].unitPrice)
                                            .toString())
                                } else {
                                    tvTotalPrice.text = ""
                                }

                                tvGrandTotal.text = "Grand Total: ".plus(grandTotal.toString())


                            } catch (e: NumberFormatException) {

                            }


                        } else {
                            dataSet[adapterPosition].qty = 0.0
                            tvTotalPrice.text = ""


                        }


                    }
                })

            }
        }

        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            // Create a new view.
            val v = LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.row_view_cart, viewGroup, false)

            return ViewHolder(v)
        }

        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {


            // Get element from your dataset at this position and replace the contents of the view
            // with that element

            // Set a positive button and its click listener on alert dialog
            when {
                dataSet[position].unitName.equals("KG", true) -> {
                    viewHolder.edtQty.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL //for decimal numbers
                    viewHolder.edtQty.setText(dataSet[position].qty.toString())

                }
                dataSet[position].unitName.equals("PIECE", true) -> {
                    viewHolder.edtQty.inputType = InputType.TYPE_CLASS_NUMBER
                    viewHolder.edtQty.setText(dataSet[position].qty.toInt().toString())


                }
                dataSet[position].unitName.equals("DOZEN", true) -> {
                    viewHolder.edtQty.inputType = InputType.TYPE_CLASS_NUMBER
                    viewHolder.edtQty.setText(dataSet[position].qty.toInt().toString())

                }
            }


            if (viewHolder.edtQty.text.toString().isNotEmpty()) {
                dataSet[position].qty = viewHolder.edtQty.text.toString().toDouble()
            }

            viewHolder.tvProductName.text = dataSet[position].productName
            viewHolder.tvPrice.text = dataSet[position].unitPrice.toString().plus(" per ").plus(dataSet[position].unitName)
            viewHolder.tvAvlQty.text = "Avl Qty: " + dataSet[position].avlQty.toString().plus(" ").plus(dataSet[position].unitName)

            try {
                if (viewHolder.edtQty.text.toString().trim().isNotEmpty()) {
                    viewHolder.tvTotalPrice.text = "Total: ".plus((viewHolder.edtQty.text.toString().toDouble() * dataSet[position].unitPrice)
                            .toString())
                } else {
                    viewHolder.tvTotalPrice.text = ""
                }

                var grandTotal = 0.0
                for (items in dataSet) {
                    grandTotal += (items.qty * items.unitPrice).toDouble()


                }

                tvGrandTotal.text = "Grand Total: ".plus(grandTotal.toString())
            } catch (e: NumberFormatException) {

            }


        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = dataSet.size


    }

    interface BunchProductOrder {

        @POST("user/addtocard_buy_api/")
        fun bunchProductOrder(@Body bunchorder: OrderList): Call<MyRes>


    }

    fun bunchOrderListService(): BunchProductOrder {
        return RetrofiltClient.getRettofitClient(Constants.BASE_URL, "x").create(BunchProductOrder::class.java)
    }


}
