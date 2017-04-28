package com.urhive.panicbutton.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.urhive.panicbutton.R;
import com.urhive.panicbutton.helpers.DBHelper;
import com.urhive.panicbutton.helpers.UIHelper;
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
    private String mode;

    public ContactsRecyclerViewAdapter(Context context, List<IceContact> contacts) {
        this.context = context;
        this.contacts = contacts;
        this.mode = "NORMAL";
    }

    public ContactsRecyclerViewAdapter(Context context, List<IceContact> contacts, String mode) {
        this.context = context;
        this.contacts = contacts;
        this.mode = mode;
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
        final int correctedPosition = holder.getLayoutPosition();
        final IceContact contact = contacts.get(correctedPosition);
        holder.contactName.setText(contact.getContactName());
        holder.contactNumberTV.setText(contact.getContactNumber());
        if (mode.equals("NORMAL")) {
            holder.contactRL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIHelper.makeCall(context, contacts.get(correctedPosition).getContactNumber());
                }
            });
        } else {
            holder.deleteContactIV.setVisibility(View.VISIBLE);
            holder.deleteContactIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    contacts.remove(correctedPosition);
                    DBHelper.updateContactsToPreferences(context, contacts);
                    notifyDataSetChanged();
                }
            });
        }
        if (contact.getImageURI() != null)
            holder.contactDPIV.setImageURI(Uri.parse(contact.getImageURI()));
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ContactsHolder extends RecyclerView.ViewHolder {
        public RelativeLayout contactRL;
        public CircleImageView contactDPIV;
        public TextView contactName;
        public TextView contactNumberTV;
        public ImageView deleteContactIV;

        public ContactsHolder(View itemView) {
            super(itemView);
            this.contactRL = (RelativeLayout) itemView.findViewById(R.id.contactRL);
            this.contactDPIV = (CircleImageView) itemView.findViewById(R.id.contactDPIV);
            this.contactName = (TextView) itemView.findViewById(R.id.contactName);
            this.contactNumberTV = (TextView) itemView.findViewById(R.id.contactNumberTV);
            this.deleteContactIV = (ImageView) itemView.findViewById(R.id.deleteContactIV);
        }
    }
}
