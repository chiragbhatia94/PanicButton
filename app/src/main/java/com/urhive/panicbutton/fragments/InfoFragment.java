package com.urhive.panicbutton.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.urhive.panicbutton.R;
import com.urhive.panicbutton.activities.EditInfoActivity;
import com.urhive.panicbutton.helpers.DBHelper;
import com.urhive.panicbutton.helpers.UIHelper;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.urhive.panicbutton.helpers.DBHelper.AGE_GENDER;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoFragment extends FragmentBase {

    private static final String TAG = "InfoFragment";

    private TextView ageGenderTV, nameTV, addressTV, bloodGroupTV, medicalNotesTV, editTV;
    private CircleImageView iv;

    private SharedPreferences prefs;

    public InfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HiveFragment.
     */
    public static InfoFragment newInstance() {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        /*args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);*/
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        iv = (CircleImageView) view.findViewById(R.id.profileIV);
        editTV = (TextView) view.findViewById(R.id.editTV);
        editTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.startActivity(getActivity(), EditInfoActivity.class);
            }
        });

        nameTV = (TextView) view.findViewById(R.id.nameTV);
        ageGenderTV = (TextView) view.findViewById(R.id.ageGenderTV);
        addressTV = (TextView) view.findViewById(R.id.setAddressTV);
        bloodGroupTV = (TextView) view.findViewById(R.id.bloodGroupTV);
        medicalNotesTV = (TextView) view.findViewById(R.id.setMedicalNotesTV);

        FirebaseUser mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        assert mFirebaseUser != null;
        if (mFirebaseUser.getPhotoUrl() != null) {
            Glide.with(getContext()).load(mFirebaseUser.getPhotoUrl()).crossFade().fitCenter()
                    .into(iv);
        }
        nameTV.setText(mFirebaseUser.getDisplayName());
        ageGenderTV.setText(prefs.getString(AGE_GENDER, getString(R.string.age_gender)));
        addressTV.setText(prefs.getString(DBHelper.ADDRESS, getString(R.string.unknown)));
        bloodGroupTV.setText(prefs.getString(DBHelper.BLOOD_GROUP, getString(R.string
                .unspecified)));
        medicalNotesTV.setText(prefs.getString(DBHelper.MEDICAL_NOTES, getString(R.string
                .unknown)));

        return view;
    }
}
