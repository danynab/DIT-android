package com.miw.dit.business;

import com.miw.dit.model.Attendee;
import com.miw.dit.model.Event;

import java.util.List;

/**
 * Created by Dani on 18/2/15.
 */
public interface AttendeeServices {

    public List<Attendee> getAttendeesByEvent(Event event);

    public void saveAttendee(Attendee attendee);

    public void deleteAttendee(int eventId, String userId);
}
