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
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkView;

public class PortifolioActivity extends AppCompatActivity {

    private static final String TAG = "PortifolioActivity";
    private Activity activity;
    private XWalkView mXWalkView;

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                Intent intent0 = new Intent(this, MainActivity.class);
                startActivity(intent0);
                finish();
                return true;
            case R.id.navigation_website:
                Intent intent1 = new Intent(this, PortifolioActivity.class);
                startActivity(intent1);
                finish();
                return true;
            case R.id.navigation_notifications:
                Intent intent2 = new Intent(this, AboutActivity.class);
                startActivity(intent2);
                finish();
                return true;
        }
        return false;
    };

    @SuppressLint({"SetJavaScriptEnabled", "ObsoleteSdkInt"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portifolio);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        getApplicationContext();
        activity = this;

        findViewById(R.id.activity_main_webView);

        mXWalkView = findViewById(R.id.xwalkview);
        mXWalkView.setVisibility(View.GONE);

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


        mXWalkView.setVisibility(View.VISIBLE);
        mXWalkView.setAnimation(fadeIn);
        mXWalkView.getSettings().setAllowContentAccess(true);
        mXWalkView.getSettings().setAllowFileAccess(true);
        mXWalkView.getSettings().setDomStorageEnabled(true);
        mXWalkView.getSettings().setAllowFileAccessFromFileURLs(true);
        mXWalkView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        mXWalkView.getSettings().setJavaScriptEnabled(true);
        mXWalkView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mXWalkView.getSettings().setSaveFormData(true);
        mXWalkView.getSettings().getCacheMode();
        mXWalkView.getSettings().setSupportMultipleWindows(true);
        mXWalkView.getSettings().setDatabaseEnabled(true);
        mXWalkView.getSettings().setLoadsImagesAutomatically(true);
        mXWalkView.getSettings().setDomStorageEnabled(true);

        mXWalkView.loadUrl("https://marcosnunes.github.io/Portifolio/", null);

        // turn on debugging
        XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);

        if (!haveNetworkConnection()) {
            AlertDialog.Builder Checkbuilder = new AlertDialog.Builder(PortifolioActivity.this);
            Checkbuilder.setMessage("Por favor conecte-se à internet!");
            AlertDialog alert = Checkbuilder.create();
            alert.show();
        } else {
            mXWalkView.loadUrl("https://marcosnunes.github.io/Portifolio", null);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        isWriteStoragePermissionGranted();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mXWalkView != null) {
            mXWalkView.pauseTimers();
            mXWalkView.onHide();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mXWalkView != null) {
            mXWalkView.resumeTimers();
            mXWalkView.onShow();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mXWalkView != null) {
            mXWalkView.onDestroy();
        }
    }

    public void isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted2");
            } else {

                Log.v(TAG, "Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted2");
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
        exitDialog();
    }

    private boolean haveNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null;

    }
}
