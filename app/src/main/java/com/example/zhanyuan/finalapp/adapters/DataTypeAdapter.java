package com.example.zhanyuan.finalapp.adapters;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhanyuan.finalapp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class DataTypeAdapter extends BaseAdapter {


    private Context context;
    private ArrayList<String> names;
    private static HashMap<Integer, Boolean> isSelected;

    private String nameSelected;

    public DataTypeAdapter(Context context, ArrayList<String> names, HashMap<Integer, Boolean> isSelected){
        this.context = context;
        this.names = names;
        this.isSelected = isSelected;
        this.nameSelected = names.get(0);

        initData();
    }

    private void initData(){
        for(int i = 0; i < names.size(); i++){
            getIsSelected().put(i, false);
        }
    }




    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int position) {
        return names.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        final LayoutInflater inflater = LayoutInflater.from(context);
        final String name = names.get(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.detail_item, null, false);
            holder = new ViewHolder();
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox1);
            holder.detaiLName = (TextView) convertView.findViewById(R.id.detailName);
            holder.layout = (ConstraintLayout)convertView.findViewById(R.id.detail_list_layout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.detaiLName.setText(name);
        // 监听checkBox并根据原来的状态来设置新的状态
        holder.layout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                // remove che check
                if (isSelected.get(position)) {
                    isSelected.put(position, false);
                    setIsSelected(isSelected);
                } else {
                    isSelected.put(position, true);
                    setIsSelected(isSelected);
                    for(int i = 0; i < names.size(); i++){
                        if(i != position){
                            getIsSelected().put(i, false);
                        }
                    }
                    nameSelected = names.get(position);

                }
                notifyDataSetChanged();
            }
        });

        holder.checkBox.setChecked(getIsSelected().get(position));
        return convertView;
    }

    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        DataTypeAdapter.isSelected = isSelected;
    }

    public String getNameSelected(){
        return nameSelected;
    }

    class ViewHolder{
        TextView detaiLName;
        CheckBox checkBox;
        ConstraintLayout layout;
    }
}
