package com.suken.bridgedetection.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.suken.bridgedetection.BridgeDetectionApplication;
import com.suken.bridgedetection.R;
import com.suken.bridgedetection.http.HttpTask;

public class WebViewActivity extends Activity implements View.OnClickListener {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_layout);
        init();

    }


    private void init() {
        webView = (WebView) findViewById(R.id.webview);

        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.back).setEnabled(false);

        findViewById(R.id.forward).setOnClickListener(this);

        findViewById(R.id.forward).setEnabled(false);


        findViewById(R.id.refresh).setOnClickListener(this);

        //WebView加载web资源
        webView.loadUrl(HttpTask.getUrl("/sso/m/brgChk/listTree.ht") + "?userId=" + BridgeDetectionApplication.mCurrentUser.getUserId() + "&token=" + BridgeDetectionApplication.mCurrentUser.getToken());
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                findViewById(R.id.back).setEnabled(webView.canGoBack());
                findViewById(R.id.forward).setEnabled(webView.canGoForward());
            }
        });
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.back) {
            if(webView.canGoBack()){
                webView.goBack();
            }
        } else  if(view.getId() == R.id.forward){
            if(webView.canGoForward()){
                webView.goForward();
            }
        } else {
            webView.reload();
        }

    }
}