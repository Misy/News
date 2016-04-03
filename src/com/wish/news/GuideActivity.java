package com.wish.news;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class GuideActivity extends Activity {
	private static final int[] imgResId = new int[] { R.drawable.guide1,
			R.drawable.guide2, R.drawable.guide3 };
	private ViewPager vpGuide;
	private List<ImageView> ivList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);

		ivList = new ArrayList<ImageView>();
		for (int i = 0; i < imgResId.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setBackgroundResource(imgResId[i]);
			ivList.add(imageView);
		}
		initView();
		initData();
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
		vpGuide.setAdapter(new GuideAdapter());

	}
}
