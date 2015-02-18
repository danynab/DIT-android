package com.miw.dit.presentation.gapiclient;

import android.app.Activity;
import android.content.IntentSender;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

/**
 * Created by Dani on 7/2/15.
 */
public class PlusApiClient implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final int RC_SIGN_IN = 0;

    private GoogleApiClient googleApiClient;
    private Activity activity;
    private PlusApiClientCallback callback;

    private boolean intentInProgress;

    public PlusApiClient(Activity activity, PlusApiClientCallback callback) {
        this.activity = activity;
        this.callback = callback;
        googleApiClient = new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (Plus.PeopleApi.getCurrentPerson(googleApiClient) != null && bundle == null) {
            Person currentPerson = Plus.PeopleApi.getCurrentPerson(googleApiClient);
            String account = Plus.AccountApi.getAccountName(googleApiClient);
            callback.onConnected(currentPerson, account);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (!intentInProgress && connectionResult.hasResolution()) {
            try {
                intentInProgress = true;
                activity.startIntentSenderForResult(connectionResult.getResolution().getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                // The intent was canceled before it was sent.  Return to the default
                // state and attempt to connect to get an updated ConnectionResult.
                intentInProgress = false;
                googleApiClient.connect();
            }
        } else {
            callback.onFailed();
        }
    }

    public void onResult() {
        if (!googleApiClient.isConnecting()) {
            googleApiClient.connect();
        }
    }

    public void connect() {
        googleApiClient.connect();
    }

    public void disconnectIfConnected() {
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    public interface PlusApiClientCallback {
        public void onFailed();

        public void onConnected(Person currentPerson, String account);
    }

}
