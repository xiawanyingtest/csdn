package com.dave.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dave.csdn.NewsFrag;


public class TabAdapter extends FragmentPagerAdapter {
	private static final String[] CONTENT = new String[] { "业界", "移动", "研发", "程序员杂志", "云计算"};
	
    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
    	switch(position){
    	case 0:
    		break;
    	default:
    		break;
    	}
        return new NewsFrag(position + 1);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return CONTENT[position % CONTENT.length].toUpperCase();
    }

    @Override
    public int getCount() {
      return CONTENT.length;
    }
}
