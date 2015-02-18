package com.miw.dit.wservices;

import com.miw.dit.model.Place;

import java.util.List;

/**
 * Created by Dani on 18/2/15.
 */
public interface PlaceWS {

    public List<Place> findByCategory(int categoryId, double lat, double lng, double radius, Integer elements);

}
