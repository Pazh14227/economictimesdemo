package com.example.malai_pt1882.economictimesdemo;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.malai_pt1882.economictimesdemo.tab_fragments.MyFragment;

import com.example.malai_pt1882.economictimesdemo.tab_fragments.NewsDisplayer;
import com.example.malai_pt1882.economictimesdemo.tabs.Tab;

/**
 * Created by malai-pt1882 on 05/04/18.
 */

public class NewsFragmentAdapter extends FragmentStatePagerAdapter {

    NewsFragmentAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public MyFragment getItem(int position) {

        MyFragment fragment = null;
        Bundle bundle;

        if(position < Tab.values().length){
            fragment = new NewsDisplayer();
            bundle = new Bundle();
            bundle.putInt(NewsDisplayer.POSITION_KEY,position);
            fragment.setArguments(bundle);
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return Tab.values().length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Tab.values()[position].getTabTitle();
    }



}
