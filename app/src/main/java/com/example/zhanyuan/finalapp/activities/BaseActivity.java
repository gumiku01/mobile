package com.example.zhanyuan.finalapp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.example.zhanyuan.finalapp.R;
import com.example.zhanyuan.finalapp.utils.ActivityManagerUtil;

public abstract class BaseActivity extends FragmentActivity {

    protected Activity mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ActivityManagerUtil.mActivities.add(this);
        mContext = this;
        bindView();
        initEventAndData();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManagerUtil.mActivities.remove(this);
    }

    protected abstract int getLayout();
    protected abstract void bindView();
    protected abstract void initEventAndData();
}
