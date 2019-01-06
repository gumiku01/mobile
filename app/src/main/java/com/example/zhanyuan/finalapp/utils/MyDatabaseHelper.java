package com.example.zhanyuan.finalapp.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.zhanyuan.finalapp.beans.DetailBean;
import com.example.zhanyuan.finalapp.beans.TypeBean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE record(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "year INTEGER, month INTEGER, day INTEGER, type VARCHAR, money REAL, description VARCHAR, detailtype VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    // build the list of ListBean using variable year and month
    public DetailBean DB2DayListBeanUsingData(int year, int month){

        String whereClause = "year = ? AND month = ?";
        String[] whereArg = new String[]{ String.valueOf(year), String.valueOf(month)};

        float m_income = 0, m_outcome = 0, d_income = 0, d_outcome = 0;
        int day = 0;

        DetailBean result = new DetailBean();
        List<DetailBean.DayListBean> dayList = new ArrayList<>();
        List<DetailBean.DayListBean.ListBean> singleList = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query("record", null, whereClause, whereArg, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int pid = cursor.getInt(cursor.getColumnIndex(DatabaseSchema.DataSchema.recid));
                int pyear = cursor.getInt(cursor.getColumnIndex(DatabaseSchema.DataSchema.year));
                int pmonnth = cursor.getInt(cursor.getColumnIndex(DatabaseSchema.DataSchema.month));
                int pday = cursor.getInt(cursor.getColumnIndex(DatabaseSchema.DataSchema.day));
                String ptype = cursor.getString(cursor.getColumnIndex(DatabaseSchema.DataSchema.rectype));
                float pmoney = cursor.getFloat(cursor.getColumnIndex(DatabaseSchema.DataSchema.money));
                String pdes = cursor.getString(cursor.getColumnIndex(DatabaseSchema.DataSchema.recdescription));
                String detail = cursor.getString(cursor.getColumnIndex(DatabaseSchema.DataSchema.recdetail));

                DetailBean.DayListBean.ListBean elem = new DetailBean.DayListBean.ListBean();
                elem.setId(pid);
                elem.setAffect_money(pmoney);
                elem.setDescription(pdes);
                elem.setType(ptype);
                elem.setDetailtype(detail);

                // if day of new record is equal to the previous one, we add it in old list
                // otherwise we initialize a new list of singleList, putting the old into dayList
                if(day == 0)
                    day = pday;
                if(day == pday){
                    if(ptype.equals(DatabaseSchema.DataSchema.income))
                        d_income += pmoney;
                    if(ptype.equals(DatabaseSchema.DataSchema.payout))
                        d_outcome += pmoney;
                    singleList.add(0, elem);
                }else{

                    DetailBean.DayListBean dayListElem = new DetailBean.DayListBean();
                    dayListElem.setDay(getWeekDayString(year, month - 1, day));
                    dayListElem.setMoney("income: " + d_income + "\t payout: " + d_outcome);
                    dayListElem.setList(singleList);
                    dayList.add(0,dayListElem);

                    m_income += d_income;
                    m_outcome += d_outcome;

                    d_income = 0;
                    d_outcome = 0;
                    singleList = new ArrayList<>();

                    // add the first of next day
                    day = pday;
                    if(ptype.equals(DatabaseSchema.DataSchema.income))
                        d_income += pmoney;
                    if(ptype.equals(DatabaseSchema.DataSchema.payout))
                        d_outcome += pmoney;
                    singleList.add(0, elem);
                }
            } while (cursor.moveToNext());
        }

        // save for last day
        DetailBean.DayListBean dayListElem = new DetailBean.DayListBean();
        dayListElem.setDay(getWeekDayString(year, month - 1, day));
        dayListElem.setMoney("income: " + d_income + "\t payout: " + d_outcome);
        dayListElem.setList(singleList);
        dayList.add(0,dayListElem);

        m_income += d_income;
        m_outcome += d_outcome;

        cursor.close();

        result.setT_income(m_income);
        result.setT_outcome(m_outcome);
        result.setDaylist(dayList);

        return result;
    }


    public TypeBean DB2TypeListBean(int year, int month, String type){

        TypeBean typeBean = new TypeBean();
        float total = 0;
        List<TypeBean.TypeMoneyBean> moneyList = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();

        ArrayList<String> names;
        if (type.equals(DatabaseSchema.DataSchema.income))
            names = DatabaseSchema.DataSchema.names_income;
        else
            names = DatabaseSchema.DataSchema.names_payout;

        for (String name : names) {
            String whereClause = "year = ? AND month = ? AND detailtype = ?";
            String[] whereArg = new String[]{ String.valueOf(year), String.valueOf(month), name};
            String orderBy = DatabaseSchema.DataSchema.money + " DESC";


            int count = 0;
            int affect_money = 0;

            List<DetailBean.DayListBean.ListBean> rank = new ArrayList<>();
            List<String> dates = new ArrayList<>();

            Cursor cursor = db.query("record", null, whereClause, whereArg, null, null, orderBy);

            if (cursor.moveToFirst()) {
                do {
                    int pid = cursor.getInt(cursor.getColumnIndex(DatabaseSchema.DataSchema.recid));
                    String ptype = cursor.getString(cursor.getColumnIndex(DatabaseSchema.DataSchema.rectype));
                    float pmoney = cursor.getFloat(cursor.getColumnIndex(DatabaseSchema.DataSchema.money));
                    String pdes = cursor.getString(cursor.getColumnIndex(DatabaseSchema.DataSchema.recdescription));
                    String detail = cursor.getString(cursor.getColumnIndex(DatabaseSchema.DataSchema.recdetail));
                    int date_year = cursor.getInt(cursor.getColumnIndex(DatabaseSchema.DataSchema.year));
                    int date_month = cursor.getInt(cursor.getColumnIndex(DatabaseSchema.DataSchema.month));
                    int date_day = cursor.getInt(cursor.getColumnIndex(DatabaseSchema.DataSchema.day));

                    DetailBean.DayListBean.ListBean elem = new DetailBean.DayListBean.ListBean();
                    elem.setId(pid);
                    elem.setAffect_money(pmoney);
                    elem.setDescription(pdes);
                    elem.setType(ptype);
                    elem.setDetailtype(detail);


                    affect_money += pmoney;

                    if(count < 3){
                        rank.add(elem);
                        dates.add(getWeekDayString(date_year, date_month, date_day));
                    }
                } while (cursor.moveToNext());
            }

            TypeBean.TypeMoneyBean typeMoneyBean = new TypeBean.TypeMoneyBean();
            if(affect_money != 0) {
                typeMoneyBean.setAffect_money(affect_money);
                typeMoneyBean.setBack_color(ResourcesUtil.typeColor.get(name));
                typeMoneyBean.setRank(rank);
                typeMoneyBean.setTypeName(name);
                typeMoneyBean.setDates(dates);

                moneyList.add(typeMoneyBean);
                total += affect_money;
            }
        }

        typeBean.setTotal(total);
        typeBean.setType_money(moneyList);

        return typeBean;
    }







    private String getWeekDayString(int year, int month, int day){
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);

        String weekdayStr = "";
        int weekday = cal.get(Calendar.DAY_OF_WEEK);

        switch (weekday){
            case 1:
                weekdayStr = "Sunday";
                break;
            case 2:
                weekdayStr = "Monday";
                break;
            case 3:
                weekdayStr = "Tuesday";
                break;
            case 4:
                weekdayStr = "Wednesday";
                break;
            case 5:
                weekdayStr = "Thursday";
                break;
            case 6:
                weekdayStr = "Friday";
                break;
            case 7:
                weekdayStr = "Saturday";
                break;
        }
        return String.valueOf(day) + "  -  " + weekdayStr;
    }

}
