package com.example.yunita.tradiogc.photo;

import android.content.Context;

import com.example.yunita.tradiogc.WebServer;
import com.google.gson.Gson;

import org.apache.http.HttpRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class PhotoController {
    private static final String tag = "PhotoController";
    private Gson gson;
    private WebServer webServer = new WebServer();
    private Context context;

    public PhotoController(Context context) {
        gson = new Gson();
        this.context = context;
    }

    public void updatePhoto(Photo photo) {
        HttpClient httpClient = new DefaultHttpClient();

        try {
            HttpPost addRequest = new HttpPost(webServer.getResourcePhotoUrl() + photo.getPhotoId());

        } catch (Exception e) {
            
        }
    }



}
