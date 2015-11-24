package com.example.yunita.tradiogc.record;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.yunita.tradiogc.R;
import com.example.yunita.tradiogc.profile.EditProfileActivity;

public class RecordActivity extends AppCompatActivity{

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trades);

    }

    // current trade: pending/offered or accepted

    /**
     *
     * @param view
     */
    public void goToCurrent(View view){
        Intent intent = new Intent(context, CurrentActivity.class);
        startActivity(intent);

        finish();
    }

}
