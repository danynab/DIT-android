package com.miw.dit.presentation.task;

import android.os.AsyncTask;

import com.miw.dit.infrastructure.Factories;
import com.miw.dit.model.Attendee;

/**
 * Created by Dani on 7/2/15.
 */
public class SaveAttendeeTask extends AsyncTask<Void, Void, Void> {

    private Attendee attendee;

    public SaveAttendeeTask(Attendee attendee) {
        this.attendee = attendee;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Factories.getBusinessFactory().getAttendeeServices().saveAttendee(attendee);
        return null;
    }

}
