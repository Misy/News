package com.wish.news;

import java.util.ArrayList;
import java.util.List;

import com.wish.news.utils.DensityUtil;
import com.wish.news.utils.PreUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class GuideActivity extends Activity {
	private static final int[] imgResId = new int[] { R.drawable.guide_1,
			R.drawable.guide_2, R.drawable.guide_3 };
	private ViewPager vpGuide;
	private List<ImageView> ivList;
	private LinearLayout llPoint;
	private Button btnEnter;
	private int pointWidth;
	private View pointRed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		initView();
		initData();
		ivList = new ArrayList<ImageView>();
		for (int i = 0; i < imgResId.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setBackgroundResource(imgResId[i]);
			ivList.add(imageView);
		}

	}

	class GuideAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return imgResId.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(ivList.get(position));
			return ivList.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

	private void initData() {

	}

	private void initView() {
		vpGuide = (ViewPager) findViewById(R.id.vp_guide);
		llPoint = (LinearLayout) findViewById(R.id.ll_point);
		btnEnter = (Button) findViewById(R.id.btn_enter);
		pointRed = findViewById(R.id.view_red_point);

		btnEnter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// 进入主页面，并把引导页状态改为true，说明做过引导页了，以后不要再显示引导页
				PreUtils.setGuideShowedPrefs(GuideActivity.this,
						"is_guide_showed", true);
				startActivity(new Intent(GuideActivity.this, MainActivity.class));
				finish();
			}
		});
		vpGuide.setAdapter(new GuideAdapter());
		vpGuide.setOnPageChangeListener(new GuidePageChangeListener());
		for (int i = 0; i < imgResId.length; i++) {
			View point = new View(this);
			point.setBackgroundResource(R.drawable.shape_point_gray);
			int pointWidth = DensityUtil.dip2px(this, 10);
			int pointHeight = DensityUtil.dip2px(this, 10);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					pointWidth, pointHeight);
			if (i > 0) {
				params.leftMargin = 10;
			}
			point.setLayoutParams(params);
			llPoint.addView(point);
		}
		// llPoint.measure(0, 0);
		llPoint.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						llPoint.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
						pointWidth = llPoint.getChildAt(1).getLeft()
								- llPoint.getChildAt(0).getLeft();// 计算两个小圆点之间的距离
					}
				});

	}

	class GuidePageChangeListener implements OnPageChangeListener {
		// 小红点移动的距离
		int distance;

		public void onPageScrollStateChanged(int state) {
		}

		// 页面滑动的时候调用
		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			distance = (int) (pointWidth * positionOffset)
					+ (position * pointWidth);// 别别忘了后面要加上对应位置，比如位置1，要加上小圆点之间的距离，它才会跳到第二个页面
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) pointRed
					.getLayoutParams();// 注意这里的RelativeLayout是小红点view的父布局，如果它的布局是Linear，那就用Linear
			params.leftMargin = distance;
			pointRed.setLayoutParams(params);
		}

		// 某一个页面被选中时调用
		@Override
		public void onPageSelected(int position) {
			if (position == imgResId.length - 1) {
				btnEnter.setVisibility(View.VISIBLE);
			} else {
				btnEnter.setVisibility(View.INVISIBLE);
			}
		}

	}
}
