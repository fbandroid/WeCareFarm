package com.codefuelindia.wecarefarm.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codefuelindia.wecarefarm.R;
import com.codefuelindia.wecarefarm.Utils.Utils;
import com.codefuelindia.wecarefarm.common.RetrofiltClient;
import com.codefuelindia.wecarefarm.cons.Constants;
import com.codefuelindia.wecarefarm.model.FarmerDetailsRes;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class FarmerAccDetailsActivity extends AppCompatActivity {

    GetFarmerDetailsAPI getFarmerDetailsAPI;

    TextView textView_availBalance, textView_name, textView_number, textView_city, textView_address;
    Button button_showTrans;
    ProgressBar progressBar;

    private String balance, name, number, city, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_acc_details);

        getFarmerDetailsAPI = getFarmerDetailsAPIService(Constants.BASE_URL);

        textView_availBalance = findViewById(R.id.farmerAccDetails_tv_availBalance);
        textView_name = findViewById(R.id.farmerAccDetails_tv_name);
        textView_number = findViewById(R.id.farmerAccDetails_tv_number);
        textView_city = findViewById(R.id.farmerAccDetails_tv_city);
        textView_address = findViewById(R.id.farmerAccDetails_tv_address);
        button_showTrans = findViewById(R.id.farmerAccDetails_btn_showTrans);

        progressBar = findViewById(R.id.farmerAccDetails_progressbar);

        setFarmerDetails();

        button_showTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FarmerAccDetailsActivity.this, FarmerTransActivity.class);
                startActivity(i);
            }
        });


    }

    private void setFarmerDetails() {
        progressBar.setVisibility(View.VISIBLE);

        String f_id = Utils.getSharedPreference(Constants.MY_PREF, this).getString(Constants.ID, "");

        getFarmerDetailsAPI.getFarmerDetails(f_id).enqueue(new Callback<List<FarmerDetailsRes>>() {
            @Override
            public void onResponse(Call<List<FarmerDetailsRes>> call, Response<List<FarmerDetailsRes>> response) {

                if (response.isSuccessful()) {

                    if (response.body() != null && response.body().size()>0) {
                        progressBar.setVisibility(View.GONE);

                        balance = response.body().get(0).getAmount();
                        name = response.body().get(0).getName();
                        number = response.body().get(0).getMobile();
                        city = response.body().get(0).getCity();
                        address = response.body().get(0).getHouseNo() + ", " + response.body().get(0).getSocietyName() + ", " + response.body().get(0).getLandmark();

                        textView_availBalance.setText(balance + " /-");
                        textView_name.setText(name);
                        textView_number.setText(number);
                        textView_city.setText(city);
                        textView_address.setText(address);

                        progressBar.setVisibility(View.GONE);

                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "No Data Available", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Response not successful", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<FarmerDetailsRes>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "onFailure", Toast.LENGTH_SHORT).show();
            }
        });


    }


//---------------------------------------- APIs -----------------------------------------//

    GetFarmerDetailsAPI getFarmerDetailsAPIService(String baseUrl) {
        return RetrofiltClient.getRettofitClient(baseUrl, "").create(GetFarmerDetailsAPI.class);
    }

    interface GetFarmerDetailsAPI {
        @POST("user/get_farmer_amount/")
        @FormUrlEncoded
        Call<List<FarmerDetailsRes>> getFarmerDetails(@Field("f_id") String id);
    }

}
