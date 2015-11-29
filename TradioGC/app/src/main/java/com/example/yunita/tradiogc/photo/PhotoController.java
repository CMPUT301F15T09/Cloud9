package com.example.yunita.tradiogc.photo;

import android.content.Context;
import android.util.Log;

import com.example.yunita.tradiogc.WebServer;
import com.example.yunita.tradiogc.data.SearchHit;
import com.example.yunita.tradiogc.friends.Friends;
import com.example.yunita.tradiogc.login.LoginActivity;
import com.example.yunita.tradiogc.user.User;
import com.example.yunita.tradiogc.user.Users;
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
import java.lang.reflect.Type;

public class PhotoController {
    private static final String TAG = "PhotoController";
    private Gson gson;
    private WebServer webServer = new WebServer();
    private Context context;
    private Photo photo = new Photo();

    /**
     * Class constructor specifying this controller class is a subclass of Context.
     *
     * @param context
     */
    public PhotoController(Context context) {
        gson = new Gson();
        this.context = context;
    }

    public void addPhoto(int item_id, Photo photo){
        UpdateItemPhotoThread updateItemPhotoThread = new UpdateItemPhotoThread(item_id, photo);
        updateItemPhotoThread.start();
    }

    public void updateItemPhotos(int itemId, Photo photos) {
        HttpClient httpClient = new DefaultHttpClient();

        try {
            HttpPost addRequest = new HttpPost(webServer.getResourcePhotoUrl() + Integer.toString(itemId));

            StringEntity stringEntity = new StringEntity(gson.toJson(photos));
            addRequest.setEntity(stringEntity);
            addRequest.setHeader("Accept", "application/json");

            HttpResponse response = httpClient.execute(addRequest);
            String status = response.getStatusLine().toString();
            Log.i(TAG, status);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Photo getItemPhoto(int itemId) {
        SearchHit<Photo> sr = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(webServer.getResourcePhotoUrl() + itemId);

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

    public class UpdateItemPhotoThread extends Thread {
        private int itemId;
        private Photo photo;

        public UpdateItemPhotoThread(int itemId, Photo photo) {
            this.itemId = itemId;
            this.photo = photo;
        }

        @Override
        public void run() {
            updateItemPhotos(itemId, photo);
        }
    }

}
