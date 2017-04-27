package com.urhive.panicbutton.helpers;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.urhive.panicbutton.R;
import com.urhive.panicbutton.activities.IntroActivity;

/**
 * Created by Chirag Bhatia on 15-03-2017.
 */

public class UIHelper {
    public static final int FROM_INTRO = 4;

    public static void startActivityClearTop(Activity mContext, Class mActivity) {
        Intent intent = new Intent(mContext, mActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent
                .FLAG_ACTIVITY_CLEAR_TASK);
        mContext.startActivity(intent);
        mContext.finish();
    }

    public static void startActivityClearTop(Activity mContext, Class mActivity, Bundle bundle) {
        Intent intent = new Intent(mContext, mActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent
                .FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
        mContext.finish();
    }

    public static void startActivity(Activity mContext, Class mActivity) {
        Intent intent = new Intent(mContext, mActivity);
        mContext.startActivity(intent);
    }

    public static void startActivityForResult(Activity mContext, Class mActivity) {
        Intent intent = new Intent(mContext, mActivity);
        mContext.startActivityForResult(intent, FROM_INTRO);
    }

    public static void startActivity(Activity mContext, Class mActivity, Bundle bundle) {
        Intent intent = new Intent(mContext, mActivity);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    public static void hideKeyboard(Activity mContext) {
        View view = mContext.getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager
                            .HIDE_NOT_ALWAYS);
        }
    }

    /**
     * Check if there is an active or soon-to-be-active network connection.
     *
     * @return true if there is no network connection, false otherwise.
     */
    public static boolean isOffline(Context mContext) {
        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context
                .CONNECTIVITY_SERVICE);

        return !(manager != null && manager.getActiveNetworkInfo() != null && manager
                .getActiveNetworkInfo().isConnectedOrConnecting());
    }

    public static void makeCall(Context context, String number) {

        Intent intent = new Intent(Intent.ACTION_CALL);

        intent.setData(Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) !=
                PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[]
            // permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the
            // documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        context.startActivity(intent);
    }

    public static void checkDrawOverlayPermission(final Activity activity, final int REQUEST_CODE) {
        /** check if we already  have permission to draw over other apps */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(activity)) {
                AlertDialog.Builder alert = new AlertDialog.Builder(activity).setTitle(R.string
                        .screen_overlay_permission_required).setMessage(R.string
                        .panic_button_requires_screen_overlay_permission_for_enabling_application_on_lockscreen).setPositiveButton("Grant Permission", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /** if not construct intent to request permission */
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri
                                .parse("package:" + activity.getPackageName()));
                        /** request permission via start activity for result */
                        activity.startActivityForResult(intent, REQUEST_CODE);
                    }
                }).setCancelable(false);
                alert.show();
            }
        }
    }

    public static void checkForFirstRun(Activity activity) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);

        boolean firstRun = prefs.getBoolean("pref_first_run", true);

        // things to do on first run
        if (firstRun) {
            UIHelper.startActivityForResult(activity, IntroActivity.class);
        }
    }
}
