package com.marcosalive2020;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PortifolioActivity extends AppCompatActivity {

    private static final String TAG = "PortifolioActivity";
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case com.marcosalive2020.R.id.navigation_home:
                Intent intent0 = new Intent(this, MainActivity.class);
                startActivity(intent0);
                finish();
                return true;
            case com.marcosalive2020.R.id.navigation_website:
                Intent intent1 = new Intent(this, PortifolioActivity.class);
                startActivity(intent1);
                finish();
                return true;
            case com.marcosalive2020.R.id.navigation_notifications:
                Intent intent2 = new Intent(this, AboutActivity.class);
                startActivity(intent2);
                finish();
                return true;
        }
        return false;
    };
    private Activity activity;
    private WebView webView;

    @SuppressLint({"SetJavaScriptEnabled", "ObsoleteSdkInt"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portifolio);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getApplicationContext();
        activity = this;

        findViewById(R.id.activity_main_webView);


        webView = findViewById(R.id.webview);
        webView.setVisibility(View.GONE);

        loadWeb();

    }

    @SuppressLint({"SetJavaScriptEnabled", "ObsoleteSdkInt"})
    private void loadWeb() {
        final Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(1000);

        final Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setStartOffset(1000);
        fadeOut.setDuration(1000);

        final AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(fadeIn);
        animationSet.addAnimation(fadeOut);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                webView.setVisibility(View.VISIBLE);
                webView.setAnimation(fadeIn);
            }
        });

        webView.loadUrl("https://marcosnunes.github.io/Portifolio");
        webView.setWebChromeClient(new WebChromeClient());

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            settings.setDatabasePath("/data/data" + webView.getContext().getPackageName() + "/databases/");
        }

        if (!haveNetworkConnection()) {
            android.app.AlertDialog.Builder Checkbuilder = new android.app.AlertDialog.Builder(PortifolioActivity.this);
            Checkbuilder.setMessage("Por favor conecte-se à internet!");
            android.app.AlertDialog alert = Checkbuilder.create();
            alert.show();
        } else {
            webView.loadUrl("https://marcosnunes.github.io/Portifolio");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        isWriteStoragePermissionGranted();
    }

    public void isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permitido");
            } else {

                Log.v(TAG, "Negado");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permitido");
        }
    }

    protected void exitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle("Deseja encerrar?");
        builder.setMessage("Você está prestes a encerrar o aplicativo");
        builder.setCancelable(true);

        builder.setPositiveButton("Sim", (dialogInterface, i) -> PortifolioActivity.super.onBackPressed());

        builder.setNegativeButton("Não", (dialogInterface, i) -> {

        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();

        } else {
            exitDialog();
        }
    }

    private boolean haveNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null;

    }
}
