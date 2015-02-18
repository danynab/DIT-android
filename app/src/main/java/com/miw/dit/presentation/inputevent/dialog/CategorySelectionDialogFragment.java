package com.miw.dit.presentation.inputevent.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miw.dit.R;
import com.miw.dit.model.Category;

import java.util.List;

/**
 * Created by Dani on 14/2/15.
 */
public class CategorySelectionDialogFragment extends DialogFragment {

    private static final String ARG_CATEGORIES = "categories";
    private static final String ARG_CATEGORY_SELECTED = "categorySelected";

    private LinearLayout layout;

    private CategorySelectionDialogListener listener;

    private Category[] categories;
    private Category categorySelected;

    public static CategorySelectionDialogFragment newInstance(List<Category> categories, Category selected) {
        CategorySelectionDialogFragment fragment = new CategorySelectionDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CATEGORY_SELECTED, selected);
        Category[] categoriesArray = new Category[categories.size()];
        args.putParcelableArray(ARG_CATEGORIES, categories.toArray(categoriesArray));
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() != null) {
            categories = (Category[]) getArguments().getParcelableArray(ARG_CATEGORIES);
            categorySelected = getArguments().getParcelable(ARG_CATEGORY_SELECTED);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View viewRoot = inflater.inflate(R.layout.dialog_category_selection, null);

        layout = (LinearLayout) viewRoot.findViewById(R.id.layout_categories_selection);
        for (Category category : categories)
            addCategory(category, category.equals(categorySelected), inflater, layout);

        builder.setView(viewRoot);
        return builder.create();
    }

    private void addCategory(final Category category, boolean selected, LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.row_category, container, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCategory(category);
            }
        });
        TextView categoryTextView = (TextView) view.findViewById(R.id.text_category);
        ImageView labelImageView = (ImageView) view.findViewById(R.id.image_label);
        ImageView checkImageView = (ImageView) view.findViewById(R.id.image_check);
        categoryTextView.setText(category.getName());
        labelImageView.setColorFilter(Color.parseColor(category.getColor()));
        if (selected) {
            checkImageView.setColorFilter(getResources().getColor(R.color.accent));
            checkImageView.setVisibility(View.VISIBLE);
            categoryTextView.setTextColor(getResources().getColor(R.color.accent));
        }
        container.addView(view);
    }

    public void selectCategory(Category category) {
        if (listener != null)
            listener.onCategorySelected(category);
        dismiss();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (CategorySelectionDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement CategorySelectionDialogListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface CategorySelectionDialogListener {
        public void onCategorySelected(Category category);
    }
}
