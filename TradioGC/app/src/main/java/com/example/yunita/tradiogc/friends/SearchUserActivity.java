package com.example.yunita.tradiogc.friends;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.yunita.tradiogc.R;
import com.example.yunita.tradiogc.login.LoginActivity;
import com.example.yunita.tradiogc.profile.ProfileActivity;
import com.example.yunita.tradiogc.user.User;
import com.example.yunita.tradiogc.user.UserController;
import com.example.yunita.tradiogc.user.Users;

public class SearchUserActivity extends AppCompatActivity {
    private Users users;
    private ListView userList;
    private ArrayAdapter<User> usersViewAdapter;
    private UserController userController;

    private EditText query_et;
    private Context mContext = this;


    private boolean clickable = false;
    private String query = "";

    public ListView getUserList() {
        return userList;
    }

    public EditText getQuery_et() {
        return query_et;
    }

    public Users getUsers() {
        return users;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_friends_search_results);

        userList = (ListView) findViewById(R.id.friendsSearchList);
        query_et = (EditText) findViewById(R.id.search_by_username_et);
    }

    /**
     * Sets up the user's view adapter and manipulates the list view
     * according to the user's query.
     * <p>This method runs the "Search Thread". While the thread is running, it
     * searches for all users in the webserver that contains the text query and returns
     * the list of users.
     * When the user clicks on a username, it sends the user to that username's
     * profile page.
     */
    @Override
    protected void onStart() {
        super.onStart();

        users = new Users();
        usersViewAdapter = new ArrayAdapter<User>(this, R.layout.friend_list_item, users);
        userList.setAdapter(usersViewAdapter);
        userController = new UserController(mContext);

        query_et.addTextChangedListener(new DelayedTextWatcher(500) {
            @Override
            public void afterTextChangedDelayed(Editable s) {
                query = s.toString();
                SearchThread thread = new SearchThread(query);
                thread.start();
            }
        });

        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                if (clickable) {
                    String username = users.get(pos).getUsername();
                    startProfileActivity(username);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SearchThread thread = new SearchThread(query);
        thread.start();
    }


    /**
     * Called when the user clicks a username in the search list view.
     * <p>This method is used to send the user to that username's profile page.
     *
     * @param username the name of the user that the login user wants to track
     */
    public void startProfileActivity(String username) {
        Intent intent = new Intent(mContext, ProfileActivity.class);
        intent.putExtra("profileTarget", username);
        startActivity(intent);
        finish();
    }

    /**
     * Called after the user's search list is updated and while the search
     * thread is running.
     * <p>This method notifies the view if there is a change in the user's search list.
     */
    public void notifyUpdated() {
        // Thread to update adapter after an operation
        Runnable doUpdateGUIList = new Runnable() {
            public void run() {
                usersViewAdapter.notifyDataSetChanged();
            }
        };
        runOnUiThread(doUpdateGUIList);
    }

    /**
     * Called when the user attempts to search for a specific user.
     * <p>This class creates a thread and runs "Search Users" in the webserver.
     * While it is running, it updates the user's search list and notifies
     * the view of the change.
     */
    class SearchThread extends Thread {
        private String search;

        public SearchThread(String search) {
            this.search = search;
        }

        @Override
        public void run() {
            synchronized (this) {
                users.clear();
                users.addAll(userController.searchStrangers(search));

                System.out.println("search users: " + users.size());
                notifyUpdated();
                clickable = true;
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
                    e.printStackTrace();
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
