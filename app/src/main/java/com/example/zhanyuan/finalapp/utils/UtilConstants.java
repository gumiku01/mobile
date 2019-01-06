package com.example.zhanyuan.finalapp.utils;


import com.example.zhanyuan.finalapp.R;
import com.example.zhanyuan.finalapp.utils.ResourcesUtil;

import java.util.HashMap;

public class UtilConstants {

    // width and height of single tile
    public static final int TILE_WIDTH = 200;
    public static final int TILE_HEIGHT = 100;

    public static final int MARGIN = 300;

    // tuple contains number to identify the buildings, how many tiles occupies horizontally and vertically, cost
    // streets
    public static final int[] STREET_HORIZONTAL = new int[]{1, 1, 1, 10};
    public static final int[] STREET_VERTICAL = new int[]{2, 1, 1, 10};
    public static final int[] STREET_BOTTOM_RIGHT = new int[]{3, 1, 1, 10};
    public static final int[] STREET_BOTTOM_T = new int[]{4, 1, 1, 10};
    public static final int[] STREET_CROSS = new int[]{5, 1, 1, 10};
    public static final int[] STREET_LEFT_BOTTOM = new int[]{6, 1, 1, 10};
    public static final int[] STREET_RIGHT_TOP = new int[]{7, 1 , 1, 10};
    public static final int[] STREET_TOP_LEFT = new int[]{8, 1, 1, 10};
    public static final int[] STREET_TOP_T =new int[]{9, 1 ,1, 10};

    // apartment
    public static final int[] APARTMENT1 = new int[]{10, 2, 2, 60};
    public static final int[] APARTMENT2 = new int[]{11, 2, 2, 100};
    public static final int[] APARTMENT3 = new int[]{12, 4, 2, 150};
    public static final int[] APARTMENT4 = new int[]{13, 4, 2, 200};

    // special building
    public static final int[] BANK = new int[]{14, 3, 3, 200};
    public static final int[] TOWN_HALL = new int[]{15, 3, 3, 500};
    public static final int[] HOSPITAL = new int[]{16, 4, 2, 300};
    public static final int[] POLICE = new int[]{17, 4, 2, 300};
    public static final int[] RESTAURANT = new int[]{18, 3, 3, 200};
    public static final int[] SCHOOL = new int[]{19, 4, 2, 300};
    public static final int[] FIREFIGHTING = new int[]{20, 4, 2, 200};

    // public area
    public static final int[] TREE = new int[]{21, 1, 1, 10};
    public static final int[] FOUNTAIN = new int[]{22, 1, 1, 20};
    public static final int[] MOUNTAIN = new int[]{23, 1, 1, 30};
    public static final int[] PARK1 = new int[]{24, 1, 1, 20};
    public static final int[] PARK2 = new int[]{25, 1, 1, 20};
    public static final int[] PARK3 = new int[]{26, 1, 1, 20};

    public static final HashMap<Integer, String[]> typeNameAndMoney = new HashMap<Integer, String[]>(){
        {
            put(STREET_HORIZONTAL[0], new String[]{ResourcesUtil.transportation, String.valueOf(STREET_HORIZONTAL[3])});
            put(STREET_VERTICAL[0], new String[]{ResourcesUtil.transportation, String.valueOf(STREET_VERTICAL[3])});
            put(STREET_BOTTOM_RIGHT[0], new String[]{ResourcesUtil.transportation, String.valueOf(STREET_BOTTOM_RIGHT[3])});
            put(STREET_BOTTOM_T[0], new String[]{ResourcesUtil.transportation, String.valueOf(STREET_BOTTOM_T[3])});
            put(STREET_CROSS[0], new String[]{ResourcesUtil.transportation, String.valueOf(STREET_CROSS[3])});
            put(STREET_LEFT_BOTTOM[0], new String[]{ResourcesUtil.transportation, String.valueOf(STREET_LEFT_BOTTOM[3])});
            put(STREET_RIGHT_TOP[0], new String[]{ResourcesUtil.transportation, String.valueOf(STREET_RIGHT_TOP[3])});
            put(STREET_TOP_LEFT[0], new String[]{ResourcesUtil.transportation, String.valueOf(STREET_TOP_LEFT[3])});
            put(STREET_TOP_T[0], new String[]{ResourcesUtil.transportation, String.valueOf(STREET_TOP_T[3])});

            put(APARTMENT1[0], new String[]{ResourcesUtil.housing, String.valueOf(APARTMENT1[3])});
            put(APARTMENT2[0], new String[]{ResourcesUtil.housing, String.valueOf(APARTMENT2[3])});
            put(APARTMENT3[0], new String[]{ResourcesUtil.housing, String.valueOf(APARTMENT3[3])});
            put(APARTMENT4[0], new String[]{ResourcesUtil.housing, String.valueOf(APARTMENT4[3])});

            put(BANK[0], new String[]{ResourcesUtil.others, String.valueOf(BANK[3])});
            put(TOWN_HALL[0], new String[]{ResourcesUtil.others, String.valueOf(TOWN_HALL[3])});
            put(HOSPITAL[0], new String[]{ResourcesUtil.medicine, String.valueOf(HOSPITAL[3])});
            put(POLICE[0], new String[]{ResourcesUtil.others, String.valueOf(POLICE[3])});
            put(RESTAURANT[0], new String[]{ResourcesUtil.food, String.valueOf(RESTAURANT[3])});
            put(SCHOOL[0], new String[]{ResourcesUtil.study, String.valueOf(SCHOOL[3])});
            put(FIREFIGHTING[0], new String[]{ResourcesUtil.others, String.valueOf(FIREFIGHTING[3])});

            put(TREE[0], new String[]{ResourcesUtil.others, String.valueOf(TREE[3])});
            put(FOUNTAIN[0], new String[]{ResourcesUtil.others, String.valueOf(FOUNTAIN[3])});
            put(MOUNTAIN[0], new String[]{ResourcesUtil.others, String.valueOf(MOUNTAIN[3])});
            put(PARK1[0], new String[]{ResourcesUtil.others, String.valueOf(PARK1[3])});
            put(PARK2[0], new String[]{ResourcesUtil.others, String.valueOf(PARK2[3])});
            put(PARK3[0], new String[]{ResourcesUtil.others, String.valueOf(PARK3[3])});
        }
    };

    // String of cost and image of each type of infrastructure
    // Street
    public static final int[][] STREET = {
            STREET_HORIZONTAL,
            STREET_VERTICAL,
            STREET_BOTTOM_RIGHT,
            STREET_BOTTOM_T,
            STREET_CROSS,
            STREET_LEFT_BOTTOM,
            STREET_RIGHT_TOP,
            STREET_TOP_LEFT,
            STREET_TOP_T,
    };

    public static final Integer[] STREET_IMAGES= {
            R.drawable.strada_orizzontale,
            R.drawable.strada_verticale,
            R.drawable.strada_bottom_right,
            R.drawable.strada_bottom_t,
            R.drawable.strada_croce,
            R.drawable.strada_left_bottom,
            R.drawable.strada_right_top,
            R.drawable.strada_top_left,
            R.drawable.strada_top_t
    };

    // Apartment
    public static final int[][] APARTMENT = {
            APARTMENT1,
            APARTMENT2,
            APARTMENT3,
            APARTMENT4
    };

    public static final Integer[] APARTMENT_IMAGES = {
            R.drawable.appartamento1,
            R.drawable.appartamento2,
            R.drawable.appartamento3,
            R.drawable.appartamento4
    };

    // Special Building
    public static final int[][] SPECIAL = {
            BANK,
            TOWN_HALL,
            HOSPITAL,
            POLICE,
            RESTAURANT,
            SCHOOL,
            FIREFIGHTING
    };
    public static final Integer[] SPECIAL_IMAGES = {
            R.drawable.banca,
            R.drawable.municipio,
            R.drawable.ospetale,
            R.drawable.polizia,
            R.drawable.restaurante,
            R.drawable.scuola,
            R.drawable.vigile_fuoco
    };

    // Public area
    public static final int[][] PUBLIC = {
            TREE,
            FOUNTAIN,
            MOUNTAIN,
            PARK1,
            PARK2,
            PARK3
    };

    public static final Integer[] PUBLIC_IMAGES = {
            R.drawable.albero,
            R.drawable.fontana,
            R.drawable.montagna,
            R.drawable.parco1,
            R.drawable.parco2,
            R.drawable.parco3
    };


    // color filter
    public static final float[] RED_FILTER = new float[]{
            2, 0, 0, 0, 0,
            0, 0, 0, 0, 0,
            0, 0, 0, 0, 0,
            0, 0, 0, 1, 0
    };
    public static final float[] GREEN_FILTER = new float[]{
            0, 0, 0, 0, 0,
            0, 2, 0, 0, 0,
            0, 0, 0, 0, 0,
            0, 0, 0, 1, 0
    };
    public static final float[] NORMAL_FILTER = new float[]{
            1, 0, 0, 0, 0,
            0, 1, 0, 0, 0,
            0, 0, 1, 0, 0,
            0, 0, 0, 1, 0
    };



}
