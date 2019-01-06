package com.example.zhanyuan.finalapp.CityObjects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.example.zhanyuan.finalapp.R;
import com.example.zhanyuan.finalapp.utils.UtilConstants;

public class Apartment extends Infrastructure {

    public Apartment(Context context, int type, int x, int y, int level) {
        super(context, type, x, y, level);
        init();
        ChangeBitmapSize();
    }

    @Override
    public void MyDraw(Canvas canvas, Paint paint) {

        setMyPosition(getmPosX() + (UtilConstants.TILE_WIDTH / 2 * (getHeight() - 1) ), getmPosY());

        canvas.save();
        canvas.clipRect(getmPosX() - getmBitmap().getWidth(), getmPosY() - getmBitmap().getHeight(), getmPosX(), getmPosY());
        canvas.drawBitmap(getmBitmap(), getmPosX() - getmBitmap().getWidth(), getmPosY() - getmBitmap().getHeight(), paint);
        canvas.restore();
    }

    @Override
    public void init(){
        if(getType() == UtilConstants.APARTMENT1[0]){
            setmBitmap(BitmapFactory.decodeResource(getmContext().getResources(), R.drawable.appartamento1));
            setWidthHeight(UtilConstants.APARTMENT1[1], UtilConstants.APARTMENT1[2]);

            setName("APARTMENT1");

            setUpgradeCost(300);
            setCost(UtilConstants.APARTMENT1[3]);


        }
        if(getType() == UtilConstants.APARTMENT2[0]){
            setmBitmap(BitmapFactory.decodeResource(getmContext().getResources(), R.drawable.appartamento2));
            setWidthHeight(UtilConstants.APARTMENT2[1], UtilConstants.APARTMENT2[2]);
            setName("APARTMENT2");

            setUpgradeCost(40);
            setCost(UtilConstants.APARTMENT2[3]);
        }
        if(getType() == UtilConstants.APARTMENT3[0]){
            setmBitmap(BitmapFactory.decodeResource(getmContext().getResources(), R.drawable.appartamento3));
            setWidthHeight(UtilConstants.APARTMENT3[1], UtilConstants.APARTMENT3[2]);
            setName("APARTMENT3");

            setUpgradeCost(50);
            setCost(UtilConstants.APARTMENT3[3]);
        }
        if(getType() == UtilConstants.APARTMENT4[0]){
            setmBitmap(BitmapFactory.decodeResource(getmContext().getResources(), R.drawable.appartamento4));
            setWidthHeight(UtilConstants.APARTMENT4[1], UtilConstants.APARTMENT4[2]);
            setName("APARTMENT4");

            setUpgradeCost(60);
            setCost(UtilConstants.APARTMENT4[3]);
        }
    }

    private void ChangeBitmapSize(){
        int width = getmBitmap().getWidth();
        int height = getmBitmap().getHeight();

        float scale;

        if(getWidth() != 4)
            scale = ((float)width / (UtilConstants.TILE_WIDTH * getWidth()));
        else
            scale = ((float)width / (UtilConstants.TILE_WIDTH * (getWidth() - 1)));

        Matrix matrix = new Matrix();
        matrix.postScale( 1 / scale, 1 / scale);

        setmBitmap(Bitmap.createBitmap(getmBitmap(),0,0, width, height, matrix,true));
    }
}
