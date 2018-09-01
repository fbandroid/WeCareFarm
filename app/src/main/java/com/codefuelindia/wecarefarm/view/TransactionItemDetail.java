package com.codefuelindia.wecarefarm.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.codefuelindia.wecarefarm.R;

public class TransactionItemDetail extends AppCompatActivity {

    TextView textView_date, textView_oId, textView_totalAmt, textView_receivedAmt;

    private String date, oId, totalAmt, receivedAmt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_item_detail);

        textView_date = findViewById(R.id.trItem_detail_tv_date);
        textView_oId = findViewById(R.id.trItem_detail_tv_Oid);
        textView_totalAmt = findViewById(R.id.trItem_detail_tv_totalAmt);
        textView_receivedAmt = findViewById(R.id.trItem_detail_tv_recAmt);

        date = getIntent().getExtras().getString("timestamp");
        oId = getIntent().getExtras().getString("oId");
        totalAmt = getIntent().getExtras().getString("totalAmt");
        receivedAmt = getIntent().getExtras().getString("received");

        setAllTexts();

    }

    private void setAllTexts() {

        textView_date.setText(date);
        textView_oId.setText(oId);
        textView_totalAmt.setText(totalAmt);
        textView_receivedAmt.setText(receivedAmt);

    }


}
