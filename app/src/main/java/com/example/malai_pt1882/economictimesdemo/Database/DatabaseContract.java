package com.example.malai_pt1882.economictimesdemo.Database;


import android.provider.BaseColumns;

import com.example.malai_pt1882.economictimesdemo.tabs.Tab;
import com.example.malai_pt1882.economictimesdemo.tabs.TabComponents;


public final class DatabaseContract implements BaseColumns{

    public static final String NEWS_TABLE = "NewsTable";
    public static final String SOURCE_NAME_COLUMN = "Source";
    public static final String AUTHOUR_NAME_COLUMN = "Author";
    public static final String TITLE_COLUMN = "Title";
    public static final String DESCRIPTION_COLUMN = "Description";
    public static final String URL_COLUMN = "Url";
    public static final String URL_TO_IMAGE_COLUMN = "ImageUrl";
    public static final String PUBLISHED_AT_COLUMN = "PublishedOn";
    public static final String DB_NAME = "NEWS";
    public static final String FAVORITE_COLUMN = "isFavorite";
    public static final int NUMBER_OF_TAB_COLUMNS = TabComponents.getNumberOfTabs();
    /**
     * Maximum amount of news to be displayed in tab
     */
    public static final int MAX_NEWS_PER_TAB = 20;
    public static final int DEFAULT_VALUE_FOR_TAB_COLUMNS = -MAX_NEWS_PER_TAB-1;

    private static final String[] ALL_TAB_COLUMN_NAMES = TabComponents.getAllTabNames();

    public static final int DEFAULT_FAVORITE_VALUE = 0;


    /**
     * DatabaseContract does not needs to be instantiated. In order to prevent instantiation the constructor is made private
     */
    private DatabaseContract(){

    }

    public static String getColumnName(int tabPosition){
            return ALL_TAB_COLUMN_NAMES[tabPosition];
    }

    public static String getColumnName(Tab tab){
        return tab.toString();
    }

 }
