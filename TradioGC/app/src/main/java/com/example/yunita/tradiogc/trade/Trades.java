package com.example.yunita.tradiogc.trade;

import java.util.ArrayList;

public class Trades extends ArrayList<Trade> {

    public Trades(){}

    public Trades(Trades t){
        this.addAll(t);
    }

    public Trades getOfferedTrades(){
        Trades offeredTrades = new Trades();
        for(Trade t : this){
            if(t.getStatus().equals("offered")){
                offeredTrades.add(t);
            }
        }
        return offeredTrades;
    }

    public Trades getAcceptedTrades(){
        Trades acceptedTrades = new Trades();
        for(Trade t : this){
            if(t.getStatus().equals("accepted")){
                acceptedTrades.add(t);
            }
        }
        return acceptedTrades;
    }

    public Trades getDeclinedTrades(){
        Trades declinedTrades = new Trades();
        for(Trade t : this){
            if(t.getStatus().equals("declined")){
                declinedTrades.add(t);
            }
        }
        return declinedTrades;
    }

    public Trades getPendingTrades(){
        Trades pendingTrades = new Trades();
        for(Trade t : this){
            if(t.getStatus().equals("pending")){
                pendingTrades.add(t);
            }
        }
        return pendingTrades;
    }

    public Trades getCompletedTrades(){
        Trades completedTrades = new Trades();
        for(Trade t : this){
            if(t.getStatus().equals("completed")){
                completedTrades.add(t);
            }
        }
        return completedTrades;
    }

    /**
     * Gets the list of offered, pending, and accepted trades.
     *
     * @return Trades
     */
    public Trades getCurrentTrades(){
        Trades currentTrades = new Trades();
        for(Trade t : this){
            if(t.getStatus().equals("offered") || t.getStatus().equals("pending") || t.getStatus().equals("accepted")){
                currentTrades.add(t);
            }
        }
        return currentTrades;
    }

    /**
     * Gets the list of accepted and declined trades.
     *
     * @return Trades
     */
    public Trades getPastTrades(){
        Trades pastTrades = new Trades();
        for(Trade t : this){
            if(t.getStatus().equals("completed") || t.getStatus().equals("declined")){
                pastTrades.add(t);
            }
        }
        return pastTrades;
    }

    public Trade findTradeById(int id) {
        for (Trade trade: this) {
            if (trade.getId() == id) {
                return trade;
            }
        }
        return null;
    }
}
