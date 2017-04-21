package com.urhive.panicbutton.fragments;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.urhive.panicbutton.R;
import com.urhive.panicbutton.activities.LockScreenActivity;
import com.urhive.panicbutton.adapters.ActivityViewPagerAdapter;
import com.urhive.panicbutton.interfaces.OnBackPressListener;

/**
 * Created by Chirag Bhatia on 21-04-2017.
 */

public class CarouselFragment extends android.support.v4.app.Fragment {
    protected ViewPager pager;

    private ActivityViewPagerAdapter adapter;

    public CarouselFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_carousel, container, false);

        pager = (ViewPager) rootView.findViewById(R.id.viewPager);
        LockScreenActivity.tabLayout.setupWithViewPager(pager);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Note that we are passing childFragmentManager, not FragmentManager
        adapter = new ActivityViewPagerAdapter(getResources(), getChildFragmentManager());

        pager.setAdapter(adapter);
    }

    /**
     * Retrieve the currently visible Tab Fragment and propagate the onBackPressed callback
     *
     * @return true = if this fragment and/or one of its associates Fragment can handle the
     * backPress
     */
    public boolean onBackPressed() {
        // currently visible tab Fragment
        OnBackPressListener currentFragment = (OnBackPressListener) adapter.getRegisteredFragment
                (pager.getCurrentItem());

        if (currentFragment != null) {
            // lets see if the currentFragment or any of its childFragment can handle onBackPressed
            return currentFragment.onBackPressed();
        }

        // this Fragment couldn't handle the onBackPressed call
        return false;
    }
}
