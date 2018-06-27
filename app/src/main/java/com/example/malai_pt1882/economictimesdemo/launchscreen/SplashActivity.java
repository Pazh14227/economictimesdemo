package com.example.malai_pt1882.economictimesdemo.launch_screen;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.malai_pt1882.economictimesdemo.Database.DatabaseContract;
import com.example.malai_pt1882.economictimesdemo.Database.NewsDatabase;
import com.example.malai_pt1882.economictimesdemo.MainActivity;
import com.example.malai_pt1882.economictimesdemo.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        final NewsDatabase newsDatabase = new NewsDatabase(this);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                SQLiteDatabase sqLiteDatabase = newsDatabase.getWritableDatabase();
//                Cursor cursor = sqLiteDatabase.query(DatabaseContract.NEWS_TABLE,null,null,null,null,null,null);
//                cursor.moveToFirst();
//                Log.d("Total count ","in table is " + cursor.getCount());
//                Log.d("Cursor",cursor.getString(cursor.getColumnIndex(DatabaseContract.TITLE_COLUMN)));
//                cursor.close();
//                sqLiteDatabase.close();
//
//
//                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
//                startActivity(intent);
//            }
//        };
//
//        Thread thread = new Thread(runnable);
//        thread.start();

        Log.d("Thread","started");

//        finish();
    }

}
