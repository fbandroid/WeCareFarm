package com.codefuelindia.wecarefarm.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codefuelindia.wecarefarm.R;
import com.codefuelindia.wecarefarm.Utils.Utils;
import com.codefuelindia.wecarefarm.common.RetrofiltClient;
import com.codefuelindia.wecarefarm.cons.Constants;
import com.codefuelindia.wecarefarm.model.FarmerProductRes;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class FarmerProductActivity extends AppCompatActivity {

    GetFarmerProductAPI getFarmerProductAPI;

    ListView listView_farmerProduct;
    ProgressBar progressBar;

    MyAdapter myAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_product);


        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setTitle("View Stock");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getFarmerProductAPI = getGetFarmerProductAPIService(Constants.BASE_URL);

        listView_farmerProduct = findViewById(R.id.farmer_product_listView);
        progressBar = findViewById(R.id.farmer_product_progressbar);

        getFarmerProducts();

    }

    private void getFarmerProducts() {
        progressBar.setVisibility(View.VISIBLE);

        String id = Utils.getSharedPreference(Constants.MY_PREF, this).getString(Constants.ID, "");

        getFarmerProductAPI.getFarmerProductAPI(id).enqueue(new Callback<List<FarmerProductRes>>() {
            @Override
            public void onResponse(Call<List<FarmerProductRes>> call, Response<List<FarmerProductRes>> response) {

                if (response.isSuccessful()) {

                    if (response.body() != null) {

                        String[] name = new String[response.body().size()];
                        String[] qty = new String[response.body().size()];
                        String[] unit = new String[response.body().size()];

                        for (int i = 0; i < response.body().size(); i++) {
                            name[i] = response.body().get(i).getName();
                            qty[i] = response.body().get(i).getTransactionQty();
                            unit[i] = response.body().get(i).getUnitName();
                        }

                        myAdapter = new MyAdapter(getApplicationContext(), R.layout.row_list_farmer_product, name, qty, unit);
                        listView_farmerProduct.setAdapter(myAdapter);

                        myAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);

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
            public void onFailure(Call<List<FarmerProductRes>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "onFailure", Toast.LENGTH_SHORT).show();
            }
        });

    }


//-------------------------------------- APIs -------------------------------------------------------//

    GetFarmerProductAPI getGetFarmerProductAPIService(String baseUrl) {
        return RetrofiltClient.getRettofitClient(baseUrl, "").create(GetFarmerProductAPI.class);
    }

    interface GetFarmerProductAPI {
        @POST("user/farmer_productapi/")
        @FormUrlEncoded
        Call<List<FarmerProductRes>> getFarmerProductAPI(@Field("id") String id);
    }


//------------------------------------- Adapter Class -------------------------------------------------//

    class MyAdapter extends ArrayAdapter<String> {

        private Context ctx;

        private String[] nameArray;
        private String[] qtyArray;
        private String[] unitArray;

        public MyAdapter(@NonNull Context context, int resource, String[] nameArray, String[] qtyArray, String[] unitArray) {
            super(context, resource);

            this.ctx = context;
            this.nameArray = nameArray;
            this.qtyArray = qtyArray;
            this.unitArray = unitArray;
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
            View row = inflater.inflate(R.layout.row_list_farmer_product, parent, false);

            TextView tv_name = row.findViewById(R.id.row_list_farmer_product_tv_name);
            tv_name.setText(nameArray[position]);

            TextView tv_qty = row.findViewById(R.id.row_list_farmer_product_tv_qty);
            tv_qty.setText(qtyArray[position]);

            TextView tv_unit = row.findViewById(R.id.row_list_farmer_product_tv_unit);
            tv_unit.setText(" " + unitArray[position]);

            return row;
        }

        @Override
        public int getCount() {
            return nameArray.length;
        }
    }


}
