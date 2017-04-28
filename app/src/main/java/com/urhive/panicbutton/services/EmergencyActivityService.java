package com.urhive.panicbutton.services;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.urhive.panicbutton.R;
import com.urhive.panicbutton.activities.LockScreenActivity;

/**
 * Created by Chirag Bhatia on 14-04-2017.
 */

public class EmergencyActivityService extends Service {
    private static final String TAG = "EmergencyActivity";
    public static ImageView floatingWidgetIV;
    private LockScreenStateReceiver mLockScreenStateReceiver;
    private WindowManager windowManager;
    private RelativeLayout floatingWidget;
    private int x_init_cord, y_init_cord, x_init_margin, y_init_margin;

    private Point szWindow = new Point();

    private Boolean _enabled;

    public static void checkLockScreenStateAndSetViews(Context context, int PR) {
        // PR = 0 nothing, 1 Pause, 2 Resume
        KeyguardManager mKeyGuardManager = (KeyguardManager) context.getSystemService(Context
                .KEYGUARD_SERVICE);
        if (mKeyGuardManager.inKeyguardRestrictedInputMode()) {
            Log.d(TAG, "LockScreenState: locked");

            if (floatingWidgetIV != null) {
                floatingWidgetIV.setVisibility(View.VISIBLE);
            }
            if (PR == 2) {
                if (floatingWidgetIV != null) {
                    floatingWidgetIV.setVisibility(View.GONE);
                }
            }
        } else {
            Log.d(TAG, "LockScreenState: unlocked");
            if (floatingWidgetIV != null) {
                floatingWidgetIV.setVisibility(View.GONE);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: getting user preferences");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences
                (getApplicationContext());
        _enabled = prefs.getBoolean("isPanicButtonEnabled", true);

        mLockScreenStateReceiver = new LockScreenStateReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);

        registerReceiver(mLockScreenStateReceiver, filter);
    }

    @SuppressLint("InflateParams")
    private void handleStart() {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        floatingWidget = (RelativeLayout) inflater.inflate(R.layout.floating_widget_layout, null);
        floatingWidgetIV = (ImageView) floatingWidget.findViewById(R.id.floating_button);


        windowManager.getDefaultDisplay().getSize(szWindow);

        /* for lower versions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            windowManager.getDefaultDisplay().getSize(szWindow);
        } else {
            int w = windowManager.getDefaultDisplay().getWidth();
            int h = windowManager.getDefaultDisplay().getHeight();
            szWindow.set(w, h);
        }
        */

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager
                .LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR, WindowManager.LayoutParams
                .FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 0;
        params.y = 100;

        windowManager.addView(floatingWidget, params);
        checkLockScreenStateAndSetViews(getApplicationContext(), 0);

        floatingWidget.setOnTouchListener(new View.OnTouchListener() {
            long time_start = 0, time_end = 0;
            boolean isLongclick = false;

            Handler handler_longClick = new Handler();
            Runnable runnable_longClick = new Runnable() {

                @Override
                public void run() {
                    Log.d(TAG, "Into runnable_longClick");

                    isLongclick = true;
                    floatingWidget_longclick();
                }
            };

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams)
                        floatingWidget.getLayoutParams();

                int x_cord = (int) event.getRawX();
                int y_cord = (int) event.getRawY();
                int x_cord_Destination, y_cord_Destination;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        time_start = System.currentTimeMillis();
                        handler_longClick.postDelayed(runnable_longClick, 600);

                        x_init_cord = x_cord;
                        y_init_cord = y_cord;

                        x_init_margin = layoutParams.x;
                        y_init_margin = layoutParams.y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int x_diff_move = x_cord - x_init_cord;
                        int y_diff_move = y_cord - y_init_cord;

                        x_cord_Destination = x_init_margin + x_diff_move;
                        y_cord_Destination = y_init_margin + y_diff_move;

                        /*if (isLongclick) {
                            //do something here later
                        }*/

                        layoutParams.x = x_cord_Destination;
                        layoutParams.y = y_cord_Destination;

                        windowManager.updateViewLayout(floatingWidget, layoutParams);
                        break;
                    case MotionEvent.ACTION_UP:
                        isLongclick = false;
                        handler_longClick.removeCallbacks(runnable_longClick);

                        int x_diff = x_cord - x_init_cord;
                        int y_diff = y_cord - y_init_cord;

                        if (Math.abs(x_diff) < 5 && Math.abs(y_diff) < 5) {
                            time_end = System.currentTimeMillis();
                            if ((time_end - time_start) < 300) {
                                floatingWidget_click();
                            }
                        }

                        y_cord_Destination = y_init_margin + y_diff;

                        int BarHeight = getStatusBarHeight();
                        if (y_cord_Destination < 0) {
                            y_cord_Destination = 0;
                        } else if (y_cord_Destination + (floatingWidget.getHeight() + BarHeight)
                                > szWindow.y) {
                            y_cord_Destination = szWindow.y - (floatingWidget.getHeight() +
                                    BarHeight);
                        }
                        layoutParams.y = y_cord_Destination;
                        resetPosition(x_cord);

                        break;
                }
                return true;
            }
        });

        // Toast.makeText(this, "Panic button activated.", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "handleStart: Panic button activated.");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() -> startId=" + startId);
        if (startId == Service.START_STICKY) {
            if (_enabled) {
                handleStart();
            }
            return super.onStartCommand(intent, flags, startId);
        } else {
            return Service.START_NOT_STICKY;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeService();
    }

    private void closeService() {
        unregisterReceiver(mLockScreenStateReceiver);
        if (floatingWidget != null) windowManager.removeView(floatingWidget);
        EmergencyActivityService.this.stopSelf();
    }

    private void floatingWidget_click() {
        Intent it = new Intent(this, LockScreenActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(it);
    }

    private void floatingWidget_longclick() {
        Log.i(TAG, "floatingWidget_longclick: longclicked");
    }

    private void resetPosition(int x_cord_now) {
        if (x_cord_now <= szWindow.x / 2) {
            moveToLeft();
        } else {
            moveToRight();
        }

    }

    private void moveToLeft() {
        WindowManager.LayoutParams mParams = (WindowManager.LayoutParams) floatingWidget
                .getLayoutParams();
        mParams.x = 0;
        windowManager.updateViewLayout(floatingWidget, mParams);
    }

    private void moveToRight() {
        WindowManager.LayoutParams mParams = (WindowManager.LayoutParams) floatingWidget
                .getLayoutParams();

        mParams.x = szWindow.x - floatingWidget.getWidth();
        windowManager.updateViewLayout(floatingWidget, mParams);
    }

    private int getStatusBarHeight() {
        return (int) Math.ceil(25 * getApplicationContext().getResources().getDisplayMetrics()
                .density);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        // TODO: 27-04-2017 restart this service but you need to check if the conditions are valid
        super.onTaskRemoved(rootIntent);
    }

    // added 27.04.2017

    public class LockScreenStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkLockScreenStateAndSetViews(context, 0);
        }
    }
}
