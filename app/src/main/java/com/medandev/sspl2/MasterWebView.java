package com.medandev.sspl2;

import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class MasterWebView extends WebViewClient {

    private WebView webView;
    public ProgressBar progressBarT4;


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        view.getContext().startActivity(intent);
        return true;
    }

}