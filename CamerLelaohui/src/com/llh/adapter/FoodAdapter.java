package com.llh.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.ipcamer.app.MyApplication;
import com.ipcamer.demo.R;
import com.llh.entity.FoodModel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FoodAdapter extends BaseAdapter {

	private ArrayList<FoodModel> array = new ArrayList<FoodModel>();
	private LayoutInflater inflater;

	public FoodAdapter() {
		inflater = LayoutInflater.from(MyApplication.getMyApplication());
	}

	public void setData(ArrayList<FoodModel> data) {
		array.clear();
		array.addAll(data);
		this.notifyDataSetChanged();
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
			holder.food_im = (ImageView) convertView.findViewById(R.id.food_im);
			holder.food_add = (ImageView) convertView.findViewById(R.id.food_add);
		}else {
			holder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象
		}
		
		holder.food_name.setText(array.get(position).proName);
		holder.food_price.setText("¥"+array.get(position).proPrice+"元");

		return convertView;
	}

	static class ViewHolder {
		TextView food_name;
		TextView food_price;
		ImageView food_im;
		ImageView food_add;
	}
}
