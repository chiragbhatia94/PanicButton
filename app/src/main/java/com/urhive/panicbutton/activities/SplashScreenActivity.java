package com.urhive.panicbutton.activities;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.urhive.panicbutton.R;
import com.urhive.panicbutton.helpers.UIHelper;

public class SplashScreenActivity extends AppCompatBase {

    private static final String TAG = "SplashScreenActivity";
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
            if (!firstRun)
                UIHelper.startActivityClearTop(SplashScreenActivity.this, SignInActivity.class);
            else UIHelper.startActivityForResult(SplashScreenActivity.this, IntroActivity.class);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.pref, false);
        firstRun = UIHelper.checkForFirstRun(SplashScreenActivity.this);
        setContentView(R.layout.activity_splash_screen);
    }
}
