package com.example.yunita.tradiogc.market;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.yunita.tradiogc.R;
import com.example.yunita.tradiogc.friends.Friends;
import com.example.yunita.tradiogc.inventory.Inventory;
import com.example.yunita.tradiogc.inventory.Item;
import com.example.yunita.tradiogc.inventory.ItemActivity;
import com.example.yunita.tradiogc.login.LoginActivity;
import com.example.yunita.tradiogc.user.User;
import com.example.yunita.tradiogc.user.UserController;
import com.example.yunita.tradiogc.user.Users;

/**
 * This activity handles the market.
 */
public class MarketActivity extends AppCompatActivity {

    private Context context = this;
    private Friends friends;
    private Users users = new Users();
    private UserController userController;
    private SearchInventory friendsItems = new SearchInventory();
    private ListView friendsItemListView;
    private ArrayAdapter<SearchItem> friendsItemViewAdapter;

    /**
     *  Updates the GUI details.
     */
    private Runnable doUpdateGUIDetails = new Runnable() {
        public void run() {
            friendsItems.clear();
            for (User user : users) {
                Inventory pItems;
                if (friends.contains(user.getUsername())) {
                    pItems = user.getInventory().getPublicItems();
                    for (Item publicItem : pItems) {
                        friendsItems.add(new SearchItem(user.getUsername(), publicItem));
                    }
                }
            }

            friendsItemViewAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.market);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        userController = new UserController(context);
        friendsItemListView = (ListView) findViewById(R.id.all_search_list_view);
    }

    /**
     * Sets up the view for the Market page.
     * <p>When the user clicks on an item, it sends the user to the Item Detail page.
     */
    @Override
    protected void onStart() {
        super.onStart();
        friendsItemViewAdapter = new ArrayAdapter<SearchItem>(this, R.layout.friend_list_item, friendsItems);
        friendsItemListView.setAdapter(friendsItemViewAdapter);

        friendsItemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchItem searchItem = friendsItems.get(position);
                viewItemDetails(searchItem, position);
            }
        });
    }

    /**
     * Updates the user login and gets all users from the webserver.
     */
    @Override
    protected void onResume() {
        super.onResume();

        // Updates user information
        Thread getUserLoginThread = userController.new GetUserLoginThread(LoginActivity.USERLOGIN.getUsername());
        getUserLoginThread.start();
        synchronized (getUserLoginThread) {
            try {
                getUserLoginThread.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        friends = LoginActivity.USERLOGIN.getFriends();

        // Gets all users in the webserver
        Thread getUsersThread = new GetUsersThread("");
        getUsersThread.start();

    }

    /**
     * Called when the user presses the "Search by Category" button in the Market page.
     * <p>This method is used to send the user to the Search Item by Category page.
     *
     * @param view "Search by Category" button
     */
    public void goToSearchByCategory(View view) {
        Intent intent = new Intent(context, ItemSearchActivity.class);
        intent.putExtra("search", "category");
        intent.putExtra("friendsItems", friendsItems);
        startActivity(intent);
    }

    /**
     * Called when the user presses the "Search by Query" button in the Market page.
     * <p>This method is used to send the user to the Search Item by Query page.
     *
     * @param view "Search by Query" button
     */
    public void goToSearchByQuery(View view) {
        Intent intent = new Intent(context, ItemSearchActivity.class);
        intent.putExtra("search", "query");
        intent.putExtra("friendsItems", friendsItems);
        startActivity(intent);
    }

    /**
     * Called when the user presses on an item.
     * This method is used to send the user to the item's Item Detail page,
     * pass the item index position, and tell the Item Detail activity
     * to show the Item Detail page from a borrower(friend)'s perspective.
     *
     * @param searchItem item pressed
     * @param position   the item's index in the friend's inventory
     */
    public void viewItemDetails(SearchItem searchItem, int position) {
        Intent intent = new Intent(context, ItemActivity.class);
        intent.putExtra("item", searchItem.getoItem()); // pass item
        intent.putExtra("owner_name", searchItem.getOwnerName()); // pass item's owner's name
        intent.putExtra("owner", "friend");
        intent.putExtra("index", position);

        startActivity(intent);
    }

    /**
     * Called when the activity starts.
     * <p>This class creates a thread and runs "Search Users".
     * The purpose of this class is to get all users from the webserver.
     */
    class GetUsersThread extends Thread {
        private String search;

        public GetUsersThread(String search) {
            this.search = search;
        }

        @Override
        public void run() {
            users.clear();
            users.addAll(userController.searchUsers(search));
            runOnUiThread(doUpdateGUIDetails);
        }

    }

}
