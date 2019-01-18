package com.example.zhanyuan.finalapp.activities;


import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhanyuan.finalapp.utils.ShotShareUtil;
import com.example.zhanyuan.finalapp.utils.UtilConstants;
import com.example.zhanyuan.finalapp.R;
import com.example.zhanyuan.finalapp.adapters.MoneyListAdapter;
import com.example.zhanyuan.finalapp.adapters.PopItemAdapter;
import com.example.zhanyuan.finalapp.utils.MessageCode;
import com.example.zhanyuan.finalapp.utils.ResourcesUtil;
import com.example.zhanyuan.finalapp.utils.SharedPrefenrenceHelper;
import com.example.zhanyuan.finalapp.views.CitySurfaceView;

public class CityActivity extends AppCompatActivity {

    private CitySurfaceView citySurfaceView;
    private Button editButton, shareButton;
    private Button streetButton, apartmentButton, specialButton, publicButton;
    private PopupWindow editPopWindow;

    private TextView level_text;
    private TextView money_text;

    private SharedPrefenrenceHelper sph;

    private HandlerThread handlerThread = new HandlerThread("myHandlerThread");
    public static Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_main);

        bindView();

        citySurfaceView.Create();
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPopWindow();
            }
        });
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShotShare();
            }
        });
        level_text.setText(sph.read("level"));
        money_text.setText("total: " + sph.read("total"));

        money_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initMoneyWindow();
            }
        });

        initHandlerThread();
    }

    private void bindView(){
        citySurfaceView = (CitySurfaceView)findViewById(R.id.cityView);
        editButton = (Button)findViewById(R.id.editButton);
        shareButton = (Button)findViewById(R.id.shareButton);
        level_text = (TextView)findViewById(R.id.levelText);
        money_text = (TextView)findViewById(R.id.moneyText);

        sph = new SharedPrefenrenceHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        citySurfaceView.Start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        citySurfaceView.Resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        citySurfaceView.Pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        citySurfaceView.Stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        citySurfaceView.Destroy();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        citySurfaceView.Restart();
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
                if(msg.what == MessageCode.UPGRADE_REQUEST_MESSAGE){

                    int type = msg.getData().getInt(MessageCode.TYPE);
                    // check if we have enough money to build
                    boolean enough = true;
                    String[] typeAndMoney = UtilConstants.typeNameAndMoney.get(type);
                    String type_name = typeAndMoney[0];
                    float money = msg.getData().getInt(MessageCode.NEED_MONEY);

                    float typeMoneyHold = Float.parseFloat(sph.read(type_name));

                    float otherMoneyHold = 0;
                    if(!type_name.equals(ResourcesUtil.others))
                        otherMoneyHold = Float.parseFloat(sph.read(ResourcesUtil.others));


                    if(money <= typeMoneyHold)
                        typeMoneyHold = typeMoneyHold - money;
                    else if(money > typeMoneyHold && money <= typeMoneyHold + otherMoneyHold){
                        otherMoneyHold = otherMoneyHold - (money - typeMoneyHold);
                        typeMoneyHold = 0;
                    }else {
                        enough = false;
                        Toast.makeText(CityActivity.this, "You have not enough money to upgrade it" + String.valueOf(money) + "  " + String.valueOf(typeMoneyHold) + "  " + String.valueOf(otherMoneyHold), Toast.LENGTH_LONG).show();
                    }
                    sph.save(type_name, String.valueOf(typeMoneyHold));
                    if(!type_name.equals(ResourcesUtil.others))
                        sph.save(ResourcesUtil.others, String.valueOf(otherMoneyHold));

                    float total = Float.parseFloat(sph.read("total"));
                    if(enough) {
                        total -= money;
                        Message reply = new Message();
                        reply.what = MessageCode.UPGRADE_REQUEST_MESSAGE;
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(MessageCode.REPLY, enough);
                        reply.setData(bundle);
                        citySurfaceView.mHandler.sendMessage(reply);
                    }
                }
            }
        };
    }

    private void initMoneyWindow(){
        // inflate the layout and create popupWindow
        final View view = LayoutInflater.from(this).inflate(R.layout.type_money_list, null, false);
        editPopWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        // set animatation style
        editPopWindow.setAnimationStyle(R.anim.pop_menu_anim);
        // able to exit if we click outside of the window
        editPopWindow.setTouchable(true);
        editPopWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        // background and the position of window when it appears
        editPopWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        editPopWindow.showAtLocation(view,Gravity.CENTER, 0, 0);

        String[] moneys = new String[ResourcesUtil.type_for_sp.length];
        for(int i = 0; i < ResourcesUtil.type_for_sp.length; i++){
            moneys[i] = sph.read(ResourcesUtil.type_for_sp[i]);
        }
        final ListView moneyListView = (ListView)view.findViewById(R.id.money_list);

        MoneyListAdapter adapter = new MoneyListAdapter(this, ResourcesUtil.type_for_sp, moneys);
        moneyListView.setAdapter(adapter);
    }


    private void initPopWindow(){
        // inflate the layout and create popupWindow
        final View view = LayoutInflater.from(this).inflate(R.layout.pop_menu_view, null, false);
        editPopWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        // set animatation style
        editPopWindow.setAnimationStyle(R.anim.pop_menu_anim);
        // able to exit if we click outside of the window
        editPopWindow.setTouchable(true);
        editPopWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        // background and the position of window when it appears
        editPopWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        editPopWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        streetButton = (Button)view.findViewById(R.id.streetButton);
        apartmentButton = (Button)view.findViewById(R.id.apartmentButton);
        specialButton = (Button)view.findViewById(R.id.specialBuildingButton);
        publicButton = (Button)view.findViewById(R.id.otherBuildingButton);

        final GridView popGridView = (GridView)view.findViewById(R.id.popGridView);

        // define the action of each button
        streetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopItemAdapter adapter = new PopItemAdapter(UtilConstants.STREET, UtilConstants.STREET_IMAGES, CityActivity.this);
                popGridView.setAdapter(adapter);
            }
        });

        apartmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopItemAdapter adapter = new PopItemAdapter(UtilConstants.APARTMENT, UtilConstants.APARTMENT_IMAGES, CityActivity.this);
                popGridView.setAdapter(adapter);
            }
        });

        specialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopItemAdapter adapter = new PopItemAdapter(UtilConstants.SPECIAL, UtilConstants.SPECIAL_IMAGES, CityActivity.this);
                popGridView.setAdapter(adapter);
            }
        });

        publicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopItemAdapter adapter = new PopItemAdapter(UtilConstants.PUBLIC, UtilConstants.PUBLIC_IMAGES, CityActivity.this);
                popGridView.setAdapter(adapter);
            }
        });
        streetButton.callOnClick();
    }

    /**
     * Send a message to the surfaceView the building to be constructed
     * @param type: type code of building
     */
    public void sendBuildingMessage(int type){

        // check if we have enough money to build
        boolean enough = true;

        String[] typeAndMoney = UtilConstants.typeNameAndMoney.get(type);
        String type_name = typeAndMoney[0];
        float money = Float.parseFloat(typeAndMoney[1]);

        float typeMoneyHold = Float.parseFloat(sph.read(type_name));

        float otherMoneyHold = 0;
        if(!type_name.equals(ResourcesUtil.others))
            otherMoneyHold = Float.parseFloat(sph.read(ResourcesUtil.others));


        if(money <= typeMoneyHold)
            typeMoneyHold = typeMoneyHold - money;
        else if(money > typeMoneyHold && money <= typeMoneyHold + otherMoneyHold){
            otherMoneyHold = otherMoneyHold - (money - typeMoneyHold);
            typeMoneyHold = 0;
        }else {
            Toast.makeText(this, "You have not enough money to build it", Toast.LENGTH_LONG).show();
            enough = false;
        }

        sph.save(type_name, String.valueOf(typeMoneyHold));
        if(!type_name.equals(ResourcesUtil.others))
            sph.save(ResourcesUtil.others, String.valueOf(otherMoneyHold));
        float total = Float.parseFloat(sph.read("total"));

        if(enough) {
            total -= money;
            sph.save("total", String.valueOf(total));
            money_text.setText("total: " + sph.read("total"));

            Message msg = new Message();
            msg.what = MessageCode.BUILD_MESSAGE;
            Bundle bundle = new Bundle();
            bundle.putInt(MessageCode.TYPE, type);
            msg.setData(bundle);
            citySurfaceView.mHandler.sendMessage(msg);
        }
        editPopWindow.dismiss();

    }

    public void UpdateView(){
        level_text.setText(sph.read("level"));
        money_text.setText("total: " + sph.read("total"));
    }

    private void ShotShare(){
        Message msg = new Message();
        msg.what = MessageCode.SCREENSHOT;
        citySurfaceView.mHandler.sendMessage(msg);

        ShotShareUtil.shotShare(this, citySurfaceView);
    }

}
