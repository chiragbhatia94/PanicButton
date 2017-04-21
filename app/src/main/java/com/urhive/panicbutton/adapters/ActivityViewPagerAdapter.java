package com.urhive.panicbutton.adapters;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.urhive.panicbutton.fragments.ContactsFragment;
import com.urhive.panicbutton.fragments.FirstAidFragment;
import com.urhive.panicbutton.fragments.InfoFragment;
import com.urhive.panicbutton.fragments.NearByHospitalFragment;

/**
 * Created by Chirag Bhatia on 21-04-2017.
 */

public class ActivityViewPagerAdapter extends FragmentPagerAdapter {
    private final Resources resources;

    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    /**
     * Create pager adapter
     *
     * @param resources
     * @param fm
     */
    public ActivityViewPagerAdapter(Resources resources, FragmentManager fm) {
        super(fm);
        this.resources = resources;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return FirstAidFragment.newInstance();
            case 1:
                return InfoFragment.newInstance();
            case 2:
                return ContactsFragment.newInstance();
            case 3:
                return NearByHospitalFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "First Aid";
            case 1:
                return "Info";
            case 2:
                return "Contacts";
            case 3:
                return "Near By Hospital";
        }
        return super.getPageTitle(position);
    }

    /**
     * On each Fragment instantiation we are saving the reference of that Fragment in a Map
     * It will help us to retrieve the Fragment by position
     *
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    /**
     * Remove the saved reference from our Map on the Fragment destroy
     *
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }


    /**
     * Get the Fragment by position
     *
     * @param position tab position of the fragment
     * @return
     */
    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}
