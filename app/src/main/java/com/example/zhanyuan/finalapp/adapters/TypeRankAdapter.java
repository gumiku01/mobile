package com.example.zhanyuan.finalapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhanyuan.finalapp.R;
import com.example.zhanyuan.finalapp.beans.DetailBean;

import java.util.List;

public class TypeRankAdapter extends RecyclerView.Adapter<TypeRankAdapter.ViewHolder>  {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<DetailBean.DayListBean.ListBean> mDatas;
    private List<String> mDataDay;

    public TypeRankAdapter(Context context, List<DetailBean.DayListBean.ListBean> datas, List<String> mDataDay){
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mDatas = datas;
        this.mDataDay = mDataDay;
    }

    public void setmDatas(List<DetailBean.DayListBean.ListBean> mDatas) {
        this.mDatas = mDatas;
    }
    public void setmDataDay(List<String> mDataDay){this.mDataDay = mDataDay;}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_type_rank, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.rank.setText(((position +1) +" "));
        viewHolder.description.setText(mDatas.get(position).getDescription());
        viewHolder.money.setText(String.valueOf(mDatas.get(position).getAffect_money()));
        //Toast.makeText(mContext, String.valueOf(mDataDay.size()), Toast.LENGTH_LONG).show();
        viewHolder.date.setText(mDataDay.get(position));
    }

    @Override
    public int getItemCount() {
        return (mDatas== null) ? 0 : mDatas.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        private TextView description;
        private TextView date;
        private TextView money;
        private TextView rank;

        public ViewHolder(View view){
            super(view);

            description = (TextView) view.findViewById(R.id.title);
            date = (TextView)view.findViewById(R.id.date);
            money = (TextView) view.findViewById(R.id.money);
            rank = (TextView) view.findViewById(R.id.rank);

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
            }
        }
    }
}
