package com.wish.news.basenews;

import android.app.Activity;
import android.view.View;

/**
 * 新闻中心页面基类
 * 
 * @author 老肥猪
 * 
 */
public abstract class BaseNewsCenterPager {
	public Activity mActivity;
	public View mRootView;

	public BaseNewsCenterPager(Activity mActivity) {
		super();
		this.mActivity = mActivity;
		mRootView = initView();
	}

	public abstract View initView();
	
	public void initData(){}
}
