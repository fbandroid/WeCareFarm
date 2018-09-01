package com.codefuelindia.wecarefarm.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.codefuelindia.wecarefarm.R
import com.codefuelindia.wecarefarm.common.RetrofiltClient
import com.codefuelindia.wecarefarm.cons.Constants
import com.codefuelindia.wecarefarm.fragment.OTPVerifyFragment
import com.codefuelindia.wecarefarm.model.MyRes

import kotlinx.android.synthetic.main.activity_forgot_pwd.*
import kotlinx.android.synthetic.main.content_forgot_pwd.*
import kotlinx.android.synthetic.main.fragment_verify_otp.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

class ForgotPwdActivity : AppCompatActivity() {

    private var lastclick = 0L
    public var type = ""
    public var id = ""
    private var checkMobile: CheckMobile? = null

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pwd)
        setSupportActionBar(toolbar)
        checkMobile = getAPIService()

        if (intent != null) {
            type = intent.getStringExtra(Constants.TYPE)
            id = intent.getStringExtra(Constants.ID)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar.title = "Forgot Password?"
        toolbar.setNavigationOnClickListener { finish() }

        val fragment = supportFragmentManager.findFragmentById(R.id.fragmentOTP)

        if (fragment != null && type != "reg") { //not registration forgot
            toolbar.title = "Forgot Password?"
            supportFragmentManager.beginTransaction().hide(fragment).commit()


            btnNext.setOnClickListener {
                if (SystemClock.elapsedRealtime() - lastclick < 2000) {
                    return@setOnClickListener
                }

                lastclick = SystemClock.elapsedRealtime()


                if (edtMobile.text.toString().trim().length != 10) {
                    Toast.makeText(this@ForgotPwdActivity, "Mobile is not valid", Toast.LENGTH_LONG).show()
                } else {

                    showProgressDialog()

                    checkMobile!!.verifymobile(edtMobile.text.toString().trim { it <= ' ' }).enqueue(object : Callback<MyRes> {
                        override fun onResponse(call: Call<MyRes>, response: Response<MyRes>) {

                            if (progressDialog.isShowing) {
                                progressDialog.dismiss()
                            }

                            if (response.isSuccessful) {

                                if (response.body()!!.msg.equals("true", true)) {

                                    toolbar.title = "OTP verify"
                                    btnNext.visibility = View.GONE
                                    edtMobile.isClickable = false
                                    edtMobile.isEnabled = false

                                    id = response.body()!!.id

                                    val bundle = Bundle()
                                    bundle.putString(Constants.TYPE, "forgot")
                                    bundle.putString(Constants.ID, response.body()!!.id)

                                    val fragment = supportFragmentManager.findFragmentById(R.id.fragmentOTP)

                                    if (fragment != null) {

                                        (fragment as OTPVerifyFragment).setID(response.body()!!.id)

                                        fragment.arguments = bundle
                                        supportFragmentManager.beginTransaction()
                                                .show(fragment).commit()

                                    }


                                } else {
                                    Toast.makeText(this@ForgotPwdActivity, "Wrong mobile", Toast.LENGTH_SHORT).show()
                                }
                            } else {

                                Toast.makeText(this@ForgotPwdActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
                            }

                        }

                        override fun onFailure(call: Call<MyRes>, t: Throwable) {
                            Toast.makeText(this@ForgotPwdActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
                            if (progressDialog.isShowing) {
                                progressDialog.dismiss()
                            }
                        }
                    })


                }


            }


        } else if (fragment != null && type == "reg") {

            txtInMobileNumber.visibility = View.GONE

            toolbar.title = "OTP verify"
            btnNext.visibility = View.GONE
            edtMobile.isClickable = false
            edtMobile.isEnabled = false
            val bundle = Bundle()
            bundle.putString(Constants.ID, id)
            bundle.putString(Constants.TYPE, type)
            fragment.arguments = bundle
            supportFragmentManager.beginTransaction().show(fragment).commit()


        }


    }

    internal fun getAPIService(): CheckMobile {

        return RetrofiltClient.getRettofitClient(Constants.BASE_URL, "x").create(CheckMobile::class.java)
    }

    fun showProgressDialog() {
        progressDialog = ProgressDialog(this@ForgotPwdActivity)
        progressDialog.setTitle("Loading..")
        progressDialog.setCancelable(false)
        progressDialog.show()
    }

    internal interface CheckMobile {

        @POST("user/checkmobileapi/")
        @FormUrlEncoded
        fun verifymobile(@Field("mobile") mobile: String

        ): Call<MyRes>
    }

}
