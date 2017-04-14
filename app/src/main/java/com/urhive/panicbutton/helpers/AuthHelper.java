package com.urhive.panicbutton.helpers;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.urhive.panicbutton.models.IdpResponse;

import static com.google.android.gms.internal.zzt.TAG;

/**
 * Created by Chirag Bhatia on 20-03-2017.
 */

public class AuthHelper {
    @Nullable
    public static AuthCredential getAuthCredential(IdpResponse idpResponse) {
        switch (idpResponse.getProviderType()) {
            case GoogleAuthProvider.PROVIDER_ID:
                return GoogleAuthProvider.getCredential(idpResponse.getIdpToken(), null);

            default:
                return null;
        }
    }

    public static AuthCredential handleGoogleAccount(GoogleSignInAccount acct, FirebaseAuth
            mFirebaseAuth, Activity activity, OnCompleteListener listener) {
        Log.d(TAG, "handleGoogleAccount: " + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(activity, listener);
        return credential;
    }
}
