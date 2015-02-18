package com.miw.dit.business.impl;

import com.miw.dit.business.CategoryServices;
import com.miw.dit.infrastructure.Factories;
import com.miw.dit.model.Category;

import java.util.List;

/**
 * Created by Dani on 7/2/15.
 */
public class CategoryServicesImpl implements CategoryServices {

    @Override
    public List<Category> getCategories() {
        return Factories.getWServicesFactory().getCategoryWS().findAll();
    }

    @Override
    public Category getCategory(int id) {
        return Factories.getWServicesFactory().getCategoryWS().findById(id);
    }
}
