package com.miw.dit.presentation.details;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miw.dit.R;
import com.miw.dit.model.Attendee;
import com.miw.dit.model.Category;
import com.miw.dit.model.Event;
import com.miw.dit.model.Place;
import com.miw.dit.presentation.inputevent.InputEventActivity;
import com.miw.dit.presentation.task.DeleteAttendeeTask;
import com.miw.dit.presentation.task.DeleteEventTask;
import com.miw.dit.presentation.task.LoadCategoryTask;
import com.miw.dit.presentation.task.LoadPlacesTask;
import com.miw.dit.presentation.task.SaveAttendeeTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Dani on 18/2/15.
 */
public class DetailsEventFragment extends Fragment implements
        LoadCategoryTask.LoadCategoryListener,
        LoadPlacesTask.LoadPlacesListener {

    private static final String ARG_EVENT = "event";
    private static final String ARG_USER_ID = "userId";
    private static final String ARG_PROFILE_IMAGE = "profileImage";

    public static DetailsEventFragment newInstance(Event event, String userId, String profileImage) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(ARG_EVENT, event);
        arguments.putString(ARG_USER_ID, userId);
        arguments.putString(ARG_PROFILE_IMAGE, profileImage);
        DetailsEventFragment fragment = new DetailsEventFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    private TextView textCategory;
    private TextView textButtonAttend;
    private TextView textNoAttendees;
    private RelativeLayout layoutAttendeesRemaining;
    private ImageView imageButtonAttend;
    private TextView textAttendeesRemaining;
    private List<ImageView> imagesAttendees;
    private List<View> viewsPlaces;

    private Event event;
    private String userId;
    private String profileImage;

    private Attendee attendedUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            event = getArguments().getParcelable(ARG_EVENT);
            userId = getArguments().getString(ARG_USER_ID);
            profileImage = getArguments().getString(ARG_PROFILE_IMAGE);

            new LoadCategoryTask(event.getCategoryId(), this).execute();
            new LoadPlacesTask(event.getCategoryId(), this).execute();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details_event, container, false);

        ImageView imageHeader = (ImageView) rootView.findViewById(R.id.image_header);
        Picasso.with(getActivity()).load(event.getHeaderImage()).into(imageHeader);

        textCategory = (TextView) rootView.findViewById(R.id.text_category);

        ImageView imageProfile = (ImageView) rootView.findViewById(R.id.image_profile);
        Picasso.with(getActivity()).load(event.getProfileImage()).into(imageProfile);

        TextView textTitle = (TextView) rootView.findViewById(R.id.text_title);
        textTitle.setText(event.getTitle());

        TextView textDescription = (TextView) rootView.findViewById(R.id.text_description);
        textDescription.setText(event.getDescription());

        TextView textAddress = (TextView) rootView.findViewById(R.id.text_address);
        textAddress.setText(event.getAddress());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(event.getTime());
        java.text.DateFormat dateFormat = DateFormat.getDateFormat(inflater.getContext());
        String dateStr = dateFormat.format(calendar.getTime());
        java.text.DateFormat timeFormat = DateFormat.getTimeFormat(inflater.getContext());
        String timeStr = timeFormat.format(calendar.getTime());

        TextView textTime = (TextView) rootView.findViewById(R.id.text_time);
        textTime.setText(dateStr + " " + timeStr);

        imageButtonAttend = (ImageView) rootView.findViewById(R.id.image_button_attend);
        textButtonAttend = (TextView) rootView.findViewById(R.id.text_button_attend);
        if (event.getUserId().equals(userId)) {
            textButtonAttend.setText("Delete");
            imageButtonAttend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DeleteEventTask(event).execute();
                    Toast.makeText(getActivity(), "Event deleted", Toast.LENGTH_SHORT).show();
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
            });
            RelativeLayout layoutButtonEdit = (RelativeLayout) rootView.findViewById(R.id.layout_button_edit);
            layoutButtonEdit.setVisibility(View.VISIBLE);
            ImageView imageButtonEdit = (ImageView) rootView.findViewById(R.id.image_button_edit);
            imageButtonEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), InputEventActivity.class);
                    i.putExtra(InputEventActivity.ARG_EVENT, event);
                    startActivity(i);
                }
            });
        }

        textNoAttendees = (TextView) rootView.findViewById(R.id.text_no_attendees);
        layoutAttendeesRemaining = (RelativeLayout) rootView.findViewById(R.id.layout_attendees_remaining);
        textAttendeesRemaining = (TextView) rootView.findViewById(R.id.text_attendees_remaining);
        imagesAttendees = new ArrayList<>();
        imagesAttendees.add((ImageView) rootView.findViewById(R.id.image_attendees_first));
        imagesAttendees.add((ImageView) rootView.findViewById(R.id.image_attendees_second));
        imagesAttendees.add((ImageView) rootView.findViewById(R.id.image_attendees_third));
        imagesAttendees.add((ImageView) rootView.findViewById(R.id.image_attendees_fourth));
        imagesAttendees.add((ImageView) rootView.findViewById(R.id.image_attendees_fifth));
        viewsPlaces = new ArrayList<>();
        viewsPlaces.add(rootView.findViewById(R.id.view_place_first));
        viewsPlaces.add(rootView.findViewById(R.id.view_place_second));
        viewsPlaces.add(rootView.findViewById(R.id.view_place_third));

        loadAttendees(event.getAttendees());

        return rootView;
    }

    private void loadAttendees(List<Attendee> attendees) {
        int attendessSize = attendees.size();
        if (attendessSize > 0) {
            textNoAttendees.setVisibility(View.GONE);
            Log.d(getClass().getSimpleName(), "loadAttendees: userId { " + userId + " }");
            for (int i = 0; i < attendessSize; i++) {
                Attendee attendee = attendees.get(i);
                if (i < imagesAttendees.size()) {
                    ImageView imageAttendee = imagesAttendees.get(i);
                    Picasso.with(getActivity()).load(attendee.getProfileImage()).into(imageAttendee);
                    imageAttendee.setVisibility(View.VISIBLE);
                }
                Log.d(getClass().getSimpleName(), "loadAttendees: attendee.userId { " + attendee.getUserId() + " }");
                Log.d(getClass().getSimpleName(), "loadAttendees: userId == attendee.userId { " + userId.equals(attendee.getUserId()) + " }");
                if (attendedUser == null && userId.equals(attendee.getUserId())) {
                    attendedUser = attendee;
                }
            }
            if (attendessSize > imagesAttendees.size()) {
                ImageView lastImageAttendee = imagesAttendees.get(imagesAttendees.size() - 1);
                lastImageAttendee.setVisibility(View.GONE);
                String remainingAttendeesStr = "+" + (attendessSize - imagesAttendees.size() + 1);
                textAttendeesRemaining.setText(remainingAttendeesStr);
                layoutAttendeesRemaining.setVisibility(View.VISIBLE);
            } else {
                layoutAttendeesRemaining.setVisibility(View.GONE);
            }
        } else {
            textNoAttendees.setVisibility(View.VISIBLE);
            for (ImageView image : imagesAttendees)
                image.setVisibility(View.GONE);
            layoutAttendeesRemaining.setVisibility(View.GONE);
        }

        if (!event.getUserId().equals(userId)) {
            if (attendedUser != null)
                setAttended();
            else
                setUnattended();
        }
    }

    private void onUnattendClick() {
        new DeleteAttendeeTask(event.getId(), userId).execute();
        event.getAttendees().remove(attendedUser);
        attendedUser = null;
        textButtonAttend.setText("Attend");
        loadAttendees(event.getAttendees());
    }

    private void onAttendClick() {
        Attendee attendee = new Attendee();
        attendee.setUserId(userId);
        attendee.setEventId(event.getId());
        attendee.setProfileImage(profileImage);
        event.getAttendees().add(0, attendee);
        new SaveAttendeeTask(attendee).execute();
        loadAttendees(event.getAttendees());
    }

    private void setUnattended() {
        textButtonAttend.setText("Attend");
        imageButtonAttend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAttendClick();
            }
        });
    }

    private void setAttended() {
        textButtonAttend.setText("Drop out");
        imageButtonAttend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUnattendClick();
            }
        });
    }

    @Override
    public void onLoadCategory(Category category) {
        textCategory.setText(category.getName());
    }

    @Override
    public void onLoadPlaces(List<Place> places) {
        for (int i = 0; i < viewsPlaces.size(); i++) {
            View view = viewsPlaces.get(i);
            Place place = places.get(i);

            String image = place.getImage();
            if (image != null) {
                ImageView imagePlace = (ImageView) view.findViewById(R.id.image_place);
                Picasso.with(getActivity()).load(image).into(imagePlace);
                imagePlace.setVisibility(View.VISIBLE);
            }

            TextView textPlaceTitle = (TextView) view.findViewById(R.id.text_place_title);
            textPlaceTitle.setText(place.getName());

            TextView textPlaceAddress = (TextView) view.findViewById(R.id.text_place_address);
            textPlaceAddress.setText(place.getAddress());

            RatingBar ratingPlace = (RatingBar) view.findViewById(R.id.rating_place);
            ratingPlace.setRating(new Float(place.getRating()));
        }

    }

}
