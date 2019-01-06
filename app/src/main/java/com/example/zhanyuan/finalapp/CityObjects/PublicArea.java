package com.example.zhanyuan.finalapp.CityObjects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.example.zhanyuan.finalapp.R;
import com.example.zhanyuan.finalapp.utils.UtilConstants;

public class PublicArea extends Infrastructure{

    public PublicArea(Context context, int type, int x, int y, int level) {
        super(context, type, x, y, level);
        init();
        ChangeBitmapSize();
    }

    @Override
    public void MyDraw(Canvas canvas, Paint paint) {
        canvas.save();
        canvas.clipRect(getmPosX() - getmBitmap().getWidth(), getmPosY() - getmBitmap().getHeight(), getmPosX(), getmPosY());
        canvas.drawBitmap(getmBitmap(), getmPosX() - getmBitmap().getWidth(), getmPosY() - getmBitmap().getHeight(), paint);
        canvas.restore();
    }

    @Override
    public void init() {
        if(getType() == UtilConstants.TREE[0]){
            setmBitmap(BitmapFactory.decodeResource(getmContext().getResources(), R.drawable.albero));
            setWidthHeight(UtilConstants.TREE[1], UtilConstants.TREE[2]);
            setCost(UtilConstants.TREE[3]);

            setName("TREE");
            setUpgradeCost(5);
        }
        if(getType() == UtilConstants.FOUNTAIN[0]){
            setmBitmap(BitmapFactory.decodeResource(getmContext().getResources(), R.drawable.fontana));
            setWidthHeight(UtilConstants.FOUNTAIN[1], UtilConstants.FOUNTAIN[2]);
            setCost(UtilConstants.FOUNTAIN[3]);

            setName("FOUNTAIN");
            setUpgradeCost(7);
        }
        if(getType() == UtilConstants.MOUNTAIN[0]){
            setmBitmap(BitmapFactory.decodeResource(getmContext().getResources(), R.drawable.montagna));
            setWidthHeight(UtilConstants.MOUNTAIN[1], UtilConstants.MOUNTAIN[2]);
            setCost(UtilConstants.MOUNTAIN[3]);

            setName("MOUNTAIN");
            setUpgradeCost(10);
        }
        if(getType() == UtilConstants.PARK1[0]){
            setmBitmap(BitmapFactory.decodeResource(getmContext().getResources(), R.drawable.parco1));
            setWidthHeight(UtilConstants.PARK1[1], UtilConstants.PARK1[2]);
            setCost(UtilConstants.PARK1[3]);

            setName("PARK1");
            setUpgradeCost(10);
        }
        if(getType() == UtilConstants.PARK2[0]){
            setmBitmap(BitmapFactory.decodeResource(getmContext().getResources(), R.drawable.parco2));
            setWidthHeight(UtilConstants.PARK2[1], UtilConstants.PARK2[2]);
            setCost(UtilConstants.PARK2[3]);

            setName("PARK2");
            setUpgradeCost(10);
        }
        if(getType() == UtilConstants.PARK3[0]){
            setmBitmap(BitmapFactory.decodeResource(getmContext().getResources(), R.drawable.parco3));
            setWidthHeight(UtilConstants.PARK3[1], UtilConstants.PARK3[2]);
            setCost(UtilConstants.PARK3[3]);

            setName("PARK3");
            setUpgradeCost(10);
        }
    }

    private void ChangeBitmapSize(){
        int width = getmBitmap().getWidth();
        int height = getmBitmap().getHeight();

        float scale = ((float)width / UtilConstants.TILE_WIDTH);

        Matrix matrix = new Matrix();
        matrix.postScale( 1 / scale, 1 / scale);

        setmBitmap(Bitmap.createBitmap(getmBitmap(),0,0, width, height, matrix,true));
    }
}
