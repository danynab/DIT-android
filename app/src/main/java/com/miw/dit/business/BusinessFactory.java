package com.miw.dit.business;

/**
 * Created by Dani on 7/2/15.
 */
public interface BusinessFactory {

    public CategoryServices getCategoryServices();

    public EventServices getEventServices();

    public AttendeeServices getAttendeeServices();

    public PlaceServices getPlacesServices();
}
