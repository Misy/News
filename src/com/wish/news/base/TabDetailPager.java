package com.wish.news.base;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.TabPageIndicator;
import com.wish.news.R;
import com.wish.news.basenews.BaseNewsCenterPager;
import com.wish.news.domain.TabData;
import com.wish.news.domain.NewsData.NewsTabData;
import com.wish.news.domain.TabData.TabNewsData;
import com.wish.news.domain.TabData.TopNewsData;
import com.wish.news.global.GlobalConstants;
import com.wish.news.view.RefreshListView;

public class TabDetailPager extends BaseNewsCenterPager implements
		OnPageChangeListener {
	private NewsTabData newsTabData;

	private String mUrl;

	@ViewInject(R.id.vp_pager)
	private ViewPager vpNews;

	@ViewInject(R.id.tv_title)
	private TextView tvTitle;

	@ViewInject(R.id.indicator)
	private CirclePageIndicator mIndicator;

	@ViewInject(R.id.lv_list)
	private RefreshListView lvList;

	private TabData mTabData;
	private ArrayList<TopNewsData> mTopNews;

	private ArrayList<TabNewsData> mNewsList;

	private NewsAdapter mNewsAdapter;

	public TabDetailPager(Activity mActivity, NewsTabData newsTabData) {
		super(mActivity);
		this.newsTabData = newsTabData;
		mUrl = GlobalConstants.SERVER_URL + newsTabData.url;
	}

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.tab_detail_pager, null);
		View headerView = View.inflate(mActivity, R.layout.list_header_topnews,
				null);
		ViewUtils.inject(this, view);
		ViewUtils.inject(this, headerView);
		lvList.addHeaderView(headerView);
		return view;
	}

	@Override
	public void initData() {
		super.initData();
		getDataFromServer();
	}

	private void getDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, mUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				System.out.println("result:" + result);
				if (result != null) {
					parseData(result);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(mActivity, "获取网络数据失败", 0).show();
				error.printStackTrace();
			}

		});
	}

	protected void parseData(String result) {
		Gson gson = new Gson();
		mTabData = gson.fromJson(result, TabData.class);
		mTopNews = mTabData.data.topnews;
		if (mTopNews != null) {
			vpNews.setAdapter(new TopNewsAdapter());
			vpNews.setOnPageChangeListener(this);
			mIndicator.setViewPager(vpNews);
			mIndicator.setOnPageChangeListener(this);
			// 防止viewpager切换的时候，新闻被回收，跳回到第一张新闻图片之后，指示器还是显示在最后离开的位置
			mIndicator.onPageSelected(0);// 进来就设置在0位置
		}
		tvTitle.setText(mTopNews.get(0).title);// 设置初始化新闻标题

		mNewsList = mTabData.data.news;// 新闻列表数据
		mNewsAdapter = new NewsAdapter();// 新闻列表适配器
		if (mNewsList != null) {
			lvList.setAdapter(mNewsAdapter);
		}
	}

	/**
	 * 新闻列表适配器
	 * 
	 * @author 老肥猪
	 * 
	 */
	class NewsAdapter extends BaseAdapter {
		private BitmapUtils bitmapUtils;

		public NewsAdapter() {
			super();
			bitmapUtils = new BitmapUtils(mActivity);
			bitmapUtils
					.configDefaultLoadingImage(R.drawable.pic_item_list_default);
		}

		@Override
		public int getCount() {
			return mNewsList.size();
		}

		@Override
		public TabNewsData getItem(int position) {
			return mNewsList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup group) {
			View view;
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(mActivity, R.layout.list_news_item,
						null);
				holder.ivPic = (ImageView) convertView
						.findViewById(R.id.iv_pic);
				holder.tvTitle = (TextView) convertView
						.findViewById(R.id.tv_title);
				holder.tvDate = (TextView) convertView
						.findViewById(R.id.tv_date);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			// 给控件设置要显示的数据
			TabNewsData newsItem = getItem(position);
			holder.tvTitle.setText(newsItem.title);
			holder.tvDate.setText(newsItem.pubdate);
			bitmapUtils.display(holder.ivPic, newsItem.listimage);

			return convertView;
		}
	}

	static class ViewHolder {
		ImageView ivPic;
		TextView tvTitle;
		TextView tvDate;
	}

	/**
	 * 头条新闻图片适配器
	 * 
	 * @author 老肥猪
	 * 
	 */
	class TopNewsAdapter extends PagerAdapter {
		private BitmapUtils bitmapUtils;

		public TopNewsAdapter() {
			super();
			bitmapUtils = new BitmapUtils(mActivity);
			bitmapUtils
					.configDefaultLoadingImage(R.drawable.topnews_item_default);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView imageView = new ImageView(mActivity);
			imageView.setScaleType(ScaleType.FIT_XY);
			bitmapUtils.display(imageView, mTopNews.get(position).topimage);
			container.addView(imageView);
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getCount() {
			return mTopNews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int position) {
		tvTitle.setText(mTopNews.get(position).title);
	}
}
