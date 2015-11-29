package com.example.yunita.tradiogc.inventory;

import android.content.Context;

import com.example.yunita.tradiogc.WebServer;
import com.example.yunita.tradiogc.login.LoginActivity;
import com.example.yunita.tradiogc.user.UserController;

/**
 * This controller handles the user's inventory.
 */
public class InventoryController {
    private static final String TAG = "InventoryController";
    private Inventory inventory = LoginActivity.USERLOGIN.getInventory();
    private UserController userController;
    private WebServer webServer = new WebServer();

    /**
     * Class constructor specifying that this controller class is a subclass of Context.
     *
     * @param context
     */
    public InventoryController(Context context) {
        super();
        this.userController = new UserController(context);
    }

    /**
     * Called after a new item is created.
     * <p>This method is used to add an item to the user's inventory
     * and run the "Update User Thread".
     *
     * @param item a new item
     */
    public void addItem(Item item) {
        inventory.add(item);
        Thread updateUserThread = userController.new UpdateUserThread(LoginActivity.USERLOGIN);
        updateUserThread.start();
    }

    /**
     * Called when the user long presses on an item that exists in the inventory.
     * <p>This method is used to remove an item from the user's inventory and
     * run the "Update User Thread".
     *
     * @param item an existing item in the user's inventory
     */
    public void removeExistingItem(Item item) {
        inventory.remove(item);
        Thread updateUserThread = userController.new UpdateUserThread(LoginActivity.USERLOGIN);
        updateUserThread.start();
    }

    /**
     * Called when the user edits an item and needs to update the items in their inventory.
     * <p>This method updates the user's inventory after an item has been edited.
     *
     * @param item an existing item in the user's inventory
     */
    public void updateItem(Item item) {
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
        } else {
            addItem(item);
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

        public DeleteItemThread(Item item) {
            this.item = item;
        }

        @Override
        public void run() {
            synchronized (this) {
                removeExistingItem(item);
                inventory.remove(item);
                notify();
            }
        }
    }
}
