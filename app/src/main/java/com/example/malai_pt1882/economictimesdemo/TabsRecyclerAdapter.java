package com.example.malai_pt1882.economictimesdemo;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.malai_pt1882.economictimesdemo.Database.DatabaseContract;
import com.example.malai_pt1882.economictimesdemo.detailednews.DetailedNews;

import java.util.ArrayList;
import java.util.HashMap;


public class TabsRecyclerAdapter extends RecyclerView.Adapter<TabsRecyclerAdapter.ViewHolder> {

    private ArrayList<Bundle> bundles;
    private ImageCaching imageCaching;
    private static HashMap<String, Boolean> isSentToDownload;
    private ImageDownloadManager imageDownloadManager;
    private Context context;

    public TabsRecyclerAdapter(ArrayList<Bundle> bundles, Context context) {
        Log.d("In TabsRecyclerAdapter", "Constructor created");
        this.bundles = bundles;
        this.imageCaching = ImageCaching.getINSTANCE();
        isSentToDownload = new HashMap<>();
        imageDownloadManager = new ImageDownloadManager(this);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.tab_content_layout, parent, false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        if (bundles != null && bundles.size() > 0 && position < bundles.size()) {

            holder.titleTextView.setText(bundles.get(position).getString(DatabaseContract.TITLE_COLUMN));
            holder.divider.setVisibility(View.VISIBLE);
            holder.newsImageView.setImageResource(R.drawable.placeholder);


            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailedNews.class);
                    intent.putParcelableArrayListExtra(DetailedNews.DATA_KEY, bundles);
                    intent.putExtra(DetailedNews.POSITION_KEY, holder.getAdapterPosition());
                    context.startActivity(intent);
                }
            });

            final String imageUrl = bundles.get(position).getString(DatabaseContract.URL_TO_IMAGE_COLUMN);

            if (imageUrl!=null && !imageUrl.equalsIgnoreCase("null")) {

                if (imageCaching.getBitmap(imageUrl) != null) {

                    holder.newsImageView.setImageBitmap(imageCaching.getBitmap(imageUrl));
                    holder.progressBar.setVisibility(View.GONE);

                } else if(isSentToDownload.get(imageUrl) == null || !isSentToDownload.get(imageUrl)) {

                    Log.d("onBindViewHolder",imageUrl);
//                    imageDownloadManager.enqueueUrl(imageUrl, position);
//
//                    if (!imageDownloadManager.isStarted) {
//                        imageDownloadManager.startLoading();
//                    }

                     holder.progressBar.setVisibility(View.VISIBLE);
                     SecondaryDownloadImage secondaryDownloadImage = new SecondaryDownloadImage(imageUrl,this,position,holder.progressBar);
                     secondaryDownloadImage.execute();
                     isSentToDownload.put(imageUrl,true);

                }
            }
        } else{
            holder.progressBar.setVisibility(View.VISIBLE);
        }
    }


//    public void dequeTask() {
//        imageDownloadManager.dequeueData();
//    }

    @Override
    public int getItemCount() {

        if(bundles!=null && bundles.size()>0){
            return (bundles.size() > DatabaseContract.MAX_NEWS_PER_TAB)?DatabaseContract.MAX_NEWS_PER_TAB:bundles.size();
        }

        return 0;
    }

    public void swapCursor(ArrayList<Bundle> bundles) {

        if (bundles == null) {
            return;
        }

        this.bundles = bundles;
        Log.d("Size of news is ", bundles.size() + " ");
        notifyDataSetChanged();

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView;
        private ImageView newsImageView;
        private RelativeLayout relativeLayout;
        private View divider;
        private ProgressBar progressBar;


        ViewHolder(View holderLayout) {
            super(holderLayout);

            relativeLayout = holderLayout.findViewById(R.id.relative_layout_id);
            titleTextView = holderLayout.findViewById(R.id.title_text_id);
            newsImageView = holderLayout.findViewById(R.id.relative_image_id);
            divider = holderLayout.findViewById(R.id.divider);
            divider.setBackgroundColor(Color.argb(12, 0, 0, 0));
            titleTextView.setTextColor(ColorStateList.valueOf(Color.DKGRAY));
            progressBar = holderLayout.findViewById(R.id.progress_image);

        }
    }
}