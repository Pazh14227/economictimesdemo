package com.example.malai_pt1882.economictimesdemo;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Stack;

public class ImageDownloadManager {

    private TabsRecyclerAdapter tabsRecyclerAdapter;
    private Stack<MyStruct> stack;
    public boolean isStarted = false;

    ImageDownloadManager(TabsRecyclerAdapter tabsRecyclerAdapter) {
        this.tabsRecyclerAdapter = tabsRecyclerAdapter;
        stack = new Stack<>();
    }


    public void enqueueUrl(String url, int position) {
        if (url != null && !url.equalsIgnoreCase("null")) {

            Log.d("NullTest"," passed ");

            MyStruct myStruct = new MyStruct(url, position);

            if (stack.contains(myStruct)) {

                if(stack.remove(myStruct)){
                    stack.add(myStruct);
                    return;
                }

                return;
            }

            stack.add(myStruct);
        }

        Log.d("Size of stack is ", stack.size() + " ");
    }

    public void dequeueData() {
        stack.removeAllElements();
    }

    public void startLoading() {

        DownloadImage downloadImage = new DownloadImage(tabsRecyclerAdapter, this, stack.pop());
        Log.d("In startLoading", "called");

        downloadImage.execute();
        Log.d("Executed task", " ");
        isStarted = true;

    }

    public static class MyStruct implements Comparable {
        public String url;
        public Integer position;

        MyStruct(String string, Integer position) {
            this.position = position;
            this.url = string;
        }

        @Override
        public int compareTo(@NonNull Object o) {
            return 0;
        }
    }

    public void finishedTask() {
        if (stack.empty()) {
            isStarted = false;
        } else {
            DownloadImage downloadImage = new DownloadImage(tabsRecyclerAdapter, this, stack.pop());
            downloadImage.execute();
        }
    }

}
