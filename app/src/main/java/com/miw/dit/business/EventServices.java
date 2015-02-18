package com.miw.dit.business;

import com.miw.dit.model.Event;

import java.util.List;

/**
 * Created by Dani on 11/2/15.
 */
public interface EventServices {

    public List<Event> getNearEvents(double lat, double lng, double radius, Integer fromId, Integer elements);

    public List<Event> getNearEventsByCategory(int categoryId, double lat, double lng, double radius, Integer fromId, Integer elements);

    public List<Event> getEventsByUser(String userId, Integer fromId, Integer elements);

    public List<Event> getEventsAttendedByUser(String userId, Integer fromId, Integer elements);

    public void saveEvent(Event event);

    public void updateEvent(Event event);

    public void deleteEvent(Event event);
}
