package com.urhive.panicbutton.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.urhive.panicbutton.R;
import com.urhive.panicbutton.helpers.UIHelper;
import com.urhive.panicbutton.models.IceContact;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Chirag Bhatia on 16-04-2017.
 */

public class ContactsAdapter extends BaseAdapter {

    Context context;
    ArrayList<IceContact> contacts;

    public ContactsAdapter(Context context, ArrayList<IceContact> contacts) {
        super();
        this.context = context;
        this.contacts = contacts;
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.contact_list_view_item, null, false);

        CircleImageView dp = (CircleImageView) view.findViewById(R.id.contactDPIV);
        // Glide.with(context).load(contacts.get(position).getImageURI()).fitCenter().into(dp);

        TextView contactName = (TextView) view.findViewById(R.id.contactName);
        contactName.setText(contacts.get(position).getContactName());

        TextView contactNumber = (TextView) view.findViewById(R.id.contactNumberTV);
        contactNumber.setText(contacts.get(position).getContactNumber());

        RelativeLayout mainRL = (RelativeLayout) view.findViewById(R.id.contactRL);
        mainRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.makeCall(context, contacts.get(position).getContactNumber());
            }
        });
        return view;
    }
}
