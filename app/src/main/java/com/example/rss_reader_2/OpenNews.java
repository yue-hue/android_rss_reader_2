package com.example.rss_reader_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class OpenNews extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_news);

        Intent intent = getIntent();
        String link = intent.getStringExtra("link");

        WebView webView = (WebView)findViewById(R.id.webView);
        webView.loadUrl(link);
    }
}