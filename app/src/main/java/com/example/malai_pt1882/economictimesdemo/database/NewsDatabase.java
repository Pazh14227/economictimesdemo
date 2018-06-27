package com.example.malai_pt1882.economictimesdemo.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NewsDatabase extends SQLiteOpenHelper {

    private static final String TAB_COLUMNS_NAME_AND_TYPE = getTabColumnsNameAndType();

    private static final String CREATE_NEWS_TABLE = "CREATE TABLE " +
            DatabaseContract.NEWS_TABLE + "( " +
            DatabaseContract.SOURCE_NAME_COLUMN + " VarChar(255), " +
            DatabaseContract.AUTHOUR_NAME_COLUMN + " VarChar(255), " +
            DatabaseContract.TITLE_COLUMN + " VarChar(255), " +
            DatabaseContract.DESCRIPTION_COLUMN + " Text, " +
            DatabaseContract.URL_COLUMN + " Text, " +
            DatabaseContract.URL_TO_IMAGE_COLUMN + " Text, " +
            DatabaseContract.PUBLISHED_AT_COLUMN + " VarChar(255), " +
            DatabaseContract.FAVORITE_COLUMN + " Int, " +
            TAB_COLUMNS_NAME_AND_TYPE;

    private static final String DROP_NEWS_TABLE = "DROP TABLE " + DatabaseContract.NEWS_TABLE;
    private static final int VERSION = 1;


    private static NewsDatabase newsDatabase = null;

    public NewsDatabase(Context context) {
        super(context, DatabaseContract.DB_NAME, null, VERSION);
    }

    public static NewsDatabase getInstance(Context context){
        if(newsDatabase == null){
            newsDatabase = new NewsDatabase(context);
            return newsDatabase;
        }

        return newsDatabase;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("Created","database");

        db.execSQL(CREATE_NEWS_TABLE);

        Log.d("InDatabaseHelper","Created table");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if(oldVersion == 1){
            db.execSQL(DROP_NEWS_TABLE);
        } else if(oldVersion == 2) {
            db.execSQL(DROP_NEWS_TABLE);
        }

        onCreate(db);
    }



    private static String getTabColumnsNameAndType() {
        StringBuilder result = new StringBuilder();
        int pos;
        for (pos = 0; pos < DatabaseContract.NUMBER_OF_TAB_COLUMNS -1; pos++) {
            String columnName = DatabaseContract.getColumnName(pos);

            result.append(columnName);
            result.append(" INTEGER, ");

            Log.d("ColumnName",columnName);

        }

        result.append(DatabaseContract.getColumnName(pos));
        Log.d("append",DatabaseContract.getColumnName(pos));
        result.append(" INTEGER)");

        Log.d("result",result.toString());
        return result.toString();
    }

}