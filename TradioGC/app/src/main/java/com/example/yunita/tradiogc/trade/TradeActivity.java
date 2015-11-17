package com.example.yunita.tradiogc.trade;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.yunita.tradiogc.R;
import com.example.yunita.tradiogc.user.UserController;

public class TradeActivity extends AppCompatActivity{

    private Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trade_display);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


    }
}
