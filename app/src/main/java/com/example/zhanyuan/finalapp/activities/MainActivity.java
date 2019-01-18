package com.example.zhanyuan.finalapp.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhanyuan.finalapp.R;
import com.example.zhanyuan.finalapp.fragments.MenuFirstFragment;
import com.example.zhanyuan.finalapp.fragments.MenuGraphFragment;
import com.example.zhanyuan.finalapp.utils.MessageCode;
import com.example.zhanyuan.finalapp.views.BaseBottomBar;

public class MainActivity extends BaseActivity implements BaseBottomBar.OnBottomBarListener {

    TextView titleTv;
    ImageView rightBtn;
    FrameLayout Container;
    BaseBottomBar mBottomBar;

    // start with tag index 0
    private int index = 0;
    private FragmentManager mFragmentManager;

    private boolean mCanClickBoolean = true;
    private int MSG_CLICK_DURATION = 200; //  time interval to respond click event

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(mHandler == null)
                return;
            int msgNum = msg.what;
            switch(msgNum){
                case MessageCode.MSG_CLICK:
                    mCanClickBoolean = true;
                    break;
            }
        }
    };

    // override function of BaseActivity
    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void bindView() {
        titleTv = findViewById(R.id.title_tv);
        rightBtn = findViewById(R.id.right_btn);
        Container = findViewById(R.id.Container);
        mBottomBar = findViewById(R.id.main_bottom_bar);


        CheckPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        CheckPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mBottomBar.showTab(this.index);
        showFragment(this.index);
    }

    @Override
    protected void initEventAndData() {
        mFragmentManager = getSupportFragmentManager();
        mBottomBar.setOnBottomBarListener(this);
        mBottomBar.showTab(0);
    }

    // override function of OnBottomBarListener
    @Override
    public void showFragment(int index) {
        if(index < 2) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(String.valueOf(index));

            if (fragment == null) {
                if (index == 0) {
                    fragment = new MenuFirstFragment();
                } else if (index == 1) {
                    fragment = new MenuGraphFragment();
                }
                if (!fragment.isAdded()) {
                    obtainFragmentTransaction(index).add(R.id.Container, fragment, String.valueOf(index)).commitAllowingStateLoss();
                }
            } else {
                obtainFragmentTransaction(index).show(fragment).commitAllowingStateLoss();
            }
        }else if(index == 2){
            Intent intent = new Intent(this, CityActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void hideFragment(int lastIndex, int curIndex) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(String.valueOf(index));
        if (fragment != null) {
            obtainFragmentTransaction(curIndex).hide(fragment).commitAllowingStateLoss();
        }
    }

    @Override
    public void onTabClick(View view) {
        if (!mCanClickBoolean) {
            return;
        }
        mCanClickBoolean = false;
        mHandler.removeMessages(MessageCode.MSG_CLICK);
        mHandler.sendEmptyMessageDelayed(MessageCode.MSG_CLICK,
                MSG_CLICK_DURATION);

        mBottomBar.setCurrentView(view);

    }

    /**
     * get a animation foe FragmentTransaction
     *
     * @param index
     * @return
     */
    private FragmentTransaction obtainFragmentTransaction(int index) {

        FragmentTransaction ft = mFragmentManager.beginTransaction();
        // set change animation
        if (index > this.index) {
            ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left);
        } else {
            ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right);
        }

        // non save if index is for city
        if(index != 2)
            this.index = index;
        return ft;
    }

    private void CheckPermission(String permission){

        if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this, new String[]{permission}, 5);
            }
        }
    }
}
