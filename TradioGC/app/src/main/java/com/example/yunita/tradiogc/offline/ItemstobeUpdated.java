package com.example.yunita.tradiogc.offline;

import android.content.Context;

import com.example.yunita.tradiogc.inventory.Inventory;
import com.example.yunita.tradiogc.inventory.InventoryController;
import com.example.yunita.tradiogc.inventory.Item;
import com.example.yunita.tradiogc.login.LoginActivity;
import com.example.yunita.tradiogc.user.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by User on 2015-11-29.
 */
public class ItemstobeUpdated {
    Inventory upInventory;
    InventoryController inventoryController;
    private Context context;
    private Gson gson;

    public ItemstobeUpdated(Context context) {
        super();
        this.context = context;

        this.upInventory = loadAddInvFromFile(LoginActivity.USERLOGIN);
    }

    public void addItem(Item item) {
        upInventory.add(item);
        saveAddInvInFile(upInventory, LoginActivity.USERLOGIN);
    }

    public void upAllItems(){
        for (Item item: upInventory){
            inventoryController.updateItem(item);
        }
    }


    private void saveAddInvInFile(Inventory inventory, User user){
        try{
            FileOutputStream fos = context.openFileOutput(user.getUsername() + "upinventory.sav", 0);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            gson.toJson(inventory, writer);
            writer.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Inventory loadAddInvFromFile(User user){
        try{
            FileInputStream fis = context.openFileInput(user.getUsername() + "upinventory.sav");
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Item>>() {}.getType();
            upInventory = gson.fromJson(in, listType);
            return upInventory;
        } catch (FileNotFoundException e) {
            upInventory = new Inventory();
            return upInventory;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
