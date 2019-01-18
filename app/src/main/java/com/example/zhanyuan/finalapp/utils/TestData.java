package com.example.zhanyuan.finalapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.Random;


public class TestData {

    public static void AddTestData(Context mContext) {
        MyDatabaseHelper myDatabaseHelper = new MyDatabaseHelper(mContext, "my.db", null, 1);
        SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();


        Random random = new Random();
        int maxMonth = 12, minMonth = 10;
        int maxDay = 30, minDay = 1;
        int maxMoney = 1000, maxMoneyDot = 100;

        for (int i = 0; i < 20; i++) {
            ContentValues contentValues = new ContentValues();

            // add data
            contentValues.put(DatabaseSchema.DataSchema.year, 2018);
            contentValues.put(DatabaseSchema.DataSchema.month, random.nextInt(maxMonth - minMonth + 1) + minMonth);
            contentValues.put(DatabaseSchema.DataSchema.day, random.nextInt(maxDay - minDay + 1) + minDay);

            // add type
            int typeNum = random.nextInt(2) + 1;
            String typeString;
            if (typeNum == 1)
                typeString = DatabaseSchema.DataSchema.income;
            else
                typeString = DatabaseSchema.DataSchema.payout;
            contentValues.put(DatabaseSchema.DataSchema.rectype, typeString);

            // add money
            String num = String.valueOf(random.nextInt(maxMoney));
            String dotNum = String.valueOf(random.nextInt(maxMoneyDot));
            contentValues.put(DatabaseSchema.DataSchema.money, Float.parseFloat(num + "." + dotNum));

            // add description
            contentValues.put(DatabaseSchema.DataSchema.recdescription, "Prova" + String.valueOf(i));

            // add category
            ArrayList<String> category_names;
            if(typeString.equals(DatabaseSchema.DataSchema.payout))
                category_names = DatabaseSchema.DataSchema.names_payout;
            else
                category_names = DatabaseSchema.DataSchema.names_income;
            int maxLength = category_names.size();
            contentValues.put(DatabaseSchema.DataSchema.recdetail, category_names.get(random.nextInt(maxLength)));

            db.insert(DatabaseSchema.DataSchema.tableName, null, contentValues);
        }
    }
}
