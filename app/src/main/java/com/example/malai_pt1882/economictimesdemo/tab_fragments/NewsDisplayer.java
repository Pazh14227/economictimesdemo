package com.example.malai_pt1882.economictimesdemo.tab_fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.malai_pt1882.economictimesdemo.Loaders.TabLoader;
import com.example.malai_pt1882.economictimesdemo.R;
import com.example.malai_pt1882.economictimesdemo.tabs.Tab;

public class NewsDisplayer extends MyFragment {

    public static final String POSITION_KEY = "position";


    @Override
    public void visibleToUser() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        if(bundle!=null) {
            int position = bundle.getInt(POSITION_KEY);

            Log.d("Bundle ","is not null");

            View layout = inflater.inflate(R.layout.fragment_view,container,false);

            ProgressBar progressBar = layout.findViewById(R.id.progress_bar);

            TabLoader tabLoader = new TabLoader(Tab.values()[position],getContext(),getLoaderManager(),progressBar);

            RecyclerView recyclerView = layout.findViewById(R.id.news_recycler);
            recyclerView.setAdapter(tabLoader.getRecyclerAdapter());

            tabLoader.startLoading();

            return layout;
        }

        return null;
    }

}
