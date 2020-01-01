package com.lupa.kostka.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lupa.kostka.BuildConfig;
import com.lupa.kostka.MainActivity;


public class AppUtils {

    public static boolean isFragmentCurrent(String name, FragmentManager fragmentManager) {
        if (fragmentManager.getBackStackEntryCount() != 0) {
            FragmentManager.BackStackEntry be = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1);
            return be.getName().equals(name);
        }
        return false;
    }

    public static void setTextByLanguage(MainActivity activity, TextView view, int textCzResourceId, int textEngResourceId) {
        view.setText(activity.getString(activity.language == MainActivity.Language.CZ ? textCzResourceId : textEngResourceId));
    }

    public static String getTextByLanguage(MainActivity activity, int textCzResourceId, int textEngResourceId) {
        return activity.language == MainActivity.Language.CZ ? activity.getResources().getString(textCzResourceId) : activity.getResources().getString(textEngResourceId);
    }

    public static String getVersion() {
        return BuildConfig.VERSION_NAME + " (" + BuildConfig.VERSION_CODE + ")";
    }

    public static void setMaxScale(View view) {
        view.setScaleX(1.0f);
        view.setScaleY(1.0f);
    }

    public static void setMinScale(View view) {
        view.setScaleX(0.0f);
        view.setScaleY(0.0f);
    }

    public static Drawable getDrawableBySdk(Context context, int drawableId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            return context.getResources().getDrawable(drawableId, context.getTheme());
        } else {
            return ContextCompat.getDrawable(context, drawableId);
        }
    }

    public static void setImage(Context context, ImageView imageView, int resId) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            imageView.setImageResource(resId);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            imageView.setImageDrawable(context.getResources().getDrawable(resId, context.getTheme()));
        } else {
            imageView.setImageDrawable(ContextCompat.getDrawable(context, resId));
        }
    }
}
