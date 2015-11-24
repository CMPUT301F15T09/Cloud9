package com.example.yunita.tradiogc.friends;

import com.example.yunita.tradiogc.Observable;
import com.example.yunita.tradiogc.Observer;

import java.util.ArrayList;

public class Friends extends ArrayList<String> implements Observable {

    private static final long serialVersionUID = 3199561696102797345L;
    private volatile ArrayList<Observer> observers = new ArrayList<Observer>();
    private FriendsController friendsController;

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

}
