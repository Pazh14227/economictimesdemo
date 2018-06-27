package com.example.malai_pt1882.economictimesdemo;


import android.support.v7.app.ActionBar;


/**
 * This class will be used to set custom toolbar preferences which all the activities share
 */
public class ToolbarPreferences {

    /**
     * This class need not want to be intiated. so constructor is made private
     */
    private ToolbarPreferences(){

    }

    public static void setToolbarPreferences(ActionBar actionBar){

        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setHomeAsUpIndicator(R.drawable.up_button);
        }
    }
}
