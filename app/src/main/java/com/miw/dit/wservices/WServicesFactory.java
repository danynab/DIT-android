package com.miw.dit.wservices;

/**
 * Created by Dani on 7/2/15.
 */
public interface WServicesFactory {

    public CategoryWS getCategoryWS();

    public EventWS getEventWS();

    public AttendeeWS getAttendeeWS();

    public PlaceWS getPlaceWS();
}
