package com.example.malai_pt1882.economictimesdemo;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private static final String[] SETTINGS_ITEM = {"Pop-up High Priority list", "White Theme", "Auto-update On Wifi"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        addTheCustomToolbar();

        setContentView(addChildToLinearLayout());

    }

    private void addTheCustomToolbar() {

        Toolbar toolbar = findViewById(R.id.settings_activity_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            ToolbarPreferences.setToolbarPreferences(getSupportActionBar());
        }
    }

    private ListView createListViewAndSetUpWithArrayAdapter() {
        ListView listView = new ListView(getApplicationContext());
        listView.setHorizontalScrollBarEnabled(false);
        listView.setAdapter(getArrayAdapter());
        return listView;
    }

    private ArrayAdapter<String> getArrayAdapter() {
        return new ArrayAdapter<>(this, R.layout.settings_list_items_layout, R.id.settings_text_view_id, SETTINGS_ITEM);
    }

    private LinearLayout addChildToLinearLayout() {
        LinearLayout linearLayout = findViewById(R.id.settings_linear_layout);
        linearLayout.addView(createListViewAndSetUpWithArrayAdapter());
        return linearLayout;
    }


}