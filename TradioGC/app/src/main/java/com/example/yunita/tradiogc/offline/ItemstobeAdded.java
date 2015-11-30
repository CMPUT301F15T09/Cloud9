package com.example.yunita.tradiogc.offline;

import android.content.Context;

import com.example.yunita.tradiogc.inventory.Inventory;
import com.example.yunita.tradiogc.inventory.InventoryController;
import com.example.yunita.tradiogc.inventory.Item;
import com.example.yunita.tradiogc.login.LoginActivity;
import com.example.yunita.tradiogc.user.User;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by User on 2015-11-29.
 */
public class ItemstobeAdded {

    Inventory addInventory;
    InventoryController inventoryController;
    private Context context;
    private Gson gson = new Gson();

    public ItemstobeAdded(Context context) {
        super();
        this.context = context;
    }

    public Inventory getAddInventory(){
        addInventory = loadAddInvFromFile(LoginActivity.USERLOGIN);
        return addInventory;
    }

    public void addItem(Item item) {
        this.addInventory = loadAddInvFromFile(LoginActivity.USERLOGIN);
        addInventory.add(item);
        saveAddInvInFile(addInventory, LoginActivity.USERLOGIN);
    }

    public void addAllItems(){
        for (Item item: addInventory){
            inventoryController.addItem(item, LoginActivity.USERLOGIN);
        }
        Inventory inventory = new Inventory();
        saveAddInvInFile(inventory, LoginActivity.USERLOGIN);
    }


    private void saveAddInvInFile(Inventory inventory, User user){
        try{
            FileOutputStream fos = context.openFileOutput(user.getUsername() + "addinventory.sav", 0);
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
            FileInputStream fis = context.openFileInput(user.getUsername() + "addinventory.sav");
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            addInventory = gson.fromJson(in, Inventory.class);
            return addInventory;
        } catch (FileNotFoundException e) {
            addInventory = new Inventory();
            return addInventory;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
