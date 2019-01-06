package com.example.zhanyuan.finalapp.CityObjects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import com.example.zhanyuan.finalapp.R;
import com.example.zhanyuan.finalapp.utils.UtilConstants;

public class Street extends Infrastructure {

    public Street(Context context, int type, int x, int y, int level) {
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
    public void init(){
        if(getType() == UtilConstants.STREET_HORIZONTAL[0]){
            setmBitmap(BitmapFactory.decodeResource(getmContext().getResources(), R.drawable.strada_orizzontale));
            setWidthHeight(UtilConstants.STREET_HORIZONTAL[1], UtilConstants.STREET_HORIZONTAL[2]);
            setCost(UtilConstants.STREET_HORIZONTAL[3]);

            setName("STREET_HORIZONTAL");
            setUpgradeCost(10);
        }
        if(getType() == UtilConstants.STREET_VERTICAL[0]){
            setmBitmap(BitmapFactory.decodeResource(getmContext().getResources(), R.drawable.strada_verticale));
            setWidthHeight(UtilConstants.STREET_VERTICAL[1], UtilConstants.STREET_VERTICAL[2]);
            setCost(UtilConstants.STREET_VERTICAL[3]);

            setName("STREET_VERTICAL");
            setUpgradeCost(10);
        }
        if(getType() == UtilConstants.STREET_BOTTOM_RIGHT[0]){
            setmBitmap(BitmapFactory.decodeResource(getmContext().getResources(), R.drawable.strada_bottom_right));
            setWidthHeight(UtilConstants.STREET_BOTTOM_RIGHT[1], UtilConstants.STREET_BOTTOM_RIGHT[2]);
            setCost(UtilConstants.STREET_BOTTOM_RIGHT[3]);

            setName("STREET_BOTTOM_RIGHT");
            setUpgradeCost(10);
        }
        if(getType() == UtilConstants.STREET_BOTTOM_T[0]){
            setmBitmap(BitmapFactory.decodeResource(getmContext().getResources(), R.drawable.strada_bottom_t));
            setWidthHeight(UtilConstants.STREET_BOTTOM_T[1], UtilConstants.STREET_BOTTOM_T[2]);
            setCost(UtilConstants.STREET_BOTTOM_T[3]);

            setName("STREET_BOTTOM_T");
            setUpgradeCost(10);
        }
        if(getType() == UtilConstants.STREET_CROSS[0]){
            setmBitmap(BitmapFactory.decodeResource(getmContext().getResources(), R.drawable.strada_croce));
            setWidthHeight(UtilConstants.STREET_CROSS[1], UtilConstants.STREET_CROSS[2]);
            setCost(UtilConstants.STREET_CROSS[3]);

            setName("STREET_CROSS");
            setUpgradeCost(10);
        }
        if(getType() == UtilConstants.STREET_LEFT_BOTTOM[0]){
            setmBitmap(BitmapFactory.decodeResource(getmContext().getResources(), R.drawable.strada_left_bottom));
            setWidthHeight(UtilConstants.STREET_LEFT_BOTTOM[1], UtilConstants.STREET_LEFT_BOTTOM[2]);
            setCost(UtilConstants.STREET_LEFT_BOTTOM[3]);

            setName("STREET_LEFT_BOTTOM");
            setUpgradeCost(10);
        }
        if(getType() == UtilConstants.STREET_RIGHT_TOP[0]){
            setmBitmap(BitmapFactory.decodeResource(getmContext().getResources(), R.drawable.strada_right_top));
            setWidthHeight(UtilConstants.STREET_RIGHT_TOP[1], UtilConstants.STREET_RIGHT_TOP[2]);
            setCost(UtilConstants.STREET_RIGHT_TOP[3]);

            setName("STREET_RIGHT_TOP");
            setUpgradeCost(10);
        }
        if(getType() == UtilConstants.STREET_TOP_LEFT[0]){
            setmBitmap(BitmapFactory.decodeResource(getmContext().getResources(), R.drawable.strada_top_left));
            setWidthHeight(UtilConstants.STREET_TOP_LEFT[1], UtilConstants.STREET_TOP_LEFT[2]);
            setCost(UtilConstants.STREET_TOP_LEFT[3]);

            setName("STREET_TOP_LEFT");
            setUpgradeCost(10);
        }
        if(getType() == UtilConstants.STREET_TOP_T[0]){
            setmBitmap(BitmapFactory.decodeResource(getmContext().getResources(), R.drawable.strada_top_t));
            setWidthHeight(UtilConstants.STREET_TOP_T[1], UtilConstants.STREET_TOP_T[2]);
            setCost(UtilConstants.STREET_TOP_T[3]);

            setName("STREET_TOP_T");
            setUpgradeCost(10);
        }
    }


    private void ChangeBitmapSize(){
        int width = getmBitmap().getWidth();
        int height = getmBitmap().getHeight();

        float scale_width = ((float)width / UtilConstants.TILE_WIDTH);
        float scale_height = ((float)height / UtilConstants.TILE_HEIGHT);

        Matrix matrix = new Matrix();
        matrix.postScale( 1 / scale_width, 1 / scale_height);

        setmBitmap(Bitmap.createBitmap(getmBitmap(),0,0, width, height, matrix,true));
    }
}
