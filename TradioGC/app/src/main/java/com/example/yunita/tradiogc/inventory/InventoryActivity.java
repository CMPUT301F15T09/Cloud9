package com.example.yunita.tradiogc.inventory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.yunita.tradiogc.R;
import com.example.yunita.tradiogc.login.LoginActivity;
import com.example.yunita.tradiogc.user.UserController;

public class InventoryActivity extends AppCompatActivity {
    private Context context = this;
    private Inventory inventory = LoginActivity.USERLOGIN.getInventory();
    private ListView itemList;
    private ArrayAdapter<Item> inventoryViewAdapter;
    private InventoryController inventoryController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.your_inventory);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        inventoryController = new InventoryController(context);
        itemList = (ListView) findViewById(R.id.inventory_list_view);
    }

    @Override
    protected void onStart(){
        super.onStart();
        inventoryViewAdapter = new ArrayAdapter<Item>(this, R.layout.inventory_list_item, inventory);
        itemList.setAdapter(inventoryViewAdapter);

        itemList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Item deletedItem = inventory.get(position);
                Thread deleteThread = inventoryController.new DeleteItemThread(deletedItem);
                deleteThread.start();
                Toast.makeText(context, "Removing " + deletedItem.toString(), Toast.LENGTH_SHORT).show();
                inventoryViewAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    public void goToAddItem(View view){
        startActivity(new Intent(InventoryActivity.this, AddItemActivity.class));
    }

}
