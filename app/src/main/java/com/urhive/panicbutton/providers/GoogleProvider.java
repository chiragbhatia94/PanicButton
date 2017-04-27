package com.urhive.panicbutton.providers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.GoogleAuthProvider;
import com.urhive.panicbutton.R;
import com.urhive.panicbutton.models.IdpProvider;
import com.urhive.panicbutton.models.IdpResponse;

/**
 * Created by Chirag Bhatia on 20-03-2017.
 */

public class GoogleProvider implements IdpProvider, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "GoogleProvider";
    private static final int RC_SIGN_IN_GOOGLE = 1000;
    private static final String ERROR_KEY = "error";
    private IdpCallback mIdpCallback;
    private Context mContext;
    private GoogleApiClient mGoogleApiClient;

    public GoogleProvider(FragmentActivity activity) {
        mContext = activity.getBaseContext();
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(mContext.getString(R.string.default_web_client_id))
                // dont use that i dont know why it is working in that way
                //.requestIdToken(getString(R.string.web_client_id_google))
                .requestEmail().build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        /*if (mGoogleApiClient != null) {*/
        mGoogleApiClient = new GoogleApiClient.Builder(mContext).enableAutoManage(activity /*
            FragmentActivity */, this /*
                OnConnectionFailedListener
                 */).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        /*}*/
    }

    public void disconnect() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
            mGoogleApiClient = null;
        }
    }

    @Override
    public String getName(Context context) {
        return context.getString(R.string.google);
    }

    @Override
    public String getProviderId() {
        return GoogleAuthProvider.PROVIDER_ID;
    }

    @Override
    public void setAuthenticationCallback(IdpCallback callback) {
        mIdpCallback = callback;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN_GOOGLE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result != null) {
                if (result.isSuccess()) {
                    GoogleSignInAccount acct = result.getSignInAccount();
                    assert acct != null;
                    IdpResponse mIdpResponse = new IdpResponse(GoogleAuthProvider.PROVIDER_ID,
                            acct.getEmail(), acct.getIdToken(), null);

                    Bundle bundle = new Bundle();
                    bundle.putParcelable("GoogleSignInAccount", acct);

                    mIdpCallback.onSuccess(mIdpResponse, bundle);
                } else {
                    onError(result);
                }
            } else {
                onError("No result found in intent");
            }
        }
    }

    private void onError(GoogleSignInResult result) {
        String errorMessage = result.getStatus().getStatusMessage();
        onError(result.getStatus().getStatusCode() + " " + errorMessage);
    }

    private void onError(String errorMessage) {
        Log.e(TAG, "Error logging in with Google. " + errorMessage);
        Bundle extra = new Bundle();
        extra.putString(ERROR_KEY, errorMessage);
        mIdpCallback.onFailure(extra);
    }

    @Override
    public void startLogin(Activity activity) {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        activity.startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(mContext, mContext.getString(R.string
                .there_is_some_problem_with_your_internet_connection_try_again_later), Toast
                .LENGTH_SHORT).show();
    }
}
