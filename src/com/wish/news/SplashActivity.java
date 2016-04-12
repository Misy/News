package com.wish.news;

import com.wish.news.utils.PreUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

public class SplashActivity extends Activity {
	AnimationSet sets;
	private ImageView ivLogo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		initView();
		initData();
	}

	private void initData() {
		initAnim();
	}

	private void initAnim() {
		sets = new AnimationSet(false);

		RotateAnimation rotate = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotate.setDuration(2000);
		rotate.setFillAfter(true);
		// 渐变动画
		AlphaAnimation alpha = new AlphaAnimation(1.0f, 0.0f);
		alpha.setDuration(2000);
		alpha.setFillAfter(true);
		// 缩放动画
		ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		scale.setDuration(1000);// 动画时间
		scale.setFillAfter(true);// 保持动画状态

		sets.addAnimation(rotate);
		sets.addAnimation(alpha);
		sets.addAnimation(scale);

		sets.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {

			}

			@Override
			public void onAnimationRepeat(Animation arg0) {

			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				goToHomePage();
			}
		});

		ivLogo.startAnimation(sets);

	}

	protected void goToHomePage() {
		boolean isGuideShowed = PreUtils.getGuideShowedPrefs(this, "is_guide_showed");
		if(isGuideShowed){
			startActivity(new Intent(SplashActivity.this, MainActivity.class));
		}else{
			startActivity(new Intent(SplashActivity.this, GuideActivity.class));
		}
		finish();
	}

	private void initView() {
		ivLogo = (ImageView) findViewById(R.id.iv_logo);
	}
}
