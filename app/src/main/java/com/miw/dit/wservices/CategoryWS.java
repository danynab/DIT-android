package com.miw.dit.wservices;

import com.miw.dit.model.Category;

import java.util.List;

/**
 * Created by Dani on 7/2/15.
 */
public interface CategoryWS {

    public List<Category> findAll();

    public Category findById(int id);
}
