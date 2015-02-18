package com.miw.dit.presentation.main;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miw.dit.R;
import com.miw.dit.infrastructure.Conf;
import com.miw.dit.model.Category;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NavigationDrawerFragment extends Fragment {

    private static final String ARG_ACCOUNT = "account";
    private static final String ARG_NAME = "name";
    private static final String ARG_COVER_URL = "coverUrl";
    private static final String ARG_CATEGORIES = "categories";

    public static NavigationDrawerFragment newInstance(String account, String name, String coverUrl, List<Category> categories) {
        NavigationDrawerFragment fragment = new NavigationDrawerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ACCOUNT, account);
        args.putString(ARG_NAME, name);
        args.putString(ARG_COVER_URL, coverUrl);
        Category[] categoriesArray = new Category[categories.size()];
        args.putParcelableArray(ARG_CATEGORIES, categories.toArray(categoriesArray));
        fragment.setArguments(args);
        return fragment;
    }

    private String userId;
    private String account;
    private String name;
    private String coverUrl;
    private Category[] categories;

    private SelectItemListener listener;

    private Category categorySelected;
    private Map<Category, RelativeLayout> mapCategories;
    private RelativeLayout layoutNearEvents;
    private RelativeLayout layoutYourEvents;
    private RelativeLayout layoutYourAttendances;


    public NavigationDrawerFragment() {
        mapCategories = new HashMap<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            account = getArguments().getString(ARG_ACCOUNT);
            name = getArguments().getString(ARG_NAME);
            coverUrl = getArguments().getString(ARG_COVER_URL);
            categories = (Category[]) getArguments().getParcelableArray(ARG_CATEGORIES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = inflater.getContext().getSharedPreferences(Conf.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(Conf.USER_PREFERENCE, "");

        String profileUrl = sharedPreferences.getString(Conf.PROFILE_IMAGE_PREFERENCE, "");

        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        TextView textName = (TextView) view.findViewById(R.id.text_name);
        textName.setText(name);

        TextView textAccount = (TextView) view.findViewById(R.id.text_account);
        textAccount.setText(account);

        layoutNearEvents = (RelativeLayout) view.findViewById(R.id.layout_near_events);
        layoutNearEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectNearEvents();
            }
        });
        layoutNearEvents.setSelected(true);

        layoutYourEvents = (RelativeLayout) view.findViewById(R.id.layout_your_events);
        layoutYourEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectYourEvents();
            }
        });

        layoutYourAttendances = (RelativeLayout) view.findViewById(R.id.layout_your_attendances);
        layoutYourAttendances.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectYourAttendaces();
            }
        });


        ImageView imageCover = (ImageView) view.findViewById(R.id.image_cover);
        FrameLayout coverForeground = (FrameLayout) view.findViewById(R.id.image_cover_foreground);

        ImageView imageProfile = (ImageView) view.findViewById(R.id.image_profile);

        if (coverUrl != null) {
            Picasso.with(getActivity()).load(coverUrl).into(imageCover);
            coverForeground.setVisibility(View.VISIBLE);
        }

        Picasso.with(getActivity()).load(profileUrl).into(imageProfile);

        LinearLayout layout = (LinearLayout) view.findViewById(R.id.layout_categories);
        for (Category category : categories)
            addCategory(category, inflater, layout);

        return view;
    }

    private void addCategory(final Category category, LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.row_category, container, false);
        RelativeLayout content = (RelativeLayout) view.findViewById(R.id.content);
        mapCategories.put(category, content);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCategory(category);
            }
        });
        TextView categoryTextView = (TextView) view.findViewById(R.id.text_category);
        ImageView labelImageView = (ImageView) view.findViewById(R.id.image_label);
        categoryTextView.setText(category.getName());
        labelImageView.setColorFilter(Color.parseColor(category.getColor()));
        container.addView(view);
    }

    private void selectCategory(Category category) {
        if (listener != null) {
            if (category.equals(categorySelected)) {
                listener.onSelectSameItem();
            } else {
                listener.onSelectCategoryListener(category);
                if (categorySelected != null) {
                    RelativeLayout oldLayout = mapCategories.get(categorySelected);
                    oldLayout.setSelected(false);
                } else {
                    layoutYourEvents.setSelected(false);
                    layoutNearEvents.setSelected(false);
                    layoutYourAttendances.setSelected(false);
                }
                RelativeLayout layout = mapCategories.get(category);
                layout.setSelected(true);
                categorySelected = category;
            }
        }
    }

    private void selectNearEvents() {
        if (listener != null) {
            if (categorySelected != null) {
                mapCategories.get(categorySelected).setSelected(false);
                categorySelected = null;
            } else {
                layoutYourEvents.setSelected(false);
                layoutYourAttendances.setSelected(false);
            }
            if (layoutNearEvents.isSelected()) {
                listener.onSelectSameItem();
            } else {
                listener.onSelectShowNearEvents();
                layoutNearEvents.setSelected(true);
            }
        }
    }

    private void selectYourEvents() {
        if (listener != null) {
            if (categorySelected != null) {
                mapCategories.get(categorySelected).setSelected(false);
                categorySelected = null;
            } else {
                layoutNearEvents.setSelected(false);
                layoutYourAttendances.setSelected(false);
            }
            if (layoutYourEvents.isSelected()) {
                listener.onSelectSameItem();
            } else {
                listener.onSelectYourEvents(userId);
                layoutYourEvents.setSelected(true);
            }
        }
    }

    private void selectYourAttendaces() {
        if (listener != null) {
            if (categorySelected != null) {
                mapCategories.get(categorySelected).setSelected(false);
                categorySelected = null;
            } else {
                layoutNearEvents.setSelected(false);
                layoutYourEvents.setSelected(false);
            }
            if (layoutYourAttendances.isSelected()) {
                listener.onSelectSameItem();
            } else {
                listener.onSelectYourAttendances(userId);
                layoutYourAttendances.setSelected(true);
            }
        }
    }

    public interface SelectItemListener {
        public void onSelectCategoryListener(Category category);

        public void onSelectSameItem();

        public void onSelectShowNearEvents();

        public void onSelectYourEvents(String userId);

        public void onSelectYourAttendances(String userId);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (SelectItemListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement SelectCategoryListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

}
