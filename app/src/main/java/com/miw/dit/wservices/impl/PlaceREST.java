package com.miw.dit.wservices.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miw.dit.model.Place;
import com.miw.dit.wservices.PlaceWS;
import com.miw.dit.wservices.impl.http.HttpGet;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dani on 18/2/15.
 */
public class PlaceREST implements PlaceWS {

    @Override
    public List<Place> findByCategory(int categoryId, double lat, double lng, double radius, Integer elements) {
        HttpGet httpGet = new HttpGet(WServicesUtil.getPlacesByCategoryUrl(categoryId));
        String json = httpGet.execute();
        Gson gson = new Gson();
        Type linesList = new TypeToken<List<Place>>() {
        }.getType();
        List<Place> result = gson.fromJson(json, linesList);
        return result == null ? new ArrayList<Place>() : result;
    }
}
