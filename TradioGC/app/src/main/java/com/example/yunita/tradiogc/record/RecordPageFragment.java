// tab layout taken from https://guides.codepath.com/android/google-play-style-tabs-using-tablayout
// (C) 2015 CodePath modified by Cloud 9

package com.example.yunita.tradiogc.record;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.yunita.tradiogc.R;
import com.example.yunita.tradiogc.login.LoginActivity;
import com.example.yunita.tradiogc.notification.Notification;
import com.example.yunita.tradiogc.trade.Trade;
import com.example.yunita.tradiogc.trade.TradeDetailActivity;
import com.example.yunita.tradiogc.trade.Trades;
import com.example.yunita.tradiogc.user.UserController;

public class RecordPageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private Context context = getActivity();
    private int mPage;
    private ListView listView;
    private ArrayAdapter<Trade> currentTradesArrayAdapter;
    private ArrayAdapter<Trade> pastTradesArrayAdapter;
    private ArrayAdapter<Trade> completedTradesArrayAdapter;
    private Trades trades = new Trades();
    private UserController userController;

    private AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Trade trade = trades.get(position);
            int tradeId = trade.getId();

            // call another intent
            Intent intent = new Intent(getActivity(), TradeDetailActivity.class);
            intent.putExtra("trade_id", tradeId);
            startActivity(intent);
        }
    };

    public static RecordPageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        RecordPageFragment fragment = new RecordPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        userController = new UserController(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.record_fragment_page, container, false);
        listView = (ListView) view;
        System.out.println("on create view");

        // refresh user data
        Thread getUserLoginThread = userController.new GetUserLoginThread(LoginActivity.USERLOGIN.getUsername());
        getUserLoginThread.start();
        synchronized (getUserLoginThread) {
            try {
                getUserLoginThread.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        switch (mPage){
            case 1:// current = pending, offered, accepted
                setCurrentTradesView();
                break;
            case 2: // completed
                setCompletedTradesView();
                break;
            case 3: // past = completed, declined
                setPastTradesView();
                break;
        }
        return view;
    }

    /**
     * Sets the current trades view.
     */
    public void setCurrentTradesView(){
        trades.clear();
        trades.addAll(LoginActivity.USERLOGIN.getTrades().getCurrentTrades());

        currentTradesArrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.trades_list_item, trades);
        listView.setAdapter(currentTradesArrayAdapter);

        listView.setOnItemClickListener(clickListener);
    }

    /**
     * Sets the past trades view.
     */
    public void setPastTradesView(){
        trades.clear();
        trades.addAll(LoginActivity.USERLOGIN.getTrades().getPastTrades());

        pastTradesArrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.trades_list_item, trades);
        listView.setAdapter(pastTradesArrayAdapter);

        listView.setOnItemClickListener(clickListener);
    }

    /**
     * Sets the completed trades view.
     */
    public void setCompletedTradesView(){
        trades.clear();
        trades.addAll(LoginActivity.USERLOGIN.getTrades().getCompletedTrades());

        completedTradesArrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.trades_list_item, trades);
        listView.setAdapter(completedTradesArrayAdapter);

        listView.setOnItemClickListener(clickListener);
    }


}