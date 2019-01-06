package com.example.zhanyuan.finalapp.CityObjects;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.zhanyuan.finalapp.R;
import com.example.zhanyuan.finalapp.activities.MainActivity;
import com.example.zhanyuan.finalapp.utils.UtilConstants;

import org.junit.Assert;
import org.junit.Before;

import static org.junit.Assert.*;

public class ApartmentTest {
    Apartment apartment = new Apartment(null, 10,5,5,1);

    @Before
    public void method(){
        Assert.assertNotEquals(apartment.getName(), null);
    }
    @org.junit.Test
    public void initTest(){
        int width = 5;
        int height = 5;

        Assert.assertEquals(width, apartment.getWidth());
        Assert.assertEquals(height, apartment.getHeight());
        Assert.assertEquals("APARTMENT1", apartment.getName());
        Assert.assertEquals(UtilConstants.APARTMENT1[3], apartment.getCost());
    }


}