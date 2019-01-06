package com.example.zhanyuan.finalapp.fragments;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhanyuan.finalapp.R;
import com.example.zhanyuan.finalapp.adapters.TypeRankAdapter;
import com.example.zhanyuan.finalapp.beans.DetailBean;
import com.example.zhanyuan.finalapp.beans.TypeBean;
import com.example.zhanyuan.finalapp.utils.DatabaseSchema;
import com.example.zhanyuan.finalapp.utils.MyDatabaseHelper;
import com.example.zhanyuan.finalapp.utils.PieChartUtil;
import com.example.zhanyuan.finalapp.utils.ResourcesUtil;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MenuGraphFragment extends BaseFragment implements View.OnClickListener, OnChartValueSelectedListener {

    private PieChart mChart;
    private TextView centerTitle;
    private TextView centerMoney;
    private LinearLayout layoutCenter;
    private ImageView centerImg;
    private TextView dateYear;
    private TextView dateMonth;
    private LinearLayout layoutData;
    private TextView cashIncome;
    private TextView cashPayout;
    private RelativeLayout layoutCircle;
    private TextView title;
    private TextView money;
    private TextView rankTitle;
    private SwipeRefreshLayout swipe;
    private ConstraintLayout itemType;
    private RecyclerView rvList;
    private LinearLayout layoutTypedata;
    private ImageView circle_imag;

    public int mYear, mMonth, mDay;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private Calendar cal;

    private MyDatabaseHelper myDatabaseHelper;

    private String TYPE = DatabaseSchema.DataSchema.payout;
    private List<TypeBean.TypeMoneyBean> tMoneyBeanList;
    private int report_type = 0;//饼状图与之相对应的类型
    private String type_name;
    private String back_color;

    private TypeRankAdapter adapter;
    private List<DetailBean.DayListBean.ListBean> list;
    private List<String> days;


    @Override
    protected void bindView() {
        mChart = mView.findViewById(R.id.chart);
        centerTitle = mView.findViewById(R.id.center_title);
        centerMoney = mView.findViewById(R.id.center_money);
        layoutCenter = mView.findViewById(R.id.layout_center);
        centerImg = mView.findViewById(R.id.center_img);
        dateYear = mView.findViewById(R.id.data_year);
        dateMonth = mView.findViewById(R.id.data_month);
        dateYear = mView.findViewById(R.id.data_year);
        dateMonth = mView.findViewById(R.id.data_month);
        layoutData = mView.findViewById(R.id.layout_data);
        cashIncome = mView.findViewById(R.id.cash_income);
        cashPayout = mView.findViewById(R.id.cash_payout);
        title = mView.findViewById(R.id.title);
        money = mView.findViewById(R.id.money);
        rankTitle = mView.findViewById(R.id.rank_title);
        swipe = mView.findViewById(R.id.swipe);
        itemType = mView.findViewById(R.id.item_type);
        rvList = mView.findViewById(R.id.rv_list);
        layoutTypedata = mView.findViewById(R.id.layout_typedata);
        circle_imag = mView.findViewById(R.id.circle_img);

        // show the data picker
        layoutData.setOnClickListener(this);
        layoutCenter.setOnClickListener(this);


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
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_menu_second;
    }

    @Override
    protected void initEventAndData() {

        myDatabaseHelper = new MyDatabaseHelper(mContext, "my.db", null, 1);

        cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH) + 1;
        mDay = cal.get(Calendar.DAY_OF_MONTH);

        PieChartUtil.initPieChart(mChart);
        mChart.setOnChartValueSelectedListener(this);

        dateYear.setText(String.valueOf(cal.get(Calendar.YEAR)));
        dateMonth.setText(String.valueOf(cal.get(Calendar.MONTH) + 1));
        //改变加载显示的颜色
        swipe.setColorSchemeColors(getResources().getColor(R.color.text_red), getResources().getColor(R.color.text_red));
        //设置向下拉多少出现刷新
        swipe.setDistanceToTriggerSync(200);
        //设置刷新出现的位置
        swipe.setProgressViewEndTarget(false, 200);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                PieChartUtil.setAnimate(mChart);
            }
        });

        rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        list = new ArrayList<>();
        days = new ArrayList<>();
        adapter = new TypeRankAdapter(getActivity(), list, days);
        rvList.setAdapter(adapter);

        UpdateData();
    }

    private void UpdateData(){
        DetailBean monthList = myDatabaseHelper.DB2DayListBeanUsingData(mYear, mMonth);
        cashIncome.setText(String.valueOf(monthList.getT_income()));
        cashPayout.setText(String.valueOf(monthList.getT_outcome()));

        TypeBean typeModel = null;
        if (TYPE.equals(DatabaseSchema.DataSchema.payout)) {
            centerTitle.setText("payout");
            centerImg.setImageResource(R.mipmap.book_output);
            typeModel = myDatabaseHelper.DB2TypeListBean(mYear, mMonth, TYPE);
        } else {
            centerTitle.setText("income");
            centerImg.setImageResource(R.mipmap.book_input);
            typeModel = myDatabaseHelper.DB2TypeListBean(mYear, mMonth, TYPE);
        }
        if (typeModel == null) {
            return;
        }

        centerMoney.setText(String.valueOf(typeModel.getTotal()));
        tMoneyBeanList = typeModel.getType_money();

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        ArrayList<Integer> colors = new ArrayList<Integer>();

        if (tMoneyBeanList != null && tMoneyBeanList.size() > 0) {
            layoutTypedata.setVisibility(View.VISIBLE);
            for (int i = 0; i < tMoneyBeanList.size(); i++) {
                float scale = tMoneyBeanList.get(i).getAffect_money() / typeModel.getTotal();
                //float value = (scale < 0.06f) ? 0.06f : scale;
                if(tMoneyBeanList.get(i).getAffect_money() != 0) {
                    entries.add(new PieEntry(scale, ResourcesUtil.getTypeDrawable(tMoneyBeanList.get(i).getTypeName(), getResources())));
                    colors.add(Color.parseColor(tMoneyBeanList.get(i).getBack_color()));
                }
            }
            setNoteData(0);
        } else {
            //if no data to show
            layoutTypedata.setVisibility(View.GONE);
            entries.add(new PieEntry(1f));
            colors.add(0xffAAAAAA);
        }

        PieChartUtil.setPieChartData(mChart, entries, colors);


    }
    private void setNoteData(int index){
        type_name = tMoneyBeanList.get(index).getTypeName();
        back_color = tMoneyBeanList.get(index).getBack_color();
        if (TYPE.equals(DatabaseSchema.DataSchema.payout)) {
            money.setText(("-" + String.valueOf(tMoneyBeanList.get(index).getAffect_money())));
        }else {
            money.setText(("+" +  String.valueOf(tMoneyBeanList.get(index).getAffect_money())));
        }
        title.setText(type_name);
        circle_imag.setImageDrawable(ResourcesUtil.getTypeDrawable(tMoneyBeanList.get(index).getTypeName(), getResources()));
        circle_imag.setBackgroundColor(Color.parseColor(ResourcesUtil.typeColor.get(tMoneyBeanList.get(index).getTypeName())));
        rankTitle.setText("classification of " + type_name);

        list = tMoneyBeanList.get(index).getRank();

        adapter.setmDatas(list);
        adapter.setmDataDay(tMoneyBeanList.get(index).getDates());
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;
        int entryIndex = (int) h.getX();
        PieChartUtil.setRotationAngle(mChart, entryIndex);
        setNoteData(entryIndex);
    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_data:
                new DatePickerDialog(getContext(), onDateSetListener, mYear, mMonth - 1, mDay).show();
                break;
            case R.id.layout_center:
                if(TYPE.equals(DatabaseSchema.DataSchema.payout))
                    TYPE = DatabaseSchema.DataSchema.income;
                else
                    TYPE = DatabaseSchema.DataSchema.payout;

                UpdateData();
        }
    }
}
