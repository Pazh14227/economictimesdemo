package com.example.malai_pt1882.economictimesdemo;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.malai_pt1882.economictimesdemo.database.DatabaseContract;
import com.example.malai_pt1882.economictimesdemo.detailednews.DetailedNews;
import com.example.malai_pt1882.economictimesdemo.imagecache.ImageCache;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class TabAdapter extends RecyclerView.Adapter<TabAdapter.ViewHolder> {

    private ArrayList<Bundle> bundles;
    private ImageCache imageCache;
    private static HashMap<String, Boolean> isSentToDownload;
    private Context context;

    public TabAdapter(ArrayList<Bundle> bundles, Context context) {

        this.bundles = bundles;
        this.imageCache = ImageCache.getInstance();
        isSentToDownload = new HashMap<>();
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.tab_content_layout, parent, false);
        return new ViewHolder(layout);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final int adapterPosition = holder.getAdapterPosition();

        holder.relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.white));

        if (bundles != null && bundles.size() > 0 && position < bundles.size()) {

            displayView(holder, position);

            displayImageView(holder, position);

            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, DetailedNews.class);
                    intent.putParcelableArrayListExtra(DetailedNews.DATA_KEY, bundles);
                    intent.putExtra(DetailedNews.POSITION_KEY, adapterPosition);

                    context.startActivity(intent);
                }
            });

        }
    }


    @Override
    public int getItemCount() {

        if (bundles != null && bundles.size() > 0) {
            return (bundles.size() > DatabaseContract.MAX_NEWS_PER_TAB) ? DatabaseContract.MAX_NEWS_PER_TAB : bundles.size();
        }

        return 0;
    }

    public void swapCursor(ArrayList<Bundle> bundles) {

        if (bundles == null) {
            return;
        }

        this.bundles = bundles;
        notifyDataSetChanged();

    }


    private void displayView(ViewHolder holder, int position) {
        holder.titleTextView.setText(bundles.get(position).getString(DatabaseContract.TITLE_COLUMN));
        holder.divider.setVisibility(View.VISIBLE);
        holder.newsImageView.setImageResource(R.drawable.placeholder);
    }

    private void displayImageView(ViewHolder holder, int position) {

        final String imageUrl = bundles.get(position).getString(DatabaseContract.URL_TO_IMAGE_COLUMN);

        if (imageUrl != null && !imageUrl.equalsIgnoreCase("null")) {

            if (imageCache.getBitmap(imageUrl) != null) {

                holder.newsImageView.setImageBitmap(imageCache.getBitmap(imageUrl));

            } else if (isSentToDownload.get(imageUrl) == null || !isSentToDownload.get(imageUrl)) {

                ImageReader imageReader = new ImageReader(imageUrl, this, position);
                imageReader.execute();
                isSentToDownload.put(imageUrl, true);

            }
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView;
        private ImageView newsImageView;
        private RelativeLayout relativeLayout;
        private View divider;


        ViewHolder(View holderLayout) {
            super(holderLayout);

            relativeLayout = holderLayout.findViewById(R.id.relative_layout_id);
            titleTextView = holderLayout.findViewById(R.id.title_text_id);
            newsImageView = holderLayout.findViewById(R.id.relative_image_id);
            divider = holderLayout.findViewById(R.id.divider);
            divider.setBackgroundColor(Color.argb(12, 0, 0, 0));
            titleTextView.setTextColor(ColorStateList.valueOf(Color.DKGRAY));

        }
    }


    private static class ImageReader extends AsyncTask<Void, Integer, Bitmap> {

        private String url;
        private int position;
        private ImageCache imageCache;

        private WeakReference<TabAdapter> tabAdapterWeakReference;


        ImageReader(String url, TabAdapter adapter, int position) {

            this.url = url;
            this.position = position;
            imageCache = ImageCache.getInstance();

            tabAdapterWeakReference = new WeakReference<>(adapter);
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

                image = BitmapFactory.decodeStream(inputStream, null, options);


                if (image != null) {
                    imageCache.addBitmap(url, image);
                }

                httpURLConnection.disconnect();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return image;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            if (bitmap != null && tabAdapterWeakReference!=null && tabAdapterWeakReference.get()!=null) {

                tabAdapterWeakReference.get().notifyItemChanged(position);

            }
        }
    }

}