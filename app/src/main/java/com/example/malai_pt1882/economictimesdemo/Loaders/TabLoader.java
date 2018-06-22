package com.example.malai_pt1882.economictimesdemo.Loaders;

import com.example.malai_pt1882.economictimesdemo.APIContract;
import com.example.malai_pt1882.economictimesdemo.TabsRecyclerAdapter;
import com.example.malai_pt1882.economictimesdemo.tabs.Tab;
import com.example.malai_pt1882.economictimesdemo.tabs.TabComponents;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;


public class TabLoader extends TabComponents implements LoaderManager.LoaderCallbacks<ArrayList<Bundle>> {

    private TabsRecyclerAdapter tabsRecyclerAdapter;
    private static final int NEWS_LOADER = 0;
    private static final int DATABASE_LOADER = 1;
    private final LoaderManager loaderManager;
    private ProgressBar progressBar;

    /**
     * Constructor. Objects can be made only for the type Tab. So the constructor is made package-private. Only Tab fragments is going to instantiate this class.
     *
     * @param tab     Any one type mentioned in Tab enum.
     * @param context Context of the fragment to create a recycler view and to display status on toasts
     */
    public TabLoader(Tab tab, Context context, LoaderManager loaderManager, ProgressBar progressBar) {
        super(tab, context);
        this.loaderManager = loaderManager;
        tabsRecyclerAdapter = new TabsRecyclerAdapter(null,context);
        this.progressBar = progressBar;
    }

    @Override
    public String getJsonUrl() {
        switch (getTab()) {
            case NEWS:
                return APIContract.NEWS_URL;
            case TECH:
                return APIContract.TECH_URL;
            case INDIA:
                return APIContract.INDIA_URL;
            case WORLD:
                return APIContract.WORLD_URL;
            case JOBS:
                return APIContract.JOBS_URL;
            case MARKET:
                return APIContract.MARKET_URL;
            case QUICK_READS:
                return APIContract.QUICK_READS_URL;
            default:
                return null;
        }
    }

    /**
     * Initially this method returns the recyclerAdapter instance with cursor object passed as null. Later it is replaced by onLoadFinished method.
     *
     * @return Recycler Adapter for fragment.
     */
    @Override
    public TabsRecyclerAdapter getRecyclerAdapter() {
        return tabsRecyclerAdapter;
    }

    @NonNull
    @Override
    public Loader<ArrayList<Bundle>> onCreateLoader(int id, @Nullable Bundle args) {
        switch (id) {
            case NEWS_LOADER:
                return new NewsLoader(this);
            case DATABASE_LOADER:
                return new DatabaseLoader(this);
            default:
                return new NewsLoader(this);
        }

    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Bundle>> loader, ArrayList<Bundle> data) {

        if(data!=null && data.size()>0){
            if(progressBar!=null && progressBar.isShown())
            progressBar.setVisibility(View.GONE);
        }

        if(loader.getId() == DATABASE_LOADER){
            loaderManager.initLoader(NEWS_LOADER,null,this).forceLoad();
        }


        tabsRecyclerAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Bundle>> loader) {
        tabsRecyclerAdapter.swapCursor(null);
    }

    public void startLoading() {
        loaderManager.initLoader(DATABASE_LOADER, null, this).forceLoad();
    }

    public void destroyLoaders(){
        if(loaderManager.hasRunningLoaders()) {

            loaderManager.destroyLoader(DATABASE_LOADER);
            loaderManager.destroyLoader(NEWS_LOADER);



        }
//        tabsRecyclerAdapter.dequeTask();

    }
}
