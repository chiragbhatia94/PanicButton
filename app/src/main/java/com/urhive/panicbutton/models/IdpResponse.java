/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.urhive.panicbutton.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

/**
 * A container that encapsulates the result of authenticating with an Identity Provider.
 */
public class IdpResponse implements Parcelable {

    public static final Creator<IdpResponse> CREATOR = new Creator<IdpResponse>() {
        @Override
        public IdpResponse createFromParcel(Parcel in) {
            return new IdpResponse(
                    in.readString(),
                    in.readString(),
                    in.readString(),
                    in.readString()
            );
        }

        @Override
        public IdpResponse[] newArray(int size) {
            return new IdpResponse[size];
        }
    };
    private final String mProviderId;
    private final String mEmail;
    private final String mToken;
    private final String mSecret;

    public IdpResponse(
            String providerId,
            String email,
            String token,
            String secret) {
        mProviderId = providerId;
        mEmail = email;
        mToken = token;
        mSecret = secret;
    }

    /**
     * Get the type of provider.
     */
    public String getProviderType() {
        return mProviderId;
    }

    /**
     * Get the email used to sign in.
     */
    public String getEmail() {
        return mEmail;
    }

    /**
     * Get the token received as a result of logging in with the specified IDP
     */
    @Nullable
    public String getIdpToken() {
        return mToken;
    }

    /**
     * Twitter only. Return the token secret received as a result of logging in with Twitter.
     */
    @Nullable
    public String getIdpSecret() {
        return mSecret;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mProviderId);
        dest.writeString(mEmail);
        dest.writeString(mToken);
        dest.writeString(mSecret);
    }

    @Override
    public String toString() {
        return "IdpResponse{" +
                "mProviderId='" + mProviderId + '\'' +
                ", mEmail='" + mEmail + '\'' +
                ", mToken='" + mToken + '\'' +
                ", mSecret=" + mSecret + '\'' +
                '}';
    }
}
