package com.example.malai_pt1882.economictimesdemo.Loaders;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.malai_pt1882.economictimesdemo.Database.DatabaseContract;
import com.example.malai_pt1882.economictimesdemo.Database.NewsDatabase;
import com.example.malai_pt1882.economictimesdemo.tabs.Tab;
import com.example.malai_pt1882.economictimesdemo.tabs.TabsManager;

import java.util.ArrayList;

public class DatabaseLoader extends AsyncTaskLoader<ArrayList<Bundle>> {

    private final TabsManager tabsManager;

    DatabaseLoader(@NonNull TabsManager tabsManager) {
        super(tabsManager.getContext());
        this.tabsManager = tabsManager;
    }

    @Nullable
    @Override
    public ArrayList<Bundle> loadInBackground() {

        Log.d("DatabaseLoader", "Started for " + tabsManager.getTab().toString());

        SQLiteDatabase sqLiteDatabase = new NewsDatabase(getContext()).getWritableDatabase();

        Cursor cursor = getRecordsOfTab(tabsManager.getTab(), sqLiteDatabase);
        NewsLoader.NewsData newsData = new NewsLoader.NewsData();
        newsData.parseCursor(cursor);

        sqLiteDatabase.close();
        return newsData.getNews();

    }

    private Cursor getRecordsOfTab(Tab tab, SQLiteDatabase sqLiteDatabase) {
        return sqLiteDatabase.query(DatabaseContract.NEWS_TABLE, null, DatabaseContract.getColumnName(tab) + " >= 0 ", null, null, null, DatabaseContract.getColumnName(tab));
    }
}
