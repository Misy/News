package com.wish.news.base.impl;

import android.R;
import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.wish.news.base.BasePager;

public class GovAffairPager extends BasePager {

	public GovAffairPager(Activity mActivity) {
		super(mActivity);
		
	}
	@Override
	public void initData() {
		super.initData();
		setSlidingMenu(true);
		btnMenu.setVisibility(View.VISIBLE);
		tvTitle.setText("政务");
		TextView tv = new TextView(mActivity);
		tv.setText("政务");
		tv.setTextColor(Color.RED);
		tv.setTextSize(25);
		tv.setGravity(Gravity.CENTER);
		flContent.addView(tv);
	}

}
