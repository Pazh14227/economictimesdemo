package com.example.malai_pt1882.economictimesdemo;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.malai_pt1882.economictimesdemo.Database.NewsDatabase;

public class MainActivity extends AppCompatActivity {
    private ActionBarDrawerToggle actionBarDrawerToggle;

    public static NewsDatabase newsDatabase;

    private NewsFragmentAdapter newsFragmentAdapter = null;

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsDatabase = NewsDatabase.getInstance(getApplicationContext());

        drawerLayout = findViewById(R.id.drawer_layout);

        Bundle bundles = new Bundle();
        bundles.putString("Hello", "World");

        Log.d("Bundle working ", bundles.getString("Hello"));

        //Add the custom toolbar to main activity
        setToolbarAsActionBar();

        //Get the tabLayout and set style for it.
        setStyleForTabLayout(getTabLayout());

        //create actionBarDrawerToggle icon in the action bar
        createDrawerToggleAndConnectWithDrawerLayout();

        //get instance of FragmentStatePagerAdapter and viewPager. Connect the viewPager with the instance and tablayout.
        setViewPagerWithFragmentStatePagerAdapterAndTabLayout(getViewPager(), getNewsFragmentAdapter(), getTabLayout());

        //obtain the navigation view and get menu instance from it.
        createMenuItemsInNavigationViewAndAddListeners();


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                 drawerLayout.openDrawer(Gravity.START);
                 return true;
        }


        return actionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    private TabLayout getTabLayout() {
        return findViewById(R.id.tab_layout_root);
    }

    private void setStyleForTabLayout(@NonNull TabLayout tabLayout) {
        tabLayout.setSelectedTabIndicatorColor(Color.WHITE);
    }


    private Toolbar getToolbar() {
        return (Toolbar)findViewById(R.id.main_activity_toolbar);
    }


    private DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }


    private void setViewPagerWithFragmentStatePagerAdapterAndTabLayout(@NonNull final ViewPager viewpager, final NewsFragmentAdapter fragmentStatePagerAdapter, @NonNull TabLayout tabLayout) {
        tabLayout.setupWithViewPager(viewpager);
        viewpager.setAdapter(fragmentStatePagerAdapter);
        viewpager.setOffscreenPageLimit(1);

    }


    private ActionBarDrawerToggle getActionBarDrawerToggle(DrawerLayout drawerLayout) {
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        DrawerArrowDrawable drawerArrowDrawable = new DrawerArrowDrawable(this);
        drawerArrowDrawable.setColor(getResources().getColor(R.color.white));
        actionBarDrawerToggle.setDrawerArrowDrawable(drawerArrowDrawable);
        return actionBarDrawerToggle;
    }


    private NewsFragmentAdapter getNewsFragmentAdapter() {
        if (newsFragmentAdapter == null) {
            newsFragmentAdapter = new NewsFragmentAdapter(getSupportFragmentManager());
        }
        return newsFragmentAdapter;
    }


    @NonNull
    private ViewPager getViewPager() {
        return findViewById(R.id.pager);
    }


    private NavigationView getNavigationView() {
        return findViewById(R.id.navigation_view);
    }


    private void populateMenuItems(Menu menu) {
        MenuItem menuItem = menu.add(Menu.NONE, R.id.home_menu_id, Menu.NONE, R.string.menu_item_home);
        menuItem.setIcon(R.drawable.ic_home_icon);
        menu.add(Menu.NONE, R.id.settings_menu_id, Menu.NONE, R.string.menu_item_settings).setIcon(R.drawable.ic_settings_icon);
        menu.add(Menu.NONE, R.id.logout_menu_id, Menu.NONE, R.string.menu_item_logout).setIcon(R.drawable.ic_logout_icon);
    }


    private void setClickListenersForMenuItems(Menu menu) {
        int size = menu.size();
        MenuItem.OnMenuItemClickListener clickListener = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_menu_id:
                        moveToFirstTab();
                        closeNavigationView();
                        break;
                    case R.id.settings_menu_id:
                        settingsMenuHandler();
                }
                return true;
            }
        };
        for (int i = 0; i < size; i++) {
            menu.getItem(i).setOnMenuItemClickListener(clickListener);
        }
    }


    private void settingsMenuHandler() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }


    private void setToolbarAsActionBar() {
        Toolbar toolbar = getToolbar();
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            ToolbarPreferences.setToolbarPreferences(getSupportActionBar());
        } else {
            Toast.makeText(this, R.string.no_action_bar, Toast.LENGTH_SHORT).show();
        }
    }


    private void closeNavigationView() {
        getDrawerLayout().closeDrawer(getNavigationView());
    }


    private void moveToFirstTab() {
        TabLayout.Tab homeTab = getTabLayout().getTabAt(0);
        if (homeTab != null) {
            homeTab.select();
        } else {
            Toast.makeText(MainActivity.this, R.string.home_tab_exception, Toast.LENGTH_SHORT).show();
        }
    }


    private void createMenuItemsInNavigationViewAndAddListeners() {
        Menu menu = getNavigationView().getMenu();
        populateMenuItems(menu);
        setClickListenersForMenuItems(menu);
    }


    private void createDrawerToggleAndConnectWithDrawerLayout() {
        DrawerLayout drawerLayout = getDrawerLayout();
        actionBarDrawerToggle = getActionBarDrawerToggle(drawerLayout);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
    }


    @Override
    protected void onStart() {
        super.onStart();

        Log.d("LifeCycle", "In onStart method");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("LifeCycle", "In onResume method");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d("LifeCycle", "In onPause method");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d("LifeCycle", "In onStop method");
    }

    @Override
    protected void onDestroy() {
        Log.d("LifeCycle", "In onDestroy method");

        newsDatabase.close();
        super.onDestroy();

    }

}