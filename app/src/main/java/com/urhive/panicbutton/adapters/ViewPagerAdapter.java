package com.urhive.panicbutton.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.urhive.panicbutton.R;

import static com.google.android.gms.internal.zzt.TAG;

/**
 * Created by Chirag Bhatia on 15-04-2017.
 */

public class ViewPagerAdapter extends PagerAdapter {

    private LayoutInflater inflater;
    private View item_view;
    private View view;

    public ViewPagerAdapter(Context context, View view) {
        inflater = LayoutInflater.from(context);
        this.view = view;
    }

    public Object instantiateItem(ViewGroup collection, int position) {
        ModelObject modelObject = ModelObject.values()[position];
        item_view = (ViewGroup) inflater.inflate(modelObject.getLayoutResId(), collection, false);
        collection.addView(item_view);
        if (position == 2) {
            Button btn = (Button) item_view.findViewById(R.id.tempBtn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "onClick: hogya mera kaam");
                }
            });
        }
        return item_view;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);
    }

    private enum ModelObject {
        RED(R.string.first_aid, R.layout.first_aid_layout), BLUE(R.string.profile, R.layout
                .profile_layout), GREEN(R.string.ice_contacts, R.layout.emergency_contacts_layout);

        private int mTitleResId;
        private int mLayoutResId;

        ModelObject(int titleResId, int layoutResId) {
            mTitleResId = titleResId;
            mLayoutResId = layoutResId;
        }

        public int getTitleResId() {
            return mTitleResId;
        }

        public int getLayoutResId() {
            return mLayoutResId;
        }

    }
}