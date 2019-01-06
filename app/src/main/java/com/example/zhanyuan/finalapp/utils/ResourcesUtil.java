package com.example.zhanyuan.finalapp.utils;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.example.zhanyuan.finalapp.R;

import java.util.HashMap;

public class ResourcesUtil {

    public static final String food = "food & drink";
    public static final String clothes = "clothes";
    public static final String transportation = "transportation";
    public static final String housing = "housing";
    public static final String entertainment = "entertainment";
    public static final String study = "study";
    public static final String medicine = "medicine";
    public static final String others = "others";

    public static final String job = "job";
    public static final String gift = "gift";
    public static final String interest = "interest";

    public static final String[] type_for_sp = new String[]{
            food, transportation, housing, study, medicine, others
    };


    public static final HashMap<String, Integer> typeStr2Int = new HashMap<String, Integer>(){
        {
            put(food, 101);
            put(clothes, 102);
            put(transportation, 103);
            put(housing, 104);
            put(entertainment, 105);
            put(study, 106);
            put(medicine, 107);
            put(job, 108);
            put(gift, 109);
            put(interest, 110);
            put(others, 111);
        }
    };

    public static final HashMap<String, String> typeColor = new HashMap<String, String>(){
        {
            put(food, "#e4b24d");
            put(clothes, "#aeba42");
            put(transportation, "#62c146");
            put(housing, "#51cc86");
            put(entertainment, "#45c1b7");
            put(study, "#4583c1");
            put(medicine, "#714bb9");
            put(job, "#92449c");
            put(gift, "#bc4d97");
            put(interest, "#aabf0c");
            put(others, "#585946");
        }
    };


    public static Drawable getTypeDrawable(String type, Resources resources){
        Drawable drawable = null;
        switch (typeStr2Int.get(type)){
            case 101: drawable = resources.getDrawable(R.mipmap.type_foods);break;
            case 102: drawable = resources.getDrawable(R.mipmap.type_clothes);break;
            case 103: drawable = resources.getDrawable(R.mipmap.type_transportation);break;
            case 104: drawable = resources.getDrawable(R.mipmap.type_housing);break;
            case 105: drawable = resources.getDrawable(R.mipmap.type_entertainment);break;
            case 106: drawable = resources.getDrawable(R.mipmap.type_study);break;
            case 107: drawable = resources.getDrawable(R.mipmap.type_medicine);break;
            case 108: drawable = resources.getDrawable(R.mipmap.type_job);break;
            case 109: drawable = resources.getDrawable(R.mipmap.type_gift);break;
            case 110: drawable = resources.getDrawable(R.mipmap.type_interest);break;
            case 111: drawable = resources.getDrawable(R.mipmap.type_others);break;
        }
        return drawable;
    }
}
