package com.miw.dit.business.impl;

import com.miw.dit.business.AttendeeServices;
import com.miw.dit.business.BusinessFactory;
import com.miw.dit.business.CategoryServices;
import com.miw.dit.business.EventServices;
import com.miw.dit.business.PlaceServices;

/**
 * Created by Dani on 7/2/15.
 */
public class BusinessFactoryImpl implements BusinessFactory {

    @Override
    public CategoryServices getCategoryServices() {
        return new CategoryServicesImpl();
    }

    @Override
    public EventServices getEventServices() {
        return new EventServicesImpl();
    }

    @Override
    public AttendeeServices getAttendeeServices() {
        return new AttendeeServicesImpl();
    }

    @Override
    public PlaceServices getPlacesServices() {
        return new PlaceServicesImpl();
    }
}
