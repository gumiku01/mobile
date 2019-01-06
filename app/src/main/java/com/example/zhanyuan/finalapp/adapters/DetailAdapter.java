package com.example.zhanyuan.finalapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhanyuan.finalapp.R;
import com.example.zhanyuan.finalapp.beans.DetailBean;
import com.example.zhanyuan.finalapp.stickyheader.StickyHeaderGridAdapter;
import com.example.zhanyuan.finalapp.utils.DatabaseSchema;
import com.example.zhanyuan.finalapp.utils.ResourcesUtil;
import com.example.zhanyuan.finalapp.views.SwipeMenuView;

import java.util.List;

public class DetailAdapter extends StickyHeaderGridAdapter {

    private Context mContext;
    private List<DetailBean.DayListBean> mDatas;

    public void setmDatas(List<DetailBean.DayListBean> mDatas) {
        this.mDatas = mDatas;
    }

    public DetailAdapter(Context context, List<DetailBean.DayListBean> datas){
        this.mContext = context;
        this. mDatas = datas;
    }

    @Override
    public int getSectionCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public int getSectionItemCount(int section) {
        return (mDatas == null || mDatas.get(section).getList() == null) ? 0 : mDatas.get(section).getList().size();
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int headerType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_header, parent, false);
        return new MyHeaderViewHolder(view);
    }

    @Override
    public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int itemType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_item, parent, false);
        return new MyItemViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(HeaderViewHolder viewHolder, int section) {
        final MyHeaderViewHolder holder = (MyHeaderViewHolder)viewHolder;
        holder.header_date.setText(mDatas.get(section).getDay());
        holder.header_money.setText(mDatas.get(section).getMoney());
    }

    @Override
    public void onBindItemViewHolder(ItemViewHolder viewHolder, int section, int position) {
        final MyItemViewHolder holder = (MyItemViewHolder)viewHolder;
        final String description = mDatas.get(section).getList().get(position).getDescription();
        final String detail = mDatas.get(section).getList().get(position).getDetailtype();
        final String type = mDatas.get(section).getList().get(position).getType();
        // write description
        holder.item_description.setText(description);
        holder.item_detail.setImageDrawable(ResourcesUtil.getTypeDrawable(detail, mContext.getResources()));
        holder.item_detail.setBackgroundColor(Color.RED);
        if(type.equals(DatabaseSchema.DataSchema.income)) {
            holder.item_money.setText(("+ " + String.valueOf(mDatas.get(section).getList().get(position).getAffect_money())));
            holder.item_money.setTextColor(Color.GREEN);
        }
        if(type.equals(DatabaseSchema.DataSchema.payout)){
            holder.item_money.setText(("- " + String.valueOf(mDatas.get(section).getList().get(position).getAffect_money())));
            holder.item_money.setTextColor(Color.RED);
        }

        holder.mSwipeMenuView.setSwipeEnable(true);
        /*
        holder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //.makeText(holder.item_layout.getContext(), "点击--"+label, Toast.LENGTH_SHORT).show();
            }
        });
        */
    }


    public static class MyHeaderViewHolder extends HeaderViewHolder {
        TextView header_date;
        TextView header_money;

        MyHeaderViewHolder(View itemView) {
            super(itemView);
            header_date = (TextView) itemView.findViewById(R.id.header_date);
            header_money = (TextView) itemView.findViewById(R.id.header_money);
        }
    }

    public static class MyItemViewHolder extends ItemViewHolder {
        TextView item_description;
        TextView item_money;
        ImageView item_detail;
        ConstraintLayout item_layout;
        SwipeMenuView mSwipeMenuView;

        MyItemViewHolder(View itemView) {
            super(itemView);
            item_detail = (ImageView) itemView.findViewById(R.id.item_detail);
            item_description = (TextView) itemView.findViewById(R.id.item_description);
            item_money = (TextView) itemView.findViewById(R.id.item_money);
            item_layout = (ConstraintLayout) itemView.findViewById(R.id.item_layout);
            mSwipeMenuView = (SwipeMenuView) itemView.findViewById(R.id.swipe_menu);
        }
    }
}
