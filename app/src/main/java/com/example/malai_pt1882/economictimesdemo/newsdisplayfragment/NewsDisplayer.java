package com.example.malai_pt1882.economictimesdemo.tabfragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.malai_pt1882.economictimesdemo.loaders.TabLoader;
import com.example.malai_pt1882.economictimesdemo.R;
import com.example.malai_pt1882.economictimesdemo.tabs.TabComponents;

public class NewsDisplayer extends Fragment {

    public static final String POSITION_KEY = "position";

    private TabLoader tabLoader = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();

        if(bundle!=null) {

            int position = bundle.getInt(POSITION_KEY);

            View layout = inflater.inflate(R.layout.fragment_view,container,false);

            ProgressBar progressBar = layout.findViewById(R.id.progress_bar);

            tabLoader = new TabLoader(TabComponents.getTab(position),getContext(),getLoaderManager(),progressBar);

            RecyclerView recyclerView = layout.findViewById(R.id.news_recycler);
            recyclerView.setAdapter(tabLoader.getRecyclerAdapter());

            tabLoader.startLoading();

            return layout;
        }

        return null;
    }

    @Override
    public void onDestroyView() {

        if(tabLoader!=null){
            tabLoader.destroyLoaders();
        }

        super.onDestroyView();
    }
}
