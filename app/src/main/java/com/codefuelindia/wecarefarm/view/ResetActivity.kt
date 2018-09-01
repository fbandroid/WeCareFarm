package com.codefuelindia.wecarefarm.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast
import com.codefuelindia.wecarefarm.R
import com.codefuelindia.wecarefarm.Utils.Utils
import com.codefuelindia.wecarefarm.common.RetrofiltClient
import com.codefuelindia.wecarefarm.cons.Constants
import com.codefuelindia.wecarefarm.model.MyRes

import kotlinx.android.synthetic.main.activity_reset.*
import kotlinx.android.synthetic.main.content_reset.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

class ResetActivity : AppCompatActivity() {

    private var id = ""
    private var lastclick = 0L
    private var progressDialog: ProgressDialog? = null
    private var resetApi: ResetApi? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset)
        setSupportActionBar(toolbar)
        resetApi = getAPIService()
        if (intent != null) {

            id = intent.getStringExtra(Constants.ID)

        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        btnResetPwd.setOnClickListener {

            if (SystemClock.elapsedRealtime() - lastclick < 2000) {
                return@setOnClickListener
            }
            lastclick = SystemClock.elapsedRealtime()

            if (id.isNotEmpty() && edtNewPwd.text.toString().trim().isNotEmpty() && edtNewPwdConfirm.text.toString().trim() ==
                    edtNewPwd.text.toString().trim()) {

                showProgressDialog()
                resetApi!!.reset(id, edtNewPwd.text.toString().trim { it <= ' ' }).enqueue(object : Callback<MyRes> {
                    override fun onResponse(call: Call<MyRes>, response: Response<MyRes>) {

                        if (progressDialog!!.isShowing) {
                            progressDialog!!.dismiss()
                        }

                        if (response.isSuccessful) {

                            if (response.body()!!.msg.equals("true", true)) {

                                Toast.makeText(applicationContext, "Successfully changed", Toast.LENGTH_SHORT).show()

                                finishAffinity()
                                val intent = Intent(applicationContext, LoginActivity::class.java)
                                startActivity(intent)

                            } else {
                                Toast.makeText(this@ResetActivity, "Not successfully changed", Toast.LENGTH_SHORT).show()

                            }

                        } else {
                            Toast.makeText(this@ResetActivity, "Not successfully changed", Toast.LENGTH_SHORT).show()

                        }
                    }

                    override fun onFailure(call: Call<MyRes>, t: Throwable) {

                        if (progressDialog!!.isShowing()) {
                            progressDialog!!.dismiss()
                        }
                        Toast.makeText(this@ResetActivity, "Not successfully changed", Toast.LENGTH_SHORT).show()

                    }
                })


            }


        }


    }

    internal interface ResetApi {

        @POST("user/resetpassword_api")
        @FormUrlEncoded
        fun reset(@Field("id") id: String, @Field("new_pwd") username: String
        ): Call<MyRes>
    }

    internal fun getAPIService(): ResetApi {

        return RetrofiltClient.getRettofitClient(Constants.BASE_URL, "x").create(ResetApi::class.java)
    }

    fun showProgressDialog() {
        progressDialog = ProgressDialog(this@ResetActivity)
        progressDialog!!.setTitle("Loading..")
        progressDialog!!.setCancelable(false)
        progressDialog!!.show()
    }

}
