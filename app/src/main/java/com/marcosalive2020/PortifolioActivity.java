package com.marcosalive2020;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkView;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class PortifolioActivity extends AppCompatActivity
        implements EasyPermissions.PermissionCallbacks {

    private static final int PERMISSIONS = 123;
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

    private boolean hasWriteStoragePermissions() {
        return EasyPermissions.hasPermissions(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private boolean hasReadStoragePermissions() {
        return EasyPermissions.hasPermissions(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @AfterPermissionGranted(PERMISSIONS)
    public void permissionsTask() {
        if (hasWriteStoragePermissions()) {
            // Have permission, do the thing!
            mXWalkView.loadUrl("https://marcosnunes.github.io/Portifolio", null);
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.rationale_write_storage),
                    PERMISSIONS,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (hasReadStoragePermissions()) {
            // Have permission, do the thing!
            mXWalkView.loadUrl("https://marcosnunes.github.io/Portifolio", null);
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.rationale_write_storage),
                    PERMISSIONS,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        permissionsTask();
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
