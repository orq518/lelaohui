package com.llh.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import com.ipcamer.app.MyApplication;
import com.ipcamer.demo.R;
import com.llh.base.BaseActivity;
import com.llh.camera.activity.Logout;
import com.llh.camera.activity.OrderFooderActivity;
import com.llh.camera.activity.OrderQueryAtivity;
import com.llh.entity.FoodModel;
import com.llh.net.NetManager;
import com.llh.utils.ImageManager;
import com.llh.utils.OrderFoodInterface;
import com.llh.utils.utils;
import com.llh.view.MyPopupWindow;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FoodAdapter extends BaseAdapter {

    private ArrayList<FoodModel> array = new ArrayList<FoodModel>();
    private LayoutInflater inflater;
    OrderFoodInterface orderFoodInterface;
    Context context;
    private final static String TODAY_FOOD = "0";
    private final static String TOMORROW_FOOD = "1";
    private final static String POSTNATAL_FOOD = "2";
    private String isScope = TODAY_FOOD;

    public FoodAdapter(Context context) {
        inflater = LayoutInflater.from(MyApplication.getMyApplication());
        this.context = context;
    }

    public void setData(ArrayList<FoodModel> data, String isScope) {
        this.isScope = isScope;
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

                if (isScope.equals(TODAY_FOOD)) {
                    Calendar calendar = Calendar.getInstance();
                    int hour = calendar.get(Calendar.HOUR_OF_DAY );//小时
                    int minute = calendar.get(Calendar.MINUTE);//分
                    Logout.d("hour:" + hour);
                    Logout.d("minute:" + minute);
                    if (foodModel.mealTime.equals("1") &&(hour > 6 || (hour == 6 && minute > 30))) {
                        Toast.makeText(context, "已过早餐订餐时间", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (foodModel.mealTime.equals("2") && (hour > 10 || (hour == 10 && minute > 30))) {
                        Toast.makeText(context, "已过午餐订餐时间", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (foodModel.mealTime.equals("3") && (hour > 16 || (hour == 16 && minute > 30))) {
                        Toast.makeText(context, "已过晚餐订餐时间", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                foodModel.buyNum++;
                notifyDataSetChanged();
                if (orderFoodInterface != null) {
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
                if (orderFoodInterface != null) {
                    orderFoodInterface.refreshShoppingList(foodModel);
                }
            }
        });
        if (!utils.isEmpty(foodModel.proPic)) {
            ImageManager.getInstance(context).getBitmap(NetManager.Ip + foodModel.proPic, new ImageManager.ImageCallBack() {
                @Override
                public void loadImage(ImageView imageView, Bitmap bitmap) {
                    if (bitmap != null && imageView != null) {
                        imageView.setImageBitmap(bitmap);
                        imageView
                                .setScaleType(ImageView.ScaleType.FIT_XY);
                    } else {
                        imageView.setImageResource(R.drawable.waimai);
                    }
                }
            }, holder.food_im);
        } else {
            holder.food_im.setImageResource(R.drawable.waimai);
        }
        holder.food_im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyPopupWindow foodDetailPOP;

                ImageView imageView = new ImageView(context);
                foodDetailPOP = new MyPopupWindow((BaseActivity)context, imageView, 1);
                ImageManager.getInstance(context).getBitmap(NetManager.Ip + foodModel.proPic, new ImageManager.ImageCallBack() {
                    @Override
                    public void loadImage(ImageView imageView, Bitmap bitmap) {
                        if (bitmap != null && imageView != null) {
                            imageView.setImageBitmap(bitmap);
                            imageView
                                    .setScaleType(ImageView.ScaleType.FIT_XY);
                        } else {
                            imageView.setImageResource(R.drawable.waimai);
                        }
                    }
                },imageView);
                //显示窗口
                foodDetailPOP.showAtLocation(((BaseActivity) context).getWindow().getDecorView(), Gravity.CENTER, 0, 0);
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
