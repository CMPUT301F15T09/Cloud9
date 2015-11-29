// tab layout taken from https://guides.codepath.com/android/google-play-style-tabs-using-tablayout
// (C) 2015 CodePath modified by Cloud 9

package com.example.yunita.tradiogc.record;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.yunita.tradiogc.R;

public class RecordActivity extends AppCompatActivity {
    private String tab_title;


    public ListView getRecordListView(){
        return (ListView) findViewById(R.id.record_list_view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_activity);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                tab_title = bundle.getString("tab_title");
            }
        }

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        final RecordFragmentPagerAdapter recordFragmentPagerAdapter = new RecordFragmentPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(recordFragmentPagerAdapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        if (tab_title != null) {
            TabLayout.Tab tab;
            switch (tab_title) {
                case "completed":
                    tab = tabLayout.getTabAt(1);
                    break;
                case "past":
                    tab = tabLayout.getTabAt(2);
                    break;
                default:
                    tab = tabLayout.getTabAt(0);
                    break;
            }
            tab.select();
        }
    }

}