package com.example.malai_pt1882.economictimesdemo.detailednews;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.example.malai_pt1882.economictimesdemo.ImageCaching;

public class LargeImageCache {

    private static LargeImageCache instance;


    /**
     * Private constructor is created because only one instance of the method should be used
     */
    private LargeImageCache(){

    }

    public static LargeImageCache getINSTANCE() {
        if (instance == null) {
            instance = new LargeImageCache();
        }
        return instance;
    }

    private LruCache<String, Bitmap> bitmapCache = new LruCache<>(6);

    public void addBitmap(String url, Bitmap bitmap) {
        bitmapCache.put(url, bitmap);

        Log.d("addBitmap","inserted image");
    }

    public Bitmap getBitmap(String url) {
        return bitmapCache.get(url);
    }
}
