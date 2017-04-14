package com.urhive.panicbutton.activities;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.urhive.panicbutton.R;
import com.urhive.panicbutton.helpers.UIHelper;

public class OfflineActivity extends AppCompatBase {

    private static final String TAG = "OfflineActivity";
    private Runnable runnable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);

        final Handler handler = new Handler();
        final int delay = 3000;

        runnable = new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "run: checking if we are online again!");
                if (!UIHelper.isOffline(OfflineActivity.this)){
                    Log.i(TAG, "run: we are online again");
                    handler.removeCallbacks(runnable);
                    UIHelper.startActivityClearTop(OfflineActivity.this, SplashScreenActivity.class);
                } else {
                    handler.postDelayed(this, delay);
                }
            }
        };
        runnable.run();
    }

    public void checkConnectivity(View view) {
        Log.i(TAG, "run: we are online again");
        if (!UIHelper.isOffline(OfflineActivity.this)) {
            UIHelper.startActivityClearTop(OfflineActivity.this, SplashScreenActivity.class);
        } else {
            Toast.makeText(this, getString(R.string.u_r_currently_offline), Toast.LENGTH_SHORT).show();
        }
    }
}
