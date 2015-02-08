package com.miw.dit.presentation;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miw.dit.R;
import com.miw.dit.model.Category;
import com.miw.dit.presentation.task.LoadImageTask;

import java.util.List;


public class NavigationDrawerFragment extends Fragment {

    private static final String ARG_ACCOUNT = "account";
    private static final String ARG_NAME = "name";
    private static final String ARG_PROFILE_URL = "profileUrl";
    private static final String ARG_COVER_URL = "coverUrl";
    private static final String ARG_CATEGORIES = "categories";

    public static NavigationDrawerFragment newInstance(String account, String name, String profileUrl, String coverUrl, List<Category> categories) {
        NavigationDrawerFragment fragment = new NavigationDrawerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ACCOUNT, account);
        args.putString(ARG_NAME, name);
        args.putString(ARG_PROFILE_URL, profileUrl);
        args.putString(ARG_COVER_URL, coverUrl);
        Category[] categoriesArray = new Category[categories.size()];
        args.putParcelableArray(ARG_CATEGORIES, categories.toArray(categoriesArray));
        fragment.setArguments(args);
        return fragment;
    }

    private String account;
    private String name;
    private String profileUrl;
    private String coverUrl;
    private Category[] categories;

    private ImageView imageCover;
    private ImageView imageProfile;
    private FrameLayout coverForeground;
    private TextView textFirstLetter;
    private RecyclerView categoriesListRecyclerView;


//    private OnFragmentInteractionListener mListener;


    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            account = getArguments().getString(ARG_ACCOUNT);
            name = getArguments().getString(ARG_NAME);
            profileUrl = getArguments().getString(ARG_PROFILE_URL);
            coverUrl = getArguments().getString(ARG_COVER_URL);
            categories = (Category[]) getArguments().getParcelableArray(ARG_CATEGORIES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        TextView textName = (TextView) view.findViewById(R.id.text_name);
        textName.setText(name);

        TextView textAccount = (TextView) view.findViewById(R.id.text_account);
        textAccount.setText(account);

        imageCover = (ImageView) view.findViewById(R.id.image_cover);
        coverForeground = (FrameLayout) view.findViewById(R.id.image_cover_foreground);

        imageProfile = (ImageView) view.findViewById(R.id.image_profile);
        textFirstLetter = (TextView) view.findViewById(R.id.text_first_letter);


        new LoadImageTask(coverUrl, new LoadImageTask.OnLoadImageListener() {
            @Override
            public void onLoadImage(Bitmap image) {
                if (image != null) {
                    imageCover.setImageBitmap(image);
                    coverForeground.setVisibility(View.VISIBLE);
                } else {
                    imageCover.setImageDrawable(getResources().getDrawable(R.drawable.cover_default));
                    coverForeground.setVisibility(View.INVISIBLE);
                }
            }
        }).execute();
        new LoadImageTask(profileUrl, new LoadImageTask.OnLoadImageListener() {
            @Override
            public void onLoadImage(Bitmap image) {
                if (image != null) {
                    imageProfile.setImageBitmap(image);
                    textFirstLetter.setVisibility(View.INVISIBLE);
                } else {
                    if (name == null)
                        textFirstLetter.setText(account.substring(0, 1).toUpperCase());
                    else
                        textFirstLetter.setText(name.substring(0, 1).toUpperCase());
                    textFirstLetter.setVisibility(View.VISIBLE);
                }
            }
        }).execute();

        LinearLayout layout = (LinearLayout) view.findViewById(R.id.layout_categories);
        for (Category category : categories)
            addCategory(category, inflater, layout);

        return view;
    }

    private void addCategory(Category category, LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.row_category, container, false);
        TextView categoryTextView = (TextView) view.findViewById(R.id.text_category);
        ImageView labelImageView = (ImageView) view.findViewById(R.id.image_label);
        categoryTextView.setText(category.getName());
        labelImageView.setColorFilter(Color.parseColor(category.getColor()));
        container.addView(view);
    }


//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p/>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        public void onFragmentInteraction(Uri uri);
//    }

}
