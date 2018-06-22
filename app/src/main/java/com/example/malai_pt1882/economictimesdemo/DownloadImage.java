package com.example.malai_pt1882.economictimesdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Stack;

public class DownloadImage extends AsyncTask<Void, Integer, Bitmap> {

    private ImageCaching imageCaching;
    private TabsRecyclerAdapter tabsRecyclerAdapter;
    private ImageDownloadManager imageDownloadManager;
    private ImageDownloadManager.MyStruct myStruct;

    DownloadImage(TabsRecyclerAdapter tabsRecyclerAdapter, ImageDownloadManager imageDownloadManager, ImageDownloadManager.MyStruct myStruct) {
        imageCaching = ImageCaching.getINSTANCE();
        this.tabsRecyclerAdapter = tabsRecyclerAdapter;
        this.imageDownloadManager = imageDownloadManager;
        this.myStruct = myStruct;
    }


    @Override
    protected Bitmap doInBackground(Void... s) {

        Bitmap image = null;

        try {

            Log.d("Image url is " , myStruct.url);

            URL url = new URL(myStruct.url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(3000);
            httpURLConnection.setReadTimeout(3000);

            InputStream inputStream = httpURLConnection.getInputStream();

            Log.d("Timeout is ",httpURLConnection.getReadTimeout() + " ");
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;

            image = BitmapFactory.decodeStream(inputStream,null,options);

            if (image != null) {
                imageCaching.addBitmap(myStruct.url, image);
            }

            inputStream.close();
            httpURLConnection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        if (bitmap != null) {
            tabsRecyclerAdapter.notifyItemChanged(myStruct.position);
        }

        Log.d("DownloadImage","onPostExecute image may be downloaded");

        imageDownloadManager.finishedTask();
    }


}

