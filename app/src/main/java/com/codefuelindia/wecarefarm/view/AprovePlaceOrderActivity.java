package com.codefuelindia.wecarefarm.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.codefuelindia.wecarefarm.R;
import com.codefuelindia.wecarefarm.Utils.Utils;
import com.codefuelindia.wecarefarm.common.RetrofiltClient;
import com.codefuelindia.wecarefarm.cons.Constants;
import com.codefuelindia.wecarefarm.model.AprovePlaceOrderRes;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class AprovePlaceOrderActivity extends AppCompatActivity {

    GetAllOrderAPI getAllOrderAPI;

    ListView listView_AnP_order;
    ProgressBar progressBar;

    MyAdapter myAdapter;
    private android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aprove_place_order);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setTitle("Current Order");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getAllOrderAPI = getGetAllOrderAPIService(Constants.BASE_URL);

        listView_AnP_order = findViewById(R.id.aprove_place_order_listView);
        progressBar = findViewById(R.id.aprove_place_order_progressbar);

        getAllOrders();

    }

    private void getAllOrders() {
        progressBar.setVisibility(View.VISIBLE);

        String id = Utils.getSharedPreference(Constants.MY_PREF, this).getString(Constants.ID, "");

        getAllOrderAPI.getAllOrders(id).enqueue(new Callback<List<AprovePlaceOrderRes>>() {
            @Override
            public void onResponse(Call<List<AprovePlaceOrderRes>> call, Response<List<AprovePlaceOrderRes>> response) {

                if (response.isSuccessful()) {

                    if (response.body() != null) {

                        String[] oNumber = new String[response.body().size()];
                        String[] status = new String[response.body().size()];
                        String[] timestamp = new String[response.body().size()];
                        String[] amount = new String[response.body().size()];

                        for (int i = 0; i < response.body().size(); i++) {
                            oNumber[i] = response.body().get(i).getOrderNo();
                            status[i] = response.body().get(i).getStatus();
                            timestamp[i] = response.body().get(i).getTimestamp();
                            amount[i] = response.body().get(i).getGrandTotal();
                        }

                        myAdapter = new MyAdapter(getApplicationContext(), R.layout.row_list_ap_orders, oNumber, status, timestamp, amount);
                        listView_AnP_order.setAdapter(myAdapter);

                        progressBar.setVisibility(View.GONE);
                        //Toast.makeText(getApplicationContext(), "Response Done", Toast.LENGTH_SHORT).show();

                    } else {
                        progressBar.setVisibility(View.GONE);
                        // Toast.makeText(getApplicationContext(), "Response body is null", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Response not successful", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<AprovePlaceOrderRes>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "onFailure", Toast.LENGTH_SHORT).show();
            }
        });

    }


//------------------------------------------ APIs -------------------------------------------------------//

    GetAllOrderAPI getGetAllOrderAPIService(String baseUrl) {
        return RetrofiltClient.getRettofitClient(baseUrl, "").create(GetAllOrderAPI.class);
    }

    interface GetAllOrderAPI {
        @POST("user/aprove_place_orderapi/")
        @FormUrlEncoded
        Call<List<AprovePlaceOrderRes>> getAllOrders(@Field("id") String id);
    }

//---------------------------------------- Adapter Class ------------------------------------------------//

    class MyAdapter extends ArrayAdapter<String> {

        private Context ctx;

        private String[] oNumberArray;
        private String[] statusArray;
        private String[] timstampArray;
        private String[] amountArray;

        public MyAdapter(@NonNull Context context, int resource, String[] oNumberArray, String[] statusArray, String[] timstampArray, String[] amountArray) {
            super(context, resource);
            this.ctx = context;
            this.oNumberArray = oNumberArray;
            this.statusArray = statusArray;
            this.timstampArray = timstampArray;
            this.amountArray = amountArray;
        }

        @Override
        public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.row_list_ap_orders, parent, false);


            TextView tv_oNumber = row.findViewById(R.id.row_list_ap_orders_tv_Ono);
            tv_oNumber.setText(oNumberArray[position]);

            TextView tv_status = row.findViewById(R.id.row_list_ap_orders_tv_status);
            String str_status = getStringStatus(statusArray[position]);
            tv_status.setText(str_status);

            TextView tv_timestamp = row.findViewById(R.id.row_list_ap_orders_tv_timestamp);
            tv_timestamp.setText(timstampArray[position]);

            TextView tv_amount = row.findViewById(R.id.row_list_ap_orders_tv_amt);
            tv_amount.setText(amountArray[position]);

            return row;
        }

        @Override
        public int getCount() {
            return oNumberArray.length;
        }

        private String getStringStatus(String intStatus) {
            String str_status = null;

            switch (intStatus) {
                case "1":
                    str_status = "Order placed";
                    break;

                case "2":
                    str_status = "Order approved";
                    break;

                case "3":
                    str_status = "Order canceled";
                    break;

                case "4":
                    str_status = "Order packed";
                    break;

                case "5":
                    str_status = "In process";
                    break;

                case "6":
                    str_status = "Delivered";
                    break;

                case "7":
                    str_status = "Completed";
                    break;

            }

            return str_status;
        }


    }


}
