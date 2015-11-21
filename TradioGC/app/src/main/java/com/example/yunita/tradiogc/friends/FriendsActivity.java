package com.example.yunita.tradiogc.friends;

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
import com.example.yunita.tradiogc.profile.ProfileActivity;


public class FriendsActivity extends AppCompatActivity {

    private Context context = this;
    private Friends friends = new Friends();
    private ListView friendList;
    private String friendname;
    private FriendsController friendsController;
    private ArrayAdapter<String> friendsViewAdapter;
    private Button search_user_button;
    private Runnable doUpdateGUIDetails = new Runnable() {
        public void run() {
            friendsViewAdapter.remove(friendname);
            friendsViewAdapter.notifyDataSetChanged();
        }
    };

    public Button getSearch_user_button() {
        return search_user_button;
    }

    public ListView getFriendList() {
        return friendList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        friendsController = new FriendsController(context);
        friendList = (ListView) findViewById(R.id.friend_list_view);
        friends.addAll(LoginActivity.USERLOGIN.getFriends());
        search_user_button = (Button) findViewById(R.id.search_user);
        System.out.println("friends: " + friends.size());
    }

    /**
     * Sets up the "Friends View Adapter" and manipulates the list view.
     * When an item in the list is pressed, it sends the user to a friend's profile.
     * When an item in the list is long pressed, it removes the friend from
     * the user's friend list and calls the "Delete Friend Thread".
     */
    @Override
    protected void onStart() {
        super.onStart();

        friendsViewAdapter = new ArrayAdapter<String>(this, R.layout.friend_list_item, friends);
        friendList.setAdapter(friendsViewAdapter);

        friendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String username = friends.get(position);
                viewFriendProfile(username);
            }
        });
        // Delete friends on long click
        friendList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                friendname = friends.get(position);
                Thread deleteThread = new DeleteFriendThread(friendname);
                deleteThread.start();
                friends.remove(friendname);
                Toast.makeText(context, "Deleting " + friendname, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    /**
     * Updates the user's friends list every time the user goes back to their Friends page.
     */
    @Override
    public void onResume() {
        super.onResume();
        // keep updating friend list (since we use tabmenu, after first
        // visit, this activity will be on resume/pause).
        friends.clear();
        friends.addAll(LoginActivity.USERLOGIN.getFriends());
    }

    /**
     * Called when the user clicks the "Add New Friend" button.
     * <p>This method is used to send the user to the Search Friend page.
     *
     * @param view "Add New Friend" button
     */
    public void searchUser(View view) {
        Intent intent = new Intent(this, SearchUserActivity.class);
        startActivity(intent);
    }

    /**
     * Called when the user presses a friend's name in the list view.
     * <p>This method is used to send the user to a friend's profile page.
     *
     * @param username friend's name
     */
    public void viewFriendProfile(String username) {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra(ProfileActivity.USERNAME, username);
        startActivity(intent);
    }

    /**
     * Called when the user attempts to delete a friend by long pressing
     * on a friend's name.
     * <p>This class creates a thread and runs "Delete Friend".
     * While it is running, it removes this friend from the user's friend list
     * and updates the Friends list view.
     */
    class DeleteFriendThread extends Thread {
        private String friendname;

        public DeleteFriendThread(String friendname) {
            this.friendname = friendname;
        }

        @Override
        public void run() {
            friendsController.deleteFriend(friendname);
            runOnUiThread(doUpdateGUIDetails);
        }
    }
}
