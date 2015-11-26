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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.yunita.tradiogc.R;
import com.example.yunita.tradiogc.market.SearchInventory;
import com.example.yunita.tradiogc.market.SearchItem;
import com.example.yunita.tradiogc.user.User;
import com.example.yunita.tradiogc.user.UserController;

import java.util.ArrayList;
import java.util.Arrays;

public class FriendsInventoryActivity extends AppCompatActivity {
    private Spinner categoriesChoice;
    private EditText query_et;
    private ListView item_list;

    private SearchInventory searchItems = new SearchInventory();
    private UserController userController;

    private String friendname = "testfriend"; // for test. Do not change it
    private User friend;
    private ArrayAdapter<SearchItem> inventoryViewAdapter;

    private Context context = this;

    private int category = -1;
    private String query = "";
    private int categorySelection = 0;
    private boolean clickable = true;

    public ListView getItem_list() {
        return item_list;
    }

    public SearchInventory getSearchItems() {
        return searchItems;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory);

        item_list = (ListView) findViewById(R.id.inventory_list_view);
        categoriesChoice = (Spinner) findViewById(R.id.item_by_category_spinner);
        query_et = (EditText) findViewById(R.id.query_et);

        Button add = (Button) findViewById(R.id.add_item_button);
        add.setVisibility(View.GONE);

        userController = new UserController(context);
    }

    /**
     * Gets the friend's name that was passed from previous activity.
     * <p>This method runs the "Get Inventory Thread" and gets the inventory of a
     * friend. In addition, when the user clicks on an item, it sends the user
     * to the Item Detail page.
     */
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                friendname = extras.getString("friend_uname");
            }
        }


        ArrayList<String> categories = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.categories_array)));
        categories.add(0, "--Category--");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoriesChoice.setAdapter(adapter);

        inventoryViewAdapter = new ArrayAdapter<SearchItem>(this, R.layout.inventory_list_item, searchItems);
        item_list.setAdapter(inventoryViewAdapter);

        item_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (clickable) {
                    SearchItem searchItem = searchItems.get(position);
                    viewItemDetails(searchItem, friend.getInventory().indexOf(searchItem.getoItem()));
                }
            }
        });

        categoriesChoice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categorySelection = position;
                category = position - 1;
                if (friend!=null) {
                    searchItem(category, query);
                }
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

    /**
     * Loading user's category choice and refreshing user data
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (!friendname.equals("")) {
            Thread refreshUserThread = new RefreshUserThread(friendname);
            refreshUserThread.start();
            synchronized (refreshUserThread) {
                try {
                    refreshUserThread.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
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
     * Called when the user clicks on an item.
     * This method is used to send the user to the Item Detail page,
     * pass the item index position, and tell the Item Detail activity
     * to show the Item Detail page from a friend's perspective.
     *
     * @param searchItem this item.
     * @param position this item's index in the inventory.
     */
    public void viewItemDetails(SearchItem searchItem, int position) {
        Intent intent = new Intent(context, ItemActivity.class);
        intent.putExtra("item", searchItem.getoItem());
        intent.putExtra("owner_name", searchItem.getOwnerName()); // pass item's owner's name
        intent.putExtra("owner", "friend");
        intent.putExtra("index", position);

        startActivity(intent);
    }

    /**
     * Called when the user changes the category selection or edittext.
     * This method is used to browse items by query and category.
     *
     * @param category the category choosed
     * @param query    input of part of item name
     */
    public void searchItem(int category, String query) {
        searchItems.clear();
        for (Item item : friend.getInventory().getPublicItems()) {
            if (item.getName().toLowerCase().contains(query.toLowerCase()) &&
                    (item.getCategory() == category || category == -1)) {
                searchItems.add(new SearchItem(friend.getUsername(), item));
            }
        }
        notifyUpdated();
        clickable = true;
    }

    /**
     * Called when the activity starts.
     * <p>This class creates a thread and runs "Refresh User".
     * While it is running, it refreshes the user data.
     */
    class RefreshUserThread extends Thread {
        private String username;

        public RefreshUserThread(String username) {
            this.username = username;
        }

        @Override
        public void run() {
            synchronized (this) {
                friend = userController.getUser(username);
                notify();
            }
        }
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
                clickable = false;
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
