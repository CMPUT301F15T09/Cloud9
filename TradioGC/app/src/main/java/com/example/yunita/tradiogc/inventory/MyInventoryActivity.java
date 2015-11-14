package com.example.yunita.tradiogc.inventory;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.yunita.tradiogc.R;
import com.example.yunita.tradiogc.login.LoginActivity;
import com.example.yunita.tradiogc.user.UserController;

import java.util.ArrayList;
import java.util.Arrays;


public class MyInventoryActivity extends AppCompatActivity {
    private Spinner categoriesChoice;
    private EditText query_et;
    private ListView itemList;

    private Inventory inventory = new Inventory();
    private ArrayAdapter<Item> inventoryViewAdapter;
    private InventoryController inventoryController;

    private Context context = this;

    private int category = -1;
    private String query = "";
    private int categorySelection = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory);

        inventoryController = new InventoryController(context);

        itemList = (ListView) findViewById(R.id.inventory_list_view);
        categoriesChoice = (Spinner) findViewById(R.id.item_by_category_spinner);
        query_et = (EditText) findViewById(R.id.query_et);
    }


    /**
     * Sets up the "Inventory View Adapter" and manipulates the list view.
     * When an item is pressed, it sends the user to the Item Detail page.
     * When an item is long pressed, it removes the item from
     * the user's inventory and calls the "Delete Item Thread".
     */
    @Override
    protected void onStart() {
        super.onStart();


        ArrayList<String> categories = new ArrayList<String> (Arrays.asList(getResources().getStringArray(R.array.categories_array)));
        categories.add(0, "--Category--");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriesChoice.setAdapter(adapter);

        inventoryViewAdapter = new ArrayAdapter<Item>(this, R.layout.inventory_list_item, inventory);
        itemList.setAdapter(inventoryViewAdapter);


        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = inventory.get(position);
                viewItemDetails(LoginActivity.USERLOGIN.getInventory().indexOf(item));
            }
        });

        itemList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Item deletedItem = inventory.get(position);
                Thread deleteThread = inventoryController.new DeleteItemThread(deletedItem);
                deleteThread.start();
                inventory.remove(deletedItem);
                Toast.makeText(context, "Removing " + deletedItem.toString(), Toast.LENGTH_SHORT).show();
                notifyUpdated();
                return true;
            }
        });

        categoriesChoice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categorySelection = position;
                category = position - 1;
                searchItem(category, query);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        query_et.addTextChangedListener(new DelayedTextWatcher(500) {
            @Override
            public void afterTextChangedDelayed(Editable s) {
                query = s.toString();
                searchItem(category, query);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        categoriesChoice.setSelection(categorySelection);
    }

    /**
     * Notify the listview to be refreshed
     */
    public void notifyUpdated() {
        Runnable doUpdateGUIList = new Runnable() {
            public void run() {
                inventoryViewAdapter.notifyDataSetChanged();
            }
        };
        runOnUiThread(doUpdateGUIList);
    }

    /**
     * Called when the user presses the "+" button in the Inventory page.
     * <p>This method is used to send the user to the Add Item page.
     *
     * @param view "+" Button in the user's Inventory page
     */
    public void goToAddItem(View view) {
        startActivity(new Intent(MyInventoryActivity.this, AddItemActivity.class));
    }

    /**
     * Called when the user presses on an item.
     * This method is used to send the user to the Item Detail page,
     * pass the item index position, and tell the Item Detail activity
     * to show the Item Detail page from the user's perspective.
     *
     * @param index     the index of item in inventory
     */
    public void viewItemDetails(int index) {
        Intent intent = new Intent(context, ItemActivity.class);
        intent.putExtra("owner", "owner");
        intent.putExtra("index", index);
        startActivity(intent);
    }


    public void searchItem(int category, String query) {
        inventory.clear();
        for (Item item : LoginActivity.USERLOGIN.getInventory()) {
            if (item.getName().contains(query) && (item.getCategory() == category || category==-1)) {
                inventory.add(item);
            }
        }
        notifyUpdated();
    }


    /**
     * This class sets up the accuracy of the search list view
     * while doing a partial search.
     */
    // taken from http://stackoverflow.com/questions/5730609/is-it-possible-to-slowdown-reaction-of-edittext-listener
    // (C) 2015 user1338795
    abstract class DelayedTextWatcher implements TextWatcher {

        private long delayTime;
        private WaitTask lastWaitTask;

        public DelayedTextWatcher(long delayTime) {
            super();
            this.delayTime = delayTime;
        }

        @Override
        public void afterTextChanged(Editable s) {
            synchronized (this) {
                if (lastWaitTask != null) {
                    lastWaitTask.cancel(true);
                }
                lastWaitTask = new WaitTask();
                lastWaitTask.execute(s);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public abstract void afterTextChangedDelayed(Editable s);

        private class WaitTask extends AsyncTask<Editable, Void, Editable> {

            @Override
            protected Editable doInBackground(Editable... params) {
                try {
                    Thread.sleep(delayTime);
                } catch (InterruptedException e) {
                }
                return params[0];
            }

            @Override
            protected void onPostExecute(Editable result) {
                super.onPostExecute(result);
                afterTextChangedDelayed(result);
            }
        }
    }
}
