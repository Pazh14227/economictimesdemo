package com.example.malai_pt1882.economictimesdemo.detailednews;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.malai_pt1882.economictimesdemo.Database.DatabaseContract;

import java.util.ArrayList;

public class DetailedNewsAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Bundle> data;

    DetailedNewsAdapter(FragmentManager fm, ArrayList<Bundle> data) {
        super(fm);

        this.data = data;
    }

    @Override
    public Fragment getItem(int position) {

        if(position >= data.size()){
            return null;
        }

        Fragment fragment = new DetailedNewsFragment();
        fragment.setArguments(data.get(position));

        return fragment;
    }

    @Override
    public int getCount() {

        if(data.size() > DatabaseContract.MAX_NEWS_PER_TAB){
            return DatabaseContract.MAX_NEWS_PER_TAB;
        }

        return data.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
