package com.miw.dit.presentation.inputevent.service;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Dani on 14/2/15.
 */
public class FetchLocationIntentService extends IntentService {


    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;

    public static final String ARG_RECEIVER = "receiver";
    public static final String ARG_ADDRESS = "address";

    public static final String RESULT_LATITUDE = "latitude";
    public static final String RESULT_LONGITUDE = "longitude";


    public FetchLocationIntentService() {
        super("FetchLocatonIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle bundle = new Bundle();
        List<Address> addresses = null;

        ResultReceiver receiver = intent.getParcelableExtra(ARG_RECEIVER);
        String addressStr = intent.getStringExtra(ARG_ADDRESS);
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocationName(addressStr, 1);
        } catch (IOException ioException) {
            Log.e(getClass().getSimpleName(), ioException.getMessage());
            receiver.send(FAILURE_RESULT, bundle);
        }

        if (addresses == null || addresses.size() == 0) {
            Log.e(getClass().getSimpleName(), "No se ha encontrado dirección");
            receiver.send(FAILURE_RESULT, bundle);
        } else {
            Address address = addresses.get(0);
            double lat = address.getLatitude();
            double lng = address.getLongitude();

            Log.d(getClass().getName(), "Lat { " + lat + " }, Lng { " + lng + " }");
            bundle.putDouble(RESULT_LATITUDE, lat);
            bundle.putDouble(RESULT_LONGITUDE, lng);
            receiver.send(SUCCESS_RESULT, bundle);
        }
    }
}
