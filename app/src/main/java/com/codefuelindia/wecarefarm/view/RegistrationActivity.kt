package com.codefuelindia.wecarefarm.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.codefuelindia.wecarefarm.R
import com.codefuelindia.wecarefarm.Utils.GlideApp
import com.codefuelindia.wecarefarm.Utils.Utils
import com.codefuelindia.wecarefarm.common.RetrofiltClient
import com.codefuelindia.wecarefarm.cons.Constants
import com.codefuelindia.wecarefarm.model.*
import com.google.gson.Gson

import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.content_registration.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.util.ArrayList



class RegistrationActivity : AppCompatActivity() {


    private lateinit var registerUser: RegisterUser
    private var lastclick = 0L
    private lateinit var getLists: ArrayList<GetList>
    private lateinit var getDistrictName: GetDistrictName
    private var city: String? = null
    private var district: String? = null
    private var state: String? = null
    private var role = ""
    private lateinit var getAllProducts: GetAllProducts
    private var productListArray = ArrayList<AllProductList>()
    private var productAdapter: ProductAdapter? = null
    private var isChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        setSupportActionBar(toolbar)
        getLists = ArrayList()
        getDistrictName = getDistrictNameAPI(Constants.BASE_URL)
        getAllProducts = getAllProductListService(Constants.BASE_URL)
        registerUser = getRegistrationService()

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.title = "Registration"

        toolbar.setNavigationOnClickListener { finish() }


        val loader = showLoader()

        getAllProducts.getAllProductListApi().enqueue(object : Callback<List<AllProductList>> {
            override fun onFailure(call: Call<List<AllProductList>>?, t: Throwable?) {

                loader.dismiss()


            }

            override fun onResponse(call: Call<List<AllProductList>>?, response: Response<List<AllProductList>>?) {

                loader.dismiss()

                if (response!!.isSuccessful) {

                    productListArray = response.body() as ArrayList<AllProductList>
                    if (productListArray.size > 0) {

                        productAdapter = ProductAdapter(productListArray)
                        rvFarmerProduct.layoutManager = GridLayoutManager(this@RegistrationActivity, 2, GridLayoutManager.VERTICAL, false)

                        rvFarmerProduct.adapter = productAdapter

                    }


                }

            }


        })



        spRoleType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                if (parent!!.selectedItem.toString().equals("User", true)) {
                    role = "3"

                    llFarmerProductContainer.visibility = View.GONE
                    Log.e("role", role)

                } else if (parent!!.selectedItem.toString().equals("Farmer(Seller)", true)) {
                    role = "2"
                    Log.e("role", role)

                    llFarmerProductContainer.visibility = View.VISIBLE
                }


            }

        }


        edtPinCode.setOnFocusChangeListener { v, hasFocus ->

            if (!hasFocus && edtPinCode.text.toString().trim().length == 6) {

                getDistrictName.districtList(edtPinCode.text.toString().trim()).enqueue(
                        object : Callback<List<PincodeList>> {
                            override fun onFailure(call: Call<List<PincodeList>>?, t: Throwable?) {

                                Log.e("failure", "fail")


                            }

                            override fun onResponse(call: Call<List<PincodeList>>?, response: Response<List<PincodeList>>?) {

                                if (response!!.isSuccessful) {

                                    city = response.body()?.get(0)?.pincode?.taluka!!
                                    district = response.body()?.get(0)?.pincode?.district!!
                                    state = response.body()?.get(0)?.pincode?.state

                                    edtCity.setText(city)
                                    edtDistrict.setText(district)
                                    edtState.setText(state)


                                } else {

                                }

                            }

                        }
                )

            } else {

            }


        }


        btnRegister.setOnClickListener {

            if (SystemClock.elapsedRealtime() - lastclick < 2000) {
                return@setOnClickListener
            }

            lastclick = SystemClock.elapsedRealtime()


            for (items in productListArray) {
                if (items.isChecked) {
                    isChecked = true
                    break
                }
            }


            when {
                edtName.text.toString().trim().isEmpty() -> {
                    edtName.requestFocus()
                    Snackbar.make(llRegContainer, "Name Required", Snackbar.LENGTH_LONG).show()

                }
                edtMobile.text.toString().trim().length != 10 -> {

                    edtMobile.requestFocus()
                    Snackbar.make(llRegContainer, "Mobile Required", Snackbar.LENGTH_LONG).show()


                }
                !edtEmail.text.toString().trim().isEmpty() && !Utils.isValidEmail(edtEmail.text.toString().trim()) -> {

                    edtEmail.requestFocus()
                    Snackbar.make(llRegContainer, "Email Required", Snackbar.LENGTH_LONG).show()

                }
                edtPinCode.text.toString().trim().length != 6 -> {


                    edtPinCode.requestFocus()
                    Snackbar.make(llRegContainer, "Pincode Required", Snackbar.LENGTH_LONG).show()

                }
                edtCity.text.toString().trim().isEmpty() -> {

                    edtCity.requestFocus()
                    Snackbar.make(llRegContainer, "City Required", Snackbar.LENGTH_LONG).show()

                }
                edtDistrict.text.toString().trim().isEmpty() -> {

                    edtDistrict.requestFocus()
                    Snackbar.make(llRegContainer, "District Required", Snackbar.LENGTH_LONG).show()

                }
                edtState.text.toString().trim().isEmpty() -> {

                    edtState.requestFocus()
                    Snackbar.make(llRegContainer, "State Required", Snackbar.LENGTH_LONG).show()

                }
                edtHouseNo.text.toString().trim().isEmpty() -> {

                    edtHouseNo.requestFocus()
                    Snackbar.make(llRegContainer, "House No Required", Snackbar.LENGTH_LONG).show()

                }
                edtPwd.text.toString().trim().isEmpty() -> {

                    edtPwd.requestFocus()
                    Snackbar.make(llRegContainer, "Password Required", Snackbar.LENGTH_LONG).show()

                }
                edtConfirmPwd.text.toString().trim() != edtPwd.text.toString().trim() -> {

                    edtConfirmPwd.requestFocus()
                    Snackbar.make(llRegContainer, "Password not matched", Snackbar.LENGTH_LONG).show()

                }
                else -> {



                    if (role == "2" && !isChecked) {
                        return@setOnClickListener
                    }




                    val registrationModel = RegistrationModel()
                    registrationModel.name = edtName.text.toString().trim()
                    registrationModel.role = role
                    registrationModel.city = edtCity.text.toString().trim()
                    registrationModel.mobile = edtMobile.text.toString().trim()
                    registrationModel.email = edtEmail.text.toString().trim()
                    registrationModel.pincode = edtPinCode.text.toString().trim()
                    registrationModel.district = edtDistrict.text.toString().trim()
                    registrationModel.state = edtState.text.toString().trim()
                    registrationModel.house_no = edtHouseNo.text.toString().trim()
                    registrationModel.society_name = edtSocietyName.text.toString().trim()
                    registrationModel.landmark = edtLandMarkName.text.toString().trim()
                    registrationModel.pwd = edtPwd.text.toString().trim()

                    val selectedProductList = ArrayList<AllProductList>()

                    for (items in productListArray) {
                        if (items.isChecked) {
                            selectedProductList.add(items)
                        }
                    }


                    if (role == "2") {
                        registrationModel.allProductLists = selectedProductList
                    }


                    Log.e("reg json", Gson().toJson(registrationModel))

                    val dialog = showLoader()

                    registerUser.registeruserapi(registrationModel)
                            .enqueue(object : Callback<MyRes> {
                                override fun onFailure(call: Call<MyRes>?, t: Throwable?) {
                                    dialog.dismiss()
                                    Toast.makeText(this@RegistrationActivity, "Connection error", Toast.LENGTH_LONG).show()


                                }

                                override fun onResponse(call: Call<MyRes>?, response: Response<MyRes>?) {
                                    dialog.dismiss()

                                    if (response!!.isSuccessful) {

                                        when {
                                            response.body()!!.msg.equals("true", true) -> {



                                                val intent = Intent(this@RegistrationActivity, ForgotPwdActivity::class.java)
                                                intent.putExtra(Constants.ID, response.body()!!.id)
                                                intent.putExtra(Constants.TYPE, "reg")
                                                startActivity(intent)

                                            }
                                            response.body()!!.msg.equals("false", true) -> Toast.makeText(this@RegistrationActivity, "Error occurred", Toast.LENGTH_LONG).show()
                                            response.body()!!.msg.equals("xxx", true) -> Toast.makeText(this@RegistrationActivity, "Already registered!", Toast.LENGTH_LONG).show()
                                        }

                                    } else {
                                        Toast.makeText(this@RegistrationActivity, "Connection error", Toast.LENGTH_LONG).show()

                                    }

                                }


                            })


                }
            }
        }


    }

    interface RegisterUser {
        @POST("user/registrationapi/")
        fun registeruserapi(@Body registrationModel: RegistrationModel): Call<MyRes>


    }

    fun getRegistrationService(): RegisterUser {
        return RetrofiltClient.getRettofitClient(Constants.BASE_URL, "x").create(RegisterUser::class.java)

    }

    fun showLoader(): AlertDialog {

        val dialogBuilder = AlertDialog.Builder(this@RegistrationActivity as Context)
// ...Irrelevant code for customizing the buttons and title
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.alert_label_editor, null)
        dialogBuilder.setView(dialogView)

        GlideApp.with(this@RegistrationActivity)
                .load(this@RegistrationActivity?.getDrawable(R.drawable.loader)).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(dialogView.findViewById<ImageView>(R.id.ivGif))
        dialogBuilder.setCancelable(false)
        val alertDialog = dialogBuilder.create()
        alertDialog.window.setBackgroundDrawable(this@RegistrationActivity?.getDrawable(android.R.color.transparent))
        alertDialog.show()

        return alertDialog


    }

    internal interface GetDistrictName {
        @POST("user/getpincodeapi/")
        @FormUrlEncoded
        fun districtList(@Field("pincode") pincode: String): Call<List<PincodeList>>
    }


    internal fun getDistrictNameAPI(baseUrl: String): GetDistrictName {
        return RetrofiltClient.getRettofitClient(baseUrl, "x")?.create(GetDistrictName::class.java)!!
    }


    internal interface GetAllProducts {
        @POST("user/list_productapi/")
        fun getAllProductListApi(): Call<List<AllProductList>>
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

            val chkProduct: CheckBox

            init {
                // Define click listener for the ViewHolder's View.

                chkProduct= v.findViewById(R.id.chkProduct)

                chkProduct.setOnCheckedChangeListener { buttonView, isChecked ->

                    dataSet[adapterPosition].isChecked = isChecked

                }


            }
        }

        // Create new views (invoked by the layout manager)
        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            // Create a new view.
            val v = LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.row_farmer_products, viewGroup, false)

            return ViewHolder(v)
        }

        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

            // Get element from your dataset at this position and replace the contents of the view
            // with that element

            viewHolder.chkProduct.isChecked = dataSet[position].isChecked

            viewHolder.chkProduct.text = dataSet[position].name.trim()


        }

        // Return the size of your dataset (invoked by the layout manager)
        override fun getItemCount() = dataSet.size


    }

}
