package com.llh.adapter;


import com.ipcamer.demo.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeAdatpterTwo extends BaseAdapter {
	String[] data = { "我的账户", "退出", "积分查询", "消费查询", "健康查询", "更多" };
	int[] id = { R.drawable.userguide_contacts_icon,
			R.drawable.talk_room_exit_btn_pressed, R.drawable.query,
			R.drawable.consumequery, R.drawable.health ,R.drawable.more };
	private LayoutInflater lif = null;

	public HomeAdatpterTwo(Context context) {
		// TODO Auto-generated constructor stub
		lif = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@SuppressLint({ "ViewHolder", "InflateParams" }) @Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		arg1 = lif.inflate(R.layout.homeitem, null);
		//if (arg0 == 0 || arg0 == 1) {
			ImageView iv = (ImageView) arg1.findViewById(R.id.iv);
			iv.setImageResource(id[arg0]);
		//}
		TextView tv = (TextView) arg1.findViewById(R.id.title);
		tv.setText(data[arg0]);
		return arg1;
	}

}
