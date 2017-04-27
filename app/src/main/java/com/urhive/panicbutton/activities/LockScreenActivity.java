package com.urhive.panicbutton.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.urhive.panicbutton.R;
import com.urhive.panicbutton.fragments.CarouselFragment;
import com.urhive.panicbutton.helpers.UIHelper;
import com.urhive.panicbutton.services.EmergencyActivityService;

public class LockScreenActivity extends AppCompatBase {

    private static final String TAG = "LockScreenActivity";
    @SuppressLint("StaticFieldLeak")
    public static TabLayout tabLayout;
    private CoordinatorLayout mainCL;
    private CarouselFragment carouselFragment;
    private Bundle bundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getIntent().getExtras();
        if (bundle == null) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        }

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
        if (bundle == null) {
            EmergencyActivityService.checkLockScreenStateAndSetViews(getApplicationContext(), 1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bundle == null) {
            EmergencyActivityService.checkLockScreenStateAndSetViews(getApplicationContext(), 2);
        }
    }

    public void callEmergency(View view) {
        Snackbar.make(mainCL, R.string.lorem_ipsum, Snackbar.LENGTH_SHORT).show();
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
        } /*else {
            // carousel handled the back pressed task
            // do not call super
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                UIHelper.startActivity(LockScreenActivity.this, SettingsActivity.class);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
