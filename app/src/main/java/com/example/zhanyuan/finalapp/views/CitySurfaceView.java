package com.example.zhanyuan.finalapp.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.zhanyuan.finalapp.CityObjects.Apartment;
import com.example.zhanyuan.finalapp.CityObjects.Infrastructure;
import com.example.zhanyuan.finalapp.CityObjects.PublicArea;
import com.example.zhanyuan.finalapp.CityObjects.SpecialBuilding;
import com.example.zhanyuan.finalapp.CityObjects.Street;
import com.example.zhanyuan.finalapp.activities.CityActivity;
import com.example.zhanyuan.finalapp.utils.UtilConstants;
import com.example.zhanyuan.finalapp.R;
import com.example.zhanyuan.finalapp.utils.MessageCode;
import com.example.zhanyuan.finalapp.utils.SharedPrefenrenceHelper;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * view within it, user can build their city using record of data
 */
public class CitySurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private int screen_width, screen_height;

    private String text = "";

    // map variables
    private Infrastructure[][] map;
    private int mapRow, mapCol;     // how many raw and col
    private int  mMapWidth, mMapHeight;    // width and height of map
    private int mMap_X, mMap_Y;     // coordinate of top-left point of map
    private int new_type;

    // draw variables
    private Canvas mCanvas = null;
    private Paint mPaint;
    private Bitmap mBitmap;
    private Thread mThread = null;
    private SurfaceHolder surfaceHolder = null;
    private boolean mIsRunning = false;

    // gesture variables
    private long pressTimeRec;
    private int prevMovePressX, prevMovePressY;     // record the position of previous point of movement
    private int prevEditPressX, prevEditPressY;     // record the position of previous point of edition mode
    private int pressX, pressY;
    private int moveThreshold = 3;      // threshold within that we consider it to stay in the same position
    private int pressTimeThreshold = 500;      // millisecond that we consider gesture to be long press
    private Vibrator vibrator;
    private boolean vibrating;

    // handle messages
     private HandlerThread handlerThread = new HandlerThread("myHandlerThread");
     public Handler mHandler;

    // edition mode
    private Infrastructure infraInEditing = null;
    private int editOrigin_x = -1, editOrigin_y = -1; // original position of infrastructure before moving
    private boolean editMode = false;

    // upgrade mode
    private Infrastructure infraInUpgrade = null;

    // sharedPrefenrence for check money
    SharedPrefenrenceHelper sph;


    // buttons
    Button editButton, infoButton, upgradeButton;

    // Constructors
    public CitySurfaceView(Context context) {
        super(context);
    }
    public CitySurfaceView(Context context, AttributeSet attrs){
        super(context, attrs);
    }
    public CitySurfaceView(Context context, AttributeSet attrs, int defStyleAttrs){
        super(context, attrs, defStyleAttrs);
    }

    // execute when MainActivity enter in onResume()
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsRunning = true;

        //check if the thread is already started
        if(mThread.getState() == Thread.State.NEW){
            mThread.start();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // do something when surfaceView is changed
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // no longer update canvas
        mIsRunning = false;
    }

    // Activity of Lifecycle
    public void Create(){
        initHandlerThread();
        initMap();
    }

    public void Start(){

    }

    public void Resume(){
        mIsRunning = true;
        init();
    }

    public void Pause(){
        if(mIsRunning)
            mIsRunning = false;

        if(infraInEditing != null){
            // if it can be exit from edit mode(with green filter)
            if(infraInEditing.isExitEditMode()){
                RemoveElementFromMap(infraInEditing);
                PutBuildingInAllCell(map, infraInEditing);
                infraInEditing.setInEdit(false);
                infraInEditing = null;
                editMode = false;
            }else{
                // if it can not be, restore the initial position or eliminate building
                if(editOrigin_x != -1 && editOrigin_y != -1)
                    infraInEditing.setMapCoordinates(editOrigin_x, editOrigin_y);
                else{
                    // usually enter in this part of code when user exit form activity during build new infrastructure
                    // do something
                }
            }
        }

        updateMap();
    }

    public void Stop(){
        if(mThread != null)
            mThread.interrupt();
        mThread = null;
        mBitmap.recycle();
        handlerThread.quitSafely();
    }

    public void Destroy(){
        // do something
    }

    public void Restart(){
        // do something
    }

    @Override
    public void run() {
        while(mIsRunning){
            try {
                Thread.sleep(10);
            }catch (InterruptedException e){
                e.printStackTrace();
            }

            // draw elements we need
            synchronized (surfaceHolder){
                mCanvas = surfaceHolder.lockCanvas();
                MyBackgroundDraw(mCanvas);
                MyBorderDraw(mCanvas);
                MyCityDraw(mCanvas);
                //MyTextDraw();
                surfaceHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    /**
     * initialize draw instruments
     */
    private void init(){
        // initialize Paint
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(25);

        // initialize background bitmap
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.desert);

        // initialize surfaceHolder
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        mThread = new Thread(this);

        sph = new SharedPrefenrenceHelper(getContext());
        vibrator = (Vibrator)getContext().getSystemService(Context.VIBRATOR_SERVICE);
    }

    /**
     * initialize a new thread to handle messages
     */
    private void initHandlerThread(){
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                // if message is used for building new infrastructure
                if(msg.what == MessageCode.BUILD_MESSAGE){
                    int type = msg.getData().getInt(MessageCode.TYPE);
                    if(!editMode)
                        BuildInfrastructure(type);
                    else
                        Toast.makeText(getContext(), "place finish previous edit", Toast.LENGTH_LONG).show();
                }
                if(msg.what == MessageCode.UPGRADE_REQUEST_MESSAGE){
                    boolean enough = msg.getData().getBoolean(MessageCode.REPLY);
                    if(enough){
                        Toast.makeText(getContext(), "upgrade successfully", Toast.LENGTH_LONG).show();
                        infraInUpgrade.setLevel(infraInUpgrade.getLevel() + 1);
                    }

                    // if the building upgraded is town hall we add dimension of map
                    if(infraInUpgrade.getType() == UtilConstants.TOWN_HALL[0]){
                        sph.save("level", String.valueOf(infraInUpgrade.getLevel()));
                        Expand();
                    }
                    infraInUpgrade = null;
                }
                if(msg.what == MessageCode.SCREENSHOT){
                    //ShotShareUtil.shotShare();

                    Bitmap bitmap = Bitmap.createBitmap(screen_width, screen_height, Bitmap.Config.ARGB_8888);
                    Canvas canvasShot = new Canvas(bitmap);
                    draw(canvasShot);
                }
            }
        };
    }

    /**
     * initialize the map
     */
    private void initMap(){

        //basic element to determine an infrastructure
        int xCoordinate, yCoordinate, type, level, cost;

        InputStream iStream;
        try {

            // load the map.xml from asset folder if we can not find local file
            try{
                iStream = getContext().openFileInput("map.xml");
            }catch (Exception e){
                iStream = getResources().getAssets().open("map.xml");
            }

            // parse the xml file
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            Document document = docBuilder.parse(iStream);

            // get map info(width, height)
            Node mapInformation = document.getElementsByTagName("basicInformation").item(0);
            NodeList infoChildNodes = mapInformation.getChildNodes();

            for (int i = 0; i < infoChildNodes.getLength(); i++){
                if(infoChildNodes.item(i).getNodeType() == Node.ELEMENT_NODE){
                    if("width".equals(infoChildNodes.item(i).getNodeName())){
                        mapCol = Integer.parseInt(infoChildNodes.item(i).getFirstChild().getNodeValue());
                    }

                    if("height".equals(infoChildNodes.item(i).getNodeName())){
                        mapRow = Integer.parseInt(infoChildNodes.item(i).getFirstChild().getNodeValue());
                    }
                }
            }

            // initialize matrix of map
            map = new Infrastructure[mapRow][mapCol];

            // load infrastructure on the map
            NodeList buildingList = document.getElementsByTagName("Element");

            for(int i = 0; i < buildingList.getLength(); i++){
                if(buildingList.item(i).getNodeType() == Node.ELEMENT_NODE){

                    // get information about building
                    Element elemInfo = (Element)buildingList.item(i);
                    type = Integer.parseInt(elemInfo.getElementsByTagName("type").item(0).getFirstChild().getNodeValue());
                    xCoordinate = Integer.parseInt(elemInfo.getElementsByTagName("xCoordinate").item(0).getFirstChild().getNodeValue());
                    yCoordinate = Integer.parseInt(elemInfo.getElementsByTagName("yCoordinate").item(0).getFirstChild().getNodeValue());
                    level = Integer.parseInt(elemInfo.getElementsByTagName("level").item(0).getFirstChild().getNodeValue());

                    // from 1 to 9: street class
                    // from 10 to 13: apartment class
                    // from 14 to 20: specialBuilding class
                    // from 21 to 26: publicArea class
                    Object building;
                    if(type > 0 && type < 10){
                        building = new Street(getContext(), type, xCoordinate, yCoordinate, level);
                        map[yCoordinate][xCoordinate] = (Street)building;
                    }
                    if(type >= 10 && type < 14){
                        building = new Apartment(getContext(),type, xCoordinate, yCoordinate, level);
                        PutBuildingInAllCell(map, (Apartment)building);
                    }
                    if(type >= 14 && type < 21){
                        building = new SpecialBuilding(getContext(), type, xCoordinate, yCoordinate, level);
                        PutBuildingInAllCell(map, (SpecialBuilding)building);
                    }
                    if(type >= 21 && type < 27){
                        building = new PublicArea(getContext(), type, xCoordinate, yCoordinate, level);
                        map[yCoordinate][xCoordinate] = (PublicArea)building;
                    }
                }
            }

            // Compute the width and height of map
            mMapWidth = mapRow * UtilConstants.TILE_WIDTH;
            mMapHeight = mapCol * UtilConstants.TILE_HEIGHT;

            // Compute the coordinate of left-top point of map in global space
            WindowManager windowManager = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screen_width = size.x;
            screen_height = size.y;
            mMap_X = (screen_width / 2) - (mMapWidth / 2);
            mMap_Y = (screen_height / 2) - (mMapHeight / 2);


        }catch (IOException | ParserConfigurationException | SAXException e){
            e.printStackTrace();
        }
    }

    /**
     * update file map.xml
     */
    private void updateMap(){

        try{

            OutputStream oStream = getContext().openFileOutput("map.xml", Context.MODE_PRIVATE);
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(oStream, "utf-8");
            serializer.startDocument("utf-8", true);

            serializer.startTag(null, "MapElements");

            serializer.startTag(null, "basicInformation");
            serializer.startTag(null, "width");
            serializer.text(Integer.toString(mapCol));
            serializer.endTag(null, "width");
            serializer.startTag(null, "height");
            serializer.text(Integer.toString(mapRow));
            serializer.endTag(null, "height");
            serializer.endTag(null, "basicInformation");

            serializer.startTag(null, "Elements");

            for(int i = 0; i < mapRow; i++){
                for(int j = 0; j < mapCol; j++){
                    if(map[i][j] != null){
                        if(map[i][j].getMapCoordX() == j && map[i][j].getMapCoordY() == i){
                            serializer.startTag(null, "Element");

                            serializer.startTag(null, "type");
                            serializer.text(Integer.toString(map[i][j].getType()));
                            serializer.endTag(null, "type");

                            serializer.startTag(null, "xCoordinate");
                            serializer.text(Integer.toString(j));
                            serializer.endTag(null, "xCoordinate");

                            serializer.startTag(null, "yCoordinate");
                            serializer.text(Integer.toString(i));
                            serializer.endTag(null, "yCoordinate");

                            serializer.startTag(null, "level");
                            serializer.text(Integer.toString(map[i][j].getLevel()));
                            serializer.endTag(null, "level");

                            serializer.endTag(null, "Element");
                        }
                    }
                }
            }

            serializer.endTag(null, "Elements");
            serializer.endTag(null, "MapElements");
            serializer.endDocument();
            oStream.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    // Draw the background
    private void MyBackgroundDraw(Canvas canvas){
        canvas.drawBitmap(mBitmap,0,0, mPaint);
    }

    // Draw the border of map
    private void MyBorderDraw(Canvas canvas){
        mPaint.setColor(Color.BLACK);
        int[] top = ComputeMapCoordinateToPixel(0,0);
        int[] right = ComputeMapCoordinateToPixel(0, mapCol);
        int[] bottom = ComputeMapCoordinateToPixel(mapRow, mapCol);
        int[] left = ComputeMapCoordinateToPixel(mapRow, 0);
        canvas.drawLine(top[1] - (UtilConstants.TILE_WIDTH / 2), top[0] - UtilConstants.TILE_HEIGHT,
                right[1] - UtilConstants.TILE_WIDTH / 2, right[0] - UtilConstants.TILE_HEIGHT, mPaint);
        canvas.drawLine(right[1] - UtilConstants.TILE_WIDTH / 2, right[0] - UtilConstants.TILE_HEIGHT,
                bottom[1] - UtilConstants.TILE_WIDTH / 2, bottom[0] - UtilConstants.TILE_HEIGHT, mPaint);
        canvas.drawLine(bottom[1] - UtilConstants.TILE_WIDTH / 2, bottom[0] - UtilConstants.TILE_HEIGHT,
                left[1] - UtilConstants.TILE_WIDTH / 2, left[0] - UtilConstants.TILE_HEIGHT, mPaint);
        canvas.drawLine(left[1] - UtilConstants.TILE_WIDTH / 2, left[0] - UtilConstants.TILE_HEIGHT,
                top[1] - (UtilConstants.TILE_WIDTH / 2), top[0] - UtilConstants.TILE_HEIGHT, mPaint);
    }

    /**
     * Draw all element on the map
     */
    private void MyCityDraw(Canvas canvas){
        int[] coordinate;
        int x, y;
        Infrastructure elem;

        for(int i = 0; i < mapRow; i++){
            for(int j = 0; j < mapCol; j++){
                elem = map[i][j];

                // draw only on the bottom right cell to avoid the overlap
                if(elem != null && i == elem.getMapCoordY() && j == elem.getMapCoordX()){
                    if(!elem.isInEdit()) {
                        coordinate = ComputeMapCoordinateToPixel(i, j);
                        y = coordinate[0];
                        x = coordinate[1];


                        elem.setMyPosition(x, y);
                        elem.MyDraw(canvas, mPaint);
                    }
                }
            }
        }

        // Draw the infrastructure in edit mode
        if(infraInEditing != null){
            int[] editPosition = ComputeMapCoordinateToPixel(infraInEditing.getMapCoordY(), infraInEditing.getMapCoordX());
            infraInEditing.setMyPosition(editPosition[1], editPosition[0]);

            // set the color filter
            if(!OutOfMap(infraInEditing) && !AnyCellOverlap(infraInEditing)){
                mPaint.setColorFilter(new ColorMatrixColorFilter(UtilConstants.GREEN_FILTER));
                infraInEditing.setExitEditMode(true);
            }else {
                mPaint.setColorFilter(new ColorMatrixColorFilter(UtilConstants.RED_FILTER));
                infraInEditing.setExitEditMode(false);
            }
            infraInEditing.MyDraw(mCanvas, mPaint);
            mPaint.setColorFilter(new ColorMatrixColorFilter(UtilConstants.NORMAL_FILTER));
        }
    }

    private void MyTextDraw(){
        mCanvas.save();
        mCanvas.clipRect(0, 0, screen_width, 30);
        mPaint.setColor(Color.WHITE);
        mCanvas.drawRect(0, 0, screen_width, 30, mPaint);
        mPaint.setColor(Color.RED);
        mCanvas.drawText(text, 0, 20, mPaint);
        mCanvas.restore();
    }

    /**
     * put the building in all cell where they rest on,
     * starting from the bottom right cell of map, put same reference in all cell occupied
     * @param map
     * @param building
     */
    private void PutBuildingInAllCell(Infrastructure[][] map, Infrastructure building){
        int width = building.getWidth();
        int height = building.getHeight();

        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                map[building.getMapCoordY() - i][building.getMapCoordX() - j] = building;
            }
        }
    }

    /**
     * Convert the map coordinate into global pixel, the point is right-bottom corner of image
     * @param row: row of map
     * @param col: col of map
     * @return 0 -> height; 1 -> width
     */
    private int[] ComputeMapCoordinateToPixel(int row, int col){
        int x, y;

        x = (UtilConstants.TILE_WIDTH / 2 * col) + (-UtilConstants.TILE_WIDTH / 2 * row) + (mMap_X + mMapWidth / 2 + UtilConstants.TILE_WIDTH / 2);
        y = (UtilConstants.TILE_HEIGHT / 2 * col) + (UtilConstants.TILE_HEIGHT / 2 * row) + (mMap_Y + UtilConstants.TILE_HEIGHT);

        return new int[]{y, x};
    }

    /**
     * Given the coordinate of global pixel, convert it into the mapCoordinate
     * @param point_x: width point
     * @param point_y: height point
     * @return
     */
    private int[] ComputePixelToMapCoordinate(int point_x, int point_y){
        // n -> new normal vector of y
        // m -> new normal vector of x
        int m, n;

        // origin of new reference of coordinate, the top point of rhombus
        int new_origin_x = mMap_X + mapCol * UtilConstants.TILE_WIDTH / 2;
        int new_origin_y = mMap_Y;

        n = FloatToInt((-(float)(point_x - new_origin_x) / (float)UtilConstants.TILE_WIDTH) + ((float)(point_y - new_origin_y) / (float)UtilConstants.TILE_HEIGHT)) - 1;
        m = FloatToInt(((float)(point_x - new_origin_x) / (float)UtilConstants.TILE_WIDTH) + ((float)(point_y - new_origin_y) / (float)UtilConstants.TILE_HEIGHT)) - 1;

        if(n < 0 || n >= mapRow){
        }
        else if(m < 0 || m >= mapCol){
        }
        else if(map[n][m] != null){
            return new int[]{n, m};
        }
        return null;
    }

    /**
     *
     * @param infra
     * @return
     */
    private boolean OutOfMap(Infrastructure infra){
        boolean outside = true;
        if(infra.getMapCoordY() - infra.getHeight() + 1 >= 0 &&
                infra.getMapCoordY() < mapRow &&
                infra.getMapCoordX() - infra.getWidth() + 1 >= 0 &&
                infra.getMapCoordX() < mapCol){
            outside = false;
        }
        return outside;
    }

    /**
     *
     * @param infra
     * @return
     */
    private boolean AnyCellOverlap(Infrastructure infra){
        boolean overlap = false;

        int count = 0;

        for(int i = 0; i < infra.getHeight(); i++){
            for(int j = 0; j < infra.getWidth(); j++){

                if(map[infra.getMapCoordY() - i][infra.getMapCoordX() - j] != null &&
                        map[infra.getMapCoordY() - i][infra.getMapCoordX() - j] != infra){
                    overlap = true;
                }
            }
        }
        return overlap;
    }

    /**
     * convert float number to integer
     * @param n: float number
     * @return correspond int number
     */
    private int FloatToInt(float n){
        if(n < 0)
            n = (int)n - 1;
        return (int)n;
    }

    /**
     *  remove element from all cell it occupies
     * @param infrastructure
     */
    private void RemoveElementFromMap(Infrastructure infrastructure){
        for(int i = 0; i < mapRow; i++)
            for(int j = 0; j < mapCol; j++)
                if(map[i][j] == infrastructure)
                    map[i][j] = null;
    }

    private int[] BuildInfrastructure(int type){

        // try to find the building we are looking for
        for (int[] elem : UtilConstants.STREET) {
            if(elem[0] == type){
                infraInEditing = new Street(getContext(), elem[0],elem[1], elem[2], 1);
            }
        }
        for (int[] elem : UtilConstants.APARTMENT) {
            if(elem[0] == type){
                infraInEditing = new Apartment(getContext(), elem[0], elem[1], elem[2], 1);
            }
        }
        for (int[] elem : UtilConstants.SPECIAL) {
            if(elem[0] == type){
                infraInEditing = new SpecialBuilding(getContext(), elem[0], elem[1], elem[2], 1);
            }
        }
        for (int[] elem : UtilConstants.PUBLIC) {
            if(elem[0] == type){
                infraInEditing = new PublicArea(getContext(), elem[0], elem[1], elem[2], 1);
            }
        }



        // go to edit mode with new building
        editMode = true;
        infraInEditing.setInEdit(true);
        infraInEditing.setExitEditMode(false);
        editOrigin_y = -1;
        editOrigin_x = -1;


        return null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:

                // record values
                pressTimeRec = System.currentTimeMillis();

                pressX = (int)event.getRawX();
                pressY = (int)event.getRawY();
                prevMovePressX = pressX;
                prevMovePressY = pressY;
                prevEditPressX = pressX;
                prevEditPressY = pressY;

                // if variable "infraInEditing" is not null, it means that we are in edit mode.
                if(infraInEditing != null){
                    int[] pixelCoordinate = ComputeMapCoordinateToPixel(infraInEditing.getMapCoordY() + 1, infraInEditing.getMapCoordX() + 1);

                    // if position pressed is outside of determine zone, and it can be exit from edit mode(with green filter)
                    if((pressX > pixelCoordinate[1] || pressX < pixelCoordinate[1] - 300 ||
                            pressY > pixelCoordinate[0] || pressY < pixelCoordinate[0] - 300) && infraInEditing.isExitEditMode()){
                        RemoveElementFromMap(infraInEditing);
                        PutBuildingInAllCell(map, infraInEditing);
                        infraInEditing.setInEdit(false);
                        infraInEditing = null;
                        editMode = false;
                    }
                }
                break;

            case MotionEvent.ACTION_MOVE:

                prevMovePressX = pressX;
                prevMovePressY = pressY;
                pressX = (int)event.getRawX();
                pressY = (int)event.getRawY();

                // if in edit mode, move only the infrastructure in editing
                if(editMode){

                    boolean updated = false;
                    // move to the right cell
                    if(pressY - prevEditPressY > UtilConstants.TILE_HEIGHT / 2 && pressX - prevEditPressX > UtilConstants.TILE_WIDTH / 2){
                        if(infraInEditing.getMapCoordX() + 1 < mapCol) {
                            infraInEditing.setMapCoordinates(infraInEditing.getMapCoordX() + 1, infraInEditing.getMapCoordY());

                            // check if we need update also origin of map in global space
                            // by moving infraInEditing to right cell, we may need more space in right-bottom
                            int[] pixel = ComputeMapCoordinateToPixel(infraInEditing.getMapCoordY(), infraInEditing.getMapCoordX());
                            if(pixel[1] > screen_width - UtilConstants.TILE_WIDTH)
                                mMap_X -= UtilConstants.TILE_WIDTH;
                            if(pixel[0] > screen_height - UtilConstants.TILE_HEIGHT * 2)
                                mMap_Y -= UtilConstants.TILE_HEIGHT * 1.5;
                        }
                        updated = true;
                    }
                    // move to the right-bottom
                    if(pressY - prevEditPressY > UtilConstants.TILE_HEIGHT && Math.abs(pressX - prevEditPressX) < UtilConstants.TILE_WIDTH / 2){
                        if(infraInEditing.getMapCoordX() + 1 < mapCol)
                            infraInEditing.setMapCoordinates(infraInEditing.getMapCoordX() + 1, infraInEditing.getMapCoordY());
                        if(infraInEditing.getMapCoordY() + 1 < mapRow)
                            infraInEditing.setMapCoordinates(infraInEditing.getMapCoordX(),infraInEditing.getMapCoordY() + 1);

                        // need space at bottom
                        int[] pixel = ComputeMapCoordinateToPixel(infraInEditing.getMapCoordY(), infraInEditing.getMapCoordX());
                        if(pixel[0] > screen_height - UtilConstants.TILE_HEIGHT * 2)
                            mMap_Y -= UtilConstants.TILE_HEIGHT * 1.5;

                        updated = true;
                    }
                    // move to bottom cell
                    if(pressY - prevEditPressY > UtilConstants.TILE_HEIGHT / 2 && pressX - prevEditPressX < - UtilConstants.TILE_WIDTH / 2){
                        if(infraInEditing.getMapCoordY() + 1 < mapRow)
                            infraInEditing.setMapCoordinates(infraInEditing.getMapCoordX(),infraInEditing.getMapCoordY() + 1);

                        // need space at left-bottom
                        int[] pixel = ComputeMapCoordinateToPixel(infraInEditing.getMapCoordY(), infraInEditing.getMapCoordX());
                        if(pixel[1] < UtilConstants.TILE_WIDTH * 2)      // since pixel computed if at right side
                            mMap_X += UtilConstants.TILE_WIDTH * 1.5;
                        if(pixel[0] > screen_height - UtilConstants.TILE_HEIGHT * 2)
                            mMap_Y -= UtilConstants.TILE_HEIGHT * 1.5;

                        updated = true;
                    }
                    // move to left-bottom
                    if(Math.abs(pressY - prevEditPressY) < UtilConstants.TILE_HEIGHT / 2 && pressX - prevEditPressX < - UtilConstants.TILE_WIDTH){
                        if(infraInEditing.getMapCoordY() + 1 < mapRow)
                            infraInEditing.setMapCoordinates(infraInEditing.getMapCoordX(),infraInEditing.getMapCoordY() + 1);
                        if(infraInEditing.getMapCoordX() - 1 >= 0)
                            infraInEditing.setMapCoordinates(infraInEditing.getMapCoordX() - 1, infraInEditing.getMapCoordY());

                        // need space at left
                        int[] pixel = ComputeMapCoordinateToPixel(infraInEditing.getMapCoordY(), infraInEditing.getMapCoordX());
                        if(pixel[1] < UtilConstants.TILE_WIDTH * 2)
                            mMap_X += UtilConstants.TILE_WIDTH * 1.5;
                        updated = true;
                    }
                    // move to left
                    if(pressY - prevEditPressY < - UtilConstants.TILE_HEIGHT /2 && pressX - prevEditPressX < - UtilConstants.TILE_WIDTH){
                        if(infraInEditing.getMapCoordX() - 1 >= 0)
                            infraInEditing.setMapCoordinates(infraInEditing.getMapCoordX() - 1, infraInEditing.getMapCoordY());

                        // need space at left-top
                        int[] pixel = ComputeMapCoordinateToPixel(infraInEditing.getMapCoordY(), infraInEditing.getMapCoordX());
                        if(pixel[1] < UtilConstants.TILE_WIDTH * 2)
                            mMap_X += UtilConstants.TILE_WIDTH * 1.5;
                        if(pixel[0] < UtilConstants.TILE_HEIGHT * 2)
                            mMap_Y += UtilConstants.TILE_HEIGHT * 1.5;
                        updated = true;
                    }
                    // move to top-left cell
                    if(pressY - prevEditPressY < - UtilConstants.TILE_HEIGHT && Math.abs(pressX - prevEditPressX) < UtilConstants.TILE_WIDTH / 2){
                        if(infraInEditing.getMapCoordY() - 1 >= 0)
                            infraInEditing.setMapCoordinates(infraInEditing.getMapCoordX(), infraInEditing.getMapCoordY() - 1);
                        if(infraInEditing.getMapCoordX() - 1 >= 0)
                            infraInEditing.setMapCoordinates(infraInEditing.getMapCoordX() - 1, infraInEditing.getMapCoordY());

                        // need spave at top
                        int[] pixel = ComputeMapCoordinateToPixel(infraInEditing.getMapCoordY(), infraInEditing.getMapCoordX());
                        if(pixel[0] < UtilConstants.TILE_HEIGHT * 2)
                            mMap_Y += UtilConstants.TILE_HEIGHT * 1.5;

                        updated = true;
                    }
                    // move tp top cell
                    if(pressY - prevEditPressY < - UtilConstants.TILE_HEIGHT / 2 && pressX - prevEditPressX > UtilConstants.TILE_WIDTH / 2){
                        if(infraInEditing.getMapCoordY() - 1 >= 0)
                            infraInEditing.setMapCoordinates(infraInEditing.getMapCoordX(),infraInEditing.getMapCoordY() - 1);

                        // need space at right-top
                        int[] pixel = ComputeMapCoordinateToPixel(infraInEditing.getMapCoordY(), infraInEditing.getMapCoordX());
                        if(pixel[1] > screen_width - UtilConstants.TILE_WIDTH)
                            mMap_X -= UtilConstants.TILE_WIDTH;
                        if(pixel[0] < UtilConstants.TILE_HEIGHT * 2)
                            mMap_Y += UtilConstants.TILE_HEIGHT * 1.5;

                        updated = true;
                    }
                    // move to top-right
                    if(Math.abs(pressY - prevEditPressY) < UtilConstants.TILE_HEIGHT / 2 && pressX - prevEditPressX > UtilConstants.TILE_WIDTH){
                        if(infraInEditing.getMapCoordY() - 1 >= 0)
                            infraInEditing.setMapCoordinates(infraInEditing.getMapCoordX(),infraInEditing.getMapCoordY() - 1);
                        if(infraInEditing.getMapCoordX() + 1 < mapCol)
                            infraInEditing.setMapCoordinates(infraInEditing.getMapCoordX() + 1, infraInEditing.getMapCoordY());

                        // need space at right
                        // need space at right-top
                        int[] pixel = ComputeMapCoordinateToPixel(infraInEditing.getMapCoordY(), infraInEditing.getMapCoordX());
                        if(pixel[1] > screen_width - UtilConstants.TILE_WIDTH)
                            mMap_X -= UtilConstants.TILE_WIDTH;

                        updated = true;
                    }

                    if(updated){
                        // update also previous pressed position
                        prevEditPressY = pressY;
                        prevEditPressX = pressX;
                    }
                }
                else if(Math.abs(prevMovePressX - pressX) > moveThreshold ||
                        Math.abs(prevMovePressY - pressY) > moveThreshold){
                    // Normal Movement Mode
                    pressTimeRec = System.currentTimeMillis();

                    mMap_X += (pressX - prevMovePressX);
                    mMap_Y += (pressY - prevMovePressY);

                    // limit origin of map
                    if(mMap_X > screen_width - UtilConstants.MARGIN)
                        mMap_X = screen_width - UtilConstants.MARGIN;
                    if(mMap_X + mMapWidth < UtilConstants.MARGIN)
                        mMap_X = UtilConstants.MARGIN - mMapWidth;
                    if(mMap_Y > screen_height - UtilConstants.MARGIN)
                        mMap_Y = screen_height - UtilConstants.MARGIN;
                    if(mMap_Y + mMapHeight < UtilConstants.MARGIN)
                        mMap_Y = UtilConstants.MARGIN - mMapHeight;

                }else{
                    // Long Press Mode

                    // if position pressed is a infrastructure, then we enter in edit mode
                    if(System.currentTimeMillis() - pressTimeRec > pressTimeThreshold){

                        pressTimeRec = System.currentTimeMillis();

                        int[] pressCoord = ComputePixelToMapCoordinate(pressX, pressY);
                        if(pressCoord != null && infraInEditing == null){
                            OpenOptionWindow(pressX - 200, pressY + 100);
                        }

                        if(!vibrating){
                            vibrator.vibrate(200);
                            vibrating = true;
                        }
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                vibrating = false;

                prevMovePressX = (int)event.getRawX();
                prevMovePressY = (int)event.getRawY();
                break;
        }
        return true;
    }


    private void OpenOptionWindow(int pos_x, int pos_y){
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.action_long_press_building, null, false);
        final PopupWindow optionWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        optionWindow.setAnimationStyle(R.anim.pop_menu_anim);
        optionWindow.setTouchable(true);
        optionWindow.setTouchInterceptor(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        optionWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        optionWindow.showAtLocation(view, Gravity.NO_GRAVITY, pos_x,pos_y);



        infoButton = (Button)view.findViewById(R.id.infoButton);
        upgradeButton = (Button)view.findViewById(R.id.upgradeButton);

        editButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] pressCoord = ComputePixelToMapCoordinate(pressX, pressY);
                infraInEditing = map[pressCoord[0]][pressCoord[1]];
                infraInEditing.setInEdit(true);
                infraInEditing.setExitEditMode(true);
                editOrigin_x = infraInEditing.getMapCoordX();
                editOrigin_y = infraInEditing.getMapCoordY();
                editMode = true;
                optionWindow.dismiss();
            }
        });

        infoButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] pressCoord = ComputePixelToMapCoordinate(pressX, pressY);
                Infrastructure infraInfo = map[pressCoord[0]][pressCoord[1]];
                optionWindow.dismiss();
                infraInfo.PopMyInfo();
            }
        });

        upgradeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] pressCoord = ComputePixelToMapCoordinate(pressX, pressY);
                infraInUpgrade = map[pressCoord[0]][pressCoord[1]];

                Message msg = new Message();
                msg.what = MessageCode.UPGRADE_REQUEST_MESSAGE;
                Bundle bundle = new Bundle();
                bundle.putInt(MessageCode.TYPE, infraInUpgrade.getType());
                bundle.putInt(MessageCode.NEED_MONEY, infraInUpgrade.getUpgradeCost());
                msg.setData(bundle);
                CityActivity.mHandler.sendMessage(msg);
                optionWindow.dismiss();
            }
        });
    }

    /**
     * this method is call when user upgrade the entire city, it causes enlarge of map
     */
    private void Expand(){
        mapRow += 2;
        mapCol += 2;
        // create a new map
        Infrastructure[][] tempMap = new Infrastructure[mapRow][mapCol];

        // copy the old one into new one
        for(int i = 0; i < mapRow; i++){
            if(i < mapRow - 2) {
                for (int j = 0; j < mapCol; j++) {
                    if (j < mapCol - 2)
                        tempMap[i][j] = map[i][j];
                    else
                        tempMap[i][j] = null;
                }
            }else
                for(int j = 0; j < mapCol; j++)
                    tempMap[i][j] = null;
        }

        mMapWidth = mapRow * UtilConstants.TILE_WIDTH;
        mMapHeight = mapCol * UtilConstants.TILE_HEIGHT;

        map = tempMap;
    }


    public void draw(Canvas canvas) {
        super.draw(canvas);

        MyBackgroundDraw(canvas);
        MyBorderDraw(canvas);
        MyCityDraw(canvas);
    }


}
