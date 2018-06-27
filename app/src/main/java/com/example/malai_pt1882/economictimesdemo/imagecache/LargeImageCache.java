package com.example.malai_pt1882.economictimesdemo.detailednews;

import android.graphics.Bitmap;
import android.util.LruCache;

class LargeImageCache {

    private static LargeImageCache instance = null;

    private LruCache<String, Bitmap> bitmapCache;

    /**
     * Private constructor is created because only one instance of the method should be used
     */
    private LargeImageCache(int memory){
     bitmapCache = new LruCache<>(memory);
    }

    protected static LargeImageCache getINSTANCE(int memory) {
        if (instance == null) {
            instance = new LargeImageCache(memory);
        }
        return instance;
    }


    protected void addBitmap(String url, Bitmap bitmap) {
        bitmapCache.put(url, bitmap);
    }

    protected Bitmap getBitmap(String url) {
        return bitmapCache.get(url);
    }
}
