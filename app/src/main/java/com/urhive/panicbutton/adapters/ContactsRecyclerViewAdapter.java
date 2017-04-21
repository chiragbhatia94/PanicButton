package com.urhive.panicbutton.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.urhive.panicbutton.R;
import com.urhive.panicbutton.models.IceContact;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Chirag Bhatia on 16-04-2017.
 */

public class ContactsRecyclerViewAdapter extends RecyclerView.Adapter<ContactsRecyclerViewAdapter
        .ContactsHolder> {

    private static final String TAG = "StepRecyclerViewAdapter";

    private Context context;
    private List<IceContact> contacts;

    public ContactsRecyclerViewAdapter(Context context, List<IceContact> contacts) {
        this.context = context;
        this.contacts = contacts;
    }

    @Override
    public ContactsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.contact_list_view_item, parent,
                false);
        return new ContactsHolder(v);
    }

    @Override
    public void onBindViewHolder(final ContactsHolder holder, final int position) {
        IceContact contact = contacts.get(holder.getLayoutPosition());
        Log.i(TAG, "onBindViewHolder: " + contact.toString());
        holder.contactName.setText(contact.getContactName());
        holder.contactNumberTV.setText(contact.getContactNumber());

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ContactsHolder extends RecyclerView.ViewHolder {
        public CircleImageView contactDPIV;
        public TextView contactName;
        public TextView contactNumberTV;

        public ContactsHolder(View itemView) {
            super(itemView);
            this.contactDPIV = (CircleImageView) itemView.findViewById(R.id.contactDPIV);
            this.contactName = (TextView) itemView.findViewById(R.id.contactName);
            this.contactNumberTV = (TextView) itemView.findViewById(R.id.contactNumberTV);
        }
    }
}
