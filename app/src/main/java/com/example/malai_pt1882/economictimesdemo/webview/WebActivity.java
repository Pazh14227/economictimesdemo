package com.example.malai_pt1882.economictimesdemo.web_view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.malai_pt1882.economictimesdemo.R;
import com.example.malai_pt1882.economictimesdemo.ToolbarPref.ToolbarPreferences;

public class WebActivity extends AppCompatActivity {

    public static final String URL_KEY = "myURl";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        WebView webView = findViewById(R.id.web_view);
        final ProgressBar progressBar = findViewById(R.id.web_loading);


        setSupportActionBar((Toolbar)findViewById(R.id.web_toolbar));

        if(getSupportActionBar()!=null) {
            ToolbarPreferences.setToolbarPreferences(getSupportActionBar());
        }

        Intent intent = getIntent();
        String url = intent.getStringExtra(URL_KEY);

        Log.d("web url is",url);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                progressBar.setVisibility(View.GONE);
            }
        });


        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

                progressBar.setProgress(newProgress);
                if(newProgress == 100){
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl(url);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

//        finish();
    }
}
