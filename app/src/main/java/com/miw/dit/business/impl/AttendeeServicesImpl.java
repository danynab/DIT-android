package com.miw.dit.business.impl;

import com.miw.dit.business.AttendeeServices;
import com.miw.dit.infrastructure.Factories;
import com.miw.dit.model.Attendee;
import com.miw.dit.model.Event;

import java.util.List;

/**
 * Created by Dani on 18/2/15.
 */
public class AttendeeServicesImpl implements AttendeeServices {
    @Override
    public List<Attendee> getAttendeesByEvent(Event event) {
        return Factories.getWServicesFactory().getAttendeeWS().findByEvent(event.getId());
    }

    @Override
    public void saveAttendee(Attendee attendee) {
        Factories.getWServicesFactory().getAttendeeWS().save(attendee);
    }

    @Override
    public void deleteAttendee(int eventId, String userId) {
        Factories.getWServicesFactory().getAttendeeWS().delete(eventId, userId);
    }
}
