package com.urhive.panicbutton.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.urhive.panicbutton.R;
import com.urhive.panicbutton.helpers.DBHelper;
import com.urhive.panicbutton.models.Emergency;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirstAidFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstAidFragment extends FragmentBase {
    private static final String TAG = "FirstAidFragment";

    public FirstAidFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HiveFragment.
     */
    public static FirstAidFragment newInstance() {
        FirstAidFragment fragment = new FirstAidFragment();
        Bundle args = new Bundle();
        /*args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);*/
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            /*mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);*/
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first_aid, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.stepsRV);
        DatabaseReference emergencyRef = FirebaseDatabase.getInstance().getReference().child
                (DBHelper.EMERGENCY);
        FirebaseRecyclerAdapter<Emergency, EmergencyHolder> mAdapter = new
                FirebaseRecyclerAdapter<Emergency, EmergencyHolder>(Emergency.class, R.layout
                        .emergency_list_view_item, EmergencyHolder.class, emergencyRef) {
            @Override
            protected void populateViewHolder(EmergencyHolder viewHolder, Emergency model, int
                    position) {
                viewHolder.setContext(getContext());
                viewHolder.setName(model.getName());
                viewHolder.setImage(model.getPhoto());
                Log.i(TAG, "populateViewHolder: " + model.getPhoto());
                viewHolder.setClickListener(model);
            }
        };
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(mAdapter);
        return view;
    }

    private static class EmergencyHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        private final TextView nameTV;
        private final ImageView imageIV;
        private Context context;
        private StorageReference mRef;

        public EmergencyHolder(View itemView) {
            super(itemView);
            this.mRef = FirebaseStorage.getInstance().getReference();
            this.cardView = (CardView) itemView.findViewById(R.id.cardView);
            this.nameTV = (TextView) itemView.findViewById(R.id.tv);
            this.imageIV = (ImageView) itemView.findViewById(R.id.iv);
        }

        public void setName(String name) {
            nameTV.setText(name);
        }

        public void setImage(String url) {
            Log.e(TAG, "setImage: " + url);
            Glide.with(context).using(new FirebaseImageLoader()).load(mRef.child(url)).fitCenter
                    ().into(imageIV);
        }

        public void setContext(Context context) {
            this.context = context;
        }

        public void setClickListener(final Emergency emergency) {
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        /*public void setClickListener(final Emergency emergency, RecyclerView
                emergencyRecyclerView, final RelativeLayout detailEmergencyView, TextView backTV,
                                     final RecyclerView stepsRecyclerView) {
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // here you have to display the list of steps
                    detailEmergencyView.setVisibility(View.VISIBLE);
                    StepsRecyclerViewAdapter mAdapter = new StepsRecyclerViewAdapter(context,
                            emergency.getSteps());
                    stepsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                    stepsRecyclerView.setAdapter(mAdapter);
                }
            });
        }*/
    }
}
