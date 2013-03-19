package com.example.anotherapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnActionExpandListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class MainActivity extends FragmentActivity {

	public SectionsPagerAdapter sectionsPagerAdapter;
	public CustomViewPager viewPager;

	public MenuItem doneButton;
	public MenuItem discardButton;
	public MenuItem editName;
	public boolean editNameExpanded;
	public boolean saveNewName;

	public MenuItem newNote;
	public MenuItem deleteList;
	public MenuItem renameList;
	public MenuItem addList;

	public static Activity context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Resource.PORTRAIT = getResources().getBoolean(R.bool.portrait);
		context = this;
		Resource.gatherListsFromSave(getPreferences(Context.MODE_PRIVATE));

		viewPager = (CustomViewPager) findViewById(R.id.pager);
		viewPager.setAdapter(sectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager()));

		if (savedInstanceState == null) {
			int page = getIntent().getIntExtra(Resource.SEND_CURRENT_PAGE, -1);
			if (page > -1)
				viewPager.setCurrentItem(page);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		newNote = menu.findItem(R.id.menu_new_note);
		deleteList = menu.findItem(R.id.menu_remove_list);
		renameList = menu.findItem(R.id.menu_name_list);
		addList = menu.findItem(R.id.menu_add_list);
		setActionbarItemsVisibility(Resource.lists.size() > 0);

		doneButton = menu.findItem(R.id.menu_done);
		discardButton = menu.findItem(R.id.menu_discard);
		editName = menu.findItem(R.id.menu_name_list);
		editName.setActionView(R.layout.actionbar_rename)
				.setOnActionExpandListener(onEditNameExpand);
		((EditText) editName.getActionView())
				.addTextChangedListener(onTextChange);
		return true;
	}

	TextWatcher onTextChange = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (editNameExpanded)
				doneButton.setVisible(((EditText) editName.getActionView())
						.getText().length() > 0);
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
		}
	};

	OnActionExpandListener onEditNameExpand = new OnActionExpandListener() {
		@Override
		public boolean onMenuItemActionCollapse(MenuItem item) {
			editNameExpanded = false;
			setActionbarItemsVisibility(true);
			toggleEditNameUi(false);
			doneButton.setVisible(false);
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(
					((EditText) item.getActionView()).getWindowToken(), 0);
			handleNewName(saveNewName);
			return true;
		}

		@Override
		public boolean onMenuItemActionExpand(MenuItem item) {
			editNameExpanded = true;
			EditText editText = (EditText) item.getActionView();
			editText.setText(Resource.lists.get(viewPager.getCurrentItem()).name);
			editText.requestFocus();
			setActionbarItemsVisibility(false);
			toggleEditNameUi(true);
			return true;
		}
	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_new_note:
			Intent intent = new Intent(this, NoteDetailActivity.class);
			intent.putExtra(Resource.SEND_LIST_INDEX,
					viewPager.getCurrentItem());
			startActivity(intent);
			return true;
		case R.id.menu_add_list:
			int listIndex = Resource.addList("");
			viewPager.setCurrentItem(listIndex, true);
			editName.expandActionView();
			return true;
		case R.id.menu_remove_list:
			Resource.deleteList(viewPager.getCurrentItem());
			sectionsPagerAdapter.notifyDataSetChanged();
			return true;
		case R.id.menu_done:
			saveNewName = true;
			editName.collapseActionView();
			return true;
		case R.id.menu_discard:
			saveNewName = false;
			editName.collapseActionView();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void handleNewName(boolean save) {
		String newName = ((EditText) editName.getActionView()).getText()
				.toString();
		if (save) {
			Resource.editListName(viewPager.getCurrentItem(), newName);
			sectionsPagerAdapter.notifyDataSetChanged();
		} else {
			if (newName.length() < 1
					&& Resource.lists.get(viewPager.getCurrentItem()).name
							.length() < 1) {
				Resource.deleteList(viewPager.getCurrentItem());
				sectionsPagerAdapter.notifyDataSetChanged();
			}
		}
	}

	public void setActionbarItemsVisibility(boolean visibility) {
		newNote.setVisible(visibility);
		deleteList.setVisible(visibility);
		renameList.setVisible(visibility);
	}

	public void toggleEditNameUi(boolean enabled) {
		viewPager.lock(enabled);
		discardButton.setVisible(enabled);
		addList.setVisible(!enabled);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	// public class SectionsPagerAdapter extends FragmentPagerAdapter {
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
//			setActionbarItemsVisibility(Resource.lists.size() > 0);
		}
	}
}