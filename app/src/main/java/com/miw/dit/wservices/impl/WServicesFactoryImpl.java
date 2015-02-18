package com.miw.dit.wservices.impl;

import com.miw.dit.wservices.AttendeeWS;
import com.miw.dit.wservices.CategoryWS;
import com.miw.dit.wservices.EventWS;
import com.miw.dit.wservices.PlaceWS;
import com.miw.dit.wservices.WServicesFactory;

/**
 * Created by Dani on 7/2/15.
 */
public class WServicesFactoryImpl implements WServicesFactory {

    @Override
    public CategoryWS getCategoryWS() {
        return new CategoryREST();
    }

    @Override
    public EventWS getEventWS() {
        return new EventREST();
    }

    @Override
    public AttendeeWS getAttendeeWS() {
        return new AttendeeREST();
    }

    @Override
    public PlaceWS getPlaceWS() {
        return new PlaceREST();
    }
}
