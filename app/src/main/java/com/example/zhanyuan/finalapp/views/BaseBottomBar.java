package com.example.zhanyuan.finalapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

public class BaseBottomBar extends LinearLayout implements View.OnClickListener{

    private View currentView;
    private View[] views;

    private OnBottomBarListener onBottomBarListener;


    // constructors
    public BaseBottomBar(Context context) {
        super(context);
    }

    public BaseBottomBar(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public BaseBottomBar(Context context, AttributeSet attrs, int defStyleAttrs){
        super(context, attrs, defStyleAttrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        views = new View[getChildCount()];
        for(int i = 0; i < views.length; i++){
            views[i] = getChildAt(i);
            views[i].setOnClickListener(this);
            views[i].setTag(i);
        }
    }

    /**
     *  show a tab
     * @param index
     */
    public void showTab(int index){
        if(index < 0 )
            return;
        if(views != null && views.length > index){
            for(int i = 0; i < views.length; i++){
                if(i != index) {
                    onBottomBarListener.hideFragment(i, index);
                } else {
                    setCurrentView(views[index]);
                }
            }
        }
    }

    public void setCurrentView(View view){
        if(view == null)
            return;
        if(onBottomBarListener == null)
            return;

        // hide previous fragment
        if(currentView != null){
            currentView.setSelected(false);
            onBottomBarListener.hideFragment((Integer)currentView.getTag(), (Integer)view.getTag());
        }

        currentView = view;
        currentView.setSelected(true);
        onBottomBarListener.showFragment((Integer)view.getTag());
    }


    @Override
    public void onClick(View v) {
        if(onBottomBarListener != null){
            onBottomBarListener.onTabClick(v);
        }
    }

    public void setOnBottomBarListener(OnBottomBarListener listener){
        this.onBottomBarListener = listener;
    }


    public int getCount(){
        return views.length;
    }


    // define onBottomBarListener
    public interface OnBottomBarListener{

        void showFragment(int index);

        void hideFragment(int lastIndex, int curIndex);

        void onTabClick(View view);
    }
}



