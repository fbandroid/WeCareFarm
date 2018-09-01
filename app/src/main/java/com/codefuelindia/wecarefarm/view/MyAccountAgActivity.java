package com.codefuelindia.wecarefarm.view;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.codefuelindia.wecarefarm.R;
import com.codefuelindia.wecarefarm.Utils.Utils;
import com.codefuelindia.wecarefarm.common.RetrofiltClient;
import com.codefuelindia.wecarefarm.cons.Constants;
import com.codefuelindia.wecarefarm.model.MyRes;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class MyAccountAgActivity extends AppCompatActivity {

    GetAgentAccountAPI getAgentAccountAPI;

    TextView textView_name, textView_collAmt, textView_godownAmt, textView_remainAmt;

    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account_ag);

        getAgentAccountAPI = getFarmerTransactionAPIService(Constants.BASE_URL);

        textView_name = findViewById(R.id.myAccAg_tv_name);
        textView_collAmt = findViewById(R.id.myAccAg_tv_collAmt);
        textView_godownAmt = findViewById(R.id.myAccAg_tv_godownAmt);
        textView_remainAmt = findViewById(R.id.myAccAg_tv_remainAmt);

        getAllData();

    }

    private void getAllData() {
        pd = new ProgressDialog(this);
        pd.setMessage("loading");
        pd.show();

        String aid = Utils.getSharedPreference(Constants.MY_PREF, this).getString(Constants.ID, "");
        String gid = Utils.getSharedPreference(Constants.MY_PREF, this).getString(Constants.GID, "");

        getAgentAccountAPI.getAgentAccountAPI(aid, gid).enqueue(new Callback<MyRes>() {
            @Override
            public void onResponse(Call<MyRes> call, Response<MyRes> response) {

                if (response.isSuccessful()) {

                    if (response.body() != null) {

                        String name = response.body().getName();
                        String collected, godown;

                        if (response.body().getAgent_rec_amt() == null || response.body().getAgent_give_amount() == null) {
                            collected = "0";
                            godown = "0";

                        } else {
                            collected = response.body().getAgent_rec_amt();
                            godown = response.body().getAgent_give_amount();
                        }


                        double remainAmt = Double.parseDouble(collected) - Double.parseDouble(godown);
                        String remaining = String.valueOf(remainAmt);

                        textView_name.setText(name);
                        textView_collAmt.setText(collected + "/-");
                        textView_godownAmt.setText(godown + "/-");
                        textView_remainAmt.setText(remaining + "/-");


                        pd.dismiss();

                    } else {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Response body is null", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), "Response not successful", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<MyRes> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "onFailure", Toast.LENGTH_SHORT).show();
            }
        });

    }


//------------------------------------------ APIs ------------------------------------------------//

    GetAgentAccountAPI getFarmerTransactionAPIService(String baseUrl) {
        return RetrofiltClient.getRettofitClient(baseUrl, "").create(GetAgentAccountAPI.class);
    }

    interface GetAgentAccountAPI {
        @POST("user/agentprofileapi/")
        @FormUrlEncoded
        Call<MyRes> getAgentAccountAPI(@Field("aid") String aid,
                                       @Field("gid") String gid
        );
    }

}
