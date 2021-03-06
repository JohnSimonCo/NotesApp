package com.example.anotherapp;

import java.util.Date;

import com.example.anotherapp.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
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
	public String currentName;

	public MenuItem newNote;
	public MenuItem deleteList;
	public MenuItem renameList;
	public MenuItem addList;
	
	public ActionMode actionMode;

	public static Activity context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.activity_main);
		Resource.portrait = getResources().getBoolean(R.bool.portrait);
		if (savedInstanceState == null || Resource.lists.size() < 1)
			Resource.gatherListsFromSave(getPreferences(Context.MODE_PRIVATE));
		viewPager = (CustomViewPager) findViewById(R.id.pager);
		viewPager.setAdapter(sectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager()));
		if (savedInstanceState == null) {
			viewPager.setCurrentItem(getIntent().getIntExtra(
					Resource.SEND_CURRENT_PAGE, 0));
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
		public boolean onMenuItemActionExpand(MenuItem item) {
			editNameExpanded = true;
			EditText editText = (EditText) item.getActionView();
			editText.setText(Resource.lists.get(viewPager.getCurrentItem()).name);
			editText.requestFocus();
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
			setActionbarItemsVisibility(false);
			toggleEditNameUi(true);
			return true;
		}

		@Override
		public boolean onMenuItemActionCollapse(MenuItem item) {
			editNameExpanded = false;
			setActionbarItemsVisibility(true);
			toggleEditNameUi(false);
			doneButton.setVisible(false);
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(
					((EditText) item.getActionView()).getWindowToken(), 0);
			return true;
		}
	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_new_note:
			Intent intent = new Intent(this, DialogActivity.class);
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
			editName.collapseActionView();
			Resource.editListName(viewPager.getCurrentItem(),
					((EditText) editName.getActionView()).getText().toString());
			sectionsPagerAdapter.notifyDataSetChanged();
			return true;
		case R.id.menu_discard:
			editName.collapseActionView();
			if (((EditText) editName.getActionView()).getText().length() < 1
					&& Resource.lists.get(viewPager.getCurrentItem()).name
							.length() < 1) {
				Resource.deleteList(viewPager.getCurrentItem());
				sectionsPagerAdapter.notifyDataSetChanged();
			}
			return true;

		case R.id.menu_move_list:
			Resource.moveList(viewPager.getCurrentItem(), 0);
			sectionsPagerAdapter.notifyDataSetChanged();
			return true;
		case R.id.menu_add_content:
			int index = Resource
					.addList(Integer.toString((int) (Math.random() * 100)));
			for (int i = 0; i < 7; i++)
				Resource.addNote(
						index,
						new Note("",
								Integer.toString((int) (Math.random() * 100)),
								"", new Date()));
			Resource.applyNoteChanges();
			sectionsPagerAdapter.notifyDataSetChanged();
			viewPager.setCurrentItem(index, true);
			return true;
		default:
			return super.onOptionsItemSelected(item);
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
	
	public ActionMode.Callback actionModeCallback = new ActionMode.Callback() {

	    // Called when the action mode is created; startActionMode() was called
	    @Override
	    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
	        // Inflate a menu resource providing context menu items
	        MenuInflater inflater = mode.getMenuInflater();
	        inflater.inflate(R.menu.contextual_actionbar, menu);
	        return true;
	    }

	    // Called each time the action mode is shown. Always called after onCreateActionMode, but
	    // may be called multiple times if the mode is invalidated.
	    @Override
	    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
	        return false; // Return false if nothing is done
	    }

	    // Called when the user selects a contextual menu item
	    @Override
	    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
	        switch (item.getItemId()) {
	            case R.id.menu_remove_notes:
//	                shareCurrentItem();
	                mode.finish(); // Action picked, so close the CAB
	                return true;
	            default:
	                return false;
	        }
	    }

	    // Called when the user exits the action mode
	    @Override
	    public void onDestroyActionMode(ActionMode mode) {
	        actionMode = null;
	    }
	};
	
}