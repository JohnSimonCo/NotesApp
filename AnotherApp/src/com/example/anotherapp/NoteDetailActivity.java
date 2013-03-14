package com.example.anotherapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.MenuItem;

public class NoteDetailActivity extends FragmentActivity {

	public int list;
	public NoteDetailFragment fragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note_detail);
		// the page just happens to be the same as SEND_NOTE_LIST_INDEX and
		// therefore only one is sent
		list = getIntent().getIntExtra(Resource.SEND_LIST_INDEX, -1);
		if (savedInstanceState == null) {
			fragment = new NoteDetailFragment();
			fragment.setArguments(getIntent().getExtras());
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.fragment_container, fragment).commit();
		} else
			fragment = (NoteDetailFragment) getSupportFragmentManager()
					.findFragmentById(R.id.fragment_container);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			fragment.saveCurrentNote();
			returnToMainActivity();
			return true;
		default:
			return false;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			fragment.saveCurrentNote();
			returnToMainActivity();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	public void returnToMainActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra(Resource.SEND_CURRENT_PAGE, fragment.listIndex);
		startActivity(intent);
	}
}
