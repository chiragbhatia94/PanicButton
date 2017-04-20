package com.urhive.panicbutton.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.urhive.panicbutton.R;
import com.urhive.panicbutton.models.Step;

import java.util.List;

/**
 * Created by Chirag Bhatia on 20-04-2017.
 */

public class StepsRecyclerViewAdapter extends RecyclerView.Adapter<StepsRecyclerViewAdapter
        .StepsHolder> {

    private static final String TAG = "StepRecyclerViewAdapter";

    private Context context;
    private List<Step> steps;

    private StorageReference mRef;

    public StepsRecyclerViewAdapter(Context context, List<Step> steps) {
        this.context = context;
        this.steps = steps;
        this.mRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public StepsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.step_view_item, parent, false);
        return new StepsHolder(v);
    }

    @Override
    public void onBindViewHolder(final StepsHolder holder, final int position) {
        Step step = steps.get(holder.getLayoutPosition());
        Log.i(TAG, "onBindViewHolder: " + step.toString());
        holder.stepNumberTV.setText(String.valueOf(holder.getLayoutPosition() + 1));
        String photoURI = step.getPhoto();
        if (photoURI != null) {
            Glide.with(context).using(new FirebaseImageLoader()).load(mRef.child(photoURI))
                    .fitCenter().into(holder.stepIV);
        }
        String stepText = step.getText();
        if (stepText != null) {
            holder.stepTV.setText(stepText);
        }
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public class StepsHolder extends RecyclerView.ViewHolder {
        public TextView stepNumberTV;
        public TextView stepTV;
        public ImageView stepIV;

        public StepsHolder(View itemView) {
            super(itemView);
            this.stepNumberTV = (TextView) itemView.findViewById(R.id.stepNoTV);
            this.stepTV = (TextView) itemView.findViewById(R.id.stepTV);
            this.stepIV = (ImageView) itemView.findViewById(R.id.stepIV);
        }
    }
}
