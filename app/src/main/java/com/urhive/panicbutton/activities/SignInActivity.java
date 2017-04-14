package com.urhive.panicbutton.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.urhive.panicbutton.R;
import com.urhive.panicbutton.helpers.AuthHelper;
import com.urhive.panicbutton.helpers.UIHelper;
import com.urhive.panicbutton.models.IdpProvider;
import com.urhive.panicbutton.models.IdpResponse;
import com.urhive.panicbutton.providers.GoogleProvider;

public class SignInActivity extends AppCompatBase implements OnCompleteListener<AuthResult> {

    private static final String TAG = "SignInActivity";
    private GoogleProvider googleProvider;
    private ConstraintLayout mainCL;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        googleProvider = new GoogleProvider(SignInActivity.this);

        mainCL = (ConstraintLayout) findViewById(R.id.mainCL);
    }

    public void signInWithGoogle(View view) {
        Log.d(TAG, "signInWithGoogle: Clicked on SignInWithGoogle");
        if (UIHelper.isOffline(SignInActivity.this)) {
            Snackbar.make(mainCL, R.string.u_r_currently_offline, Snackbar.LENGTH_SHORT).show();
            return;
        }
        showProgressDialog();
        googleProvider.startLogin(SignInActivity.this);
        googleProvider.setAuthenticationCallback(new IdpProvider.IdpCallback() {
            @Override
            public void onSuccess(IdpResponse idpResponse, final Bundle bundle) {
                final GoogleSignInAccount acct = (GoogleSignInAccount) bundle.get
                        ("GoogleSignInAccount");
                if (acct != null) {
                    AuthHelper.handleGoogleAccount(acct, mFirebaseAuth, SignInActivity.this,
                            SignInActivity.this);
                }
            }

            @Override
            public void onFailure(Bundle extra) {
                // Signed out, show unauthenticated UI.
                hideProgressDialog();
                Snackbar.make(mainCL, R.string.sign_in_cancelled, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        Log.d(TAG, "onComplete: " + task.isSuccessful());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        googleProvider.onActivityResult(requestCode, resultCode, data);
    }
}
