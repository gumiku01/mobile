package com.example.zhanyuan.finalapp.CityObjects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.example.zhanyuan.finalapp.R;
import com.example.zhanyuan.finalapp.utils.UtilConstants;

public class SpecialBuilding extends Infrastructure {


    public SpecialBuilding(Context context, int type, int x, int y, int level) {
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
        if(getType() == UtilConstants.BANK[0]) {
            setmBitmap(BitmapFactory.decodeResource(getmContext().getResources(), R.drawable.banca));
            setWidthHeight(UtilConstants.BANK[1], UtilConstants.BANK[2]);
            setCost(UtilConstants.BANK[3]);

            setName("BANK");
            setUpgradeCost(50);
        }
        if(getType() == UtilConstants.TOWN_HALL[0]){
            setmBitmap(BitmapFactory.decodeResource(getmContext().getResources(), R.drawable.municipio));
            setWidthHeight(UtilConstants.TOWN_HALL[1], UtilConstants.TOWN_HALL[2]);
            setCost(UtilConstants.TOWN_HALL[3]);

            setName("TOWN_HALL");
            setUpgradeCost(100);
        }
        if(getType() == UtilConstants.HOSPITAL[0]){
            setmBitmap(BitmapFactory.decodeResource(getmContext().getResources(), R.drawable.ospetale));
            setWidthHeight(UtilConstants.HOSPITAL[1], UtilConstants.HOSPITAL[2]);
            setCost(UtilConstants.HOSPITAL[3]);

            setName("HOSPITAL");
            setUpgradeCost(50);
        }
        if(getType() == UtilConstants.POLICE[0]){
            setmBitmap(BitmapFactory.decodeResource(getmContext().getResources(), R.drawable.polizia));
            setWidthHeight(UtilConstants.POLICE[1], UtilConstants.POLICE[2]);
            setCost(UtilConstants.POLICE[3]);

            setName("POLICE OFFICE");
            setUpgradeCost(50);
        }
        if(getType() == UtilConstants.RESTAURANT[0]){
            setmBitmap(BitmapFactory.decodeResource(getmContext().getResources(), R.drawable.restaurante));
            setWidthHeight(UtilConstants.RESTAURANT[1], UtilConstants.RESTAURANT[2]);
            setCost(UtilConstants.RESTAURANT[3]);

            setName("RESTAURANT");
            setUpgradeCost(150);
        }
        if(getType() == UtilConstants.SCHOOL[0]){
            setmBitmap(BitmapFactory.decodeResource(getmContext().getResources(), R.drawable.scuola));
            setWidthHeight(UtilConstants.SCHOOL[1],UtilConstants.SCHOOL[2]);
            setCost(UtilConstants.SCHOOL[3]);

            setName("SCHOOL");
            setUpgradeCost(70);
        }
        if(getType() == UtilConstants.FIREFIGHTING[0]){
            setmBitmap(BitmapFactory.decodeResource(getmContext().getResources(), R.drawable.vigile_fuoco));
            setWidthHeight(UtilConstants.FIREFIGHTING[1], UtilConstants.FIREFIGHTING[2]);
            setCost(UtilConstants.FIREFIGHTING[3]);

            setName("FIREFIGHTING");
            setUpgradeCost(30);
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
