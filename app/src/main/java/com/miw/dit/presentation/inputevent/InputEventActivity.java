package com.miw.dit.presentation.inputevent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miw.dit.R;
import com.miw.dit.infrastructure.Conf;
import com.miw.dit.model.Category;
import com.miw.dit.model.Event;
import com.miw.dit.presentation.gapiclient.LocationApiClient;
import com.miw.dit.presentation.inputevent.dialog.AddressInputDialogFragment;
import com.miw.dit.presentation.inputevent.dialog.CategorySelectionDialogFragment;
import com.miw.dit.presentation.inputevent.dialog.DatePickerFragment;
import com.miw.dit.presentation.inputevent.dialog.TimePickerFragment;
import com.miw.dit.presentation.inputevent.service.FetchAddressIntentService;
import com.miw.dit.presentation.inputevent.service.FetchLocationIntentService;
import com.miw.dit.presentation.task.LoadCategoriesTask;
import com.miw.dit.presentation.task.SaveEventTask;
import com.miw.dit.presentation.task.UpdateEventTask;

import java.util.Calendar;
import java.util.List;

public class InputEventActivity extends ActionBarActivity implements
        TimePickerFragment.TimePickerListener,
        DatePickerFragment.DatePickerListener,
        CategorySelectionDialogFragment.CategorySelectionDialogListener,
        AddressInputDialogFragment.AddressInputDialogListener,
        LocationApiClient.LocationApiClientCallback,
        LoadCategoriesTask.LoadCategoriesListener {

    public static final String ARG_EVENT = "event";
    public static final String ARG_CATEGORY = "category";

    private TextView textDate;
    private TextView textTime;
    private RelativeLayout contentCategory;
    private TextView textCategorySelection;
    private ImageView imageLabelCategorySelection;
    private TextView textAddress;
    private EditText textTitle;
    private EditText textDescription;

    private Event event;

    private Calendar calendar;
    private Category categorySelected;
    private List<Category> categories;

    private LocationApiClient locationApiClient;
    private AddressResultReceiver addressResultReceiver;
    private LocationResultReceiver locationResultReceiver;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_event);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();

            event = getIntent().getParcelableExtra(ARG_EVENT);
            if (event == null)
                event = new Event();

            categorySelected = extras.getParcelable(ARG_CATEGORY);

            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setElevation(8);

            new LoadCategoriesTask(this).execute();

            initialize();
        }
    }

    @Override
    public void onLoadCategories(List<Category> categories) {
        this.categories = categories;
        if (event.getId() == 0) {
            SharedPreferences sharedPreferences = getSharedPreferences(Conf.SHARED_PREFERENCES, Context.MODE_PRIVATE);
            String userId = sharedPreferences.getString(Conf.USER_PREFERENCE, "");
            String profileImage = sharedPreferences.getString(Conf.PROFILE_IMAGE_PREFERENCE, "");
            event.setUserId(userId);
            event.setProfileImage(profileImage);

            toolbar.setNavigationIcon(R.drawable.ic_clear_white_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavUtils.navigateUpFromSameTask(InputEventActivity.this);
                }
            });
            if (categorySelected == null)
                categorySelected = categories.get(0);
        } else {
            getSupportActionBar().setTitle("Edit event");
            int categoryId = event.getCategoryId();
            for (int i = 0; i < categories.size() && categorySelected == null; i++) {
                Category category = categories.get(i);
                if (categoryId == category.getId())
                    categorySelected = category;
            }
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setCategory(categorySelected);
    }


    private void initialize() {
        locationApiClient = new LocationApiClient(this, this);

        addressResultReceiver = new AddressResultReceiver();
        locationResultReceiver = new LocationResultReceiver();

        calendar = Calendar.getInstance();
        if (event.getId() != 0)
            calendar.setTimeInMillis(event.getTime());

        textDate = (TextView) findViewById(R.id.text_date);
        textDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePicker();
                }
            }
        });
        textDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        textDate.setText(dateToStr(calendar));

        textTime = (TextView) findViewById(R.id.text_time);
        textTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showTimePicker();
                }
            }
        });
        textTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });
        textTime.setText(timeToStr(calendar));

        contentCategory = (RelativeLayout) findViewById(R.id.content_category);
        contentCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategorySelectionDialog();
            }
        });

        imageLabelCategorySelection = (ImageView) findViewById(R.id.image_label_category_selection);

        textAddress = (TextView) findViewById(R.id.text_address);
        textAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddressInputDialog();
            }
        });
        textAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    showAddressInputDialog();
            }
        });

        textTitle = (EditText) findViewById(R.id.text_title);
        textDescription = (EditText) findViewById(R.id.text_description);

        if (event.getId() != 0) {
            textAddress.setText(event.getAddress());
            textTitle.setText(event.getTitle());
            textDescription.setText(event.getDescription());
        } else {
            event.setTime(calendar.getTimeInMillis());
        }

        textCategorySelection = (TextView) findViewById(R.id.text_category_selection);
    }

    @Override
    protected void onStart() {
        super.onStart();
        locationApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationApiClient.disconnectIfConnected();
    }

    private void setCategory(Category category) {
        imageLabelCategorySelection.setColorFilter(Color.parseColor(category.getColor()));
        textCategorySelection.setText(category.getName());
        categorySelected = category;
        event.setCategoryId(category.getId());
    }

    private void showTimePicker() {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerFragment fragment = TimePickerFragment.newInstance(hour, minute);
        fragment.show(getSupportFragmentManager(), "TimePickerFragment");
    }

    private void showCategorySelectionDialog() {
        CategorySelectionDialogFragment fragment = CategorySelectionDialogFragment.newInstance(categories, categorySelected);
        fragment.show(getSupportFragmentManager(), "CategorySelectionDialogFragment");
    }

    private void showAddressInputDialog() {
        AddressInputDialogFragment fragment = AddressInputDialogFragment.newInstance(textAddress.getText().toString());
        fragment.show(getSupportFragmentManager(), "AddressInputDialogFragment");
    }

    private void showDatePicker() {
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        DatePickerFragment fragment = DatePickerFragment.newInstance(day, month, year);
        fragment.show(getSupportFragmentManager(), "DatePickerFragment");
    }

    private String dateToStr(Calendar calendar) {
        java.text.DateFormat dateFormat = DateFormat.getDateFormat(this);
        return dateFormat.format(calendar.getTime());
    }

    private String timeToStr(Calendar calendar) {
        java.text.DateFormat dateFormat = DateFormat.getTimeFormat(this);
        return dateFormat.format(calendar.getTime());
    }

    private void setAddress(String address) {
        textAddress.setText(address);
        event.setAddress(address);
    }

    private void setLocation(double lat, double lng) {
        event.setLat(lat);
        event.setLng(lng);
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(FetchAddressIntentService.ARG_RECEIVER, addressResultReceiver);
        intent.putExtra(FetchAddressIntentService.ARG_LATITUDE, lat);
        intent.putExtra(FetchAddressIntentService.ARG_LONGITUDE, lng);
        startService(intent);
    }

    @Override
    public void onTimePicked(int hour, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        textTime.setText(timeToStr(calendar));
        event.setTime(calendar.getTimeInMillis());
    }

    @Override
    public void onDatePicked(int day, int month, int year) {
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        textDate.setText(dateToStr(calendar));
        event.setTime(calendar.getTimeInMillis());
    }

    @Override
    public void onCategorySelected(Category category) {
        setCategory(category);
    }


    @Override
    public void onAddressInput(String address) {
        Intent intent = new Intent(this, FetchLocationIntentService.class);
        intent.putExtra(FetchLocationIntentService.ARG_RECEIVER, locationResultReceiver);
        intent.putExtra(FetchLocationIntentService.ARG_ADDRESS, address);
        startService(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.input_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_event:
                String title = textTitle.getText().toString();
                String description = textDescription.getText().toString();
                if (title.isEmpty())
                    Toast.makeText(this, "The title is required", Toast.LENGTH_LONG).show();
                else if (description.isEmpty())
                    Toast.makeText(this, "The descrition is required", Toast.LENGTH_SHORT).show();
                else {
                    event.setTitle(title);
                    event.setDescription(description);
                    if (event.getId() == 0)
                        new SaveEventTask(event).execute();
                    else
                        new UpdateEventTask(event).execute();
                    NavUtils.navigateUpFromSameTask(InputEventActivity.this);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onConnected(double lat, double lng) {
        if (event.getId() == 0)
            setLocation(lat, lng);
    }


    public class AddressResultReceiver extends ResultReceiver {

        public AddressResultReceiver() {
            super(new Handler());
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            if (resultCode == FetchAddressIntentService.SUCCESS_RESULT) {
                String address = resultData.getString(FetchAddressIntentService.RESULT_ADDRESS);
                setAddress(address);
            }
        }
    }

    public class LocationResultReceiver extends ResultReceiver {

        public LocationResultReceiver() {
            super(new Handler());
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            if (resultCode == FetchAddressIntentService.SUCCESS_RESULT) {
                double lat = resultData.getDouble(FetchLocationIntentService.RESULT_LATITUDE);
                double lng = resultData.getDouble(FetchLocationIntentService.RESULT_LONGITUDE);
                setLocation(lat, lng);
            }

        }
    }
}
