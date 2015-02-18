package com.miw.dit.wservices.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miw.dit.model.Category;
import com.miw.dit.wservices.CategoryWS;
import com.miw.dit.wservices.impl.http.HttpGet;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dani on 7/2/15.
 */
public class CategoryREST implements CategoryWS {

    @Override
    public List<Category> findAll() {
        HttpGet httpGet = new HttpGet(WServicesUtil.getCategoriesUrl());
        String json = httpGet.execute();
        Gson gson = new Gson();
        Type linesList = new TypeToken<List<Category>>() {
        }.getType();
        List<Category> result = gson.fromJson(json, linesList);
        return result == null ? new ArrayList<Category>() : result;
    }

    @Override
    public Category findById(int id) {
        HttpGet httpGet = new HttpGet(WServicesUtil.getCategoryUrl(id));
        String json = httpGet.execute();
        Gson gson = new Gson();
        Category category = gson.fromJson(json, Category.class);
        return category;
    }
}
