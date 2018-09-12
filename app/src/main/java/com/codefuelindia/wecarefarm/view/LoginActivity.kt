package com.codefuelindia.wecarefarm.view

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import android.provider.ContactsContract
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.codefuelindia.wecarefarm.R
import com.codefuelindia.wecarefarm.Utils.GlideApp
import com.codefuelindia.wecarefarm.Utils.Utils
import com.codefuelindia.wecarefarm.common.RetrofiltClient
import com.codefuelindia.wecarefarm.cons.Constants
import com.codefuelindia.wecarefarm.model.MyRes

import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.content_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

class LoginActivity : AppCompatActivity() {

    private var lastClick = 0L
    private lateinit var loginUser: LoginUser
    private var isLogin = false
    private var role = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(main_toolbar)
        loginUser = getLoginService()

        val sharedPreferences = Utils.getSharedPreference(Constants.MY_PREF, this@LoginActivity)
        isLogin = sharedPreferences.getBoolean(Constants.IS_LOGIN, false)
        role = sharedPreferences.getString(Constants.ROLE, "")

        if (isLogin) {

            when (role) {
                "3" -> { // user
                    startActivity(Intent(this@LoginActivity, UserDashBoard22::class.java))
                    finish()
                }
                "2" -> { // farmer
                    startActivity(Intent(this@LoginActivity, FarmerHomeActivity::class.java))
                    finish()
                }
                "4" -> {
                    startActivity(Intent(this@LoginActivity, DeliveryBoyDashboard::class.java))
                    finish()

                }
            }


        }

        btnSignUp.setOnClickListener {

            if (SystemClock.elapsedRealtime() - lastClick < 2000) {
                return@setOnClickListener
            }

            lastClick = SystemClock.elapsedRealtime()

            startActivity(Intent(this@LoginActivity, RegistrationActivity::class.java))


        }

        tvForgotPwd.setOnClickListener {
            if (SystemClock.elapsedRealtime() - lastClick < 2000) {
                return@setOnClickListener
            }

            lastClick = SystemClock.elapsedRealtime()
            val intent = Intent(this@LoginActivity, ForgotPwdActivity::class.java)
            intent.putExtra(Constants.TYPE, "forgot")
            intent.putExtra(Constants.ID, "")

            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            if (SystemClock.elapsedRealtime() - lastClick < 2000) {
                return@setOnClickListener
            }

            lastClick = SystemClock.elapsedRealtime()

            when {
                edtUserName.text.toString().trim().isEmpty() -> {
                    edtUserName.requestFocus()
                    val snackBar = Snackbar.make(loginContainer, "UserName Required", Snackbar.LENGTH_LONG)
                    snackBar.view.setBackgroundColor(Color.RED)
                    snackBar.show()

                }
                edtPwd.text.toString().trim().isEmpty() -> {
                    edtPwd.requestFocus()
                    val snackBar = Snackbar.make(loginContainer, "Password Required", Snackbar.LENGTH_LONG)
                    snackBar.view.setBackgroundColor(Color.RED)
                    snackBar.show()
                }
                else -> {

                    val dialog = showLoader()
                    loginUser.loginapi(edtUserName.text.toString().trim(),
                            edtPwd.text.toString().trim())
                            .enqueue(object : Callback<MyRes> {
                                override fun onFailure(call: Call<MyRes>?, t: Throwable?) {
                                    dialog.dismiss()
                                    Toast.makeText(this@LoginActivity, "Connection error", Toast.LENGTH_LONG).show()
                                }

                                override fun onResponse(call: Call<MyRes>?, response: Response<MyRes>?) {
                                    dialog.dismiss()

                                    if (response!!.isSuccessful) {

                                        if (response.body()!!.msg.equals("true", true)) {


                                            val editor = Utils.writeToPreference(Constants.MY_PREF, this@LoginActivity)
                                            editor.putString(Constants.ID, response.body()!!.id)
                                            editor.putString(Constants.USER_NAME, response.body()!!.user_name)
                                            editor.putString(Constants.NAME, response.body()!!.name)
                                            editor.putString(Constants.ROLE, response.body()!!.role)
                                            editor.putString(Constants.GID, response.body()!!.gid)
                                            editor.putString(Constants.REF_MOBILE,response.body()!!.ref_mobile)

                                            if (response.body()!!.ref_status == "1") {
                                                editor.putBoolean(Constants.IS_REF_ADDED, true)
                                            } else {
                                                editor.putBoolean(Constants.IS_REF_ADDED, false)
                                            }


                                            editor.putBoolean(Constants.IS_LOGIN, true)
                                            editor.apply()
                                            Toast.makeText(this@LoginActivity, "Successfully login", Toast.LENGTH_LONG).show()

                                            when {
                                                response!!.body()!!.role == "3" -> { // user
                                                    startActivity(Intent(this@LoginActivity, UserDashBoard22::class.java))
                                                    finish()
                                                }
                                                response!!.body()!!.role == "2" -> {  // farmer
                                                    startActivity(Intent(this@LoginActivity, FarmerHomeActivity::class.java))
                                                    finish()
                                                }
                                                response!!.body()!!.role == "4" -> {  //Delivery agent
                                                    startActivity(Intent(this@LoginActivity, DeliveryBoyDashboard::class.java))
                                                    finish()
                                                }
                                            }


                                        } else if (response.body()!!.msg.equals("xxx", true)) {
                                            Toast.makeText(this@LoginActivity, "Your Godown Master is not assigned.\nPlease contact Administrator.", Toast.LENGTH_LONG).show()

                                        } else {
                                            Toast.makeText(this@LoginActivity, "Invalid credential", Toast.LENGTH_LONG).show()

                                        }

                                    } else {
                                        Toast.makeText(this@LoginActivity, "Connection error", Toast.LENGTH_LONG).show()

                                    }
                                }


                            })


                    //
                }
            }


        }

    }


    fun showLoader(): AlertDialog {

        val dialogBuilder = AlertDialog.Builder(this@LoginActivity as Context)
// ...Irrelevant code for customizing the buttons and title
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.alert_label_editor, null)
        dialogBuilder.setView(dialogView)

        GlideApp.with(this@LoginActivity)
                .load(this@LoginActivity?.getDrawable(R.drawable.loader)).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(dialogView.findViewById<ImageView>(R.id.ivGif))
        dialogBuilder.setCancelable(false)
        val alertDialog = dialogBuilder.create()
        alertDialog.window.setBackgroundDrawable(this@LoginActivity?.getDrawable(android.R.color.transparent))
        alertDialog.show()

        return alertDialog


    }


    interface LoginUser {

        @POST("user/loginapi/")
        @FormUrlEncoded
        fun loginapi(@Field("email") email: String, @Field("password") password: String): Call<MyRes>

    }

    fun getLoginService(): LoginUser {

        return RetrofiltClient.getRettofitClient(Constants.BASE_URL, "x").create(LoginUser::class.java)

    }

}
