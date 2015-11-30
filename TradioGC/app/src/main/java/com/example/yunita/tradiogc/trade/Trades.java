package com.example.yunita.tradiogc.trade;

import java.util.ArrayList;

/**
 * This class defines a list of trades.
 */
public class Trades extends ArrayList<Trade> {

    /**
     * Class constructor for trades.
     */
    public Trades(){}

    public Trades(Trades t){
        this.addAll(t);
    }

    /**
     * Gets the list of the user's offered trades.
     *
     * @return offeredTrades user's offered trades
     */
    public Trades getOfferedTrades(){
        Trades offeredTrades = new Trades();
        for(Trade t : this){
            if(t.getStatus().equals("offered")){
                offeredTrades.add(t);
            }
        }
        return offeredTrades;
    }

    /**
     * Gets the list of the user's accepted trades.
     *
     * @return acceptedTrades user's accepted trades
     */
    public Trades getAcceptedTrades(){
        Trades acceptedTrades = new Trades();
        for(Trade t : this){
            if(t.getStatus().equals("accepted")){
                acceptedTrades.add(t);
            }
        }
        return acceptedTrades;
    }

    /**
     * Gets the list of the user's declined trades.
     *
     * @return declinedTrades user's declined trades
     */
    public Trades getDeclinedTrades(){
        Trades declinedTrades = new Trades();
        for(Trade t : this){
            if(t.getStatus().equals("declined")){
                declinedTrades.add(t);
            }
        }
        return declinedTrades;
    }

    /**
     * Gets the list of the user's pending trades.
     *
     * @return pendingTrades user's pending trades
     */
    public Trades getPendingTrades(){
        Trades pendingTrades = new Trades();
        for(Trade t : this){
            if(t.getStatus().equals("pending")){
                pendingTrades.add(t);
            }
        }
        return pendingTrades;
    }

    /**
     * Gets the list of the user's completed trades.
     *
     * @return completedTrades user's completed trades
     */
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
     * Gets the list of the user's offered, pending, and accepted trades.
     *
     * @return currentTrades user's current trades
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
     * Gets the list of the user's completed and declined trades.
     *
     * @return pastTrades user's past trades
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

    /**
     * Searches and returns a trade by its id.
     *
     * @param id        id of the trade
     * @return trade    trade that was searched for
     */
    public Trade findTradeById(int id) {
        for (Trade trade: this) {
            if (trade.getId() == id) {
                return trade;
            }
        }
        return null;
    }
}
