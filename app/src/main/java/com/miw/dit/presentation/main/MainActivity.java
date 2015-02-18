package com.miw.dit.presentation.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.plus.model.people.Person;
import com.miw.dit.R;
import com.miw.dit.infrastructure.Conf;
import com.miw.dit.model.Category;
import com.miw.dit.model.Event;
import com.miw.dit.presentation.details.DetailsEventActivity;
import com.miw.dit.presentation.gapiclient.LocationApiClient;
import com.miw.dit.presentation.gapiclient.PlusApiClient;
import com.miw.dit.presentation.inputevent.InputEventActivity;
import com.miw.dit.presentation.task.LoadCategoriesTask;

import java.util.List;


public class MainActivity extends ActionBarActivity implements
        PlusApiClient.PlusApiClientCallback,
        LoadCategoriesTask.LoadCategoriesListener,
        NavigationDrawerFragment.SelectItemListener,
        EventsListFragment.EventListFragmentCallback,
        LocationApiClient.LocationApiClientCallback {

    private PlusApiClient plusApiClient;
    private LocationApiClient locationApiClient;

    private String personName;
    private String account;
    private String profilePhotoUrl;
    private String coverPhotoUrl;
    private String userId;

    private boolean profileLoaded = false;
    private List<Category> categories;
    private boolean categoriesLoaded = false;
    private boolean locationLoaded = false;
    private double lat;
    private double lng;
    private Category categorySelected;

    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private ProgressBar progressLoadingEvents;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            plusApiClient = new PlusApiClient(this, this);
            locationApiClient = new LocationApiClient(this, this);

            new LoadCategoriesTask(this).execute();

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setElevation(8);

            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawerLayout.setStatusBarBackgroundColor(
                    getResources().getColor(R.color.primary_dark));

            drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
            drawerLayout.setDrawerListener(drawerToggle);

            progressLoadingEvents = (ProgressBar) findViewById(R.id.progress_loading_events);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        plusApiClient.connect();
        locationApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        plusApiClient.disconnectIfConnected();
        locationApiClient.disconnectIfConnected();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PlusApiClient.RC_SIGN_IN)
            plusApiClient.onResult();

    }

    @Override
    public void onFailed() {
        finish();
    }

    @Override
    public void onConnected(Person currentPerson, String account) {
        personName = currentPerson.getDisplayName();
        profilePhotoUrl = currentPerson.getImage().getUrl().split("\\?")[0];
        Person.Cover cover = currentPerson.getCover();
        if (cover != null)
            coverPhotoUrl = cover.getCoverPhoto().getUrl();
        userId = currentPerson.getId();

        this.account = account;
        profileLoaded = true;

        SharedPreferences sharedPreferences = getSharedPreferences(Conf.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Conf.USER_PREFERENCE, userId);
        editor.putString(Conf.PROFILE_IMAGE_PREFERENCE, profilePhotoUrl);
        editor.apply();
        finishLoading();
    }

    @Override
    public void onLoadCategories(List<Category> categories) {
        this.categories = categories;
        categoriesLoaded = true;
        finishLoading();
    }

    private void finishLoading() {
        if (categoriesLoaded && profileLoaded && locationLoaded) {
            categoriesLoaded = false;
            profileLoaded = false;
            EventsListFragment eventsListFragment = EventsListFragment.newNearEventsInstance(lat, lng);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, eventsListFragment)
                    .commit();

            NavigationDrawerFragment navigationDrawerFragment = NavigationDrawerFragment.newInstance(account, personName, coverPhotoUrl, categories);
            getSupportFragmentManager().beginTransaction().add(R.id.navigation_drawer_content, navigationDrawerFragment).commit();
            drawerLayout.openDrawer(Gravity.LEFT);
        }
    }


    @Override
    public void onSelectCategoryListener(Category category) {
        categorySelected = category;
        progressLoadingEvents.setVisibility(View.VISIBLE);
        EventsListFragment eventsListFragment = EventsListFragment.newEventsByCategoryInstance(category, lat, lng);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, eventsListFragment)
                .commit();
        drawerLayout.closeDrawers();
    }

    @Override
    public void onSelectSameItem() {
        drawerLayout.closeDrawers();
    }

    @Override
    public void onSelectShowNearEvents() {
        categorySelected = null;
        progressLoadingEvents.setVisibility(View.VISIBLE);
        EventsListFragment eventsListFragment = EventsListFragment.newNearEventsInstance(lat, lng);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, eventsListFragment)
                .commit();
        drawerLayout.closeDrawers();
    }

    @Override
    public void onSelectYourEvents(String userId) {
        categorySelected = null;
        progressLoadingEvents.setVisibility(View.VISIBLE);
        EventsListFragment eventsListFragment = EventsListFragment.newEventsOfUserInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, eventsListFragment)
                .commit();
        drawerLayout.closeDrawers();
    }

    @Override
    public void onSelectYourAttendances(String userId) {
        categorySelected = null;
        progressLoadingEvents.setVisibility(View.VISIBLE);
        EventsListFragment eventsListFragment = EventsListFragment.newAttendedEventsOfUserInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, eventsListFragment)
                .commit();
        drawerLayout.closeDrawers();
    }

    @Override
    public void onNewEventClick() {
        Intent i = new Intent(this, InputEventActivity.class);
        i.putExtra(InputEventActivity.ARG_CATEGORY, categorySelected);
        startActivity(i);
    }

    @Override
    public void onDetailsEventClick(Event event) {
        Intent i = new Intent(this, DetailsEventActivity.class);
        i.putExtra(DetailsEventActivity.ARG_EVENT, event);
        i.putExtra(DetailsEventActivity.ARG_USER_ID, userId);
        i.putExtra(DetailsEventActivity.ARG_PROFILE_IMAGE, profilePhotoUrl);
        startActivity(i);
    }

    @Override
    public void onConnected(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
        Log.d(getClass().getSimpleName(), "onConnected: lat { " + lat + " }, lng { " + lng + " }");
        locationLoaded = true;
        finishLoading();
    }
}
