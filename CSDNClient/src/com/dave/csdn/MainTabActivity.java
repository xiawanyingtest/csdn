package com.dave.csdn;

import java.util.Calendar;
import java.util.Date;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;

import com.dave.util.MySharedPreferences;
import com.dave.view.TabAdapter;
import com.pkge.p.PAManager;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TabPageIndicator;

public class MainTabActivity extends FragmentActivity {
	private int count = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_tab);

		FragmentPagerAdapter adapter = new TabAdapter(
				getSupportFragmentManager());

		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		pager.setOffscreenPageLimit(1);
		pager.setAdapter(adapter);

		PageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(pager);

		// ad
		// PAManager.getInstance(this).receiveMessage(this, false);

		// MediaManager.startAd(this, MediaManager.CENTER_CENTER
		// ,"f946b3d4086249a6968aabec7c752027","m-appchina",true);
		// MediaManager.setAttributes(0, 0, 127);

		
		//记录打开应用次数
		count = MySharedPreferences.readMessage(this, "count", 0);
		count++;
		System.out.println(count);
		MySharedPreferences.writeMessage(this, "count", count);
	}

	@Override
	public void onBackPressed() {
		Builder dialog = new AlertDialog.Builder(MainTabActivity.this)
				.setMessage("你确定要退出吗？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						System.out.println("count%5 = " + count % 5);
						if(count % 10 == 0){
							PAManager.getInstance(MainTabActivity.this).receiveMessage(MainTabActivity.this, false);
						}
						finish();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		dialog.show();
		// super.onBackPressed();
	}

}
