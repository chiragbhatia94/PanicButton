package com.urhive.panicbutton.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.urhive.panicbutton.R;
import com.urhive.panicbutton.helpers.UIHelper;

public class SplashScreenActivity extends AppCompatBase {

    private static final String TAG = "SplashScreenActivity";
    private static final int REQUEST_CODE_DRAW_OVERLAY = 1234;
    private boolean firstRun;

    @Override
    protected void switchWhenUserSignsIn() {
        Log.d(TAG, "switchWhenUserSignsIn: " + mFirebaseUser.getUid());
        UIHelper.startPanicButtonService(SplashScreenActivity.this);
        Bundle bundle = new Bundle();
        bundle.putString("from", "SplashScreenActivity");
        UIHelper.startActivityClearTop(SplashScreenActivity.this, LockScreenActivity.class, bundle);
    }

    @Override
    protected void switchWhenUserSignsOut() {
        Log.d(TAG, "onAuthStateChanged: signed_out");
        UIHelper.stopPanicButtonService(SplashScreenActivity.this);
        if (UIHelper.isOffline(SplashScreenActivity.this)) {
            // Log.i(TAG, "switchWhenUserSignsOut: moving to offline activity");
            if (!firstRun)
                UIHelper.startActivityClearTop(SplashScreenActivity.this, OfflineActivity.class);
            else UIHelper.startActivityForResult(SplashScreenActivity.this, IntroActivity.class);
        } else {
            if (!firstRun) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.canDrawOverlays(this)) {
                        Toast.makeText(this, getString(R.string
                                .panic_button_requires_screen_overlay_permission_for_enabling_application_on_lock_screen), Toast.LENGTH_LONG).show();
                        // if not construct intent to request permission
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri
                                .parse("package:" + getPackageName()));
                        // request permission via start activity for result
                        startActivityForResult(intent, REQUEST_CODE_DRAW_OVERLAY);
                    } else {
                        UIHelper.startActivityClearTop(SplashScreenActivity.this, SignInActivity
                                .class);

                    }
                }
            } else UIHelper.startActivityForResult(SplashScreenActivity.this, IntroActivity.class);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.pref, false);
        firstRun = UIHelper.checkForFirstRun(SplashScreenActivity.this);
        setContentView(R.layout.activity_splash_screen);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_DRAW_OVERLAY:
                    recreate();
                    break;
                default:
                    break;
            }
        }
    }
}
