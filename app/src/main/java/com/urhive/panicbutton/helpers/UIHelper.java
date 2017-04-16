package com.urhive.panicbutton.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Chirag Bhatia on 15-03-2017.
 */

public class UIHelper {
    public static void startActivityClearTop(Activity mContext, Class mActivity) {
        Intent intent = new Intent(mContext, mActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
        mContext.finish();
    }

    public static void startActivityClearTop(Activity mContext, Class mActivity, Bundle bundle) {
        Intent intent = new Intent(mContext, mActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
        mContext.finish();
    }

    public static void startActivity(Activity mContext, Class mActivity) {
        Intent intent = new Intent(mContext, mActivity);
        mContext.startActivity(intent);
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
}
