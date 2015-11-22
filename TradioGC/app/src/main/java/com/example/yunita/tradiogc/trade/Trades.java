package com.example.yunita.tradiogc.trade;

import com.example.yunita.tradiogc.inventory.Item;

import java.security.acl.Owner;
import java.util.ArrayList;

public class Trades extends ArrayList<Trade> {

    public Trades(){

    }

    public Trades(Trades t){
        this.addAll(t);
    }

    public Trades getOfferedTrade(){
        Trades offeredTrades = new Trades();
        for(Trade t : this){
            if(t instanceof OfferedTrade){
                offeredTrades.add(t);
            }
        }
        return offeredTrades;
    }

    public Trades getAcceptedTrade(){
        Trades acceptedTrades = new Trades();
        for(Trade t : this){
            if(t instanceof AcceptedTrade){
                acceptedTrades.add(t);
            }
        }
        return acceptedTrades;
    }

}
