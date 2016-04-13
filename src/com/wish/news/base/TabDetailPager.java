package com.wish.news.base;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

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
import com.wish.news.NewsDetailActivity;
import com.wish.news.R;
import com.wish.news.basenews.BaseNewsCenterPager;
import com.wish.news.domain.NewsData.NewsTabData;
import com.wish.news.domain.TabData;
import com.wish.news.domain.TabData.TabNewsData;
import com.wish.news.domain.TabData.TopNewsData;
import com.wish.news.global.GlobalConstants;
import com.wish.news.utils.CacheUtils;
import com.wish.news.utils.PreUtils;
import com.wish.news.view.RefreshListView;
import com.wish.news.view.RefreshListView.onRefreshListener;

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

	private String more;
	private String mMoreUrl;

	private Handler mHandler;

	private TopNewsAdapter mTopNewsAdapter;

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
		lvList.setOnRefreshListener(new onRefreshListener() {

			@Override
			public void onRefresh() {
				getDataFromServer();
			}

			@Override
			public void onLoadingMore() {
				if (mMoreUrl != null) {
					getMoreDataFromServer();
				} else {
					Toast.makeText(mActivity, "最后一页了", Toast.LENGTH_SHORT)
							.show();
					lvList.refreshComplete(false);// 收起加载更多的布局
				}
			}
		});

		lvList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				System.out.println("position==" + position);
				String newsIds = PreUtils.getStringPrefs(mActivity, "read_ids");
				String newsId = mNewsList.get(position).id;
				if (!newsIds.contains(newsId)) {
					newsIds = newsIds + newsId + ",";
					PreUtils.setStringPrefs(mActivity, "read_ids", newsIds);
				}

				changeItemState(view);

				Intent intent = new Intent();
				intent.putExtra("news_url", mNewsList.get(position).url);
				intent.setClass(mActivity, NewsDetailActivity.class);
				mActivity.startActivity(intent);
			}
		});
		return view;
	}

	protected void changeItemState(View view) {
		TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
		tvTitle.setTextColor(Color.GRAY);
	}

	/**
	 * 从网络加载更多数据
	 */
	protected void getMoreDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				System.out.println("result:" + result);
				if (result != null) {
					parseData(result, true);
				}
				lvList.refreshComplete(true);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(mActivity, "获取网络数据失败", 0).show();
				lvList.refreshComplete(false);
				error.printStackTrace();
			}

		});
	}

	@Override
	public void initData() {
		super.initData();
		String cache = CacheUtils.getCache(mActivity, mUrl);
		if (!TextUtils.isEmpty(cache)) {
			parseData(cache, false);
		}
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
					parseData(result, false);
				}
				lvList.refreshComplete(true);
				CacheUtils.setCache(mUrl, result, mActivity);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(mActivity, "获取网络数据失败", 0).show();
				lvList.refreshComplete(false);
				error.printStackTrace();
			}

		});
	}

	protected void parseData(String result, boolean isMore) {
		Gson gson = new Gson();
		mTabData = gson.fromJson(result, TabData.class);
		mTopNews = mTabData.data.topnews;

		more = mTabData.data.more;
		if (!TextUtils.isEmpty(more)) {
			mMoreUrl = GlobalConstants.SERVER_URL + more;
		} else {
			mMoreUrl = null;
		}
		if (!isMore) {
			if (mTopNews != null) {
				mTopNewsAdapter = new TopNewsAdapter();
				vpNews.setAdapter(mTopNewsAdapter);
				vpNews.setOnPageChangeListener(this);
				mIndicator.setViewPager(vpNews);
				mIndicator.setSnap(true);
				mIndicator.setOnPageChangeListener(this);
				// 防止viewpager切换的时候，新闻被回收，跳回到第一张新闻图片之后，指示器还是显示在最后离开的位置
				mIndicator.onPageSelected(0);// 进来就设置在0位置
				tvTitle.setText(mTopNews.get(0).title);// 设置初始化新闻标题
			}

			mNewsList = mTabData.data.news;// 新闻列表数据
			if (mNewsList != null) {
				mNewsAdapter = new NewsAdapter();// 新闻列表适配器
				lvList.setAdapter(mNewsAdapter);
			}
			if (mHandler == null) {
				mHandler = new Handler() {
					public void handleMessage(android.os.Message msg) {
						int currentItem = vpNews.getCurrentItem();
						vpNews.setCurrentItem((currentItem + 1)
								% mTopNews.size());
						mHandler.sendEmptyMessageDelayed(0, 2000);
					};
				};
				mHandler.sendEmptyMessageDelayed(0, 2000);
			}
		} else {
			// 加载更多数据
			ArrayList<TabNewsData> news = mTabData.data.news;
			mNewsList.addAll(news);
			mNewsAdapter.notifyDataSetChanged();
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

			String newsIds = PreUtils.getStringPrefs(mActivity, "read_ids");
			if (newsIds.contains(getItem(position).id)) {
				holder.tvTitle.setTextColor(Color.GRAY);
			}
			return convertView;
		}
	}

	class TopNewsTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mHandler.removeCallbacksAndMessages(null);// 删除Handler中的所有消息
				// mHandler.postDelayed(new Runnable() {
				//
				// @Override
				// public void run() {
				//
				// }
				// }, 3000);
				break;
			case MotionEvent.ACTION_CANCEL:
				mHandler.sendEmptyMessageDelayed(0, 2000);
				break;
			case MotionEvent.ACTION_UP:
				mHandler.sendEmptyMessageDelayed(0, 2000);
				break;
			}
			return true;

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
			imageView.setOnTouchListener(new TopNewsTouchListener());
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

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int position) {
		tvTitle.setText(mTopNews.get(position).title);
	}
}
