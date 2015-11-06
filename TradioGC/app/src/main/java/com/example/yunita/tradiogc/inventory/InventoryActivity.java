package com.example.yunita.tradiogc.inventory;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.yunita.tradiogc.R;
import com.example.yunita.tradiogc.login.LoginActivity;

import java.util.ArrayList;

public class InventoryActivity extends ActionBarActivity {
    public static Inventory inventory = new Inventory();
    private InventoryController inventoryController;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.your_inventory);
        inventoryController = new InventoryController(context);

        Thread thread = inventoryController.new LoadInventoryThread(LoginActivity.USERLOGIN.getUsername());
        thread.start();
        //inventory = inventoryController.loadInventory(LoginActivity.USERLOGIN.getUsername());
        onClickListeners();
    }

    @Override
    protected void onResume(){
        super.onResume();

        System.out.println(LoginActivity.USERLOGIN.getUsername());

    }

    public void onClickListeners() {
        Button plus = (Button) findViewById(R.id.plus_button);
        ListView inventoryList = (ListView) findViewById(R.id.your_inventory_listview);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InventoryActivity.this, NewItemActivity.class));
            }
        });
        ArrayList<String> inventoryNames = new ArrayList<String>();
        ArrayList<Double> inventoryPrices = new ArrayList<Double>();
        int inventorySize = inventory.getSize();
        for(int i = 0; i<inventorySize; i++) {
            inventoryNames.add(inventory.getItem(i).getName());
            inventoryPrices.add(inventory.getItem(i).getPrice());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, inventoryNames);
        ArrayAdapter<Double> arrayAdapter1 = new ArrayAdapter<Double>(this, android.R.layout.simple_list_item_2, inventoryPrices);

        inventoryList.setAdapter(arrayAdapter);
        inventoryList.setAdapter(arrayAdapter1);
    }


}
