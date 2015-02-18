package com.miw.dit.business;

import com.miw.dit.model.Place;

import java.util.List;

/**
 * Created by Dani on 18/2/15.
 */
public interface PlaceServices {

    public List<Place> getPlacesByCategoryId(int categoryId, double lat, double lng, double radius, Integer elements);

}
