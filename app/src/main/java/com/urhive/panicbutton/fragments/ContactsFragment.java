package com.urhive.panicbutton.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.urhive.panicbutton.R;
import com.urhive.panicbutton.activities.EditContactsActivity;
import com.urhive.panicbutton.adapters.ContactsRecyclerViewAdapter;
import com.urhive.panicbutton.helpers.DBHelper;
import com.urhive.panicbutton.helpers.UIHelper;
import com.urhive.panicbutton.models.IceContact;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactsFragment extends FragmentBase {
    RecyclerView recyclerView;

    public ContactsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HiveFragment.
     */
    public static ContactsFragment newInstance() {
        ContactsFragment fragment = new ContactsFragment();
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
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.contactsRecyclerView);

        TextView editTV = (TextView) view.findViewById(R.id.editTV);
        editTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.startActivity(getActivity(), EditContactsActivity.class);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    void updateRecyclerView() {
        List<IceContact> contactsList = DBHelper.getContactsList(getActivity());
        recyclerView.setAdapter(new ContactsRecyclerViewAdapter(getContext(), contactsList));
    }

    @Override
    public void onResume() {
        super.onResume();
        updateRecyclerView();
    }
}
