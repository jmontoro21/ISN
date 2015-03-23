package com.inftel.isn.utility;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.inftel.isn.fragment.GroupFragment;
import com.inftel.isn.fragment.HomeFragment;

public class PageAdapterFragment extends FragmentPagerAdapter {

	public PageAdapterFragment(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0) {
		switch (arg0) {
		case 0:
			   return new HomeFragment();
		case 1:
			   return new GroupFragment();
		case 2:
			  return new HomeFragment();
		default:
			break;
		}
		return null;
	}

	@Override
	public int getCount() {
		return 3;
	}

}
