package com.miw.dit.presentation.task;

import android.os.AsyncTask;

import com.miw.dit.infrastructure.Factories;

/**
 * Created by Dani on 7/2/15.
 */
public class DeleteAttendeeTask extends AsyncTask<Void, Void, Void> {

    private int eventId;
    private String userId;

    public DeleteAttendeeTask(int eventId, String userId) {
        this.eventId = eventId;
        this.userId = userId;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Factories.getBusinessFactory().getAttendeeServices().deleteAttendee(eventId, userId);
        return null;
    }

}
