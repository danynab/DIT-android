package com.miw.dit.business.impl;

import com.miw.dit.business.EventServices;
import com.miw.dit.infrastructure.Factories;
import com.miw.dit.model.Event;

import java.util.List;

/**
 * Created by Dani on 11/2/15.
 */
public class EventServicesImpl implements EventServices {

    @Override
    public List<Event> getNearEvents(double lat, double lng, double radius, Integer fromId, Integer elements) {
        return Factories.getWServicesFactory().getEventWS().findNearEvents(lat, lng, radius, fromId, elements);
    }

    @Override
    public List<Event> getNearEventsByCategory(int categoryId, double lat, double lng, double radius, Integer fromId, Integer elements) {
        return Factories.getWServicesFactory().getEventWS().findNearEventsByCategoryId(categoryId, lat, lng, radius, fromId, elements);
    }

    @Override
    public List<Event> getEventsByUser(String userId, Integer fromId, Integer elements) {
        return Factories.getWServicesFactory().getEventWS().findEventsByUserId(userId, fromId, elements);
    }

    @Override
    public List<Event> getEventsAttendedByUser(String userId, Integer fromId, Integer elements) {
        return Factories.getWServicesFactory().getEventWS().findEventsAttendedByUserId(userId, fromId, elements);
    }

    @Override
    public void saveEvent(Event event) {
        Factories.getWServicesFactory().getEventWS().save(event);
    }

    @Override
    public void updateEvent(Event event) {
        Factories.getWServicesFactory().getEventWS().update(event);
    }

    @Override
    public void deleteEvent(Event event) {
        Factories.getWServicesFactory().getEventWS().delete(event);
    }
}
