package com.llh.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.ipcamer.app.MyApplication;
import com.ipcamer.demo.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OrderFoodTypeAdapter extends BaseAdapter{

	private ArrayList<String> array=new ArrayList<String>();
	private LayoutInflater inflater;
	public OrderFoodTypeAdapter(){
		inflater = LayoutInflater.from(MyApplication.getMyApplication());
	}
	public void setData(ArrayList<String> data){
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
		if(null==convertView)
		{
			convertView = inflater.inflate(R.layout.order_food_left_list_item, null);
		}
		TextView type = (TextView) convertView.findViewById(R.id.food_type);
		type.setText(array.get(position));
		return convertView;
	}

}
