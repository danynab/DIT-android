package com.miw.dit.business.impl;

import com.miw.dit.business.PlaceServices;
import com.miw.dit.infrastructure.Factories;
import com.miw.dit.model.Place;

import java.util.List;

/**
 * Created by Dani on 18/2/15.
 */
public class PlaceServicesImpl implements PlaceServices {
    @Override
    public List<Place> getPlacesByCategoryId(int categoryId, double lat, double lng, double radius, Integer elements) {
        return Factories.getWServicesFactory().getPlaceWS().findByCategory(categoryId, lat, lng, radius, elements);
    }
}
