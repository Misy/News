package com.wish.news.basenews.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.wish.news.basenews.BaseNewsCenterPager;

public class InteractPager extends BaseNewsCenterPager {

	public InteractPager(Activity mActivity) {
		super(mActivity);
	}

	@Override
	public View initView() {
		TextView tv = new TextView(mActivity);
		tv.setText("互动");
		tv.setTextColor(Color.RED);
		tv.setTextSize(25);
		tv.setGravity(Gravity.CENTER);
		return tv;
	}

}
