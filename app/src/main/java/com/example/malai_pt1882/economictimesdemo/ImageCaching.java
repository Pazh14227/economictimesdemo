package com.example.malai_pt1882.economictimesdemo;

import android.graphics.Bitmap;
import android.media.Image;
import android.util.LruCache;

public class ImageCaching {

    private static ImageCaching instance;


    /**
     * Private constructor is created because only one instance of the method should be used
     */
    private ImageCaching(){

    }

    public static ImageCaching getINSTANCE() {
        if (instance == null) {
        instance = new ImageCaching();
    }
        return instance;
    }

    private LruCache<String, Bitmap> bitmapCache = new LruCache<>(1000);

    public void addBitmap(String url, Bitmap bitmap) {
        bitmapCache.put(url, bitmap);
    }

    public Bitmap getBitmap(String url) {
        return bitmapCache.get(url);
    }
}