package com.miw.dit.presentation.gapiclient;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Dani on 16/2/15.
 */
public class LocationApiClient implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient googleApiClient;
    private Activity activity;
    private LocationApiClientCallback callback;

    public LocationApiClient(Activity activity, LocationApiClientCallback callback) {
        this.activity = activity;
        this.callback = callback;
        googleApiClient = new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location lastConnection = LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);
        double lat = lastConnection.getLatitude();
        double lng = lastConnection.getLongitude();
        callback.onConnected(lat, lng);
    }

    public void connect() {
        googleApiClient.connect();
    }

    public void disconnectIfConnected() {
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public interface LocationApiClientCallback {
        public void onConnected(double lat, double lng);
    }
}
