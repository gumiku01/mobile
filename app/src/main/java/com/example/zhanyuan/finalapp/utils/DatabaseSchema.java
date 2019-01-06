package com.example.zhanyuan.finalapp.utils;

import android.content.res.Resources;

import java.util.ArrayList;

/**
 * define some useful string for database items
 */
public class DatabaseSchema {

    public static class DataSchema{
        public static final String  tableName = "record";
        public static final String recid ="id";
        public static final String year = "year";
        public static final String month = "month";
        public static final String day = "day";
        public static final String rectype = "type"; // income or payout
        public static final String recdetail = "detailtype"; // restaurant, cafe, ecc...
        public static final String money = "money";
        public static final String recdescription = "description";

        public static final String income = "income";
        public static final String payout = "payout";

        public static final ArrayList<String> names_payout = new ArrayList<String>(){
            {
                add(ResourcesUtil.food);
                add(ResourcesUtil.clothes);
                add(ResourcesUtil.transportation);
                add(ResourcesUtil.housing);
                add(ResourcesUtil.entertainment);
                add(ResourcesUtil.study);
                add(ResourcesUtil.medicine);
                add(ResourcesUtil.others);
            }
        };

        public static final ArrayList<String> names_income = new ArrayList<String>(){
            {
                add(ResourcesUtil.job);
                add(ResourcesUtil.gift);
                add(ResourcesUtil.interest);
                add(ResourcesUtil.others);
            }
        };

    }
}
