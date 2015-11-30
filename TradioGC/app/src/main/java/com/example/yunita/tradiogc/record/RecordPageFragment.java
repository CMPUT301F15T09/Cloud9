// tab layout taken from https://guides.codepath.com/android/google-play-style-tabs-using-tablayout
// (C) 2015 CodePath modified by Cloud 9

package com.example.yunita.tradiogc.record;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.yunita.tradiogc.R;
import com.example.yunita.tradiogc.login.LoginActivity;
import com.example.yunita.tradiogc.trade.Trade;
import com.example.yunita.tradiogc.trade.TradeDetailActivity;
import com.example.yunita.tradiogc.trade.Trades;
import com.example.yunita.tradiogc.user.UserController;

/**
 * This class creates a fragment used for the Record Activity
 */
public class RecordPageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    private ListView listView;
    private Trades trades = new Trades();
    private RecordListAdapter recordListAdapter;
    private Context context;
    private UserController userController = new UserController(context);

    private AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Trade trade = trades.get(position);
            int tradeId = trade.getId();

            // Calls another intent
            Intent intent = new Intent(getActivity(), TradeDetailActivity.class);
            intent.putExtra("trade_id", tradeId);

            switch (mPage) {
                case 0:
                    intent.putExtra("tab_title", "current");
                    break;
                case 1:
                    intent.putExtra("tab_title", "completed");
                    break;
                case 2:
                    intent.putExtra("tab_title", "past");
                    break;
            }

            startActivity(intent);
            getActivity().overridePendingTransition(0, 0);
            getActivity().finish();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Override this method in the activity that hosts the Fragment and call super
        // in order to receive the result inside onActivityResult from the fragment.
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == 2 ) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();

        }

    }

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
        if (getActivity()!=null) {
            context = getActivity();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.record_fragment_page, container, false);
        listView = (ListView) view;

        setView();
        return view;
    }

    /**
     * Sets the list view for the trade records.
     */
    public void setView() {
        switch (mPage) {
            case 0:// current = pending, offered, accepted
                setCurrentTradesView();
                System.out.println("**********");
                break;
            case 1: // completed
                setCompletedTradesView();
                System.out.println("++++++++++");
                break;
            case 2: // past = completed, declined
                setPastTradesView();
                System.out.println("==========");
                break;
        }
    }

    /**
     * Sets the current trades view.
     */
    public void setCurrentTradesView() {
        trades.clear();
        trades.addAll(LoginActivity.USERLOGIN.getTrades().getCurrentTrades());

        recordListAdapter = new RecordListAdapter(context, R.layout.record_list_item, trades);
        listView.setAdapter(recordListAdapter);

        listView.setOnItemClickListener(clickListener);
    }

    /**
     * Sets the past trades view.
     */
    public void setPastTradesView() {
        trades.clear();
        trades.addAll(LoginActivity.USERLOGIN.getTrades().getPastTrades());

        recordListAdapter = new RecordListAdapter(context, R.layout.record_list_item, trades);
        listView.setAdapter(recordListAdapter);

        listView.setOnItemClickListener(clickListener);
    }

    /**
     * Sets the completed trades view.
     */
    public void setCompletedTradesView() {
        trades.clear();
        trades.addAll(LoginActivity.USERLOGIN.getTrades().getCompletedTrades());

        recordListAdapter = new RecordListAdapter(context, R.layout.record_list_item, trades);
        listView.setAdapter(recordListAdapter);

        listView.setOnItemClickListener(clickListener);
    }

    /**
     * Updates the view.
     */
    public void update(){
        Thread getUserLoginThread = userController.new GetUserLoginThread(LoginActivity.USERLOGIN.getUsername());
        getUserLoginThread.start();
        synchronized (getUserLoginThread) {
            try {
                getUserLoginThread.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        setView();
    }
}