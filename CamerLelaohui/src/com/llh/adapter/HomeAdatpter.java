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

public class HomeAdatpter extends BaseAdapter {
	String[] data = { "活动", "资讯", "̽探望", "积分", "订餐", "设置" };
	int[] id = { R.drawable.activity, R.drawable.info1, R.drawable.look,
			R.drawable.exchange, R.drawable.eat, R.drawable.personage };

	private LayoutInflater lif = null;

	public HomeAdatpter(Context context) {
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
		return arg0;
	}

	@SuppressLint({ "ViewHolder", "InflateParams" }) @Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		arg1 = lif.inflate(R.layout.homeitem, null);
		ImageView iv = (ImageView) arg1.findViewById(R.id.iv);
		iv.setImageResource(id[arg0]);
		TextView tv = (TextView) arg1.findViewById(R.id.title);
		tv.setText(data[arg0]);
		return arg1;
	}

}
