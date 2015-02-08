package com.miw.dit.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.plus.model.people.Person;
import com.miw.dit.R;
import com.miw.dit.model.Category;
import com.miw.dit.presentation.task.LoadCategoriesTask;
import com.miw.dit.presentation.util.GoogleApiClientImpl;

import java.util.List;


public class MainActivity extends ActionBarActivity implements
        GoogleApiClientImpl.GoogleApiClientCallback, LoadCategoriesTask.onLoadCategoriesListener {

    private static final String TAG = "MainActivity";

    private GoogleApiClientImpl googleApiClient;

    private String personName;
    private String account;
    private String profilePhotoUrl;
    private String coverPhotoUrl;
    private boolean profileLoaded = false;
    private List<Category> categories;
    private boolean categoriesLoaded = false;

    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            googleApiClient = new GoogleApiClientImpl(this, this);

            new LoadCategoriesTask(this).execute();

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setElevation(8);

            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawerLayout.setStatusBarBackgroundColor(
                    getResources().getColor(R.color.primary_dark));

            drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
            drawerLayout.setDrawerListener(drawerToggle);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnectIfConnected();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GoogleApiClientImpl.RC_SIGN_IN)
            googleApiClient.onResult();

    }

    @Override
    public void onFailed() {
        finish();
    }

    @Override
    public void onConnected(Person currentPerson, String account) {
        personName = currentPerson.getDisplayName();
        profilePhotoUrl = currentPerson.getImage().getUrl();
        coverPhotoUrl = currentPerson.getCover().getCoverPhoto().getUrl();

        //String id = currentPerson.getId();

        this.account = account;
        profileLoaded = true;
        finishLoading();
    }

    @Override
    public void onLoadCategories(List<Category> categories) {
        this.categories = categories;
        categoriesLoaded = true;
        finishLoading();
    }

    private void finishLoading() {
        if (categoriesLoaded && profileLoaded) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();

            NavigationDrawerFragment navigationDrawerFragment = NavigationDrawerFragment.newInstance(account, personName, profilePhotoUrl, coverPhotoUrl, categories);
            getSupportFragmentManager().beginTransaction().add(R.id.navigation_drawer_content, navigationDrawerFragment).commit();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {


        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            return rootView;
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

        }
    }
}
