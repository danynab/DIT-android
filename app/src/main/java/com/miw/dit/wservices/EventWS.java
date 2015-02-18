package com.miw.dit.wservices;

import com.miw.dit.model.Event;

import java.util.List;

/**
 * Created by Dani on 10/2/15.
 */
public interface EventWS {

    public List<Event> findNearEvents(double lat, double lng, double radius, Integer fromId, Integer elements);

    public List<Event> findNearEventsByCategoryId(int categoryId, double lat, double lng, double radius, Integer fromId, Integer elements);

    public List<Event> findEventsByUserId(String userId, Integer fromId, Integer elements);

    public List<Event> findEventsAttendedByUserId(String userId, Integer fromId, Integer elements);

    public void save(Event event);

    public void update(Event event);

    public void delete(Event event);
}
