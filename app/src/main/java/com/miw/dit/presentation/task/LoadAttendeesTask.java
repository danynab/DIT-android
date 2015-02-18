package com.miw.dit.presentation.task;

import android.os.AsyncTask;

import com.miw.dit.infrastructure.Factories;
import com.miw.dit.model.Attendee;
import com.miw.dit.model.Event;

import java.util.List;

/**
 * Created by Dani on 7/2/15.
 */
public class LoadAttendeesTask extends AsyncTask<Void, Void, List<Attendee>> {

    private LoadAttendeesListener callback;

    private Event event;

    public LoadAttendeesTask(Event event, LoadAttendeesListener callback) {
        this.callback = callback;
        this.event = event;
    }

    @Override
    protected List<Attendee> doInBackground(Void... params) {
        List<Attendee> attendees = Factories.getBusinessFactory().getAttendeeServices().getAttendeesByEvent(event);
        return attendees;
    }

    @Override
    protected void onPostExecute(List<Attendee> attendees) {
        super.onPostExecute(attendees);
        callback.onLoadAttendees(attendees);
    }

    public interface LoadAttendeesListener {
        public void onLoadAttendees(List<Attendee> attendees);
    }
}
