package com.urhive.panicbutton.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.urhive.panicbutton.R;
import com.urhive.panicbutton.helpers.DBHelper;
import com.urhive.panicbutton.models.Emergency;
import com.urhive.panicbutton.models.IceContact;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.urhive.panicbutton.R.id.tv;

/**
 * Created by Chirag Bhatia on 15-04-2017.
 */

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater inflater;
    private View item_view;
    private View view;

    public ViewPagerAdapter(Context context, View view) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.view = view;
    }

    public Object instantiateItem(ViewGroup collection, int position) {
        ModelObject modelObject = ModelObject.values()[position];
        item_view = (ViewGroup) inflater.inflate(modelObject.getLayoutResId(), collection, false);
        collection.addView(item_view);
        FirebaseUser mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (position == 0) {
            RecyclerView rv = (RecyclerView) view.findViewById(R.id.recyclerView);
            rv.setHasFixedSize(false);
            // rv.setLayoutManager(new LinearLayoutManager(context));
            rv.setLayoutManager(new GridLayoutManager(context, 2));

            DatabaseReference rootref = FirebaseDatabase.getInstance().getReference().child
                    (DBHelper.EMERGENCY);

            FirebaseRecyclerAdapter<Emergency, EmergencyHolder> mAdapter = new
                    FirebaseRecyclerAdapter<Emergency, EmergencyHolder>(Emergency.class, R.layout
                            .emergency_list_view_item, EmergencyHolder.class, rootref) {
                @Override
                protected void populateViewHolder(EmergencyHolder viewHolder, Emergency model,
                                                  int position) {
                    viewHolder.setContext(context);
                    viewHolder.setName(model.getName());
                    viewHolder.setImage(model.getPhoto());
                    viewHolder.setClickListener(model);
                }
            };

            rv.setAdapter(mAdapter);
        } else if (position == 1) {
            CircleImageView iv = (CircleImageView) item_view.findViewById(R.id.profileIV);
            assert mFirebaseUser != null;
            if (mFirebaseUser.getPhotoUrl() != null) {
                Glide.with(context).load(mFirebaseUser.getPhotoUrl()).fitCenter().into(iv);
            }
        } else if (position == 2) {
            // ICE Contact Page
            ListView listView = (ListView) item_view.findViewById(R.id.contactListView);
            ArrayList<IceContact> contactsList = new ArrayList<>();
            contactsList.add(new IceContact(null, "Chirag Bhatia", "9893604590"));
            contactsList.add(new IceContact(null, "Mahesh Bhatia", "9827562730"));
            contactsList.add(new IceContact(null, "Yash Bhatia", "9039563022"));
            listView.setAdapter(new ContactsAdapter(context, contactsList));
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

    private static class EmergencyHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        private final TextView nameTV;
        private final ImageView imageIV;
        private Context context;

        public EmergencyHolder(View itemView) {
            super(itemView);
            this.cardView = (CardView) itemView.findViewById(R.id.cardView);
            this.nameTV = (TextView) itemView.findViewById(tv);
            this.imageIV = (ImageView) itemView.findViewById(R.id.iv);
        }

        public void setName(String name) {
            nameTV.setText(name);
        }

        public void setImage(String url) {
            Glide.with(context).load(url).fitCenter().into(imageIV);
        }

        public void setContext(Context context) {
            this.context = context;
        }

        public void setClickListener(Emergency emergency) {
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // here you have to display the list of steps
                }
            });
        }
    }
}