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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codefuelindia.wecarefarm.R;
import com.codefuelindia.wecarefarm.Utils.Utils;
import com.codefuelindia.wecarefarm.common.RetrofiltClient;
import com.codefuelindia.wecarefarm.cons.Constants;
import com.codefuelindia.wecarefarm.model.ShowTransactionModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class ShowTransactionActivity extends AppCompatActivity {

    GetCreditDebitAPI getCreditDebitAPI;
    GetCreditDebitHistoryAPI getCreditDebitHistoryAPI;

    TextView textView_remainAmt, textView_availBalance;
    ListView listView_transactions;
    ProgressBar progressBar;

    MyAdapter myAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_transaction);

        toolbar = findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getCreditDebitAPI = getGetCreditDebitAPIService(Constants.BASE_URL);
        getCreditDebitHistoryAPI = getGetCreditDebitHistoryAPIService(Constants.BASE_URL);

        textView_remainAmt = findViewById(R.id.showTr_tv_remain_amt);
        textView_availBalance = findViewById(R.id.showTr_tv_avail_balance);
        listView_transactions = findViewById(R.id.showTransactions_listView);
        progressBar = findViewById(R.id.progressBar_showTransactions);

        setCreditDebitData();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    private void setCreditDebitData() {
        progressBar.setVisibility(View.VISIBLE);

        final String id = Utils.getSharedPreference(Constants.MY_PREF, this).getString(Constants.ID, "");

        getCreditDebitAPI.getCreditDebit(id).enqueue(new Callback<ShowTransactionModel>() {
            @Override
            public void onResponse(Call<ShowTransactionModel> call, Response<ShowTransactionModel> response) {

                if (response.isSuccessful()) {

                    if (response.body() != null) {

                        textView_availBalance.setText(response.body().getAvailable());
                        textView_remainAmt.setText(response.body().getRemaining());

                        progressBar.setVisibility(View.GONE);

                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Response body is null", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Response not successful", Toast.LENGTH_SHORT).show();
                }

                setAllTransData(id);
            }

            @Override
            public void onFailure(Call<ShowTransactionModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "onFailure1", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setAllTransData(String id) {
        progressBar.setVisibility(View.VISIBLE);

        getCreditDebitHistoryAPI.getCreditDebitHistory(id).enqueue(new Callback<List<ShowTransactionModel>>() {
            @Override
            public void onResponse(Call<List<ShowTransactionModel>> call, Response<List<ShowTransactionModel>> response) {

                if (response.isSuccessful()) {

                    if (response.body() != null) {

                        if (response.body().size() == 0) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "No transactions found", Toast.LENGTH_LONG).show();

                        } else {
                            String[] dateArray = new String[response.body().size()];
                            String[] oIdArray = new String[response.body().size()];
                            String[] totalAmtArray = new String[response.body().size()];
                            String[] recAmtArray = new String[response.body().size()];

                            for (int i = 0; i < response.body().size(); i++) {
                                dateArray[i] = response.body().get(i).getTimestamp();
                                oIdArray[i] = response.body().get(i).getOrder_no();
                                totalAmtArray[i] = response.body().get(i).getAmount();
                                recAmtArray[i] = response.body().get(i).getRecieved();
                            }

                            myAdapter = new MyAdapter(getApplicationContext(), R.layout.row_list_ap_orders, dateArray, oIdArray, totalAmtArray, recAmtArray);
                            listView_transactions.setAdapter(myAdapter);

                            myAdapter.notifyDataSetChanged();

                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Response Done", Toast.LENGTH_SHORT).show();

                        }

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
            public void onFailure(Call<List<ShowTransactionModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "onfailure2", Toast.LENGTH_SHORT).show();
            }
        });
    }


//----------------------------------------- APIs --------------------------------------------------------//

    GetCreditDebitAPI getGetCreditDebitAPIService(String baseUrl) {
        return RetrofiltClient.getRettofitClient(baseUrl, "").create(GetCreditDebitAPI.class);
    }

    GetCreditDebitHistoryAPI getGetCreditDebitHistoryAPIService(String baseUrl) {
        return RetrofiltClient.getRettofitClient(baseUrl, "").create(GetCreditDebitHistoryAPI.class);
    }


    interface GetCreditDebitAPI {
        @POST("user/get_credit_debit_api/")
        @FormUrlEncoded
        Call<ShowTransactionModel> getCreditDebit(@Field("id") String id);
    }

    interface GetCreditDebitHistoryAPI {
        @POST("user/credit_debit_history_api/")
        @FormUrlEncoded
        Call<List<ShowTransactionModel>> getCreditDebitHistory(@Field("id") String id);
    }


//------------------------------------- Adapter Class -------------------------------------------------//

    class MyAdapter extends ArrayAdapter<String> {

        private Context ctx;

        private String[] dateArray;
        private String[] oIdArray;
        private String[] totalAmtArray;
        private String[] recAmtArray;

        public MyAdapter(@NonNull Context context, int resource, String[] dateArray, String[] oIdArray, String[] totalAmtArray, String[] recAmtArray) {
            super(context, resource);

            this.ctx = context;
            this.dateArray = dateArray;
            this.oIdArray = oIdArray;
            this.totalAmtArray = totalAmtArray;
            this.recAmtArray = recAmtArray;
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
            View row = inflater.inflate(R.layout.row_transaction_item, parent, false);


            TextView tv_date = row.findViewById(R.id.row_trItem_tv_date);
            tv_date.setText(dateArray[position]);

            TextView tv_oId = row.findViewById(R.id.row_trItem_tv_Oid);
            tv_oId.setText(oIdArray[position]);

            TextView tv_totAmt = row.findViewById(R.id.row_trItem_tv_totalAmt);
            tv_totAmt.setText(totalAmtArray[position]);

            TextView tv_recAmt = row.findViewById(R.id.row_trItem_tv_recAmt);
            tv_recAmt.setText(recAmtArray[position]);

            return row;
        }

        @Override
        public int getCount() {
            return dateArray.length;
        }

    }

}
