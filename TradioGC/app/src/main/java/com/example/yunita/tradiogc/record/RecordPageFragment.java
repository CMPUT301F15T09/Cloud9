// tab layout taken from https://guides.codepath.com/android/google-play-style-tabs-using-tablayout
// (C) 2015 CodePath modified by Cloud 9

package com.example.yunita.tradiogc.record;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.yunita.tradiogc.R;
import com.example.yunita.tradiogc.inventory.Inventory;
import com.example.yunita.tradiogc.inventory.Item;
import com.example.yunita.tradiogc.login.LoginActivity;
import com.example.yunita.tradiogc.trade.Trade;
import com.example.yunita.tradiogc.trade.Trades;

public class RecordPageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;
    private ListView listView;
    private ArrayAdapter<Trade> currentTradesArrayAdapter;
    private Trades temp = LoginActivity.USERLOGIN.getTrades() ;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        listView = (ListView) view;

        Trades currentTrades = new Trades(temp);
        currentTrades = currentTrades.getCurrentTrades();

        switch (mPage){
            case 1:// current = pending, offered, accepted
                currentTradesArrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.trades_list_item, currentTrades);
                listView.setAdapter(currentTradesArrayAdapter);
                break;
            case 2: //
                break;
            case 3: //
                break;
        }
        return view;
    }
}