package com.inftel.isn.utility;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.inftel.isn.fragment.GroupFragment;
import com.inftel.isn.fragment.HomeFragment;
import com.inftel.isn.fragment.NotaFragment;

public class PageAdapterFragment extends FragmentPagerAdapter {

private String email;
	public PageAdapterFragment(FragmentManager fm, String email) {
		super(fm);
        this.email = email;
	}



	@Override
	public Fragment getItem(int arg0) {
		switch (arg0) {
		case 0:
            Bundle bundle = new Bundle();
            bundle.putString("emailGoogle", email);
            HomeFragment fragobj = new HomeFragment();
            fragobj.setArguments(bundle);
            fragobj.setEmailLogin(email);
			return fragobj;
		case 1:
			return new GroupFragment();
		case 2:
			return new NotaFragment();
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
