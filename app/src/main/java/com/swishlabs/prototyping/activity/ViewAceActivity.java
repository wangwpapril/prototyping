package com.swishlabs.prototyping.activity;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.swishlabs.prototyping.MyApplication;
import com.swishlabs.prototyping.R;

public class ViewAceActivity extends ActionBarActivity implements View.OnClickListener {

    WebView webView;
    ImageView backViewIv;

    public static ViewAceActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        instance = this;
        setContentView(R.layout.activity_view_ace);

        webView = (WebView) findViewById(R.id.ace_view);

        backViewIv = (ImageView) findViewById(R.id.title_back);
        backViewIv.setOnClickListener(this);

        initialWebView();

        webView.loadUrl("https://www.aceworldview.com/WVEnt/WorldView/ADLogin");

    }

    public void initialWebView(){
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);

        int screenDensity = getResources().getDisplayMetrics().densityDpi;
        WebSettings.ZoomDensity zoomDensity = WebSettings.ZoomDensity.MEDIUM;
        switch (screenDensity) {
            case DisplayMetrics.DENSITY_LOW:
                zoomDensity = WebSettings.ZoomDensity.CLOSE;
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                zoomDensity = WebSettings.ZoomDensity.MEDIUM;
                break;
            case DisplayMetrics.DENSITY_HIGH:
                zoomDensity = WebSettings.ZoomDensity.FAR;
                break;
        }
        webView.getSettings().setDefaultZoom(zoomDensity);
        webView.getSettings().setTextZoom(120);

        webView.setBackgroundColor(Color.LTGRAY);

        webView.setWebViewClient( new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }


        });


    }

/*    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event){
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode,event);
    }
*/

    @Override
    public void onClick(View v) {
        if(v == backViewIv){

            onBackPressed();
            this.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
    }
}
