package com.example.malai_pt1882.economictimesdemo;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


import com.example.malai_pt1882.economictimesdemo.detailednews.DetailedNews;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class SecondaryDownloadImage extends AsyncTask<Void, Integer, Bitmap> {

    private String url;
    private TabsRecyclerAdapter adapter;
    private int position;
    private ImageCaching imageCaching;

    private WeakReference<ProgressBar> progressBarWeakReference;


    SecondaryDownloadImage(String url, TabsRecyclerAdapter adapter, int position,ProgressBar progressBar) {

        this.url = url;
        this.adapter = adapter;
        this.position = position;
        imageCaching = ImageCaching.getINSTANCE();
        progressBarWeakReference = new WeakReference<>(progressBar);

    }

    @Override
    protected Bitmap doInBackground(Void... voids) {

        Bitmap image = null;

        try {
            URL imageUrl = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) imageUrl.openConnection();
            httpURLConnection.setConnectTimeout(2500);
            httpURLConnection.setReadTimeout(2000);

            InputStream inputStream = httpURLConnection.getInputStream();

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;

            publishProgress(70);

            image = BitmapFactory.decodeStream(inputStream,null,options);

//            publishProgress(70);

            if (image != null) {
                imageCaching.addBitmap(url, image);
            }

            httpURLConnection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        if(progressBarWeakReference!=null && progressBarWeakReference.get()!=null) {
            progressBarWeakReference.get().setProgress(values[0]);
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        if(bitmap!=null){

            adapter.notifyItemChanged(position);
            Log.d("Notified adapter ", "true ");

            if(progressBarWeakReference!=null && progressBarWeakReference.get()!=null) {
                progressBarWeakReference.get().setVisibility(View.GONE);
            }

        }
    }
}
