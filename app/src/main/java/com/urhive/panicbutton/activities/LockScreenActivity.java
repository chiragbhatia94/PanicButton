package com.urhive.panicbutton.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.WindowManager;

import com.urhive.panicbutton.R;
import com.urhive.panicbutton.fragments.CarouselFragment;

public class LockScreenActivity extends AppCompatBase {

    @SuppressLint("StaticFieldLeak")
    public static TabLayout tabLayout;
    private CoordinatorLayout mainCL;
    private CarouselFragment carouselFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_lockscreen);
        setToolbar(getString(R.string.app_name));

        mainCL = (CoordinatorLayout) findViewById(R.id.mainCL);

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        // tabLayout.setupWithViewPager();

        if (savedInstanceState == null) {
            // withholding the previously created fragment from being created again
            // On orientation change, it will prevent fragment recreation
            // its necessary to reserve the fragment stack inside each tab
            initScreen();
        } else {
            // restoring the previously created fragment
            // and getting the reference
            carouselFragment = (CarouselFragment) getSupportFragmentManager().getFragments().get(0);
        }
    }

    private void initScreen() {
        // Creating the ViewPager container fragment once
        carouselFragment = new CarouselFragment();

        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, carouselFragment).commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //EmergencyActivityService.checkLockScreenStateAndSetViews(getApplicationContext(), 1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //EmergencyActivityService.checkLockScreenStateAndSetViews(getApplicationContext(), 2);
    }

    public void clickedwow(View view) {
        Snackbar.make(mainCL, "This is awesome", Snackbar.LENGTH_SHORT).show();
    }

    /**
     * Only Activity has this special callback method
     * Fragment doesn't have any onBackPressed callback
     * <p>
     * Logic:
     * Each time when the back button is pressed, this Activity will propagate the call to the
     * container Fragment and that Fragment will propagate the call to its each tab Fragment
     * those Fragments will propagate this method call to their child Fragments and
     * eventually all the propagated calls will get back to this initial method
     * <p>
     * If the container Fragment or any of its Tab Fragments and/or Tab child Fragments couldn't
     * handle the onBackPressed propagated call then this Activity will handle the callback itself
     */
    @Override
    public void onBackPressed() {
        if (!carouselFragment.onBackPressed()) {
            // container Fragment or its associates couldn't handle the back pressed task
            // delegating the task to super class
            super.onBackPressed();
        } else {
            // carousel handled the back pressed task
            // do not call super
        }
    }
}