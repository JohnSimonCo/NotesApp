package com.example.anotherapp;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class NoteDetailFragment extends Fragment {
	public int listIndex;
	public int changedListIndex;
	public int noteIndex;

	public EditText noteView;
	public View editUi;
	public View focusDummy;
	public Button doneButton;
	public Button discardButton;
	public String savedNote;
	private boolean editing = false;

	public Spinner spinner;
	public ArrayList<String> listNames = new ArrayList<String>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_note_detail,
				container, false);
		initViews(rootView);
		setHasOptionsMenu(true);

		listIndex = getArguments().getInt(Resource.SEND_LIST_INDEX);
		changedListIndex = listIndex;
		noteIndex = getArguments().getInt(Resource.SEND_NOTE_INDEX, -1);
		savedNote = (noteIndex == -1) ? null : Resource.getNote(listIndex,
				noteIndex).note;

		String editedNote = savedNote;
		int changedListIndex = listIndex;
		boolean didEdit = false;
		if (savedInstanceState != null) {
			editedNote = savedInstanceState.getString(
					Resource.SEND_EDITED_NOTE, savedNote);
			didEdit = savedInstanceState.getBoolean(Resource.SEND_DID_EDIT,
					false);
			changedListIndex = savedInstanceState.getInt(
					Resource.SEND_CHANGED_LIST_INDEX, listIndex);
		}
		try {
			spinner.setSelection(changedListIndex);
		} catch (Exception e) {
			Resource.toast("NoteDetailFragmentWTF");
			e.printStackTrace();
		}

		if (editedNote == null || didEdit) {
			noteView.requestFocus();
			// Open keyboard

			noteView.postDelayed(new Runnable() {

				public void run() {
					// ((EditText) findViewById(R.id.et_find)).requestFocus();
					//
					// InputMethodManager imm = (InputMethodManager)
					// getSystemService(Context.INPUT_METHOD_SERVICE);
					// imm.showSoftInput(yourEditText,
					// InputMethodManager.SHOW_IMPLICIT);

					noteView.dispatchTouchEvent(MotionEvent.obtain(
							SystemClock.uptimeMillis(),
							SystemClock.uptimeMillis(),
							MotionEvent.ACTION_DOWN, 0, 0, 0));
					noteView.dispatchTouchEvent(MotionEvent.obtain(
							SystemClock.uptimeMillis(),
							SystemClock.uptimeMillis(), MotionEvent.ACTION_UP,
							0, 0, 0));

				}
			}, 100);

			// InputMethodManager imm = (InputMethodManager) getActivity()
			// .getSystemService(Context.INPUT_METHOD_SERVICE);
			// imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
		} else if (editedNote != null) {
			noteView.setText(editedNote);
		}
		return rootView;
	}

	private void initViews(View rootView) {
		noteView = (EditText) rootView.findViewById(R.id.note);
		doneButton = (Button) rootView.findViewById(R.id.done);
		discardButton = (Button) rootView.findViewById(R.id.discard);
		focusDummy = rootView.findViewById(R.id.dummy);
		editUi = rootView.findViewById(R.id.edit_ui);
		noteView.setOnFocusChangeListener(onNoteFocusChange);
		doneButton.setOnClickListener(onDoneClick);
		discardButton.setOnClickListener(onDiscardClick);
	}

	public OnFocusChangeListener onNoteFocusChange = new OnFocusChangeListener() {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				startEditing();
			}
		}
	};

	public View.OnClickListener onDoneClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			saveCurrentNote();
			stopEditing();
		}
	};

	public void saveCurrentNote() {
		String note = noteView.getText().toString();
		if (!note.equals("")) {
			if (noteIndex == -1) {
				noteIndex = Resource.addNote(changedListIndex, "Title", note,
						"Image", new Date());
			} else {
				Resource.editNoteText(listIndex, noteIndex, note);
				if (listIndex != changedListIndex) {
					noteIndex = Resource.moveNote(listIndex, noteIndex,
							changedListIndex);
				}
			}
			listIndex = changedListIndex;
			savedNote = note;
		}
	}

	public View.OnClickListener onDiscardClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (savedNote == null) {
				Activity holder = getActivity();
				if (holder instanceof NoteDetailActivity)
					((NoteDetailActivity) holder).returnToMainActivity();
			} else {
				noteView.setText(savedNote);
				stopEditing();
			}
		}
	};

	public void startEditing() {
		editing = true;
		editUi.setVisibility(View.VISIBLE);
	}

	public void stopEditing() {
		editing = false;
		focusDummy.requestFocus();
		editUi.setVisibility(View.GONE);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_note_detail, menu);
		View view = menu.findItem(R.id.menu_list_spinner).getActionView();
		if (view instanceof Spinner) {
			spinner = (Spinner) view;
			listNames = new ArrayList<String>();
			for (NoteList nl : Resource.lists) {
				listNames.add(nl.name);
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					getActivity(), R.layout.spinner_item, listNames);
			adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
			spinner.setAdapter(adapter);
			spinner.setOnItemSelectedListener(onSpinnerClick);
		}

	}

	OnItemSelectedListener onSpinnerClick = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> av, View v1, int i, long l) {
			changedListIndex = i;
		}

		@Override
		public void onNothingSelected(AdapterView<?> av) {
		}
	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_remove_note:
			if (noteIndex >= 0)
				Resource.deleteNote(listIndex, noteIndex);
			Activity holder = getActivity();
			if (holder instanceof NoteDetailActivity)
				((NoteDetailActivity) holder).returnToMainActivity();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getArguments().putInt(Resource.SEND_NOTE_INDEX, noteIndex);
		if (listIndex != changedListIndex)
			outState.putInt(Resource.SEND_CHANGED_LIST_INDEX, changedListIndex);
		if (editing) {
			outState.putBoolean(Resource.SEND_DID_EDIT, editing);
			outState.putString(Resource.SEND_EDITED_NOTE, noteView.getText()
					.toString());
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		Resource.applyNoteChanges();
		// Remove keyboard to prevent bug next onCreateView
		if (editing) {
//			InputMethodManager imm = (InputMethodManager) getActivity()
//					.getSystemService(Context.INPUT_METHOD_SERVICE);
//			imm.hideSoftInputFromWindow(noteView.getWindowToken(), 0);
		}
	}
}
