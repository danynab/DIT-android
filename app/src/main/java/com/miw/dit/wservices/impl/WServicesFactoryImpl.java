package com.miw.dit.wservices.impl;

import com.miw.dit.wservices.CategoryWS;
import com.miw.dit.wservices.WServicesFactory;

/**
 * Created by Dani on 7/2/15.
 */
public class WServicesFactoryImpl implements WServicesFactory {

    @Override
    public CategoryWS getCategoryWS() {
        return new CategoryREST();
    }
}
