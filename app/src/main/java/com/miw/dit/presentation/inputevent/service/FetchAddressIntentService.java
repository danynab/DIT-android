package com.miw.dit.presentation.inputevent.service;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Dani on 14/2/15.
 */
public class FetchAddressIntentService extends IntentService {


    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;

    public static final String ARG_RECEIVER = "receiver";
    public static final String ARG_LATITUDE = "latitude";
    public static final String ARG_LONGITUDE = "longitude";

    public static final String RESULT_ADDRESS = "address";


    public FetchAddressIntentService() {
        super("FetchAddressIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle bundle = new Bundle();
        List<Address> addresses = null;

        ResultReceiver receiver = intent.getParcelableExtra(ARG_RECEIVER);
        double lat = intent.getDoubleExtra(ARG_LATITUDE, 0);
        double lng = intent.getDoubleExtra(ARG_LONGITUDE, 0);
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
        } catch (IOException ioException) {
            Log.e(getClass().getSimpleName(), ioException.getMessage());
            receiver.send(FAILURE_RESULT, bundle);
        }


        if (addresses == null || addresses.size() == 0) {
            Log.e(getClass().getSimpleName(), "No se ha encontrado dirección - Lat { " + lat + " }, Lng { " + lng + " }");
            receiver.send(FAILURE_RESULT, bundle);
        } else {
            Address address = addresses.get(0);
            List<String> addressFragments = new ArrayList<>();

            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
            String addressStr = TextUtils.join(System.getProperty("line.separator"),
                    addressFragments);

            Log.d(getClass().getName(), "Address { " + addressStr + " }");
            bundle.putString(RESULT_ADDRESS, addressStr);
            receiver.send(SUCCESS_RESULT, bundle);
        }
    }
}
