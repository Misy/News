package com.wish.news.fragment;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.Subject;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.wish.news.R;
import com.wish.news.base.BasePager;
import com.wish.news.base.impl.GovAffairPager;
import com.wish.news.base.impl.HomePager;
import com.wish.news.base.impl.NewsCenterPager;
import com.wish.news.base.impl.SettingPager;
import com.wish.news.base.impl.SmartPager;
import com.wish.news.basenews.BaseNewsCenterPager;
import com.wish.news.basenews.impl.InteractPager;
import com.wish.news.basenews.impl.NewsPager;
import com.wish.news.basenews.impl.PhotoPager;
import com.wish.news.basenews.impl.SubjectPager;

public class ContentFrag extends BaseFragment {
	@ViewInject(R.id.rg_group)
	private RadioGroup rgGroup;
	@ViewInject(R.id.vp_content)
	private ViewPager vpContent;
	private List<BasePager> mPagers;
	

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.fragment_content, null);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	protected void initData() {
		super.initData();
		rgGroup.check(R.id.rb_home);// 设置默认选中哪个radioButton
		mPagers = new ArrayList<BasePager>();
		mPagers.add(new HomePager(mActivity));
		mPagers.add(new NewsCenterPager(mActivity));
		mPagers.add(new SmartPager(mActivity));
		mPagers.add(new GovAffairPager(mActivity));
		mPagers.add(new SettingPager(mActivity));


		vpContent.setAdapter(new ContentAdapter());
		mPagers.get(0).initData();
		vpContent.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				mPagers.get(position).initData();
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		rgGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_home:
					// mViewPager.setCurrentItem(0);// 设置当前页面
					vpContent.setCurrentItem(0, false);// 去掉切换页面的动画
					break;
				case R.id.rb_news:
					//System.out.println("新闻你妹的");
					vpContent.setCurrentItem(1, false);// 设置当前页面
					break;
				case R.id.rb_smart:
					vpContent.setCurrentItem(2, false);// 设置当前页面
					break;
				case R.id.rb_gov:
					vpContent.setCurrentItem(3, false);// 设置当前页面
					break;
				case R.id.rb_setting:
					vpContent.setCurrentItem(4, false);// 设置当前页面
					break;

				}
			}
		});

	}

	class ContentAdapter extends PagerAdapter {
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			BasePager mPager = mPagers.get(position);
			// mPager.initData();// 更新数据
			container.addView(mPager.mRootView);
			return mPager.mRootView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getCount() {
			return mPagers.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}

	public void setCurrentNewsPager(int position) {
		NewsCenterPager pager = (NewsCenterPager) mPagers.get(1);
		pager.setCurrentDetailPager(position);
	}

}
