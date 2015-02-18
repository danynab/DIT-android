package com.miw.dit.presentation.details;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.miw.dit.R;
import com.miw.dit.model.Event;

public class DetailsEventActivity extends ActionBarActivity {

    public static final String ARG_EVENT = "event";
    public static final String ARG_USER_ID = "userId";
    public static final String ARG_PROFILE_IMAGE = "profileImage";

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_event);
        if (savedInstanceState == null) {
            toolbar = (Toolbar) findViewById(R.id.toolbar);

            setSupportActionBar(toolbar);
            getSupportActionBar().setElevation(8);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            String userId = getIntent().getStringExtra(ARG_USER_ID);
            String profileImage = getIntent().getStringExtra(ARG_PROFILE_IMAGE);
            Event event = getIntent().getParcelableExtra(ARG_EVENT);

            DetailsEventFragment fragment = DetailsEventFragment.newInstance(event, userId, profileImage);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }

}
