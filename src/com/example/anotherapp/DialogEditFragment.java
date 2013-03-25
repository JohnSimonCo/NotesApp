package com.example.anotherapp;

import java.util.Date;

import com.example.anotherapp.R;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class DialogEditFragment extends DialogFragment {
	private EditText titleView;
	private EditText noteView;

	@Override
	protected void init(View rootView, Bundle savedInstanceState, Note note) {
		rootView.findViewById(R.id.dialog_dicard).setOnClickListener(
				onDiscardClick);
		rootView.findViewById(R.id.dialog_done).setOnClickListener(onDoneClick);

		titleView = (EditText) rootView.findViewById(R.id.dialog_edit_title);
		noteView = (EditText) rootView.findViewById(R.id.dialog_edit_note);
		noteView.requestFocus();
		titleView.setText(savedInstanceState == null ? note.title
				: savedInstanceState.getString(Resource.SEND_NOTE_TITLE));
		noteView.setText(savedInstanceState == null ? note.note
				: savedInstanceState.getString(Resource.SEND_NOTE_TEXT));
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(Resource.SEND_NOTE_TITLE, titleView.getText()
				.toString());
		outState.putString(Resource.SEND_NOTE_TEXT, noteView.getText()
				.toString());
	}

	public View.OnClickListener onDiscardClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (newNote) {
				activity.finish();
			} else {
				activity.changeFragment(getArguments());
			}
		}
	};

	public View.OnClickListener onDoneClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			String noteText = noteView.getText().toString();
			String titleText = titleView.getText().toString();
			if (noteText.isEmpty() && titleText.isEmpty())
				return;
			Note note = new Note(titleText, noteText, "", new Date());
			if (newNote) {
				newNote = false;
				noteIndex = Resource.addNote(listIndex, note);
				Resource.applyNoteChanges();
				activity.finish();
			} else {
				Resource.editNote(listIndex, noteIndex, note);
				Resource.applyNoteChanges();
				activity.changeFragment(getArguments());
			}
		}
	};

	@Override
	protected int getContentView() {
		return R.layout.fragment_dialog_edit;
	}

}
