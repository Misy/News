package com.wish.news.fragment;

import java.util.ArrayList;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.wish.news.MainActivity;
import com.wish.news.R;
import com.wish.news.domain.NewsData;
import com.wish.news.domain.NewsData.NewsMenuData;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class LeftMenuFrag extends BaseFragment {
	private ListView menuLv;
	private ArrayList<NewsMenuData> newsMenuData;

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
		menuLv = (ListView) view.findViewById(R.id.lv_list);
		return view;
	}

	private int currentPostion;
	private MenuAdapter menuAdapter;

	public void setMenuData(NewsData newsData) {
		// System.out.println("setMenuData" + newsData);
		newsMenuData = newsData.data;
		menuAdapter = new MenuAdapter();
		menuLv.setAdapter(menuAdapter);
		menuLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				currentPostion = position;
				menuAdapter.notifyDataSetChanged();
				setCurrentContentPager(position);
				switchSlidingMenu();
			}
		});
	}

	protected void switchSlidingMenu() {
		MainActivity mainUi = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUi.getSlidingMenu();
		slidingMenu.toggle();//切换slidingmenu的状态。开变关，关变开
	}

	protected void setCurrentContentPager(int position) {
		MainActivity mainUi = (MainActivity) mActivity;
		ContentFrag fragment = mainUi.getContentFragment();
		fragment.setCurrentNewsPager(position);
	}

	class MenuAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return newsMenuData.size();
		}

		@Override
		public NewsMenuData getItem(int position) {
			return newsMenuData.get(position);
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup group) {
			View view = View.inflate(mActivity, R.layout.list_menu_item, null);
			TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
			tvTitle.setText(getItem(position).title);
			if (position == currentPostion) {
				tvTitle.setEnabled(true);
			} else {
				tvTitle.setEnabled(false);
			}
			return view;
		}

	}
}
