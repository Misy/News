package com.wish.news.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.spec.IvParameterSpec;

import com.wish.news.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class RefreshListView extends ListView implements OnScrollListener,
		OnItemClickListener {

	// 下拉刷新三种状态：1.下拉刷新； 2.松开刷新； 3.正在刷新
	public static final int PULL_REFRESH = 0;
	public static final int RELEASE_REFRESH = 1;
	public static final int REFRESHING = 2;

	// 当前下拉刷新的状态
	private int mCurrentState = PULL_REFRESH;// 默认等于下拉刷新

	private int startY = -1;
	private int endY;
	private int measuredHeight;
	private View refreshHeader;
	private ImageView ivArrow;
	private ProgressBar pbBar;
	private TextView tvTitle;
	private TextView tvDate;
	private RotateAnimation upAnim;
	private RotateAnimation downAnim;

	public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initHeaderView();
		initFooterView();
	}

	/**
	 * 加载底部加载更多布局
	 */
	private void initFooterView() {
		footerView = View.inflate(getContext(), R.layout.footer_loading_more,
				null);
		footerView.measure(0, 0);
		footerHeight = footerView.getMeasuredHeight();
		this.addFooterView(footerView);
		footerView.setPadding(0, -footerHeight, 0, 0);// 隐藏底部加载更多布局

		this.setOnScrollListener(this);// 为RefreshListView设置滚动监听事件
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeaderView();
		initFooterView();
	}

	public RefreshListView(Context context) {
		super(context);
		initHeaderView();
		initFooterView();
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startY = (int) ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			if (mCurrentState == REFRESHING) {
				break;// 如果当前正在刷新，则不去响应触摸滑动事件
			}
			if (startY == -1) {
				startY = (int) ev.getRawY();// 如果获取不到y按下的值，那我们再重新获取一次
			}
			endY = (int) ev.getRawY();
			int dy = endY - startY;// 用户手指滑动的距离
			if (dy > 0 && getFirstVisiblePosition() == 0) {// 用户往下拉才去进行下面的操作
				int padding = dy - measuredHeight;// 我们要的padding是负值，所以两个参数交换一下位置(原本：measuredHeight-dy)
				refreshHeader.setPadding(0, padding, 0, 0);
				if (padding >= 0 && mCurrentState != RELEASE_REFRESH) {
					mCurrentState = RELEASE_REFRESH;
					refreshState();
				} else if (padding < 0 && mCurrentState != PULL_REFRESH) {
					mCurrentState = PULL_REFRESH;
					System.out.println("mCurrentState" + mCurrentState);
					refreshState();
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			startY = -1; // 重置
			if (mCurrentState == PULL_REFRESH) {
				refreshHeader.setPadding(0, -measuredHeight, 0, 0);
			} else if (mCurrentState == RELEASE_REFRESH) {
				refreshHeader.setPadding(0, 0, 0, 0);
				mCurrentState = REFRESHING;
				refreshState();
			}
			break;
		}
		return super.onTouchEvent(ev);
	}

	private void refreshState() {
		ivArrow.clearAnimation();
		switch (mCurrentState) {
		case PULL_REFRESH:
			ivArrow.setVisibility(View.VISIBLE);
			pbBar.setVisibility(View.INVISIBLE);
			tvTitle.setText("下拉刷新");
			ivArrow.startAnimation(downAnim);
			break;
		case RELEASE_REFRESH:
			ivArrow.setVisibility(View.VISIBLE);
			pbBar.setVisibility(View.INVISIBLE);
			tvTitle.setText("松开刷新");
			ivArrow.startAnimation(upAnim);
			break;
		case REFRESHING:
			ivArrow.setVisibility(View.INVISIBLE);
			pbBar.setVisibility(View.VISIBLE);
			tvTitle.setText("正在刷新...");
			mListener.onRefresh();// 刷新界面
			break;
		}
	}

	public void refreshComplete(boolean isSuccess) {
		// 收起头部布局（下拉刷新）
		mCurrentState = PULL_REFRESH;
		ivArrow.setVisibility(View.VISIBLE);
		pbBar.setVisibility(View.INVISIBLE);
		tvTitle.setText("下拉刷新");

		footerView.setPadding(0, -footerHeight, 0, 0);
		if (isSuccess) {
			tvDate.setText(getCurrentTime());
		}

		// 收起底部布局（加载更多）
		if (isLoadingMore) {
			refreshHeader.setPadding(0, -measuredHeight, 0, 0);// 隐藏加载更多布局
			isLoadingMore = false;
		}
	}

	public String getCurrentTime() {
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formater.format(new Date());
	}

	private void initHeaderView() {
		refreshHeader = View.inflate(getContext(),
				R.layout.list_refresh_header, null);
		ivArrow = (ImageView) refreshHeader.findViewById(R.id.iv_arrow);
		pbBar = (ProgressBar) refreshHeader.findViewById(R.id.pb_progress);
		tvTitle = (TextView) refreshHeader.findViewById(R.id.tv_title);
		tvDate = (TextView) refreshHeader.findViewById(R.id.tv_date);

		this.addHeaderView(refreshHeader);
		refreshHeader.measure(0, 0);
		measuredHeight = refreshHeader.getMeasuredHeight();
		refreshHeader.setPadding(0, -measuredHeight, 0, 0);// 隐藏头部下拉刷新布局
		tvDate.setText(getCurrentTime());
		initAnim();
	}

	private void initAnim() {
		upAnim = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF,
				0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		upAnim.setDuration(500);
		upAnim.setFillAfter(true);

		downAnim = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		downAnim.setDuration(500);
		downAnim.setFillAfter(true);
	}

	public onRefreshListener mListener;
	private int footerHeight;

	public void setOnRefreshListener(onRefreshListener listener) {
		mListener = listener;
	}

	public interface onRefreshListener {
		public void onRefresh();

		public void onLoadingMore();
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

	}

	private boolean isLoadingMore;
	private View footerView;

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_FLING
				|| scrollState == SCROLL_STATE_IDLE) {// 如果处于空闲或惯性滑动则去加载

			if (getLastVisiblePosition() == getCount() - 1 && !isLoadingMore) {
				footerView.setPadding(0, 0, 0, 0);// 隐藏加载更多布局
				isLoadingMore = true;
				if (mListener != null) {// 加载更多
					mListener.onLoadingMore();
				}
				setSelection(getCount() - 1);// 把位置显示到最后一个
			}
		}
	}

	private OnItemClickListener mItemClickListener;

	@Override
	public void setOnItemClickListener(
			android.widget.AdapterView.OnItemClickListener listener) {
		super.setOnItemClickListener(this);
		mItemClickListener = listener;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(mItemClickListener!=null){
			//对position进行处理，去掉头部布局的个数
			mItemClickListener.onItemClick(parent, view, position-getHeaderViewsCount(), id);
		}
	}
}
