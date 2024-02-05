package br.brlgames.moonprincess;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MPContent extends AppCompatActivity {
    private WebView webMoonPrincess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpcontent);


        getWindow().setFlags(1024, 1024);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();


        webMoonPrincess = findViewById(R.id.MoonPrincess);
        webMoonPrincess.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage cm) {
                return true;
            }
        });
        webMoonPrincess.getSettings().setLoadsImagesAutomatically(true);
        webMoonPrincess.getSettings().setJavaScriptEnabled(true);
        webMoonPrincess.getSettings().setDomStorageEnabled(true);
        webMoonPrincess.getSettings().setMediaPlaybackRequiresUserGesture(true);
        webMoonPrincess.getSettings().setAllowFileAccess(true);
        webMoonPrincess.getSettings().setAllowContentAccess(true);
        webMoonPrincess.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webMoonPrincess.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webMoonPrincess.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webMoonPrincess.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 11; Pixel 4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.131 Mobile Safari/537.36");
        webMoonPrincess.setWebViewClient(new WebViewClient());
        webMoonPrincess.addJavascriptInterface(new JSinterface(this ), "jsBridge");
        webMoonPrincess.loadUrl(MPConfig.gameURL);


    }

    public static class JSinterface {
        Context contextGame;

        public JSinterface(Context context) {
            this.contextGame = context;
        }

        @JavascriptInterface
        public void postMessage(String name, String data) {
            Map<String, Object> eventValue = new HashMap<>();

            if ("openWindow".equals(name)) {
                try {
                    JSONObject extLink = new JSONObject();
                    Intent neWindow = new Intent(Intent.ACTION_VIEW);
                    neWindow.setData(Uri.parse(extLink.getString("url")));
                    neWindow.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    contextGame.startActivity(neWindow);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                eventValue.put(name, data);

                Toast.makeText(contextGame, name, Toast.LENGTH_SHORT).show();
            }
        }
    }
}