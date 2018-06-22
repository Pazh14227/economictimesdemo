package com.example.malai_pt1882.economictimesdemo.detailednews;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.malai_pt1882.economictimesdemo.Database.DatabaseContract;
import com.example.malai_pt1882.economictimesdemo.R;
import com.example.malai_pt1882.economictimesdemo.ToolbarPreferences;

import java.util.ArrayList;

public class DetailedNews extends AppCompatActivity {

    public static final String POSITION_KEY = "position";
    public static final String DATA_KEY = "data";
    private Toast toast;

    private Menu menu;

    private static final int FAVORITE_MENU_ID = 1;

    private ArrayList<Bundle> data;

    public static DetailedNewsAdapter detailedNewsAdapter;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_news);

        toast = Toast.makeText(this,"",Toast.LENGTH_SHORT);

        Toolbar toolbar = findViewById(R.id.detailed_activity_toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null) {
            ToolbarPreferences.setToolbarPreferences(getSupportActionBar());
        }

        Intent intent = getIntent();
        data = intent.getParcelableArrayListExtra(DATA_KEY);

        detailedNewsAdapter = new DetailedNewsAdapter(getSupportFragmentManager(),data);

        ViewPager viewPager = getViewPager();
        viewPager.setAdapter(getAdapter());

        viewPager.setCurrentItem(intent.getIntExtra(POSITION_KEY,0));

        setOnPageChangeListenerForViewPager(viewPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                    onBackPressed();
                    return true;
            case FAVORITE_MENU_ID:
                    if(!item.isChecked()){
                        item.setIcon(R.drawable.favorite_selected);
                        item.setChecked(true);
                        toast.setText("Added to favorite");
                        toast.show();
                    } else{
                        item.setIcon(R.drawable.favorite);
                        item.setChecked(false);
                        toast.setText("Removed From favorites");
                        toast.show();
                    }
                    return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        this.menu = menu;

        MenuItem favoriteItem = menu.add(0,FAVORITE_MENU_ID,0,"Favorite icon");

        if(!data.get(getIntent().getIntExtra(POSITION_KEY,0)).getBoolean(DatabaseContract.FAVORITE_COLUMN)) {
            favoriteItem.setIcon(R.drawable.favorite);
            favoriteItem.setChecked(false);
        } else{
            favoriteItem.setIcon(R.drawable.favorite_selected);
            favoriteItem.setChecked(true);
        }
        favoriteItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
    }

    private ViewPager getViewPager(){
        return findViewById(R.id.view_pager);
    }

    private DetailedNewsAdapter getAdapter(){
        return detailedNewsAdapter;
    }

    private void favoriteMenuClicked(){

    }

    private void favoriteMenuUnclicked(){

    }

    private void setOnPageChangeListenerForViewPager(ViewPager viewPager){
       viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
           @Override
           public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

           }

           @Override
           public void onPageSelected(int position) {
                if(data.get(position).getBoolean(DatabaseContract.FAVORITE_COLUMN)){
                    MenuItem menuItem = menu.getItem(0);
                    menuItem.setIcon(R.drawable.favorite_selected);
                    menuItem.setChecked(true);
                } else{
                    MenuItem menuItem = menu.getItem(0);
                    menuItem.setIcon(R.drawable.favorite);
                    menuItem.setChecked(false);
                }
           }

           @Override
           public void onPageScrollStateChanged(int state) {

           }
       });
    }
}