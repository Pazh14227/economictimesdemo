package com.example.malai_pt1882.economictimesdemo.detailednews;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.malai_pt1882.economictimesdemo.Database.DatabaseContract;
import com.example.malai_pt1882.economictimesdemo.R;
import com.example.malai_pt1882.economictimesdemo.tab_fragments.MyFragment;
import com.example.malai_pt1882.economictimesdemo.web_view.WebActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class DetailedNewsFragment extends MyFragment {

    @Override
    public void visibleToUser() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final Bundle bundle = getArguments();

        View layout = inflater.inflate(R.layout.detailed_news, container, false);

        TextView title = layout.findViewById(R.id.title_text);
        TextView source = layout.findViewById(R.id.source_and_date_text);
        ImageView imageView = layout.findViewById(R.id.detailed_image);
        TextView description = layout.findViewById(R.id.description_text);
        Button readArticle = layout.findViewById(R.id.read_article_button);

        readArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bundle != null) {
                    if (bundle.getString(DatabaseContract.URL_COLUMN) != null) {

                        Intent intent = new Intent(getContext(), WebActivity.class);

                        intent.putExtra(WebActivity.URL_KEY, bundle.getString(DatabaseContract.URL_COLUMN));
                        startActivity(intent);
                    } else {
                        Toast.makeText(getContext(), "Url not specified by provider", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Data not available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (bundle != null) {
            title.setText(bundle.getString(DatabaseContract.TITLE_COLUMN));

            String text = bundle.getString(DatabaseContract.SOURCE_NAME_COLUMN) + " | " + bundle.getString(DatabaseContract.PUBLISHED_AT_COLUMN);
            source.setText(text);
            description.setText(bundle.getString(DatabaseContract.DESCRIPTION_COLUMN));

            LargeImageCache largeImageCache = LargeImageCache.getINSTANCE();

            if (largeImageCache.getBitmap(bundle.getString(DatabaseContract.URL_TO_IMAGE_COLUMN)) != null) {

                imageView.setImageBitmap(largeImageCache.getBitmap(bundle.getString(DatabaseContract.URL_TO_IMAGE_COLUMN)));

            } else {

                LargeImageDownload largeImageDownload = new LargeImageDownload(bundle.getString(DatabaseContract.URL_TO_IMAGE_COLUMN));

                largeImageDownload.execute();
            }
        }
        return layout;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    private static class LargeImageDownload extends AsyncTask<Void, Integer, Bitmap> {

        private final String url;

        LargeImageDownload(String url) {
            this.url = url;
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            Bitmap image = null;

            Log.d("doInBackground", "Downloading Image");
            try {
                URL imageUrl = new URL(url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) imageUrl.openConnection();
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setReadTimeout(5000);

                InputStream inputStream = httpURLConnection.getInputStream();

                image = BitmapFactory.decodeStream(inputStream);

                if (image != null) {
                    LargeImageCache.getINSTANCE().addBitmap(url, image);
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

            if (DetailedNews.detailedNewsAdapter != null && bitmap != null) {
                DetailedNews.detailedNewsAdapter.notifyDataSetChanged();
            }
        }


    }
}
