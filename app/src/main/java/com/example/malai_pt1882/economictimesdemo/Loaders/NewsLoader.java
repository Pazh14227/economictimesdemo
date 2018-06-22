package com.example.malai_pt1882.economictimesdemo.Loaders;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.example.malai_pt1882.economictimesdemo.APIContract;
import com.example.malai_pt1882.economictimesdemo.Database.DatabaseContract;
import com.example.malai_pt1882.economictimesdemo.Database.NewsDatabase;
import com.example.malai_pt1882.economictimesdemo.MainActivity;
import com.example.malai_pt1882.economictimesdemo.tabs.Tab;
import com.example.malai_pt1882.economictimesdemo.tabs.TabsManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


import javax.net.ssl.HttpsURLConnection;

public class NewsLoader extends AsyncTaskLoader<ArrayList<Bundle>> {

    private final String jsonUrl;
    private final TabsManager tabsManager;

    public int downloadStatus;
    public static final int MALFORMED_URL_EXCEPTION = 1;
    public static final int IO_EXCEPTION = 2;
    public static final int JSON_EXCEPTION = 3;
    public static final int DOWNLOAD_SUCCESSFUL = 0;

    private int checked = 0;

    NewsLoader(TabsManager tabsManager) {
        super(tabsManager.getContext());
        jsonUrl = tabsManager.getJsonUrl();
        this.tabsManager = tabsManager;
    }

    @Nullable
    @Override
    public ArrayList<Bundle> loadInBackground() {

        Cursor cursor = null;

        SQLiteDatabase sqLiteDatabase = null;

        try {
            String jsonData = getJsonDataFromUrl(getUrlFromString(jsonUrl));

            JSONParser jsonParser = new JSONParser(jsonData);
            jsonParser.parseJson();


            NewsDatabase newsDatabase = MainActivity.newsDatabase;
            sqLiteDatabase = newsDatabase.getWritableDatabase();


            insertNewArticlesIntoDatabase(sqLiteDatabase, jsonParser, tabsManager);

            cursor = getRecordsOfTab(tabsManager.getTab(), sqLiteDatabase);

            ArrayList<Bundle> newsData = storeDataInLocalTemp(cursor);

            downloadStatus = DOWNLOAD_SUCCESSFUL;

            return newsData;

        } catch (MalformedURLException e) {
            downloadStatus = MALFORMED_URL_EXCEPTION;
//            Toast.makeText(getContext(),"Malformed URL Exception caught",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            downloadStatus = IO_EXCEPTION;
//            Toast.makeText(getContext(),"IOException caught",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (JSONException e) {
            downloadStatus = JSON_EXCEPTION;
//            Toast.makeText(getContext(),"JSONException caught",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return null;
    }

    public static class JSONParser {
        String[] sourceName;
        String[] authourName;
        public String[] title;
        public String[] description;
        String[] url;
        String[] urlToImage;
        String[] publishedAt;
        private String jsonData;

        private int size;

        JSONParser(String jsonData) {
            this.jsonData = jsonData;
        }

        void parseJson() throws JSONException {
            JSONObject jsonObject = new JSONObject(jsonData);

            size = (jsonObject.getInt(APIContract.TOTAL_RESULTS_KEYWORD) < DatabaseContract.MAX_NEWS_PER_TAB) ? jsonObject.getInt(APIContract.TOTAL_RESULTS_KEYWORD) : DatabaseContract.MAX_NEWS_PER_TAB;

            sourceName = new String[size];
            authourName = new String[size];
            title = new String[size];
            description = new String[size];
            url = new String[size];
            urlToImage = new String[size];
            publishedAt = new String[size];


            JSONArray jsonArray = jsonObject.getJSONArray(APIContract.ARTICLE_KEYWORD);

            for (int i = 0; i < size; i++) {
                sourceName[i] = jsonArray.getJSONObject(i).getJSONObject(APIContract.ARTICLE_SOURCE_KEYWORD).getString(APIContract.ARTICLE_SOURCE_NAME_KEYWORD);
                authourName[i] = jsonArray.getJSONObject(i).getString(APIContract.ARTICLE_AUTHOR_KEYWORD);
                title[i] = jsonArray.getJSONObject(i).getString(APIContract.ARTICLE_TITLE_KEYWORD);
                description[i] = jsonArray.getJSONObject(i).getString(APIContract.ARTICLE_DESCRIPTION_KEYWORD);
                url[i] = jsonArray.getJSONObject(i).getString(APIContract.ARTICLE_URL_KEYWORD);
                urlToImage[i] = jsonArray.getJSONObject(i).getString(APIContract.ARTICLE_URL_TO_IMAGE_KEYWORD);
                publishedAt[i] = jsonArray.getJSONObject(i).getString(APIContract.ARTICLE_PUBLISHED_AT_KEYWORD);
            }
        }
    }


    public static class NewsData {

        private ArrayList<Bundle> arrayList;

        NewsData() {
            arrayList = new ArrayList<>();
        }

        void parseCursor(Cursor cursor) {

            cursor.moveToFirst();

            if (cursor.getCount() > 0) {
                for (int pos = 0; !cursor.isAfterLast(); pos++, cursor.moveToNext()) {

                    Bundle bundle = new Bundle();

                    bundle.putString(DatabaseContract.SOURCE_NAME_COLUMN, cursor.getString(cursor.getColumnIndex(DatabaseContract.SOURCE_NAME_COLUMN)));
                    bundle.putString(DatabaseContract.AUTHOUR_NAME_COLUMN, cursor.getString(cursor.getColumnIndex(DatabaseContract.AUTHOUR_NAME_COLUMN)));
                    bundle.putString(DatabaseContract.TITLE_COLUMN, cursor.getString(cursor.getColumnIndex(DatabaseContract.TITLE_COLUMN)));
                    bundle.putString(DatabaseContract.DESCRIPTION_COLUMN, cursor.getString(cursor.getColumnIndex(DatabaseContract.DESCRIPTION_COLUMN)));
                    bundle.putString(DatabaseContract.URL_COLUMN, cursor.getString(cursor.getColumnIndex(DatabaseContract.URL_COLUMN)));
                    bundle.putString(DatabaseContract.URL_TO_IMAGE_COLUMN, cursor.getString(cursor.getColumnIndex(DatabaseContract.URL_TO_IMAGE_COLUMN)));
                    bundle.putString(DatabaseContract.PUBLISHED_AT_COLUMN, cursor.getString(cursor.getColumnIndex(DatabaseContract.PUBLISHED_AT_COLUMN)));
                    bundle.putBoolean(DatabaseContract.FAVORITE_COLUMN,cursor.getInt(cursor.getColumnIndex(DatabaseContract.FAVORITE_COLUMN)) == 1);

                    arrayList.add(bundle);
                }

            }
        }

        ArrayList<Bundle> getNews() {
            return arrayList;
        }
    }


    private String getJsonDataFromUrl(URL url) throws IOException {
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
        httpsURLConnection.setConnectTimeout(5000);
        httpsURLConnection.setReadTimeout(5000);
        InputStream inputStream = httpsURLConnection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        StringBuilder JSON = new StringBuilder();

        while (line != null) {
            line = bufferedReader.readLine();
            JSON.append(line);
        }

        httpsURLConnection.disconnect();

        return JSON.toString();
    }

    private URL getUrlFromString(String jsonUrl) throws MalformedURLException {
        return new URL(jsonUrl);
    }


    private void insertNewArticlesIntoDatabase(SQLiteDatabase sqLiteDatabase, JSONParser jsonParser, TabsManager tabManager) {
        int tabPosition = tabManager.getPositionForTab(tabManager.getTab());
        String tabColumnName = DatabaseContract.getColumnName(tabManager.getTab());


        int toBeInsertedAtPosition = 0;

        Log.d("Total size is ", jsonParser.size + " ");
        for (int pos = 0; pos < jsonParser.size; pos++, toBeInsertedAtPosition++) {

            String selection = DatabaseContract.URL_COLUMN + " = ? OR " + DatabaseContract.TITLE_COLUMN + " = ?";
            String selectionArgs[] = {jsonParser.url[pos],jsonParser.title[pos]};


            String allRecordsSelection = tabColumnName + " >= 0";

            Cursor allRecords = sqLiteDatabase.query(DatabaseContract.NEWS_TABLE, null, allRecordsSelection, null, null, null, tabColumnName);
            Cursor cursor = sqLiteDatabase.query(DatabaseContract.NEWS_TABLE, new String[]{tabColumnName, DatabaseContract.URL_COLUMN}, selection, selectionArgs, null, null, null);

            String[] columns = allRecords.getColumnNames();
            int i=0;
            while(i<columns.length){
                Log.d("Column Name is",columns[i]);
                i++;
            }
            Log.d("Json parser url is ", jsonParser.url[pos]);

            if (cursor.getCount() == 0) {
                if (allRecords.getCount() > 0) {
                    Log.d("AllRecords", "Count is " + allRecords.getCount());
                    sqLiteDatabase.execSQL("UPDATE " + DatabaseContract.NEWS_TABLE + " SET " + tabColumnName + " = " + tabColumnName + " + 1 " + "WHERE " + tabColumnName + " >= " + toBeInsertedAtPosition);
                }

                Log.d("for tab ", tabColumnName);
                insertData(sqLiteDatabase, DatabaseContract.NEWS_TABLE, jsonParser.sourceName[pos], jsonParser.authourName[pos], jsonParser.title[pos], jsonParser.description[pos], jsonParser.url[pos], jsonParser.urlToImage[pos], jsonParser.publishedAt[pos],DatabaseContract.DEFAULT_FAVORITE_VALUE, getPositionForTabColumns(toBeInsertedAtPosition, tabPosition));


                Log.d("Fresh data ", "Inserted : " + jsonParser.title[pos]);
            } else {

                cursor.moveToFirst();
                Log.d("unique identifier ", cursor.getString(cursor.getColumnIndex(DatabaseContract.URL_COLUMN)));

                if (cursor.getCount() > 1) {
                    Log.e("Caught twice ", "Data : " + jsonParser.title[pos]);
                    Log.e("cursor", "count is " + cursor.getCount());
                } else {
                    Log.d("Aldready Present in Tab", " Data : " + jsonParser.title[pos]);
                    Log.d("cursor", "count is " + cursor.getCount());
                }

                ContentValues contentValues = new ContentValues();
                int existingPos = cursor.getInt(cursor.getColumnIndex(tabColumnName));

                if (existingPos > toBeInsertedAtPosition) {

                    int toPosition = cursor.getInt(cursor.getColumnIndex(tabColumnName)) - 1;
                    sqLiteDatabase.execSQL("UPDATE " + DatabaseContract.NEWS_TABLE + " SET " + tabColumnName + " = " + tabColumnName + " + 1 " + " WHERE " + tabColumnName + " BETWEEN " + toBeInsertedAtPosition + " AND " + toPosition);

                    contentValues.put(tabColumnName, toBeInsertedAtPosition);
                    sqLiteDatabase.update(DatabaseContract.NEWS_TABLE, contentValues, selection, selectionArgs);

                    Log.d("Present Later in tab", " Data : " + jsonParser.title[pos]);

                } else if (existingPos == DatabaseContract.DEFAULT_VALUE_FOR_TAB_COLUMNS) {

                    Log.d("Fresh for this tab ", "Data : " + jsonParser.title[pos]);
                    contentValues.put(tabColumnName, toBeInsertedAtPosition);
                    sqLiteDatabase.update(DatabaseContract.NEWS_TABLE, contentValues, selection, selectionArgs);
                }

            }

            cursor.close();
            allRecords.close();

        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(tabColumnName,DatabaseContract.DEFAULT_VALUE_FOR_TAB_COLUMNS);

        String whereClause = tabColumnName + " > " + DatabaseContract.MAX_NEWS_PER_TAB;
        Log.d("UpdatedRown is ",sqLiteDatabase.update(DatabaseContract.NEWS_TABLE,contentValues,whereClause,null) + " ");

        int rowsDeleted = deleteUnwantedNewsData(sqLiteDatabase);
        Log.d("RowsDeleted", " is " + rowsDeleted);

    }


    private int[] getPositionForTabColumns(int toBeInsertedAt, int tabPosition) {
        int[] positions = new int[DatabaseContract.NUMBER_OF_TAB_COLUMNS];
        for (int i = 0; i < DatabaseContract.NUMBER_OF_TAB_COLUMNS; i++) {
            positions[i] = DatabaseContract.DEFAULT_VALUE_FOR_TAB_COLUMNS;
        }
        positions[tabPosition] = toBeInsertedAt;
        Log.d("Column Value", DatabaseContract.getColumnName(tabPosition) + toBeInsertedAt);
        return positions;
    }


    private boolean insertData(SQLiteDatabase db, String tableName, String sourceName, String authourName, String title, String description, String url, String urlToImage, String publishedAt,int favorite, int[] valuesForTabColumns) {
        try {

            int pos;

            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseContract.SOURCE_NAME_COLUMN, sourceName);
            contentValues.put(DatabaseContract.AUTHOUR_NAME_COLUMN, authourName);
            contentValues.put(DatabaseContract.TITLE_COLUMN, title);
            contentValues.put(DatabaseContract.DESCRIPTION_COLUMN, description);
            contentValues.put(DatabaseContract.URL_COLUMN, url);
            contentValues.put(DatabaseContract.URL_TO_IMAGE_COLUMN, urlToImage);
            contentValues.put(DatabaseContract.PUBLISHED_AT_COLUMN, publishedAt);

            if(checked == 0){
                contentValues.put(DatabaseContract.FAVORITE_COLUMN,favorite);
                checked = 1;
            } else if(checked == 1){
                contentValues.put(DatabaseContract.FAVORITE_COLUMN,1);
                checked=0;


            }

            for (pos = 0; pos < DatabaseContract.NUMBER_OF_TAB_COLUMNS; pos++) {
                contentValues.put(DatabaseContract.getColumnName(pos), valuesForTabColumns[pos]);
            }

            db.insert(tableName, null, contentValues);

        } catch (SQLException s) {

            Log.d("NewsDatabase ", "SQLException caught");

            return false;
        }
        return true;
    }


    private int deleteUnwantedNewsData(SQLiteDatabase sqLiteDatabase) {
        StringBuilder whereClause = new StringBuilder();
        int pos;
        whereClause.append(" ( ");
        for (pos = 0; pos < DatabaseContract.NUMBER_OF_TAB_COLUMNS; pos++) {
            whereClause.append(" ( ");
            whereClause.append(DatabaseContract.getColumnName(pos));
            whereClause.append(" = ");
            whereClause.append(DatabaseContract.DEFAULT_VALUE_FOR_TAB_COLUMNS);
            whereClause.append(" OR ");
            whereClause.append(DatabaseContract.getColumnName(pos));
            whereClause.append(" >= ");
            whereClause.append(DatabaseContract.MAX_NEWS_PER_TAB);
            whereClause.append(" ) ");
            whereClause.append(" AND ");
        }


        whereClause.append(" ( ");
        whereClause.append(DatabaseContract.FAVORITE_COLUMN);
        whereClause.append(" != ");
        whereClause.append(1);
        whereClause.append(" ) ");
        whereClause.append(" ) ");

        Log.d("WhereClause is", whereClause.toString());

        return sqLiteDatabase.delete(DatabaseContract.NEWS_TABLE, whereClause.toString(), null);
    }

    private Cursor getRecordsOfTab(Tab tab, SQLiteDatabase sqLiteDatabase) {
        return sqLiteDatabase.query(DatabaseContract.NEWS_TABLE, null, DatabaseContract.getColumnName(tab) + " >= 0 ", null, null, null, DatabaseContract.getColumnName(tab));
    }


    private ArrayList<Bundle> storeDataInLocalTemp(Cursor cursor) {
        NewsData newsData = new NewsData();

        Log.d("cursor size ", cursor.getCount() + " ");

        newsData.parseCursor(cursor);
        return newsData.getNews();
    }

}
