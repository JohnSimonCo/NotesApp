package com.example.anotherapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class DialogFragment extends Fragment {
	public Note note;
	public boolean newNote;

	public int listIndex;
	public int noteIndex;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_dialog_view,
				container, false);
		listIndex = getArguments().getInt(Resource.SEND_LIST_INDEX);
		noteIndex = getArguments().getInt(Resource.SEND_NOTE_INDEX, -1);
		newNote = noteIndex < 0;
		note = !newNote ? Resource.getNote(listIndex, noteIndex)
				: new Note(null, null, null, null);
		init(rootView);
		return rootView;
	}

	public abstract void init(View rootView);
}
