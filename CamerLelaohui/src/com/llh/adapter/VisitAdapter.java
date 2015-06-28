package com.llh.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;

public class VisitAdapter<T> extends PagerAdapter {
	private SparseArray<T> list;

	public VisitAdapter(SparseArray<T> list) {
		this.list = list;
	}

	@Override
	public void destroyItem(View view, int position, Object arg2) {
		ViewPager pViewPager = ((ViewPager) view);
		pViewPager.removeView((View) list.get(position));
	}

	@Override
	public Object instantiateItem(View view, int position) {
		ViewPager pViewPager = ((ViewPager) view);
		pViewPager.addView((View) list.get(position));
		return list.get(position);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

}
