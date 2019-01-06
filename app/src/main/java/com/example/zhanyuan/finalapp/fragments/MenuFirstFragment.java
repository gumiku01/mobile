package com.example.zhanyuan.finalapp.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zhanyuan.finalapp.R;
import com.example.zhanyuan.finalapp.activities.BookNoteActivity;
import com.example.zhanyuan.finalapp.adapters.DetailAdapter;
import com.example.zhanyuan.finalapp.beans.DetailBean;
import com.example.zhanyuan.finalapp.stickyheader.StickyHeaderGridLayoutManager;
import com.example.zhanyuan.finalapp.utils.MyDatabaseHelper;

import java.util.Calendar;
import java.util.List;

public class MenuFirstFragment extends BaseFragment implements View.OnClickListener{

    private TextView dateYear;
    private TextView dateMonth;
    private LinearLayout layoutData;
    private TextView cashPayout;
    private TextView cashIncome;
    private RecyclerView rvList;
    private SwipeRefreshLayout swipe;
    private FloatingActionButton floatBtn;

    public int mYear, mMonth, mDay;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private Calendar cal;

    private static final int SPAN_SIZE = 1;
    private StickyHeaderGridLayoutManager mLayoutManager;
    private DetailAdapter adapter;
    private List<DetailBean.DayListBean> list;
    private MyDatabaseHelper myDatabaseHelper;



    @Override
    protected void bindView() {
        //bind views
        layoutData = mView.findViewById(R.id.layout_data);
        dateYear = mView.findViewById(R.id.data_year);
        dateMonth = mView.findViewById(R.id.data_month);
        cashPayout = mView.findViewById(R.id.cash_payout);
        cashIncome = mView.findViewById(R.id.cash_income);
        rvList = mView.findViewById(R.id.rv_list);
        swipe = mView.findViewById(R.id.swipe);
        floatBtn = mView.findViewById(R.id.float_btn);

        // define the listeners

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mYear = year;
                mMonth = month + 1;
                mDay = dayOfMonth;

                dateYear.setText(String.valueOf(mYear));
                dateMonth.setText((String.valueOf(mMonth) ));
                UpdateData();
            }
        };

        // show the data picker
        layoutData.setOnClickListener(this);

        // start fragment to record a new data
        floatBtn.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        UpdateData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_menu_first;
    }

    @Override
    protected void initEventAndData() {
        myDatabaseHelper = new MyDatabaseHelper(mContext, "my.db", null, 1);

        cal = Calendar.getInstance();

        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH) + 1;
        mDay = cal.get(Calendar.DAY_OF_MONTH);

        dateYear.setText(String.valueOf(cal.get(Calendar.YEAR)));
        dateMonth.setText(String.valueOf(cal.get(Calendar.MONTH) + 1));
        // change color of loading
        swipe.setColorSchemeColors(getResources().getColor(R.color.text_red), getResources().getColor(R.color.text_red));
        // set distance for activate
        swipe.setDistanceToTriggerSync(200);
        // set position of refresh view
        swipe.setProgressViewEndTarget(false, 200);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                UpdateData();
            }
        });

        mLayoutManager = new StickyHeaderGridLayoutManager(SPAN_SIZE);
        mLayoutManager.setHeaderBottomOverlapMargin(5);

        // Workaround RecyclerView limitation when playing remove animations. RecyclerView always
        // puts the removed item on the top of other views and it will be drawn above sticky header.
        // The only way to fix this, abandon remove animations :(
        rvList.setItemAnimator(new DefaultItemAnimator() {
            @Override
            public boolean animateRemove(RecyclerView.ViewHolder holder) {
                dispatchRemoveFinished(holder);
                return false;
            }
        });
        rvList.setLayoutManager(mLayoutManager);
        adapter = new DetailAdapter(mContext, list);
        rvList.setAdapter(adapter);
        UpdateData();
    }

    private void UpdateData() {
        DetailBean monthList = myDatabaseHelper.DB2DayListBeanUsingData(mYear, mMonth);
        list = monthList.getDaylist();

        adapter.setmDatas(list);
        cashIncome.setText(String.valueOf(monthList.getT_income()));
        cashPayout.setText(String.valueOf(monthList.getT_outcome()));
        adapter.notifyAllSectionsDataSetChanged();
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.layout_data:
                new DatePickerDialog(getContext(), onDateSetListener, mYear, mMonth - 1, mDay).show();
                break;
            case R.id.float_btn:
                Intent intent = new Intent(getContext(), BookNoteActivity.class);
                startActivity(intent);
                break;
        }
    }
}
