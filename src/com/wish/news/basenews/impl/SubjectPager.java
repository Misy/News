package com.wish.news.basenews.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.wish.news.basenews.BaseNewsCenterPager;

public class SubjectPager extends BaseNewsCenterPager {

	public SubjectPager(Activity mActivity) {
		super(mActivity);
	}

	@Override
	public View initView() {
		TextView tv = new TextView(mActivity);
		tv.setText("专题");
		tv.setTextColor(Color.RED);
		tv.setTextSize(25);
		tv.setGravity(Gravity.CENTER);
		return tv;
	}

}
