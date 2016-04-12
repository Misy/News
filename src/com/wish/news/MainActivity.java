package com.wish.news;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.wish.news.fragment.ContentFrag;
import com.wish.news.fragment.LeftMenuFrag;

public class MainActivity extends SlidingFragmentActivity {

	private static final int SLIDINGMENU_BEHIND_OFFSET = 400;
	private static final String LEFT_MENU_FRAG = "leftMenuFrag";
	private static final String CONTENT_FRAG = "contentFrag";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setBehindContentView(R.layout.left_menu);
		SlidingMenu slidingMenu = getSlidingMenu();
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		slidingMenu.setBehindOffset(SLIDINGMENU_BEHIND_OFFSET);

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.replace(R.id.fl_left_menu, new LeftMenuFrag(),
				LEFT_MENU_FRAG);
		transaction.replace(R.id.fl_content, new ContentFrag(), CONTENT_FRAG);
		transaction.commit();

	}
	
	/**
	 * 获取LeftMenuFrag对象
	 * @return
	 */
	public LeftMenuFrag getLeftMenuFrag(){
		FragmentManager fm = getSupportFragmentManager();
		LeftMenuFrag fragment = (LeftMenuFrag)fm.findFragmentByTag(LEFT_MENU_FRAG);
		return fragment;
	}

	public ContentFrag getContentFragment() {
		return (ContentFrag) getSupportFragmentManager().findFragmentByTag(CONTENT_FRAG);
	}
}
