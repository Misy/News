package com.wish.news.base.impl;

import android.R;
import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.wish.news.base.BasePager;

public class SettingPager extends BasePager {

	public SettingPager(Activity mActivity) {
		super(mActivity);
		
	}
	@Override
	public void initData() {
		super.initData();
		setSlidingMenu(false);
		btnMenu.setVisibility(View.INVISIBLE);
		tvTitle.setText("设置中心");
		TextView tv = new TextView(mActivity);
		tv.setText("设置");
		tv.setTextColor(Color.RED);
		tv.setTextSize(25);
		tv.setGravity(Gravity.CENTER);
		flContent.addView(tv);
	}

}
