package com.llh.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.ipcamer.app.MyApplication;
import com.ipcamer.demo.R;
import com.llh.entity.FoodModel;
import com.llh.utils.OrderFoodInterface;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FoodAdapter extends BaseAdapter {

    private ArrayList<FoodModel> array = new ArrayList<FoodModel>();
    private LayoutInflater inflater;
    OrderFoodInterface orderFoodInterface;

    public FoodAdapter() {
        inflater = LayoutInflater.from(MyApplication.getMyApplication());
    }

    public void setData(ArrayList<FoodModel> data) {
        array.clear();
        array.addAll(data);
        this.notifyDataSetChanged();
    }

    public void registerCallBack(OrderFoodInterface foodInterface) {
        orderFoodInterface = foodInterface;
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return array.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.food_list_item, null);
            holder = new ViewHolder();
            convertView.setTag(holder);// 绑定ViewHolder对象
            holder.food_name = (TextView) convertView.findViewById(R.id.food_name);
            holder.food_price = (TextView) convertView.findViewById(R.id.food_price);
            holder.food_num = (TextView) convertView.findViewById(R.id.tv_num);
            holder.food_im = (ImageView) convertView.findViewById(R.id.food_im);
            holder.food_add = (ImageView) convertView.findViewById(R.id.food_add);
            holder.im_reduce = (ImageView) convertView.findViewById(R.id.im_reduce);

        } else {
            holder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象
        }
        final FoodModel foodModel = array.get(position);
        holder.food_name.setText(foodModel.proName);
        holder.food_price.setText("¥" + foodModel.proPrice + "元");
        holder.food_num.setText(foodModel.buyNum + "");
        holder.food_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodModel.buyNum++;
                notifyDataSetChanged();
                if(orderFoodInterface!=null){
                    orderFoodInterface.refreshShoppingList(foodModel);
                }
            }
        });
        holder.im_reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodModel.buyNum--;
                if (foodModel.buyNum <= 0) {
                    foodModel.buyNum = 0;
                }
                notifyDataSetChanged();
                if(orderFoodInterface!=null){
                    orderFoodInterface.refreshShoppingList(foodModel);
                }
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView food_name;
        TextView food_num;
        TextView food_price;
        ImageView food_im;
        ImageView food_add;
        ImageView im_reduce;

    }
}
