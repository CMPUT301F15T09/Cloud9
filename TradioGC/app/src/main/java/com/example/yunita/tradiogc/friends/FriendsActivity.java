package com.example.yunita.tradiogc.friends;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.yunita.tradiogc.R;
import com.example.yunita.tradiogc.login.LoginActivity;
import com.example.yunita.tradiogc.profile.ProfileActivity;
import com.example.yunita.tradiogc.user.User;
import com.example.yunita.tradiogc.user.UserController;
import com.example.yunita.tradiogc.user.Users;

/**
 * This activity handles the user's Friend page.
 */
public class FriendsActivity extends AppCompatActivity {

    private Context context = this;
    private Users friendsInUser = new Users();
    private Users resultUsers = new Users();

    private ListView friendList;
    private String friendname;
    private FriendsController friendsController;
    private UserListAdapter friendsViewAdapter;
    private Button search_user_button;
    private ProgressBar progressBar;

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

        search_user_button = (Button) findViewById(R.id.search_user);
        progressBar = (ProgressBar) findViewById(R.id.friends_main_progressBar);
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

        friendsViewAdapter = new UserListAdapter(this, R.layout.user_list_item, friendsInUser);
        friendList.setAdapter(friendsViewAdapter);

        friendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String username = friendsInUser.get(position).getUsername();
                viewFriendProfile(username);
            }
        });
        // Delete friends on long click
        friendList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                friendname = friendsInUser.get(position).getUsername();
                Thread deleteThread = new DeleteFriendThread(friendname);
                deleteThread.start();
                friendsInUser.remove(position);
                friendsViewAdapter.notifyDataSetChanged();
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
        progressBar.setVisibility(View.VISIBLE);
        friendsInUser.clear();
        friendsViewAdapter.notifyDataSetChanged();
        // Keeps updating friend list (since we use a tab menu, after the first
        // visit, this activity will be on resume/pause).
        Thread getFriendsInUserThread = new GetFriendsInUserThread();
        getFriendsInUserThread.start();
    }

    /**
     * Called when the user presses the "Add New Friend" button.
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
        intent.putExtra("profileTarget", username);
        startActivity(intent);
    }

    /**
     * Called after the friends list is updated and while the getFriendsInUser
     * thread is running.
     * <p>This method notifies the view if there is a change in the user's friends list.
     */
    public void notifyUpdated() {
        // Thread to update adapter after an operation
        Runnable doUpdateGUIList = new Runnable() {
            public void run() {
                friendsInUser.clear();
                friendsInUser.addAll(resultUsers);
                friendsViewAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }
        };
        runOnUiThread(doUpdateGUIList);
    }
    /**
     * This class is called when the user deletes a friend by long pressing
     * on a friend's name.
     * <p>This class creates a thread and runs "Delete Friend".
     * While it is running, it removes this friend from the user's friend list
     * and updates the friends list view.
     */
    class DeleteFriendThread extends Thread {
        private String friendname;

        public DeleteFriendThread(String friendname) {
            this.friendname = friendname;
        }

        @Override
        public void run() {
            friendsController.deleteFriend(friendname);
        }
    }

    /**
     * This class is called when the friends list is refreshed.
     * <p>This class creates a thread and runs "Refresh Friends List".
     */
    class GetFriendsInUserThread extends Thread {

        public GetFriendsInUserThread() {}

        public void run() {
            UserController userController = new UserController(context);

            resultUsers.clear();
            for (String friend : LoginActivity.USERLOGIN.getFriends()) {
                User friendInUser = userController.getUser(friend);
                if (friendInUser != null) {
                    resultUsers.add(friendInUser);
                }
            }

            resultUsers.sortByNumberOfTrades();
            notifyUpdated();
        }
    }
}
