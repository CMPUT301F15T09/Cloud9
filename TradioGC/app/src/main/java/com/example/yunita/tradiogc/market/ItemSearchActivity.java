package com.example.yunita.tradiogc.market;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.yunita.tradiogc.R;
import com.example.yunita.tradiogc.inventory.ItemActivity;

import java.util.ArrayList;

public class ItemSearchActivity extends AppCompatActivity {

    private Context context = this;

    private LinearLayout query_panel;
    private LinearLayout category_panel;

    private Spinner categoriesChoice;
    private ImageButton querySearchBtn;
    private EditText queryEditText;
    private ListView queryListView;
    private ListView categoryListView;

    private ArrayAdapter<SearchItem> queryViewAdapter;
    private ArrayAdapter<SearchItem> categoryViewAdapter;

    private SearchInventory friendsItems;

    // temporary friends' inventories to manipulate the list view
    private SearchInventory tempFriendsItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_search);

        query_panel = (LinearLayout) findViewById(R.id.search_query_panel);
        category_panel = (LinearLayout) findViewById(R.id.search_category_panel);

        querySearchBtn = (ImageButton) findViewById(R.id.query_search_btn);
        queryEditText = (EditText) findViewById(R.id.search_by_query_et);

        categoriesChoice = (Spinner) findViewById(R.id.item_by_category_spinner);
        queryListView = (ListView) findViewById(R.id.item_by_query_list_view);
        categoryListView = (ListView) findViewById(R.id.item_by_category_list_view);

        friendsItems = new SearchInventory();
        tempFriendsItems = new SearchInventory();
    }

    /**
     * Gets the search type that was passed from previous activity.
     * <p>This method runs the view setup (either search by query
     * or by category) and the search method according to the search
     * type that this user chose. In addition, when the user clicks on
     * an item, it sends the user to the Item Detail page.
     */
    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        //this is how to handle class that extends ArrayList<> and implements Serializable
        // all inventories are stored in this temporary Inventory
        ArrayList<SearchItem> tempInventory = (ArrayList<SearchItem>) extras.getSerializable("friendsItems");
        friendsItems.addAll(tempInventory);

        if (extras.getString("search").equals("query")) {
            //if search by query is chosen
            query_panel.setVisibility(View.VISIBLE);

            queryViewAdapter = new ArrayAdapter<>(this, R.layout.friend_list_item, tempFriendsItems);
            queryListView.setAdapter(queryViewAdapter);

            querySearchBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String query = queryEditText.getText().toString();
                    SearchInventory searchByQuery = new SearchInventory();
                    searchByQuery.setInventoryByQuery(friendsItems, query);
                    queryViewAdapter.clear();
                    queryViewAdapter.addAll(searchByQuery);
                    queryViewAdapter.notifyDataSetChanged();
                }
            });

            // view item detail
            queryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    SearchItem searchItem = tempFriendsItems.get(position);
                    viewItemDetails(searchItem, position);
                }
            });

        } else {
            // if search by category is chosen
            category_panel.setVisibility(View.VISIBLE);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories_array, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categoriesChoice.setAdapter(adapter);

            categoryViewAdapter = new ArrayAdapter<>(this, R.layout.friend_list_item, tempFriendsItems);
            categoryListView.setAdapter(categoryViewAdapter);

            categoriesChoice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    int category = categoriesChoice.getSelectedItemPosition();
                    SearchInventory searchByCategory = new SearchInventory();
                    searchByCategory.setInventoryByCategory(friendsItems, category);
                    categoryViewAdapter.clear();
                    categoryViewAdapter.addAll(searchByCategory);
                    categoryViewAdapter.notifyDataSetChanged();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }

            });

            // view item detail
            categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    SearchItem searchItem = tempFriendsItems.get(position);
                    viewItemDetails(searchItem, position);
                }
            });

        }

    }

    /**
     * Called when the user clicks on item.
     * This method is used to send user to Item Detail page and
     * pass item index position and tell Item Detail activity
     * to show the Item Detail page from borrower(friend)'s perspective.
     *
     * @param searchItem this item.
     * @param position   this item's index in friends' item list.
     */
    public void viewItemDetails(SearchItem searchItem, int position) {
        Intent intent = new Intent(context, ItemActivity.class);
        intent.putExtra("item", searchItem.getoItem());
        intent.putExtra("owner", "friend");
        intent.putExtra("index", position);

        startActivity(intent);
        finish();
    }

}
