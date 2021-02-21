package com.example.workitapp.More;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.workitapp.R;

public class MyScreenUtils {
    // set navigation & notification bars color
    public static void setThemeColor(AppCompatActivity activity) {
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(activity, R.color.DARK_RED));
        window.setNavigationBarColor(ContextCompat.getColor(activity,R.color.DARK_RED));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.setNavigationBarDividerColor(ContextCompat.getColor(activity,R.color.DARK_RED));
        }
    }

}