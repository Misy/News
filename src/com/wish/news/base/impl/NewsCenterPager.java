package com.wish.news.base.impl;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wish.news.domain.NewsData.NewsMenuData;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.wish.news.MainActivity;
import com.wish.news.base.BasePager;
import com.wish.news.basenews.BaseNewsCenterPager;
import com.wish.news.basenews.impl.InteractPager;
import com.wish.news.basenews.impl.NewsPager;
import com.wish.news.basenews.impl.PhotoPager;
import com.wish.news.basenews.impl.SubjectPager;
import com.wish.news.domain.NewsData;
import com.wish.news.fragment.LeftMenuFrag;
import com.wish.news.global.GlobalConstants;
import com.wish.news.utils.CacheUtils;

public class NewsCenterPager extends BasePager {

	private NewsData mNewsData;

	public NewsCenterPager(Activity mActivity) {
		super(mActivity);
	}

	@Override
	public void initData() {
		super.initData();
		setSlidingMenu(true);
		btnMenu.setVisibility(View.VISIBLE);
		tvTitle.setText("新闻中心");
		String cache = CacheUtils.getCache(mActivity,
				GlobalConstants.CATEGORIES_URL);
		if (!TextUtils.isEmpty(cache)) {
			parseData(cache);
		}
		getDataFromServer();
	}

	private void getDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, GlobalConstants.CATEGORIES_URL,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String result = responseInfo.result;
						Toast.makeText(mActivity, "获取json数据成功", 0).show();
						if (result != null) {
							parseData(result);
							// 成功获取到网络数据，要把数据缓存起来
							CacheUtils.setCache(GlobalConstants.CATEGORIES_URL,
									result, mActivity);
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Toast.makeText(mActivity, "获取json数据失败！", 0).show();
						error.printStackTrace();
					}

				});
	}

	public List<BaseNewsCenterPager> newsPagers;

	protected void parseData(String result) {
		Gson gson = new Gson();
		mNewsData = gson.fromJson(result, NewsData.class);
		System.out.println("解析之后的json数据：" + mNewsData);
		setLeftMenuData();
		// 初始化新闻中心页面
		newsPagers = new ArrayList<BaseNewsCenterPager>();
		newsPagers
				.add(new NewsPager(mActivity, mNewsData.data.get(0).children));
		newsPagers.add(new SubjectPager(mActivity));
		newsPagers.add(new PhotoPager(mActivity));
		newsPagers.add(new InteractPager(mActivity));

		setCurrentDetailPager(0);
	}

	private void setLeftMenuData() {
		MainActivity mainUi = (MainActivity) mActivity;
		LeftMenuFrag leftMenuFrag = mainUi.getLeftMenuFrag();
		leftMenuFrag.setMenuData(mNewsData);
	}

	public void setCurrentDetailPager(int position) {
		BaseNewsCenterPager mPagers = newsPagers.get(position);
		flContent.removeAllViews();
		flContent.addView(mPagers.mRootView);

		// 设置当前页的标题
		NewsMenuData menuData = mNewsData.data.get(position);
		tvTitle.setText(menuData.title);

		mPagers.initData();// 初始化当前页面的数据
	}
}
