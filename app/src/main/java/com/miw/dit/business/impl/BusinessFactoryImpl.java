package com.miw.dit.business.impl;

import com.miw.dit.business.BusinessFactory;
import com.miw.dit.business.CategoryServices;

/**
 * Created by Dani on 7/2/15.
 */
public class BusinessFactoryImpl implements BusinessFactory {


    @Override
    public CategoryServices getCategoryServices() {
        return new CategoryServicesImpl();
    }
}
