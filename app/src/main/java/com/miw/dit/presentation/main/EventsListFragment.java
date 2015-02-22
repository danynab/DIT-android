package com.miw.dit.presentation.main;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.miw.dit.R;
import com.miw.dit.infrastructure.Conf;
import com.miw.dit.model.Attendee;
import com.miw.dit.model.Category;
import com.miw.dit.model.Event;
import com.miw.dit.presentation.task.DeleteAttendeeTask;
import com.miw.dit.presentation.task.DeleteEventTask;
import com.miw.dit.presentation.task.LoadEventsAttendedTask;
import com.miw.dit.presentation.task.LoadEventsListener;
import com.miw.dit.presentation.task.LoadNearEventsTask;
import com.miw.dit.presentation.task.LoadUserEventsTask;
import com.miw.dit.presentation.task.SaveAttendeeTask;

import java.util.List;

/**
 * Created by Dani on 10/2/15.
 */
public class EventsListFragment extends Fragment implements LoadEventsListener, EventsAdapter.EventsAdapterListener {

    private static final String ARG_CATEGORY = "category";
    private static final String ARG_LATITUDE = "latitude";
    private static final String ARG_LONGITUDE = "longitude";
    private static final String ARG_ATTENDANCES = "attendaces";

    public static EventsListFragment newEventsByCategoryInstance(Category category, double lat, double lng) {
        EventsListFragment fragment = new EventsListFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CATEGORY, category);
        args.putDouble(ARG_LATITUDE, lat);
        args.putDouble(ARG_LONGITUDE, lng);
        fragment.setArguments(args);
        return fragment;
    }

    public static EventsListFragment newNearEventsInstance(double lat, double lng) {
        EventsListFragment fragment = new EventsListFragment();
        Bundle args = new Bundle();
        args.putDouble(ARG_LATITUDE, lat);
        args.putDouble(ARG_LONGITUDE, lng);
        fragment.setArguments(args);
        return fragment;
    }

    public static EventsListFragment newEventsOfUserInstance() {
        EventsListFragment fragment = new EventsListFragment();
        return fragment;
    }

    public static EventsListFragment newAttendedEventsOfUserInstance() {
        EventsListFragment fragment = new EventsListFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_ATTENDANCES, true);
        fragment.setArguments(args);
        return fragment;
    }

    private EventListFragmentCallback callback;

    private Category category;
    private String userId;
    private String profileImage;
    private double lat;
    private double lng;
    private boolean attendees;

    private RecyclerView recyclerViewEvents;
    private EventsAdapter eventsAdapter;
    private LinearLayout watermarkNoEvents;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category = getArguments().getParcelable(ARG_CATEGORY);
            lat = getArguments().getDouble(ARG_LATITUDE);
            lng = getArguments().getDouble(ARG_LONGITUDE);
            attendees = getArguments().getBoolean(ARG_ATTENDANCES, false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        SharedPreferences sharedPreferences = inflater.getContext().getSharedPreferences(Conf.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(Conf.USER_PREFERENCE, "");
        profileImage = sharedPreferences.getString(Conf.PROFILE_IMAGE_PREFERENCE, "");

        View rootView = inflater.inflate(R.layout.fragment_list_events, container, false);

        recyclerViewEvents = (RecyclerView) rootView.findViewById(R.id.recycler_view_events);
        LinearLayoutManager recyclerlinearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewEvents.setLayoutManager(recyclerlinearLayoutManager);

        FloatingActionButton buttonNewEvent = (FloatingActionButton) rootView.findViewById(R.id.button_new_event);
        buttonNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null)
                    callback.onNewEventClick();
            }
        });

        watermarkNoEvents = (LinearLayout) rootView.findViewById(R.id.watermark_no_events);

        executeGetEvents();

        return rootView;
    }


    @Override
    public void onLoadEvents(List<Event> events) {
        if (eventsAdapter == null) {
            eventsAdapter = new EventsAdapter(getActivity(), this, events, userId);
            recyclerViewEvents.setAdapter(eventsAdapter);
            callback.onLoadEvents();
            if (events.isEmpty())
                setFullEventsAdapter();
            if (eventsAdapter.getItemCount() == 0)
                watermarkNoEvents.setVisibility(View.VISIBLE);
            else
                watermarkNoEvents.setVisibility(View.GONE);
        } else {
            if (events.isEmpty())
                setFullEventsAdapter();
            else {
                eventsAdapter.addEvents(events);
            }
        }
    }


    private void setFullEventsAdapter() {
        eventsAdapter.setFull();
        if (eventsAdapter.getItemCount() > 0)
            Toast.makeText(getActivity(), "There are not more events", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBindLoadingEvent(Event lastEvent) {
        executeGetEvents(lastEvent.getId());
    }

    @Override
    public void onButtonDetailsClick(Event event) {
        callback.onDetailsEventClick(event);
    }

    @Override
    public void onButtonDeleteClick(Event event, Button button) {
        new DeleteEventTask(event).execute();
        eventsAdapter.removeEvent(event);
        Toast.makeText(getActivity(), "Event deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onButtonAttendClick(Event event, Button button) {
        button.setText("Drop out");
        Attendee attendee = new Attendee();
        attendee.setEventId(event.getId());
        attendee.setUserId(userId);
        attendee.setProfileImage(profileImage);
        new SaveAttendeeTask(attendee).execute();
    }

    @Override
    public void onButtonDropOutClick(Event event, Button button) {
        if (attendees)
            eventsAdapter.removeEvent(event);
        else
            button.setText("Attend");
        new DeleteAttendeeTask(event.getId(), userId).execute();
    }


    private void executeGetEvents() {
        executeGetEvents(null);
    }

    private void executeGetEvents(Integer lastId) {
        Log.d(getClass().getSimpleName(), "executeGetEvents: lat { " + lat + " }, lng { " + lng + " }");
        if (lat == 0 || lng == 0) {
            if (userId != null) {
                if (attendees)
                    new LoadEventsAttendedTask(userId, lastId, Conf.ELEMENTS_EVENTS, this).execute();
                else
                    new LoadUserEventsTask(this, userId, lastId, Conf.ELEMENTS_EVENTS).execute();
            }
        } else if (category != null)
            new LoadNearEventsTask(this, category.getId(), lat, lng, Conf.RADIUS_EVENTS, lastId, Conf.ELEMENTS_EVENTS).execute();
        else
            new LoadNearEventsTask(this, lat, lng, Conf.RADIUS_EVENTS, lastId, Conf.ELEMENTS_EVENTS).execute();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callback = (EventListFragmentCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement EventListFragmentCallback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    public interface EventListFragmentCallback {
        public void onNewEventClick();

        public void onDetailsEventClick(Event event);

        public void onLoadEvents();
    }
}
