package com.example.software;

import com.example.software.database.serves.Business;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class InformationBuffer {
    private static List<Business> businessList= Collections.synchronizedList(new ArrayList<>());

    public static Business getBusiness(String username)
    {
        for (Business business : businessList)
        {
            if (business.getUser().getUserName().equals(username))
                return business;
        }
        return null;
    }

    public static Business getBusiness(int id)
    {
        for (Business business : businessList)
        {
            if (business.getUser().getUserId() == id)
                return business;
        }
        return null;
    }

    public static void addBusiness(Business business)
    {
        businessList.add(business);
    }

    public static void deleteBusiness(Business business)
    {
        businessList.remove(business);
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
