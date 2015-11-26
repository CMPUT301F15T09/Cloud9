package com.example.yunita.tradiogc;

import android.test.ActivityInstrumentationTestCase2;

import com.example.yunita.tradiogc.notification.Notification;
import com.example.yunita.tradiogc.notification.Notifications;
import com.example.yunita.tradiogc.trade.Trade;
import com.example.yunita.tradiogc.trade.Trades;
import com.example.yunita.tradiogc.user.User;

public class TradeUseCaseTest extends ActivityInstrumentationTestCase2 {

    public TradeUseCaseTest() {
        super(com.example.yunita.tradiogc.MainActivity.class);
    }

    /**
     * Use Case 18
     * 04.01.01
     * Test for offering a trade to a friend.
     */
    public void testOfferTradeWithFriends(){
        User john = new User();
        User ann = new User();

        Trade offeredTrade = new Trade();
        // ann is owner, so ann will have "offered" trade
        offeredTrade.setStatus("offered");
        ann.getTrades().add(offeredTrade);

        // john as a borrower will have "pending" trade
        Trade pendingTrade = new Trade(offeredTrade);
        pendingTrade.setStatus("pending");
        john.getTrades().add(pendingTrade);

        // john should have 1 pending trade while ann should have 1 offered trade
        assertEquals(john.getTrades().getPendingTrades().size(), 1);
        assertEquals(ann.getTrades().getOfferedTrades().size(), 1);

    }

    /**
     * Use Case 19
     * 04.02.01
     * Test for getting trade notifications when a trade is offered.
     */
    public void testGetTradeNotifications(){
        Trade offeredTrade = new Trade();
        Notification new_offered_trade_notif = new Notification(offeredTrade);
        Notifications ann_notifications = new Notifications();
        ann_notifications.add(new_offered_trade_notif);

        assertEquals(ann_notifications.size(), 1);
    }

    /**
     * Use Case 20
     * 04.03.01
     * Test for accepting a trade.
     */
    public void testAcceptTrade(){
        // ann has an offered trade from john.
        User ann = new User();
        Trade offeredTrade = new Trade();
        offeredTrade.setStatus("offered");
        ann.getTrades().add(offeredTrade);

        // ann accepts john's trade.
        offeredTrade.setStatus("accepted");

        // ann should have 1 accepted trade, and no offered trade.
        assertEquals(ann.getTrades().getOfferedTrades().size(), 0);
        assertEquals(ann.getTrades().getAcceptedTrades().size(), 1);

        // john has an pending trade before, and now should be
        // updated to accepted trade.
        User john = new User();
        Trade pendingTrade = new Trade(offeredTrade);
        pendingTrade.setStatus("pending");
        john.getTrades().add(pendingTrade);
        pendingTrade.setStatus("accepted");

        // john should have 1 accepted trade, and no pending trade.
        assertEquals(ann.getTrades().getPendingTrades().size(), 0);
        assertEquals(john.getTrades().getAcceptedTrades().size(), 1);

    }

    /**
     * Use Case 21
     * 04.03.01
     * Test for declining a trade.
     */
    public void testDeclineTrade(){
        // ann has an offered trade from john.
        User ann = new User();
        Trade offeredTrade = new Trade();
        offeredTrade.setStatus("offered");
        ann.getTrades().add(offeredTrade);

        // ann declines john's trade and does not do counter trade.
        offeredTrade.setStatus("declined");

        // ann should have 1 declined trade, and no offered trade.
        assertEquals(ann.getTrades().getOfferedTrades().size(), 0);
        assertEquals(ann.getTrades().getDeclinedTrades().size(), 1);

        // john has an pending trade before, and now should be
        // updated to declined trade.
        User john = new User();
        Trade pendingTrade = new Trade(offeredTrade);
        pendingTrade.setStatus("pending");
        john.getTrades().add(pendingTrade);
        pendingTrade.setStatus("declined");

        // john should have 1 declined trade, and no pending trade.
        assertEquals(ann.getTrades().getPendingTrades().size(), 0);
        assertEquals(john.getTrades().getDeclinedTrades().size(), 1);
    }

    /**
     * Use Case 22
     * 04.04.01
     * Test for offering a counter trade.
     */
    public void testOfferCounterTrade(){

    }

    /**
     * Use Case 23
     * 04.05.01
     * Test for editing a trade while it is getting composed.
     */
    public void testEditTrade(){

    }

    /**
     * Use Case 24
     * 04.06.01
     * Test for deleting a trade while it is getting composed.
     */
    public void testDeleteTrade(){

    }

    /**
     * Use Case 25
     * 04.07.01
     * Test for sending emails with a trade's information.
     */
    public void testEmailTradeInfo(){

    }

    /**
     * Use Case 26
     * 04.08.01, 04.09.01
     * Test for browsing a user's past trades.
     */

    public void testBrowsePastTrades(){
        User john = new User();
        Trade pendingTrade = new Trade();
        pendingTrade.setStatus("pending");
        Trade offeredTrade = new Trade();
        offeredTrade.setStatus("offered");
        Trade acceptedTrade = new Trade();
        acceptedTrade.setStatus("accepted");
        Trade declinedTrade = new Trade();
        declinedTrade.setStatus("declined");
        Trade completedTrade = new Trade();
        completedTrade.setStatus("completed");

        Trades john_trades = john.getTrades();
        john_trades.add(pendingTrade);
        john_trades.add(offeredTrade);
        john_trades.add(acceptedTrade);
        john_trades.add(declinedTrade);
        john_trades.add(completedTrade);

        // past trade = declined and completed trade
        assertEquals(john_trades.getPastTrades().size(), 2);


    }

    /**
     * Use Case 27
     * 04.08.01, 04.09.01
     * Test for browsing a user's current trades.
     */
    public void testBrowseCurrentTrades(){
        User john = new User();
        Trade pendingTrade = new Trade();
        pendingTrade.setStatus("pending");
        Trade offeredTrade = new Trade();
        offeredTrade.setStatus("offered");
        Trade acceptedTrade = new Trade();
        acceptedTrade.setStatus("accepted");
        Trade declinedTrade = new Trade();
        declinedTrade.setStatus("declined");
        Trade completedTrade = new Trade();
        completedTrade.setStatus("completed");

        Trades john_trades = john.getTrades();
        john_trades.add(pendingTrade);
        john_trades.add(offeredTrade);
        john_trades.add(acceptedTrade);
        john_trades.add(declinedTrade);
        john_trades.add(completedTrade);

        // current trade = pending, offered, accepted
        assertEquals(john_trades.getCurrentTrades().size(), 3);
    }

    /**
     * Use Case 28
     * 04.08.01, 04.09.01
     * Test for browsing trades that the user had sent.
     */
    public void testBrowseMySentTrades(){
        User john = new User();
        Trade pendingTrade = new Trade();
        pendingTrade.setStatus("pending");
        Trade offeredTrade = new Trade();
        offeredTrade.setStatus("offered");
        Trade acceptedTrade = new Trade();
        acceptedTrade.setStatus("accepted");
        Trade declinedTrade = new Trade();
        declinedTrade.setStatus("declined");
        Trade completedTrade = new Trade();
        completedTrade.setStatus("completed");

        Trades john_trades = john.getTrades();
        john_trades.add(pendingTrade);
        john_trades.add(offeredTrade);
        john_trades.add(acceptedTrade);
        john_trades.add(declinedTrade);
        john_trades.add(completedTrade);

        // john is a borrower, so every time he sent a trade
        // then he will have a "pending" trade
        assertEquals(john_trades.getPendingTrades().size(), 1);
    }

    /**
     * Use Case 29
     * 04.08.01, 04.09.01
     * Test for browsing trades that were offered to the user.
     */
    public void testBrowseTradesOfferedToMe(){
        User john = new User();
        Trade pendingTrade = new Trade();
        pendingTrade.setStatus("pending");
        Trade offeredTrade = new Trade();
        offeredTrade.setStatus("offered");
        Trade acceptedTrade = new Trade();
        acceptedTrade.setStatus("accepted");
        Trade declinedTrade = new Trade();
        declinedTrade.setStatus("declined");
        Trade completedTrade = new Trade();
        completedTrade.setStatus("completed");

        Trades john_trades = john.getTrades();
        john_trades.add(pendingTrade);
        john_trades.add(offeredTrade);
        john_trades.add(acceptedTrade);
        john_trades.add(declinedTrade);
        john_trades.add(completedTrade);

        // john as an owner will have an offered trade
        // every time someone sent him a trade.
        assertEquals(john_trades.getOfferedTrades().size(), 1);
    }

    // NEW REQUIREMENTS


}
