package com.example.yunita.tradiogc.user;

import android.content.Context;
import android.util.Log;

import com.example.yunita.tradiogc.WebServer;
import com.example.yunita.tradiogc.data.SearchHit;
import com.example.yunita.tradiogc.data.SearchResponse;
import com.example.yunita.tradiogc.data.SimpleSearchCommand;
import com.example.yunita.tradiogc.friends.Friends;
import com.example.yunita.tradiogc.login.LoginActivity;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

public class UserController {
    private static final String TAG = "UserController";
    private Gson gson;
    private WebServer webServer = new WebServer();
    private Users users = new Users();
    private Context context;
    private Friends friends = LoginActivity.USERLOGIN.getFriends();

    /**
     * Class constructor specifying this controller class is a subclass of Context.
     *
     * @param context
     */
    public UserController(Context context) {
        gson = new Gson();
        this.context = context;
    }

    /**
     * Called when update user thread is running.
     * <p>This method is used to update this user information
     * in the webserver.
     *
     * @param user this user.
     */
    public void updateUser(User user) {
        HttpClient httpClient = new DefaultHttpClient();

        try {
            HttpPost addRequest = new HttpPost(webServer.getResourceUrl() + user.getUsername());

            StringEntity stringEntity = new StringEntity(gson.toJson(user));
            addRequest.setEntity(stringEntity);
            addRequest.setHeader("Accept", "application/json");

            HttpResponse response = httpClient.execute(addRequest);
            String status = response.getStatusLine().toString();
            Log.i(TAG, status);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the user information from the webserver.
     *
     * @param username this user's name.
     */
    public User getUser(String username) {
        SearchHit<User> sr = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(webServer.getResourceUrl() + username);

        HttpResponse response = null;

        try {
            response = httpClient.execute(httpGet);
        } catch (ClientProtocolException e1) {
            throw new RuntimeException(e1);
        } catch (IOException e1) {
            throw new RuntimeException(e1);
        }

        Type searchHitType = new TypeToken<SearchHit<User>>() {
        }.getType();

        try {
            sr = gson.fromJson(
                    new InputStreamReader(response.getEntity().getContent()),
                    searchHitType);
        } catch (JsonIOException e) {
            throw new RuntimeException(e);
        } catch (JsonSyntaxException e) {
            throw new RuntimeException(e);
        } catch (IllegalStateException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return sr.getSource();

    }

    /**
     * Gets users with the specified search string. If the search does not
     * specify fields, it searches on all the fields.
     *
     * @param field search string.
     */
    public Users getAllUsers(String field, String origin) {
        Users result = new Users();


        HttpPost searchRequest = new HttpPost(webServer.getSearchUrl());

        String[] fields = null;
        if (field != null) {
            throw new UnsupportedOperationException("Not implemented!");
        }

        SimpleSearchCommand command = new SimpleSearchCommand("*");

        String query = gson.toJson(command);
        Log.i(TAG, "Json command: " + query);

        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(query);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        searchRequest.setHeader("Accept", "application/json");
        searchRequest.setEntity(stringEntity);

        HttpClient httpClient = new DefaultHttpClient();

        HttpResponse response = null;
        try {
            response = httpClient.execute(searchRequest);
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        Type searchResponseType = new TypeToken<SearchResponse<User>>() {
        }.getType();

        SearchResponse<User> esResponse;

        try {
            esResponse = gson.fromJson(
                    new InputStreamReader(response.getEntity().getContent()),
                    searchResponseType);
        } catch (JsonIOException e) {
            throw new RuntimeException(e);
        } catch (JsonSyntaxException e) {
            throw new RuntimeException(e);
        } catch (IllegalStateException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (SearchHit<User> hit : esResponse.getHits().getHits()) {
            if (origin.equals("userSearch")) {
                // Removes the user's name and any friends from the search result
                if (!hit.get_id().equals(LoginActivity.USERLOGIN.getUsername()) &&
                        !friends.contains(hit.get_id())) {
                    result.add(hit.getSource());
                }
            } else {
                // Returns all results
                result.add(hit.getSource());
            }
        }

        return result;
    }

    /**
     * Adds all user into user list.
     *
     * @param searchString search string.
     * @return Users.
     */
    public Users searchUsers(String searchString, String origin) {
        Users result = new Users();
        Users allUsers = getAllUsers(null, origin);
        for (User user : allUsers) {
            if (user.getUsername().toLowerCase().contains(searchString.toLowerCase())) {
                result.add(user);
            }
        }
        result.notifyObservers();
        return result;
    }

    /**
     * Called when this user information needs to be updated in
     * webserver.
     * <p>While it is running, it updates this user information in
     * webserver.
     */
    public class UpdateUserThread extends Thread {
        private User user;

        public UpdateUserThread(User user) {
            this.user = user;
        }

        @Override
        public void run() {
            synchronized (this) {
                updateUser(user);
                notify();
            }
        }
    }

    /**
     * Called when this user information needs to be updated.
     * While it is running, it updates this user information.
     */
    public class GetUserLoginThread extends Thread {
        private String username;

        public GetUserLoginThread(String username) {
            this.username = username;
        }

        @Override
        public void run() {
            synchronized (this) {
                LoginActivity.USERLOGIN = getUser(username);
                notify();
            }
        }
    }

}

