package com.urhive.panicbutton.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;
import com.heinrichreimersoftware.materialintro.slide.Slide;
import com.urhive.panicbutton.R;

/**
 * Created by Chirag Bhatia on 25-04-2017.
 */

public class IntroActivity extends com.heinrichreimersoftware.materialintro.app.IntroActivity {
    private static final String TAG = "IntroActivity";

    private static final int REQUEST_CODE_DRAW_OVERLAY = 1234;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setFullscreen(true);
        super.onCreate(savedInstanceState);

        addSlide(new SimpleSlide.Builder().title(R.string.app_introduction_pg1_title).description
                (R.string.app_introduction_pg1_desc).image(R.mipmap.ic_launcher).background(R
                .color.blue_300).backgroundDark(R.color.blue_600).scrollable(true).build());

        addSlide(new SimpleSlide.Builder().description(R.string.app_introduction_pg2_desc).image
                (R.mipmap.ic_launcher).background(R.color.amber_300).backgroundDark(R.color
                .amber_600).scrollable(true).build());


        addSlide(new SimpleSlide.Builder().description(R.string.app_introduction_pg3_desc).image
                (R.mipmap.ic_launcher_round).background(R.color.blue_grey_300).backgroundDark(R
                .color.blue_grey_600).scrollable(true).build());


        addSlide(new SimpleSlide.Builder().description(R.string.app_introduction_pg4_desc).image
                (R.mipmap.ic_launcher_round).background(R.color.light_blue_300).backgroundDark(R
                .color.light_blue_600).scrollable(true).build());


        addSlide(new SimpleSlide.Builder().description(R.string.app_introduction_pg5_desc).image
                (R.mipmap.ic_launcher_round).background(R.color.grey_300).backgroundDark(R.color
                .grey_600).scrollable(true).build());

        final Slide permissionsSlide;
        if (checkAllPermission(this)) {
            String[] permissions;
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                permissions = new String[]{Manifest.permission.INTERNET, Manifest.permission
                        .ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.CALL_PHONE, Manifest.permission.READ_CONTACTS, Manifest.permission.WAKE_LOCK, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.RECEIVE_BOOT_COMPLETED};
            } else {
                permissions = new String[]{Manifest.permission.INTERNET, Manifest.permission
                        .ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.CALL_PHONE, Manifest.permission.READ_CONTACTS, Manifest.permission.WAKE_LOCK, Manifest.permission.RECEIVE_BOOT_COMPLETED};
            }
            permissionsSlide = new SimpleSlide.Builder().title(R.string
                    .app_introduction_permissions_title).description(R.string
                    .app_introduction_permissions_desc).background(R.color.blue_grey_300)
                    .backgroundDark(R.color.blue_grey_600).scrollable(true).permissions
                            (permissions).build();
            addSlide(permissionsSlide);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                addSlide(new SimpleSlide.Builder().title(R.string
                        .draw_overlay_app_permission_title).description(R.string
                        .draw_overlay_app_permission_desc).image(R.mipmap.ic_launcher).background
                        (R.color.green_300).backgroundDark(R.color.green_600).scrollable(true)
                        .buttonCtaLabel(R.string.grant_permission).buttonCtaClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View v) {
                        /* if not construct intent to request permission */
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri
                                .parse("package:" + getPackageName()));
                        /* request permission via start activity for result */
                        startActivityForResult(intent, REQUEST_CODE_DRAW_OVERLAY);
                    }
                }).build());
            }
        }

        addSlide(new SimpleSlide.Builder().description(R.string.lets_start).image(R.mipmap
                .ic_launcher_round).background(R.color.teal_300).backgroundDark(R.color.teal_600)
                .scrollable(true).build());

        /*
        * new SimpleSlide.Builder().title(R.string.sign_in).description(R.string
                .sign_in_to_let_us_gather_your_information_for_the_panic_button).image(R.mipmap
                .ic_launcher).background(R.color.blue_grey_300).backgroundDark(R.color
                .blue_grey_600)
                .scrollable(true).build()*/
        /*addSlide(new FragmentSlide.Builder().background(R.color.blue_grey_300).backgroundDark(R
                .color.blue_grey_600).fragment(SignInFragment.newInstance()).build());*/
    }

    private boolean checkAllPermission(Activity activity) {
        /*
        * Manifest.permission.INTERNET, Manifest.permission.ACCESS_FINE_LOCATION,
        * Manifest.permission.ACCESS_COARSE_LOCATION,
        * Manifest.permission.CALL_PHONE, Manifest.permission.READ_CONTACTS,
        * Manifest.permission.WAKE_LOCK, Manifest.permission.SYSTEM_ALERT_WINDOW
        * */
        PackageManager pm = activity.getPackageManager();
        int hasPermFL = pm.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, activity
                .getPackageName());
        Log.i(TAG, "checkAllPermission Fine Location: " + hasPermFL);
        int hasPermCL = pm.checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, activity
                .getPackageName());
        Log.i(TAG, "checkAllPermission Coarse Location: " + hasPermCL);
        int hasPermCP = pm.checkPermission(Manifest.permission.CALL_PHONE, activity
                .getPackageName());
        Log.i(TAG, "checkAllPermission Call Phone: " + hasPermCP);
        int hasPermRC = pm.checkPermission(Manifest.permission.READ_CONTACTS, activity
                .getPackageName());
        Log.i(TAG, "checkAllPermission Read Contacts: " + hasPermRC);
        int hasPermBC = pm.checkPermission(Manifest.permission.RECEIVE_BOOT_COMPLETED, activity
                .getPackageName());
        Log.i(TAG, "checkAllPermission Boot Completed: " + hasPermBC);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // use this only when the system is below M
            int hasPermSAW = pm.checkPermission(Manifest.permission.SYSTEM_ALERT_WINDOW, activity
                    .getPackageName());
            Log.i(TAG, "checkAllPermission System Alert Window: " + hasPermSAW);
        }
        if (hasPermFL == PackageManager.PERMISSION_GRANTED && hasPermCL == PackageManager
                .PERMISSION_GRANTED && hasPermCP == PackageManager.PERMISSION_GRANTED &&
                hasPermRC == PackageManager.PERMISSION_GRANTED/* && hasPermSAW == PackageManager
                .PERMISSION_GRANTED*/) {
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_DRAW_OVERLAY:
                    nextSlide();
                    break;
                default:
                    break;
            }
        }
    }

    /*@Override
    public void onBackPressed() {
        //super.onBackPressed();
    }*/
}
