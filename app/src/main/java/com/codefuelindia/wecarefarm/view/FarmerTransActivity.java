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

import com.codefuelindia.wecarefarm.R;
import com.codefuelindia.wecarefarm.Utils.Utils;
import com.codefuelindia.wecarefarm.common.RetrofiltClient;
import com.codefuelindia.wecarefarm.cons.Constants;
import com.codefuelindia.wecarefarm.model.FarmerTransRes;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class FarmerTransActivity extends AppCompatActivity {

    GetFarmerTransactionAPI getFarmerTransactionAPI;

    ListView listView_transactions;
    ProgressBar progressBar;

    MyAdapter adapter_transList;

    private String[] pName, qty, date, price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_trans);

        getFarmerTransactionAPI = getFarmerTransactionAPIService(Constants.BASE_URL);

        listView_transactions = findViewById(R.id.farmerTrans_listView);
        progressBar = findViewById(R.id.farmerTrans_progressbar);

        getTransData();

    }

    private void getTransData() {
        progressBar.setVisibility(View.VISIBLE);

        String f_id = Utils.getSharedPreference(Constants.MY_PREF, this).getString(Constants.ID, "");

        getFarmerTransactionAPI.getFarmerTransactions(f_id).enqueue(new Callback<List<FarmerTransRes>>() {
            @Override
            public void onResponse(Call<List<FarmerTransRes>> call, Response<List<FarmerTransRes>> response) {

                if (response.isSuccessful()) {

                    if (response.body() != null) {

                        pName = new String[response.body().size()];
                        qty = new String[response.body().size()];
                        date = new String[response.body().size()];
                        price = new String[response.body().size()];

                        for (int i = 0; i < response.body().size(); i++) {
                            pName[i] = response.body().get(i).getProductName();
                            qty[i] = response.body().get(i).getQty();
                            date[i] = response.body().get(i).getCreateAt();
                            price[i] = response.body().get(i).getApprovePrice();
                        }

                        adapter_transList = new MyAdapter(getApplicationContext(), R.layout.row_list_farmer_product, pName, qty, date, price);
                        listView_transactions.setAdapter(adapter_transList);

                        adapter_transList.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Response Done", Toast.LENGTH_SHORT).show();

                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Response body is null", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Response not successful", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<FarmerTransRes>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "onFailure", Toast.LENGTH_SHORT).show();
            }
        });

    }


//------------------------------------------ APIs -------------------------------------------------------//

    GetFarmerTransactionAPI getFarmerTransactionAPIService(String baseUrl) {
        return RetrofiltClient.getRettofitClient(baseUrl, "").create(GetFarmerTransactionAPI.class);
    }

    interface GetFarmerTransactionAPI {
        @POST("user/get_farmer_transaction/")
        @FormUrlEncoded
        Call<List<FarmerTransRes>> getFarmerTransactions(@Field("f_id") String id);
    }


//----------------------------------- Adapter Class -----------------------------------//

    class MyAdapter extends ArrayAdapter<String> {

        private Context ctx;

        private String[] pNameArray;
        private String[] qtyArray;
        private String[] dateArray;
        private String[] priceArray;

        public MyAdapter(@NonNull Context context, int resource, String[] pNameArray, String[] qtyArray, String[] dateArray, String[] priceArray) {
            super(context, resource);

            this.ctx = context;
            this.pNameArray = pNameArray;
            this.qtyArray = qtyArray;
            this.dateArray = dateArray;
            this.priceArray = priceArray;
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

        View getCustomView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.row_farmer_trans_item, parent, false);

            TextView tv_pName = row.findViewById(R.id.farmerTransItem_tv_pName);
            tv_pName.setText(pNameArray[position]);

            TextView tv_qty = row.findViewById(R.id.farmerTransItem_tv_qty);
            tv_qty.setText(qtyArray[position]);

            TextView tv_date = row.findViewById(R.id.farmerTransItem_tv_date);
            tv_date.setText(dateArray[position]);

            TextView tv_price = row.findViewById(R.id.farmerTransItem_tv_price);
            tv_price.setText(priceArray[position]);

            return row;
        }

        @Override
        public int getCount() {
            return pNameArray.length;
        }
    }

}
