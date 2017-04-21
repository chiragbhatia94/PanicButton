package com.urhive.panicbutton.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.urhive.panicbutton.models.Step;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirstAidStepsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstAidStepsFragment extends FragmentBase {

    private static final String TAG = "FirstAidFragment";

    private static final String EMERGENCY_NAME = "emergencyName";

    private String emergencyName;

    public FirstAidStepsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HiveFragment.
     */
    public static FirstAidStepsFragment newInstance(String emergencyName) {
        FirstAidStepsFragment fragment = new FirstAidStepsFragment();
        Bundle args = new Bundle();
        args.putString(EMERGENCY_NAME, emergencyName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            emergencyName = getArguments().getString(EMERGENCY_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first_aid_steps, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.stepsRV);
        DatabaseReference stepsRef = FirebaseDatabase.getInstance().getReference().child(DBHelper
                .EMERGENCY).child(emergencyName).child(DBHelper.STEPS);
        FirebaseRecyclerAdapter<Step, StepsHolder> mAdapter = new FirebaseRecyclerAdapter<Step,
                StepsHolder>(Step.class, R.layout.step_view_item, StepsHolder.class, stepsRef) {
            @Override
            protected void populateViewHolder(StepsHolder viewHolder, Step model, int position) {
                viewHolder.setContext(getContext());
                viewHolder.setStepNumber(position);
                viewHolder.setStepText(model.getText());
                viewHolder.setImage(model.getPhoto());
            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter);
        return view;
    }

    private static class StepsHolder extends RecyclerView.ViewHolder {
        private final TextView stepNumberTV;
        private final TextView stepTV;
        private final ImageView stepIV;
        private Context context;
        private StorageReference mRef;

        public StepsHolder(View itemView) {
            super(itemView);
            this.mRef = FirebaseStorage.getInstance().getReference();
            this.stepNumberTV = (TextView) itemView.findViewById(R.id.stepNoTV);
            this.stepTV = (TextView) itemView.findViewById(R.id.stepTV);
            this.stepIV = (ImageView) itemView.findViewById(R.id.stepIV);
        }

        public void setStepNumber(int number) {
            stepNumberTV.setText(String.valueOf(number + 1));
        }

        public void setStepText(String text) {
            stepTV.setText(text);
        }

        public void setImage(String url) {
            if (url != null && !url.isEmpty()) {
                Glide.with(context).using(new FirebaseImageLoader()).load(mRef.child(url))
                        .fitCenter().into(stepIV);
            }
        }

        public void setContext(Context context) {
            this.context = context;
        }
    }
}
