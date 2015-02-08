package com.miw.dit.infrastructure;

import com.miw.dit.business.BusinessFactory;
import com.miw.dit.business.impl.BusinessFactoryImpl;
import com.miw.dit.wservices.WServicesFactory;
import com.miw.dit.wservices.impl.WServicesFactoryImpl;

/**
 * Created by Dani on 7/2/15.
 */
public class Factories {

    private static WServicesFactory wServicesFactory;

    public static WServicesFactory getWServicesFactory() {
        if (wServicesFactory == null)
            wServicesFactory = new WServicesFactoryImpl();
        return wServicesFactory;
    }


    private static BusinessFactory businessFactory;

    public static BusinessFactory getBusinessFactory() {
        if (businessFactory == null)
            businessFactory = new BusinessFactoryImpl();
        return businessFactory;
    }
}


