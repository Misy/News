package com.wish.news.base;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.wish.news.MainActivity;
import com.wish.news.R;

public class BasePager {
	public Activity mActivity;

	public View mRootView;// 布局对象
	public TextView tvTitle;// 标题对象
	public FrameLayout flContent;// 内容
	public ImageButton btnMenu;// 菜单按钮

	public BasePager(Activity mActivity) {
		super();
		this.mActivity = mActivity;
		initView();
	}

	protected void initView() {
		mRootView = View.inflate(mActivity, R.layout.base_pager, null);
		tvTitle = (TextView) mRootView.findViewById(R.id.tv_title);
		flContent = (FrameLayout) mRootView.findViewById(R.id.fl_content);
		btnMenu = (ImageButton) mRootView.findViewById(R.id.btn_menu);
		btnMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				switchSlidingMenu();
			}
		});
	}

	protected void switchSlidingMenu() {
		MainActivity mainUi = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUi.getSlidingMenu();
		slidingMenu.toggle();
	}

	public void initData() {
	}

	public void setSlidingMenu(boolean enabled) {
		MainActivity mainUi = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUi.getSlidingMenu();
		if (enabled) {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
	}
}
