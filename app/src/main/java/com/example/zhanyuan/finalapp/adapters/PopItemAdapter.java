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

public class PopItemAdapter extends BaseAdapter {

    private Context mContext;
    private List<Item> items = new ArrayList<Item>();

    // Constructor
    public PopItemAdapter(int[][] infrastructures, Integer[] images, Context context){
        super();
        mContext = context;

        for(int i = 0; i < images.length; i++){
            Item item = new Item(infrastructures[i][0], String.valueOf(infrastructures[i][3]) + " $", images[i]);
            items.add(item);
        }
    }


    @Override
    public int getCount() {
        if((items != null))
            return items.size();
        else
            return 0;
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
        ViewHolder holder;

        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(this.mContext).inflate(R.layout.pop_item, null);

            holder.itemButton = (Button)convertView.findViewById(R.id.imageButton);
            holder.itemCost = (TextView)convertView.findViewById(R.id.imageCost);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        final Item item = items.get(position);
        holder.itemButton.setBackgroundResource(item.getImageId());
        holder.itemCost.setText(item.getItemCost());
        holder.itemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CityActivity)mContext).sendBuildingMessage(item.getItemCode());
            }
        });

        return convertView;
    }


    class ViewHolder{
        public TextView itemCost;
        public Button itemButton;
        public ImageView coin;
    }

    class Item{
        private int itemCode;
        private String itemCost;
        private int imageId;

        public Item(int itemCode, String itemCost, Integer imageId){
            this.itemCode = itemCode;
            this.itemCost = itemCost;
            this.imageId = imageId;
        }

        public int getItemCode() {
            return itemCode;
        }

        public int getImageId() {
            return imageId;
        }

        public String getItemCost() {
            return itemCost;
        }
    }
}
