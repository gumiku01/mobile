package com.example.zhanyuan.finalapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhanyuan.finalapp.R;
import com.example.zhanyuan.finalapp.activities.CityActivity;

import java.util.ArrayList;
import java.util.List;

public class MoneyListAdapter extends BaseAdapter {

    private Context mContext;
    private List<MoneyListAdapter.Item> items = new ArrayList<MoneyListAdapter.Item>();

    // Constructor
    public MoneyListAdapter(Context context, String[] type_names, String[] moneys){
        super();
        mContext = context;
        for(int i = 0; i < type_names.length; i++){
            Item item = new Item(type_names[i], moneys[i]);
            items.add(item);
        }
    }

    @Override
    public int getCount() {
        return items != null ? items.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        if(items != null && position < getCount()){
            return items.get(position);
        }else
            return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MoneyListAdapter.ViewHolder holder;

        if(convertView == null){
            holder = new MoneyListAdapter.ViewHolder();
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.type_money_item, null);

            holder.type = (TextView) convertView.findViewById(R.id.type_name);
            holder.money = (TextView)convertView.findViewById(R.id.money_value);

            convertView.setTag(holder);
        }else{
            holder = (MoneyListAdapter.ViewHolder) convertView.getTag();
        }

        final MoneyListAdapter.Item item = items.get(position);
        holder.type.setText(item.getType_name());
        holder.money.setText(item.getMoney());

        return convertView;
    }

    class ViewHolder{
        public TextView type;
        public TextView money;
    }

    class Item{
        private String type_name;
        private String money;

        public Item(String type_name, String money){
            this.type_name = type_name;
            this.money = money;
        }

        public String getMoney() {
            return money;
        }

        public String getType_name() {
            return type_name;
        }
    }
}
