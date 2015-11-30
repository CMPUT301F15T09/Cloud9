package com.example.yunita.tradiogc.inventory;

import android.content.Context;

import com.example.yunita.tradiogc.CheckNetwork;
import com.example.yunita.tradiogc.WebServer;
import com.example.yunita.tradiogc.login.LoginActivity;
import com.example.yunita.tradiogc.offline.ItemstobeAdded;
import com.example.yunita.tradiogc.offline.ItemstobeDeleted;
import com.example.yunita.tradiogc.offline.ItemstobeUpdated;
import com.example.yunita.tradiogc.user.User;
import com.example.yunita.tradiogc.user.UserController;
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
 * This controller handles the user's inventory.
 */
public class InventoryController {
    private static final String TAG = "InventoryController";
    private Inventory inventory = LoginActivity.USERLOGIN.getInventory();
    private UserController userController;
    private WebServer webServer = new WebServer();
    private CheckNetwork checkNetwork;
    private Context context;
    private Gson gson = new Gson();

    private ItemstobeAdded newItems;
    private ItemstobeDeleted oldItems;
    private ItemstobeUpdated changedItems;

    /**
     * Class constructor specifying that this controller class is a subclass of Context.
     *
     * @param context
     */
    public InventoryController(Context context) {
        super();
        this.context = context;
        this.userController = new UserController(context);
        this.checkNetwork = new CheckNetwork(context);
        this.newItems = new ItemstobeAdded(context);
        this.oldItems = new ItemstobeDeleted(context);
        this.changedItems = new ItemstobeUpdated(context);
    }

    /**
     * Called after a new item is created.
     * <p>This method is used to add an item to the user's inventory
     * and run the "Update User Thread".
     *
     * @param item a new item
     */
    public void addItem(Item item, User user) {
        inventory.add(item);
        if (checkNetwork.isOnline()) {
            saveInventoryInFile(inventory, user);
            Thread updateUserThread = userController.new UpdateUserThread(LoginActivity.USERLOGIN);
            updateUserThread.start();
            synchronized (updateUserThread) {
                try {
                    updateUserThread.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            saveInventoryInFile(inventory, user);
            newItems.addItem(item);
        }
    }

    /**
     * Called when the user long presses on an item that exists in the inventory.
     * <p>This method is used to remove an item from the user's inventory and
     * run the "Update User Thread".
     *
     * @param item an existing item in the user's inventory
     */
    public void removeExistingItem(Item item, User user) {
        inventory.remove(item);
        if(checkNetwork.isOnline()) {
            saveInventoryInFile(inventory, user);
            Thread updateUserThread = userController.new UpdateUserThread(LoginActivity.USERLOGIN);
            updateUserThread.start();
            synchronized (updateUserThread) {
                try {
                    updateUserThread.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }else {
            saveInventoryInFile(inventory, user);
            oldItems.addItem(item);
        }
    }

    /**
     * Called when the user edits an item and needs to update the items in their inventory.
     * <p>This method updates the user's inventory after an item has been edited.
     *
     * @param item an existing item in the user's inventory
     */
    public void updateItem(Item item, User user) {
        if (checkNetwork.isOnline()) {
            if (LoginActivity.USERLOGIN.getInventory().contains(item)) {
                Thread updateUserThread = userController.new UpdateUserThread(LoginActivity.USERLOGIN);
                updateUserThread.start();
                synchronized (updateUserThread) {
                    try {
                        updateUserThread.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Thread getUserLoginThread = userController.new GetUserLoginThread(LoginActivity.USERLOGIN.getUsername());
                    getUserLoginThread.start();
                    synchronized (getUserLoginThread) {
                        try {
                            getUserLoginThread.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                saveInventoryInFile(inventory, user);
            } else {
                addItem(item, user);
                saveInventoryInFile(inventory, user);
            }
        }else{
            changedItems.addItem(item);
        }
    }

    /**
     * Called when the user long presses on an existing item in their inventory.
     * <p>This class creates a thread and runs "Delete Item".
     * While it is running, it removes the item from the user's inventory
     * and updates the inventory.
     */
    class DeleteItemThread extends Thread {
        private Item item;
        private User user;

        public DeleteItemThread(Item item, User user) {
            this.item = item;
            this.user = user;
        }

        @Override
        public void run() {
            synchronized (this) {
                removeExistingItem(item, user);
                inventory.remove(item);
                notify();
            }
        }
    }

    public void saveInventoryInFile(Inventory inventory, User user){
        try{
            FileOutputStream fos = context.openFileOutput(user.getUsername() + "inventory.sav", 0);
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

    public Inventory loadInventoryInFile(User user){
        try{
            FileInputStream fis = context.openFileInput(user.getUsername() + "inventory.sav");
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Item>>() {}.getType();
            inventory = gson.fromJson(in, listType);
            return inventory;
        } catch (FileNotFoundException e) {
            inventory = new Inventory();
            return inventory;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
