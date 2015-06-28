package com.llh.adapter;

import java.util.ArrayList;
import java.util.Map;

import com.ipcamer.demo.R;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class InformationListAdapter extends BaseAdapter{
	private ArrayList<Map<String, Object>> data ;
	private LayoutInflater li;
	public InformationListAdapter(Context context,ArrayList<Map<String, Object>> data){
		this.data = data;
		this.li=LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = li.inflate(R.layout.informationi_meun_item,null);
			Holder holder = new Holder();
			holder.title = (TextView)convertView.findViewById(R.id.text_title);
			holder.title2 = (TextView)convertView.findViewById(R.id.text_title2);
			holder.updTime = (TextView)convertView.findViewById(R.id.text_updtime);
			convertView.setTag(holder);
		}
		Map<String, Object> bundle = (Map<String, Object>) getItem(position);
		Holder hodler = (Holder) convertView.getTag();
		if (bundle != null) {
			hodler.title.setText(bundle.get("title").toString());
			hodler.title2.setText(bundle.get("title2").toString());
			hodler.updTime.setText(bundle.get("updTime").toString());
		}
		return convertView;
	}
	class Holder {
		TextView updTime;
		TextView title; 
		TextView title2;
	}
}
