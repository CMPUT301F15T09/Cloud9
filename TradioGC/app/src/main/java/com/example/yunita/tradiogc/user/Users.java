package com.example.yunita.tradiogc.user;

import com.example.yunita.tradiogc.Observable;
import com.example.yunita.tradiogc.Observer;

import java.util.ArrayList;

public class Users extends ArrayList<User> implements Observable {

    private static final long serialVersionUID = 3199561696102797345L;
    private volatile ArrayList<Observer> observers = new ArrayList<Observer>();

    public void sortByNumberOfTrades() {
        Users result = new Users();
        for (User user: this) {
            if (user.getTrades() != null) {
                int numofTrades = user.getTrades().getCurrentTrades().size()
                        + user.getTrades().getCompletedTrades().size();

                int i;
                for (i = 0; i < result.size(); i++) {
                    User resultUser = result.get(i);
                    int resultNum = resultUser.getTrades().getCurrentTrades().size()
                            + resultUser.getTrades().getCompletedTrades().size();
                    if (numofTrades >= resultNum ) {
                        break;
                    }
                }

                result.add(i, user);
            }
        }

        this.clear();
        this.addAll(result);
    }


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
