package com.codefuelindia.wecarefarm.fragment

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.codefuelindia.wecarefarm.R
import com.codefuelindia.wecarefarm.Utils.Utils
import com.codefuelindia.wecarefarm.common.RetrofiltClient
import com.codefuelindia.wecarefarm.cons.Constants
import com.codefuelindia.wecarefarm.model.MyRes
import com.codefuelindia.wecarefarm.view.ForgotPwdActivity
import com.codefuelindia.wecarefarm.view.LoginActivity
import com.codefuelindia.wecarefarm.view.ResetActivity
import kotlinx.android.synthetic.main.content_forgot_pwd.*
import kotlinx.android.synthetic.main.fragment_verify_otp.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

class OTPVerifyFragment : Fragment() {

    private var id = ""
    private var type = ""
    private var sendOTP: SendOTP? = null
    private var resendOTP: ResendOTP? = null
    private var progressDialog: ProgressDialog? = null
    private var lastclick = 0L
    private lateinit var myContext: ForgotPwdActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sendOTP = getAPIService()
        resendOTP = getAPIService22()

        myContext = (activity as ForgotPwdActivity)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_verify_otp, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }


    internal interface SendOTP {

        @POST("user/verifycodeapi/")
        @FormUrlEncoded
        fun senotp(@Field("id") id: String,
                   @Field("code") code: String
        ): Call<MyRes>
    }

    internal interface ResendOTP {

        @POST("user/resetcodeapi/")
        @FormUrlEncoded
        fun resend(@Field("id") id: String

        ): Call<MyRes>


    }


    private fun getAPIService(): SendOTP {

        return RetrofiltClient.getRettofitClient(Constants.BASE_URL, "x").create(SendOTP::class.java)
    }

    private fun getAPIService22(): ResendOTP {

        return RetrofiltClient.getRettofitClient(Constants.BASE_URL, "x").create(ResendOTP::class.java)
    }


    public fun setID(id:String){
        this.id=id
    }

    fun showProgressDialog() {
        progressDialog = ProgressDialog(activity)
        progressDialog?.setTitle("Loading..")
        progressDialog?.setCancelable(false)
        progressDialog?.show()
    }

    override fun onResume() {
        super.onResume()
        type = myContext.type
        id = myContext.id
        if (type == "reg") { // registration

           

            btnSubmitOTP.setOnClickListener {



                if (SystemClock.elapsedRealtime() - lastclick < 2000) {
                    return@setOnClickListener
                }
                lastclick = SystemClock.elapsedRealtime()


                if (edtOTP.text.toString().trim().isEmpty()) {
                    Toast.makeText(activity, "Enter OTP", Toast.LENGTH_LONG).show()

                } else {

                    showProgressDialog()

                    sendOTP!!.senotp(id, edtOTP.text.toString().trim { it <= ' ' }).enqueue(object : Callback<MyRes> {
                        override fun onResponse(call: Call<MyRes>, response: Response<MyRes>) {

                            if (progressDialog!!.isShowing()) {
                                progressDialog!!.dismiss()
                            }

                            if (response.isSuccessful) {

                                if (response.body()!!.msg.equals("true", true)) {

                                    val editor = Utils.writeToPreference(Constants.MY_PREF, activity)
                                    editor.putString(Constants.REG_IN_PROGRESS, "")
                                    editor.putString(Constants.USER_NAME, "")
                                    editor.apply()

                                    Toast.makeText(activity, "Successfully registered", Toast.LENGTH_SHORT).show()
                                    activity!!.finishAffinity()

                                    val intent = Intent(context, LoginActivity::class.java)
                                    startActivity(intent)
                                    Toast.makeText(context, "Mobile number is Your Username", Toast.LENGTH_LONG).show()

                                } else {
                                    Toast.makeText(context, "Not Successfully registered", Toast.LENGTH_SHORT).show()

                                }


                            } else {
                                Toast.makeText(context, "Not Successfully registered", Toast.LENGTH_SHORT).show()

                            }


                        }

                        override fun onFailure(call: Call<MyRes>, t: Throwable) {
                            if (progressDialog!!.isShowing()) {
                                progressDialog!!.dismiss()
                            }

                            Toast.makeText(context, "Not Successfully registered", Toast.LENGTH_SHORT).show()

                        }
                    })


                }


            }

            tvResend.setOnClickListener {
                if (SystemClock.elapsedRealtime() - lastclick < 2000) {
                    return@setOnClickListener
                }
                lastclick = SystemClock.elapsedRealtime()

                showProgressDialog()
                resendOTP!!.resend(id).enqueue(object : Callback<MyRes> {
                    override fun onResponse(call: Call<MyRes>, response: Response<MyRes>) {

                        if (progressDialog!!.isShowing()) {
                            progressDialog!!.dismiss()
                        }

                        if (response.isSuccessful) {

                        } else {
                            Toast.makeText(activity, "Try again", Toast.LENGTH_SHORT).show()
                        }

                    }

                    override fun onFailure(call: Call<MyRes>, t: Throwable) {
                        if (progressDialog!!.isShowing()) {
                            progressDialog!!.dismiss()
                        }

                        Toast.makeText(activity, "Try again", Toast.LENGTH_SHORT).show()

                    }
                })

            }

        } else { // forgot

            btnSubmitOTP.setOnClickListener {
                if (SystemClock.elapsedRealtime() - lastclick < 2000) {
                    return@setOnClickListener
                }
                lastclick = SystemClock.elapsedRealtime()


                if (edtOTP.text.toString().trim { it <= ' ' }.isEmpty()) {
                    Toast.makeText(activity, "Please enter OTP", Toast.LENGTH_SHORT).show()

                } else {
                    showProgressDialog()

                    sendOTP!!.senotp(id, edtOTP.text.toString().trim { it <= ' ' }).enqueue(object : Callback<MyRes> {
                        override fun onResponse(call: Call<MyRes>, response: Response<MyRes>) {

                            if (progressDialog!!.isShowing()) {
                                progressDialog!!.dismiss()
                            }

                            if (response.isSuccessful) {

                                if (response.body()!!.msg.equals("true", true)) {

                                    val intent = Intent(activity, ResetActivity::class.java)
                                    intent.putExtra(Constants.ID, id)
                                    startActivity(intent)


                                } else {
                                    Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()

                                }


                            } else {
                                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()

                            }


                        }

                        override fun onFailure(call: Call<MyRes>, t: Throwable) {
                            if (progressDialog!!.isShowing()) {
                                progressDialog!!.dismiss()
                            }

                            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
                        }
                    })
                }


            }


            tvResend.setOnClickListener {
                if (SystemClock.elapsedRealtime() - lastclick < 2000) {
                    return@setOnClickListener
                }
                lastclick = SystemClock.elapsedRealtime()

                showProgressDialog()
                resendOTP!!.resend(id).enqueue(object : Callback<MyRes> {
                    override fun onResponse(call: Call<MyRes>, response: Response<MyRes>) {

                        if (progressDialog!!.isShowing()) {
                            progressDialog!!.dismiss()
                        }

                        if (response.isSuccessful) {

                        } else {
                            Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show()
                        }

                    }

                    override fun onFailure(call: Call<MyRes>, t: Throwable) {
                        if (progressDialog!!.isShowing()) {
                            progressDialog!!.dismiss()
                        }

                        Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show()

                    }
                })


            }


        }

    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)



        if (!hidden) {


        }


    }

}