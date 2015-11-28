// tab layout taken from https://guides.codepath.com/android/google-play-style-tabs-using-tablayout
// (C) 2015 CodePath modified by Cloud 9

package com.example.yunita.tradiogc.record;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

import java.util.ArrayList;


public class RecordFragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[]{"Current", "Completed", "Past"};
    private Context context;
    private ArrayList<String> tagList = new ArrayList<>();
    private FragmentManager fm;


    public RecordFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.fm = fm;
        this.context = context;
    }

    public Object instantiateItem(ViewGroup container, int position) {
        tagList.add(makeFragmentName(container.getId(), getItemId(position)));
        return super.instantiateItem(container, position);
    }

    public void update(int item){
        RecordPageFragment recordPageFragment = (RecordPageFragment) fm.findFragmentByTag(tagList.get(item));
        if(recordPageFragment != null){
            getItem(item).update();
        }
    }

    /**
     * Gets the number of the page.
     *
     * @return Integer
     */
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    /**
     * Gets the next tab.
     *
     * @param position tab index
     * @return RecordPageFragment
     */
    @Override
    public RecordPageFragment getItem(int position) {
        return RecordPageFragment.newInstance(position);
    }

    /**
     * Gets the title of the current tab.
     *
     * @param position tab index
     * @return CharSequence
     */
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

    private static String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }
}
