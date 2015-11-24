package com.example.yunita.tradiogc.record;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.yunita.tradiogc.R;
import com.example.yunita.tradiogc.inventory.Inventory;
import com.example.yunita.tradiogc.inventory.Item;
import com.example.yunita.tradiogc.login.LoginActivity;
import com.example.yunita.tradiogc.trade.Trade;
import com.example.yunita.tradiogc.trade.Trades;

public class CurrentActivity extends AppCompatActivity {

    private ListView currentTradesListView;
    private ArrayAdapter<Trade> currentTradesArrayAdapter;
    private Trades currentTrades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_record);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        currentTradesListView = (ListView) findViewById(R.id.current_trades_list_view);

    }

    @Override
    protected void onStart(){
        super.onStart();

        Trades temp = LoginActivity.USERLOGIN.getTrades();
        currentTrades = new Trades(temp);

        // give a method called getCurrentTrade in trades
        // currentTrades = currentTrades.getCurrentTrades();

        currentTradesArrayAdapter = new ArrayAdapter<>(this, R.layout.trades_list_item, currentTrades);
        currentTradesListView.setAdapter(currentTradesArrayAdapter);


    }

}
