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
public class ItemstobeDeleted {
    Inventory delInventory;
    InventoryController inventoryController;
    private Context context;
    private Gson gson;

    public ItemstobeDeleted(Context context) {
        super();
        this.context = context;

        this.delInventory = loadDelInvFromFile(LoginActivity.USERLOGIN);
    }

    public void addItem(Item item) {
        delInventory.add(item);
        saveDelInvInFile(delInventory, LoginActivity.USERLOGIN);
    }
    public void delAllItems(){
        for (Item item: delInventory){
            inventoryController.removeExistingItem(item);
        }
    }


    private void saveDelInvInFile(Inventory inventory, User user){
        try{
            FileOutputStream fos = context.openFileOutput(user.getUsername() + "delinventory.sav", 0);
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

    private Inventory loadDelInvFromFile(User user){
        try{
            FileInputStream fis = context.openFileInput(user.getUsername() + "delinventory.sav");
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Item>>() {}.getType();
            delInventory = gson.fromJson(in, listType);
            return delInventory;
        } catch (FileNotFoundException e) {
            delInventory = new Inventory();
            return delInventory;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
