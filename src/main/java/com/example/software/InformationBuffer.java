package com.example.software;

import com.example.software.database.serves.Business;

import java.util.List;
import java.util.ArrayList;

public class InformationBuffer {
    private static List<Business> businessList= new ArrayList<>();

    public static Business getBusiness(String username)
    {
        for (Business business : businessList)
        {
            if (business.getUser().getUserName().equals(username))
                return business;
        }
        return null;
    }

    public static void addBusiness(Business business)
    {
        businessList.add(business);
    }

    public static boolean checkUser(String username)
    {
        for(Business business: businessList)
        {
            if(business.getUser().getUserName().equals(username))
            {
                return false;
            }
        }
        return true;
    }
}
