package com.example.malai_pt1882.economictimesdemo.tabs;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

public interface TabsManager {

    /**
     * This recycler view should be associated with each tab.
     *
     * @return Recycler view for displaying content in tab
     */
    RecyclerView getView();

    /**
     * This tab context is the context of the fragment and it is now used by subclasses to create loaders.
     *
     * @return returns the context of the fragment.
     */
    Context getContext();

    /**
     * The adapter will be used by JSONRetriever to notify when the content is updated.
     *
     * @return The Recycler View adapter.
     */
    RecyclerView.Adapter getRecyclerAdapter();

    /**
     * Returns the enum tab associated with current fragment. Will be used by subclass to return the json url.
     *
     * @return Enum tab
     */
    Tab getTab();

    /**
     * Returns position with respect to tab
     *
     * @return position of tab.
     * else -1
     */
    int getPositionForTab(Tab tab);


    String getJsonUrl();

}
