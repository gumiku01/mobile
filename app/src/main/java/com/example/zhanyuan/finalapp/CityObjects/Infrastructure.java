package com.example.zhanyuan.finalapp.CityObjects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.zhanyuan.finalapp.R;

public abstract class Infrastructure {

    private Context mContext;

    private String name;
    private int cost;
    private int upgradeCost;
    private int income;
    private Bitmap mBitmap;
    private int type;      // type of infrastructure
    private int mapCoordX, mapCoordY;   // row and col position within map(bottom right cell of map)
    private int width, height;      // how many tile occupies horizontally and vertically
    private int level;      // level of infrastructure

    private int mPosX, mPosY;       // global coordinates of right-bottom point

    private boolean inEdit = false;
    private boolean exitEditMode = true;

    public Infrastructure(Context context, int type, int x, int y, int level){
        this.mContext = context;
        this.type = type;
        this.mapCoordX = x;
        this.mapCoordY = y;
        this.level = level;
    }

    public abstract void init();
    public abstract void MyDraw(Canvas canvas, Paint paint);

    public void PopMyInfo(){
        final View view = LayoutInflater.from(getmContext()).inflate(R.layout.building_info_layout,null, false);
        final PopupWindow infoWindow = new PopupWindow(view, 1000, 700, true);

        infoWindow.setAnimationStyle(R.anim.pop_menu_anim);
        infoWindow.setTouchable(true);
        infoWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        infoWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        infoWindow.showAtLocation(view, Gravity.CENTER,0,0);

        ImageView imageView = (ImageView)view.findViewById(R.id.buildingImage);
        TextView buildingName = (TextView)view.findViewById(R.id.buildingName);
        TextView buildingLevel = (TextView)view.findViewById(R.id.buildingLevel);
        TextView buildingNext = (TextView)view.findViewById(R.id.buildingToNextLevel);

        imageView.setImageBitmap(getmBitmap());
        buildingName.setText( ("Name:  "  + String.valueOf(getName())));
        buildingLevel.setText(("Level:   " + String.valueOf(getLevel())));
        buildingNext.setText(("Cost to next level:   " + String.valueOf(getUpgradeCost()) + "$"));
    }


    // Getter and Setter
    public void setName(String name) {
        this.name = name;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setUpgradeCost(int upgradeCost) {
        this.upgradeCost = upgradeCost;
    }

    public void setmBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public void setIncome(int income) {
        this.income = income;
    }

    public void setInEdit(boolean inEdit) {
        this.inEdit = inEdit;
    }

    /**
     *  Set the level of infrastructure
     * @param level
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Set the main cell where infrastructure start to draw
     * @param x
     * @param y
     */
    public void setMapCoordinates(int x, int y){
        this.mapCoordX = x;
        this.mapCoordY = y;
    }

    /**
     * Set the real position in global coordinate where start to draw
     * @param x
     * @param y
     */
    public void setMyPosition(int x, int y){
        this.mPosX = x;
        this.mPosY = y;
    }

    /**
     * Set the tiles occupy horizontally and vertically
     * @param width
     * @param height
     */
    public void setWidthHeight(int width, int height){
        this.width = width;
        this.height = height;
    }

    public void setExitEditMode(boolean exitEditMode) {
        this.exitEditMode = exitEditMode;
    }

    public Context getmContext() {
        return mContext;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public int getUpgradeCost() {
        return upgradeCost;
    }

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public int getLevel() {
        return level;
    }

    public int getIncome() {
        return income;
    }

    public int getMapCoordX() {
        return mapCoordX;
    }

    public int getMapCoordY() {
        return mapCoordY;
    }

    public int getmPosX() {
        return mPosX;
    }

    public int getmPosY() {
        return mPosY;
    }

    public int getType() {
        return type;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public boolean isInEdit() {
        return inEdit;
    }

    public boolean isExitEditMode() {
        return exitEditMode;
    }
}
