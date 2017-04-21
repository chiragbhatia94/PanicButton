package com.urhive.panicbutton.fragments.controllers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.urhive.panicbutton.interfaces.OnBackPressListener;

/**
 * Created by Chirag Bhatia on 21-04-2017.
 */

public class BackPressImpl implements OnBackPressListener {

    private Fragment parentFragment;

    public BackPressImpl(Fragment parentFragment) {
        this.parentFragment = parentFragment;
    }

    @Override
    public boolean onBackPressed() {

        if (parentFragment == null) return false;

        int childCount = parentFragment.getChildFragmentManager().getBackStackEntryCount();

        if (childCount == 0) {
            // it has no child Fragment
            // can not handle the onBackPressed task by itself
            return false;

        } else {
            // get the child Fragment
            FragmentManager childFragmentManager = parentFragment.getChildFragmentManager();
            OnBackPressListener childFragment = (OnBackPressListener) childFragmentManager
                    .getFragments().get(0);

            // propagate onBackPressed method call to the child Fragment
            if (!childFragment.onBackPressed()) {
                // child Fragment was unable to handle the task
                // It could happen when the child Fragment is last last leaf of a chain
                // removing the child Fragment from stack
                childFragmentManager.popBackStackImmediate();

            }

            // either this Fragment or its child handled the task
            // either way we are successful and done here
            return true;
        }
    }
}
