package com.wish.news;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

import com.wish.news.utils.PreUtils;

public class NewsDetailActivity extends Activity implements OnClickListener {
	private WebView mWebView;
	private ImageButton btnBack;
	private ImageButton btnSize;
	private ImageButton btnShare;
	private ProgressBar pbLoading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_detail);

		mWebView = (WebView) findViewById(R.id.wv_web);
		btnBack = (ImageButton) findViewById(R.id.btn_back);
		btnSize = (ImageButton) findViewById(R.id.btn_size);
		btnShare = (ImageButton) findViewById(R.id.btn_share);
		pbLoading = (ProgressBar) findViewById(R.id.pb_progress);

		String newsUrl = getIntent().getExtras().getString("news_url");
		settings = mWebView.getSettings();
		settings.setJavaScriptEnabled(true);// 开启支持javascript
		settings.setBuiltInZoomControls(true);// 开启缩放
		settings.setUseWideViewPort(true);// 开启双击缩放

		mTextSize = PreUtils.getTextSizePrefs(NewsDetailActivity.this,
				"text_size");
		setCurrentTextSize();
		mWebView.loadUrl(newsUrl);// 加载网页
		mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				// System.out.println("onPageFinished");
				// pbLoading.setVisibility(View.GONE);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				// pbLoading.setVisibility(View.VISIBLE);
			}

		});

		btnBack.setOnClickListener(this);
		btnShare.setOnClickListener(this);
		btnSize.setOnClickListener(this);
	}

	private String[] items = new String[] { "超大号字体", "大号字体", "正常字体", "小号字体",
			"超小号字体" };
	private int mTextSize;
	private WebSettings settings;

	protected void showTextSizeDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("请选择字体大小");
		mTextSize = PreUtils.getTextSizePrefs(NewsDetailActivity.this,
				"text_size");
		builder.setSingleChoiceItems(items, mTextSize,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						mTextSize = which;
					}
				});
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				setCurrentTextSize();
				PreUtils.setTextSizePrefs(NewsDetailActivity.this, "text_size",
						mTextSize);
			}

		});
		builder.setNegativeButton("取消", null);
		builder.show();
	}

	private void setCurrentTextSize() {
		switch (mTextSize) {
		case 0:
			settings.setTextSize(TextSize.LARGEST);
			break;
		case 1:
			settings.setTextSize(TextSize.LARGER);
			break;
		case 2:
			settings.setTextSize(TextSize.NORMAL);
			break;
		case 3:
			settings.setTextSize(TextSize.SMALLER);
			break;
		case 4:
			settings.setTextSize(TextSize.SMALLEST);
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:

			break;
		case R.id.btn_share:
			showShare();
			break;
		case R.id.btn_size:
			showTextSizeDialog();
			break;
		}
	}

	/**
	 * 分享
	 */
	private void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 设置主题
		// oks.setTheme(OnekeyShareTheme.SKYBLUE);// 设置天蓝色的主题
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
		// 分享时Notification的图标和文字
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(getString(R.string.share));
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl("http://sharesdk.cn");
		// text是分享文本，所有平台都需要这个字段
		oks.setText("我是分享文本");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		oks.setImagePath("/sdcard/test.jpg");// 确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://sharesdk.cn");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://sharesdk.cn");
		// 启动分享GUI
		oks.show(this);
	}
}
