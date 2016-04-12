package com.wish.news.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class TopNewsViewPager extends ViewPager {
	private int startX, startY;
	private int endX, endY;

	public TopNewsViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TopNewsViewPager(Context context) {
		super(context);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			getParent().requestDisallowInterceptTouchEvent(true);
			startX = (int) ev.getRawX();
			startY = (int) ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			endX = (int) ev.getRawX();
			endY = (int) ev.getRawY();
			if (Math.abs(endX - startX) > Math.abs(endY - startY)) {// 左右滑动
				if (endX > startX) {
					// 右滑
					if (getCurrentItem() == 0) {
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				} else {
					// 左滑
					if (getCurrentItem() == getAdapter().getCount() - 1) {
						// 如果处于最后一张新闻图片左滑需要跳转到下一个tab，则父控件自己消费掉事件，不往下分发
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				}
			} else {
				// 上下滑动
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			break;
		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
}