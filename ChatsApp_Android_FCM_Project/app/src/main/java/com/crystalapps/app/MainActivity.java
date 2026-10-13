package com.crystalapps.app;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.content.Intent;
import android.net.Uri;
import android.view.WindowInsets;

import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

public class MainActivity extends Activity {

    private WebView webView;

    private static final String APP_URL =
            "https://chatsapp-online.pages.dev/";

    private static final String APP_HOST =
            "chatsapp-online.pages.dev";

    public class ChatsAppBridge {

        @JavascriptInterface
        public void requestFcmToken() {

            FirebaseMessaging.getInstance()
                .getToken()
                .addOnSuccessListener(token -> {

                    getSharedPreferences(
                        "fcm",
                        MODE_PRIVATE
                    ).edit()
                     .putString("token", token)
                     .apply();

                    String js =
                        "window.onChatsAppFcmToken && " +
                        "window.onChatsAppFcmToken(" +
                        JSONObject.quote(token) +
                        ");";

                    runOnUiThread(() -> {
                        if (webView != null &&
                            webView.getUrl() != null &&
                            APP_HOST.equals(
                                Uri.parse(
                                    webView.getUrl()
                                ).getHost()
                            )) {

                            webView.evaluateJavascript(
                                js,
                                null
                            );
                        }
                    });
                });
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 33 &&
            checkSelfPermission(
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                new String[]{
                    Manifest.permission.POST_NOTIFICATIONS
                },
                101
            );
        }

        webView = new WebView(this);

        webView.getSettings()
            .setJavaScriptEnabled(true);

        webView.getSettings()
            .setDomStorageEnabled(true);

        webView.getSettings()
            .setMediaPlaybackRequiresUserGesture(false);

        webView.addJavascriptInterface(
            new ChatsAppBridge(),
            "ChatsAppAndroid"
        );

        webView.setWebViewClient(
            new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(
                    WebView view,
                    WebResourceRequest request
                ) {

                    Uri uri = request.getUrl();

                    if ("https".equals(uri.getScheme()) &&
                        APP_HOST.equals(uri.getHost())) {
                        return false;
                    }

                    if (request.isForMainFrame()) {
                        try {
                            startActivity(
                                new Intent(
                                    Intent.ACTION_VIEW,
                                    uri
                                )
                            );
                        } catch (Exception ignored) {
                        }
                        return true;
                    }

                    return false;
                }
            }
        );

        webView.setWebChromeClient(
            new WebChromeClient()
        );

        setContentView(webView);

        if (Build.VERSION.SDK_INT >= 35) {
            webView.setOnApplyWindowInsetsListener(
                (view, insets) -> {

                    android.graphics.Insets bars =
                        insets.getInsets(
                            WindowInsets.Type.systemBars()
                        );

                    view.setPadding(
                        bars.left,
                        bars.top,
                        bars.right,
                        bars.bottom
                    );

                    return insets;
                }
            );
        }

        webView.loadUrl(APP_URL);
    }

    @Override
    public void onBackPressed() {

        if (webView != null &&
            webView.canGoBack()) {

            webView.goBack();

        } else {
            super.onBackPressed();
        }
    }
}
