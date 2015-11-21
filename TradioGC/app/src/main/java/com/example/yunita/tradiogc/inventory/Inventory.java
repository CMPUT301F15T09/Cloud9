package com.example.yunita.tradiogc.inventory;

import com.example.yunita.tradiogc.Observer;

import java.io.Serializable;
import java.util.ArrayList;

public class Inventory extends ArrayList<Item> implements com.example.yunita.tradiogc.Observable, Serializable {

    private static final long serialVersionUID = 3199561696102797345L;
    private volatile ArrayList<Observer> observers = new ArrayList<Observer>();

    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void deleteObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.notifyUpdated(this);
        }
    }

    /**
     * Class constructor.
     */
    public Inventory(){

    }

    /**
     * Class constructor that clones static inventory,
     * and creates new reference.
     *
     * @param temp userlogin inventory.
     */
    public Inventory(Inventory temp){
        this.addAll(temp);
    }

    /**
     * Gets the user's list of public items.
     *
     * @return Inventory inventory that only contains public items
     */
    public Inventory getPublicItems() {
        Inventory pInventory = new Inventory();
        for (Item item : this) {
            if (item.getVisibility()) {
                pInventory.add(item);
            }
        }
        return pInventory;
    }

}
