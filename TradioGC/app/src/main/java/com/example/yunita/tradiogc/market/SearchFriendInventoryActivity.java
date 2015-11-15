package com.example.yunita.tradiogc.market;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.yunita.tradiogc.R;

public class SearchFriendInventoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

    }
}
