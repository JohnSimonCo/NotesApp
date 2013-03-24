package com.example.anotherapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class DialogActivity extends Activity {
	public static boolean EDIT_MODE = true;
	public static boolean VIEW_MODE = false;

	public int list;
	public DialogFragment fragment;
	public boolean mode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note_detail);
		list = getIntent().getIntExtra(Resource.SEND_LIST_INDEX, -1);
		if (savedInstanceState == null) {
			selectFragment(getIntent()
					.getIntExtra(Resource.SEND_NOTE_INDEX, -1) < 0, getIntent()
					.getExtras());
			getFragmentManager().beginTransaction()
					.replace(R.id.fragment_container, fragment).commit();
		} else
			fragment = (DialogFragment) getFragmentManager().findFragmentById(
					R.id.fragment_container);
	}

	public void selectFragment(boolean mode, Bundle bundle) {
		fragment = mode == EDIT_MODE ? new DialogEditFragment()
				: new DialogViewFragment();
		fragment.setArguments(bundle);
	}

	public void changeFragment(Bundle bundle) {
		selectFragment(mode = !mode, bundle);
		// Should animate and shits
		getFragmentManager().beginTransaction()
				.replace(R.id.fragment_container, fragment).commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_note_dialog, menu);
		return true;
	}

}
