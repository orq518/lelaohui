package com.llh.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ipcamer.app.MyApplication;
import com.ipcamer.demo.R;
import com.llh.entity.FoodModel;
import com.llh.utils.OrderFoodInterface;

import java.util.ArrayList;

public class ShoppingAdapter extends BaseAdapter {

	private ArrayList<FoodModel> array = new ArrayList<FoodModel>();
	private LayoutInflater inflater;

	public ShoppingAdapter() {
		inflater = LayoutInflater.from(MyApplication.getMyApplication());
	}
	OrderFoodInterface orderFoodInterface;
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
			convertView = inflater.inflate(R.layout.shopping_list_item, null);
			holder = new ViewHolder();
			convertView.setTag(holder);// 绑定ViewHolder对象
			holder.food_name = (TextView) convertView.findViewById(R.id.food_name);
			holder.food_price = (TextView) convertView.findViewById(R.id.tv_price);
			holder.food_num = (TextView) convertView.findViewById(R.id.tv_num);
			holder.im_reduce = (ImageView) convertView.findViewById(R.id.im_reduce);
			holder.food_add = (ImageView) convertView.findViewById(R.id.im_add);
		}else {
			holder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象
		}

		final FoodModel foodModel = array.get(position);
		if(foodModel.mealTime.equals("1")){
			holder.food_name.setText("(早)"+foodModel.proName);
		}else if(foodModel.mealTime.equals("2")){
			holder.food_name.setText("(中)"+foodModel.proName);
		}else if(foodModel.mealTime.equals("3")){
			holder.food_name.setText("(晚)"+foodModel.proName);
		}else{
			holder.food_name.setText(foodModel.proName);
		}
//		holder.food_name.setText(foodModel.proName);
		holder.food_price.setText("¥" + foodModel.proPrice + "元");
		holder.food_num.setText(foodModel.buyNum + "");
		holder.food_add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				foodModel.buyNum++;
				notifyDataSetChanged();
				if (orderFoodInterface != null) {
					orderFoodInterface.refreshShoppingList(foodModel);
					orderFoodInterface.refreshFoodList();
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
					orderFoodInterface.refreshFoodList();
				}
			}
		});

		return convertView;
	}

	static class ViewHolder {
		TextView food_name;
		TextView food_price;
		TextView food_num;
		ImageView food_add;
		ImageView im_reduce;
	}
}
