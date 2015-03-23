package com.inftel.isn.request;

import android.os.AsyncTask;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by José on 23/03/2015.
 */
public class ShowYoutuveVideo extends AsyncTask<String, Void, String> {
        private WebView mWebView;

        public ShowYoutuveVideo(WebView mWebView)
        {
            this.mWebView = mWebView;
        }

        @Override
        protected String doInBackground(String... urls) {

            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
            mWebView.loadUrl("http://www.youtube.com/embed/" + urls[0] + "?autoplay=1&vq=small");


            return null;
        }


        @Override

        protected void onPostExecute(String result) {
            mWebView.setWebChromeClient(new WebChromeClient());
        }
    }

