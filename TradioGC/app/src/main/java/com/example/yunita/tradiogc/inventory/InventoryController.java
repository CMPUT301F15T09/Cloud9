package com.example.yunita.tradiogc.inventory;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.example.yunita.tradiogc.User;
import com.example.yunita.tradiogc.WebServer;
import com.example.yunita.tradiogc.data.SearchHit;
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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;

/**
 * Created by dshin on 10/31/15.
 */
public class InventoryController {
    private static final String TAG = "InventoryController";
    private Gson gson = new Gson();
    private Inventory newInventory;
    private Context context;
    private WebServer webServer = new WebServer();

    public InventoryController(Context context){
        super();
        this.context = context;
    }
    // Thread that close the activity after finishing add
    private Runnable doFinishAdd = new Runnable() {
        public void run() {
            ((Activity) context).finish();
        }
    };


    public void createItem(Inventory inventory, String name,int category,double price,String description,Boolean visibility){
        Item item = new Item(name, category, price, description, visibility);
        inventory.addItem(item);
    }

    public void addInventory(User user, Inventory inventory) {
        newInventory = inventory;
        HttpClient httpClient = new DefaultHttpClient();

        saveInventoryInFile(inventory);
        try {
            HttpPost addRequest = new HttpPost(webServer.getInventoryUrl() + user.getUsername());
            // check http://cmput301.softwareprocess.es:8080/cmput301f15t09/inventory/[username]

            StringEntity stringEntity = new StringEntity(gson.toJson(newInventory));
            addRequest.setEntity(stringEntity);
            addRequest.setHeader("Accept", "application/json");

            HttpResponse response = httpClient.execute(addRequest);
            String status = response.getStatusLine().toString();
            Log.i(TAG, status);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Inventory loadInventory(User username){
        SearchHit<Inventory> sr = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(webServer.getInventoryUrl() + username.getUsername());

        HttpResponse response = null;

        try {
            response = httpClient.execute(httpGet);
        } catch (ClientProtocolException e1) {
            throw new RuntimeException(e1);
        } catch (IOException e1) {
            throw new RuntimeException(e1);
        }

        Type searchHitType = new TypeToken<SearchHit<Inventory>>() {}.getType();

        try {
            sr = gson.fromJson(
                    new InputStreamReader(response.getEntity().getContent()), searchHitType);
        }catch (JsonIOException e) {
            throw new RuntimeException(e);
        }catch (JsonSyntaxException e) {
            throw new RuntimeException(e);
        }catch (IllegalStateException e){
            throw new RuntimeException(e);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sr.getSource();
    }

    private void saveInventoryInFile(Inventory inventory) {
        try {
            FileOutputStream fos = context.openFileOutput(inventory + ".sav", 0);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            gson.toJson(newInventory, writer);
            writer.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    class UpdateInventoryThread extends Thread {
        private User user;
        private Inventory inventory;

        public UpdateInventoryThread(User user, Inventory inventory) {
            this.inventory = inventory;
            this.user = user;
        }

        @Override
        public void run() {
            addInventory(user, inventory);
            // Give some time to get updated info
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ((Activity) context).runOnUiThread(doFinishAdd);
        }
    }


    class LoadInventoryThread extends Thread {
        private User user;
        private Inventory inventory;

        public LoadInventoryThread(User user, Inventory inventory) {
            this.inventory = inventory;
            this.user = user;
        }

        @Override
        public void run() {
            inventory = loadInventory(user);
            // Give some time to get updated info
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ((Activity) context).runOnUiThread(doFinishAdd);
        }

    }
}
