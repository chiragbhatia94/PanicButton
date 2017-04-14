package com.urhive.panicbutton.services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.urhive.panicbutton.R;
import com.urhive.panicbutton.adapters.ViewPagerAdapter;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Chirag Bhatia on 14-04-2017.
 */

public class EmergencyButtonService extends Service {

    private static final String TAG = "EmergencyButtonService";
    private static WindowManager windowManager;
    private static WindowManager.LayoutParams params;
    private boolean mHasDoubleClicked = false;
    private long lastPressTime;
    private Boolean _enable = true;
    private Boolean _expanded = false;
    private View view;
    private ViewPager viewPager;
    private RelativeLayout mainRL;
    private ImageView floatingIV, cancelIV;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("InflateParams")
    @Override
    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        view = LayoutInflater.from(this).inflate(R.layout.floating_layout, null);
        floatingIV = (ImageView) view.findViewById(R.id.floating_button);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        mainRL = (RelativeLayout) view.findViewById(R.id.mainRL);
        cancelIV = (ImageView) view.findViewById(R.id.cancel_button);

        cancelIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUnexpanded();
            }
        });

        CircleIndicator indicator = (CircleIndicator) view.findViewById(R.id.indicator);
        viewPager.setAdapter(new ViewPagerAdapter(this, view));
        indicator.setViewPager(viewPager);

        params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                // WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR, WindowManager.LayoutParams
                .FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 0;
        params.y = 100;

        // for testing
        windowManager.addView(view, params);
        setUnexpanded();

        Toast.makeText(EmergencyButtonService.this, R.string.panic_button_activated, Toast
                .LENGTH_SHORT).show();

        try {
            view.setOnTouchListener(new View.OnTouchListener() {
                private WindowManager.LayoutParams paramsF = params;
                private int initialX;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:

                            // Get current time in nano seconds.
                            long pressTime = System.currentTimeMillis();

                            // If double click...
                            if (pressTime - lastPressTime <= 300) {
                                // my code here
                                //unregisterReceiver(mLockScreenStateReceiver);
                                /*ServiceEmergencyButton.this.stopSelf();

                                createNotification();*/
                                mHasDoubleClicked = true;
                            } else {
                                // If not double click....
                                mHasDoubleClicked = false;
                            }
                            lastPressTime = pressTime;
                            initialX = paramsF.x;
                            initialY = paramsF.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            break;
                        case MotionEvent.ACTION_UP:
                            break;
                        case MotionEvent.ACTION_MOVE:
                            paramsF.x = initialX + (int) (event.getRawX() - initialTouchX);
                            paramsF.y = initialY + (int) (event.getRawY() - initialTouchY);
                            windowManager.updateViewLayout(view, paramsF);
                            break;
                    }
                    return false;
                }
            });
        } catch (Exception e) {
            // TODO: handle exception
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_expanded) {
                    // set hide mode now
                    setUnexpanded();
                } else {
                    // set show mode now
                    setExpanded();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (view != null) windowManager.removeView(view);
    }

    private void setUnexpanded() {
        // set hide mode now
        _expanded = false;
        mainRL.setVisibility(View.GONE);
        floatingIV.setVisibility(View.VISIBLE);
        Log.i(TAG, "onClick: this is hidden mode");
    }

    private void setExpanded() {
        // set show mode now
        mainRL.setVisibility(View.VISIBLE);
        floatingIV.setVisibility(View.GONE);
        _expanded = true;
        Log.i(TAG, "onClick: this is shown mode");
    }
}
