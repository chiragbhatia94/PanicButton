package com.urhive.panicbutton.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.urhive.panicbutton.R;
import com.urhive.panicbutton.helpers.AuthHelper;
import com.urhive.panicbutton.helpers.UIHelper;
import com.urhive.panicbutton.models.IdpProvider;
import com.urhive.panicbutton.models.IdpResponse;
import com.urhive.panicbutton.providers.GoogleProvider;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignInFragment extends FragmentBase implements OnCompleteListener<AuthResult> {

    private static final String TAG = "SignInFragment";
    private GoogleProvider googleProvider;
    private ConstraintLayout mainCL;
    private Button googleBtn;
    private FirebaseAuth mFirebaseAuth;

    public SignInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HiveFragment.
     */
    public static SignInFragment newInstance() {
        SignInFragment fragment = new SignInFragment();
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

        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        googleProvider = new GoogleProvider(getActivity());

        mainCL = (ConstraintLayout) view.findViewById(R.id.mainCL);
        googleBtn = (Button) view.findViewById(R.id.google_button);
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });
        return view;
    }


    public void signInWithGoogle() {
        Log.d(TAG, "signInWithGoogle: Clicked on SignInWithGoogle");
        if (UIHelper.isOffline(getActivity())) {
            Snackbar.make(mainCL, R.string.u_r_currently_offline, Snackbar.LENGTH_SHORT).show();
            return;
        }
        // showProgressDialog();
        googleProvider.startLogin(getActivity());
        googleProvider.setAuthenticationCallback(new IdpProvider.IdpCallback() {
            @Override
            public void onSuccess(IdpResponse idpResponse, final Bundle bundle) {
                final GoogleSignInAccount acct = (GoogleSignInAccount) bundle.get
                        ("GoogleSignInAccount");
                if (acct != null) {
                    AuthHelper.handleGoogleAccount(acct, mFirebaseAuth, getActivity(),
                            SignInFragment.this);
                }
            }

            @Override
            public void onFailure(Bundle extra) {
                // Signed out, show unauthenticated UI.
                // hideProgressDialog();
                Snackbar.make(mainCL, R.string.sign_in_cancelled, Snackbar.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        Log.d(TAG, "onComplete: this is stupid " + task.isSuccessful());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        googleProvider.onActivityResult(requestCode, resultCode, data);
    }
}
