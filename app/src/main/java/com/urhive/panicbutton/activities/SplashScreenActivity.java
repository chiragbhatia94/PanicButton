package com.urhive.panicbutton.activities;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.urhive.panicbutton.R;
import com.urhive.panicbutton.helpers.UIHelper;

public class SplashScreenActivity extends AppCompatBase {

    private static final String TAG = "SplashScreenActivity";

    @Override
    protected void switchWhenUserSignsIn() {
        Log.d(TAG, "switchWhenUserSignsIn: " + mFirebaseUser.getUid());
        Bundle bundle = new Bundle();
        bundle.putString("from", "SplashScreenActivity");
        UIHelper.startActivityClearTop(SplashScreenActivity.this, LockScreenActivity.class, bundle);
    }

    @Override
    protected void switchWhenUserSignsOut() {
        Log.d(TAG, "onAuthStateChanged: signed_out");
        if (UIHelper.isOffline(SplashScreenActivity.this)) {
            // Log.i(TAG, "switchWhenUserSignsOut: moving to offline activity");
            UIHelper.startActivityClearTop(SplashScreenActivity.this, OfflineActivity.class);
        } else {
            UIHelper.startActivityClearTop(SplashScreenActivity.this, SignInActivity.class);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        PreferenceManager.setDefaultValues(this, R.xml.pref, false);
    }
}
