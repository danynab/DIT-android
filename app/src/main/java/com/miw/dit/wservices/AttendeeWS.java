package com.miw.dit.wservices;

import com.miw.dit.model.Attendee;

import java.util.List;

/**
 * Created by Dani on 17/2/15.
 */
public interface AttendeeWS {

    public List<Attendee> findByEvent(int eventId);

    public void save(Attendee attendee);

    public void delete(int eventId, String userId);
}
