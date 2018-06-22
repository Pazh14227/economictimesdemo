package com.example.malai_pt1882.economictimesdemo.tabs;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by malai-pt1882 on 05/04/18.
 */

public abstract class TabComponents implements TabsManager {

    /**
     * View for tabs
     */
    private final RecyclerView recyclerView;

    /**
     * TabFragment context is for creating new recycler view with respect to the fragments context.
     */
    private final Context context;

    /**
     * This is the tab which the fragment is associated with
     */
    private final Tab tab;

    /**
     * Constructor. Objects can be made only for the type Tab. So the constructor is made package-private. Only Tab fragments is going to instantiate this class.
     *
     * @param tab     Any one type mentioned in Tab enum.
     * @param context Context of the fragment to create a recycler view and to display status on toasts
     */


    public TabComponents(Tab tab, Context context) {
        this.context = context;
        recyclerView = new RecyclerView(this.context);
        this.tab = tab;
    }


    public static int getNumberOfTabs() {
        return Tab.values().length;
    }


    public static String[] getAllTabNames() {
        int size = getNumberOfTabs();
        String[] result = new String[size];
        for (int i = 0; i < size; i++) {
            result[i] = Tab.values()[i].toString();
        }
        return result;
    }


    @Override
    public int getPositionForTab(Tab tab) {
        int size = getNumberOfTabs();

        for (int i = 0; i < size; i++) {
            if (Tab.values()[i].equals(tab)) {
                return i;
            }
        }

        return -1;
    }


    @Override
    public Tab getTab() {
        return tab;
    }


    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public abstract RecyclerView.Adapter getRecyclerAdapter();

    @Override
    public abstract String getJsonUrl();

    @Override
    public RecyclerView getView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(getRecyclerAdapter());

        return recyclerView;
    }

}

