package com.wish.news.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class HorizontalScrollViewPager extends ViewPager {

	public HorizontalScrollViewPager(Context context) {
		super(context);
	}

	public HorizontalScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if(getCurrentItem()!=0){
			//如果不是第一个页面，则需要响应viewpager的左右滑动事件，这时候需要父控件分发事件给子控件，
			//也就是不去拦截事件往下分发。
			getParent().requestDisallowInterceptTouchEvent(true);
		}else{
			//如果处于第一个页面，这时候Slidingmenu需要响应侧滑操作，那么Slidingmenu自身需要该事件，那么就不往下分发。
			getParent().requestDisallowInterceptTouchEvent(false);
		}
		return super.dispatchTouchEvent(ev);
	}

}
