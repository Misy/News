package com.wish.news.basenews.impl;

import java.util.ArrayList;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;
import com.wish.news.MainActivity;
import com.wish.news.R;
import com.wish.news.base.TabDetailPager;
import com.wish.news.basenews.BaseNewsCenterPager;
import com.wish.news.domain.NewsData.NewsTabData;
import com.wish.news.view.HorizontalScrollViewPager;

public class NewsPager extends BaseNewsCenterPager implements
		OnPageChangeListener {

	private ArrayList<NewsTabData> mNewsTabData;
	private ArrayList<TabDetailPager> mTabPagers;

	private ViewPager vpNewsTab;
	private NewsMenuTabAdapter mAdapter;
	private TabPageIndicator tabIndicator;

	public NewsPager(Activity mActivity, ArrayList<NewsTabData> children) {
		super(mActivity);
		mNewsTabData = children;
	}

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.news_menu_tab, null);
		ViewUtils.inject(this, view);
		vpNewsTab = (ViewPager) view
				.findViewById(R.id.vp_menu_detail);
		tabIndicator = (TabPageIndicator) view
				.findViewById(R.id.vp_tab_indicator);
		return view;
	}

	@OnClick(R.id.ibtn_next)
	public void btnClick(View view) {
		int currentItem = vpNewsTab.getCurrentItem();
		vpNewsTab.setCurrentItem(++currentItem);
	}

	@Override
	public void initData() {
		super.initData();
		mTabPagers = new ArrayList<TabDetailPager>();
		for (int i = 0; i < mNewsTabData.size(); i++) {
			TabDetailPager tabPager = new TabDetailPager(mActivity,
					mNewsTabData.get(i));
			mTabPagers.add(tabPager);
		}
		mAdapter = new NewsMenuTabAdapter();
		vpNewsTab.setAdapter(mAdapter);
		tabIndicator.setViewPager(vpNewsTab);// 给Indicator设置对应viewpager，必须在viewpager设置了Adapter之后设置才有效
		tabIndicator.setOnPageChangeListener(this);
	}

	class NewsMenuTabAdapter extends PagerAdapter {

		@Override
		public CharSequence getPageTitle(int position) {
			return mNewsTabData.get(position).title;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			TabDetailPager tabPager = mTabPagers.get(position);
			container.addView(tabPager.mRootView);
			tabPager.initData();
			return tabPager.mRootView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getCount() {
			return mTabPagers.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int position) {
		MainActivity mainUi = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUi.getSlidingMenu();
		if (position == 0) {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
	}
}
