package com.wish.news.view;

import com.wish.news.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

public class RefreshListView extends ListView {

	public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initHeaderView();
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeaderView();
	}

	public RefreshListView(Context context) {
		super(context);
		initHeaderView();
	}

	private void initHeaderView() {
		View refreshHeader = View.inflate(getContext(),
				R.layout.list_refresh_header, null);
		this.addHeaderView(refreshHeader);
		refreshHeader.measure(0, 0);
		int measuredHeight = refreshHeader.getMeasuredHeight();
		refreshHeader.setPadding(0, -measuredHeight, 0, 0);//隐藏头部下拉刷新布局
	}

}
