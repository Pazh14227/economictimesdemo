package com.example.malai_pt1882.economictimesdemo;

import android.graphics.Bitmap;
import android.util.LruCache;

public class ImageCaching {

    private static ImageCaching instance = null;

    private LruCache<String, Bitmap> bitmapCache;


    /**
     * Private constructor is created because only one instance of the method should be used
     */
    private ImageCaching(int memory) {

        bitmapCache = new LruCache<>(memory);

    }

    public static void createInstance(int memory) {

        if (instance == null) {
            instance = new ImageCaching(memory);
        }

    }

    public static ImageCaching getInstance() {
        return instance;
    }

    public void addBitmap(String url, Bitmap bitmap) {
        bitmapCache.put(url, bitmap);
    }

    public Bitmap getBitmap(String url) {
        return bitmapCache.get(url);
    }
}