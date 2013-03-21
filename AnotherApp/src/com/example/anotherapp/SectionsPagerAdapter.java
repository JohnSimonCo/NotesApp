package com.example.anotherapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

	public SectionsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		Fragment fragment = new NoteListFragment();
		if (Resource.lists.size() > 0) {
			Bundle args = new Bundle();
			args.putInt(Resource.SEND_LIST_INDEX, position);
			fragment.setArguments(args);
		} else {
			fragment = new NoListFragment();
		}

		return fragment;
	}

	@Override
	public int getCount() {
		// The amount of pages
		return Resource.lists.size() > 0 ? Resource.lists.size() : 1;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return Resource.lists.size() > 0 ? (Resource.lists.get(position).name
				.equals("") ? Resource.STRING_UNNAMED_LIST : Resource.lists
				.get(position).name) : "No list";
	}

	@Override
	public int getItemPosition(Object object) {
		return PagerAdapter.POSITION_NONE;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		// setActionbarItemsVisibility(Resource.lists.size() > 0);
	}
}
