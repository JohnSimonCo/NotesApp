package com.example.anotherapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class DialogFragment extends Fragment {
	protected boolean newNote;
	protected int listIndex;
	protected int noteIndex;
	private View imageView;

	protected DialogActivity activity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(getContentView(), container, false);
		activity = (DialogActivity) getActivity();
		listIndex = getArguments().getInt(Resource.SEND_LIST_INDEX);
		noteIndex = getArguments().getInt(Resource.SEND_NOTE_INDEX, -1);
		newNote = noteIndex < 0;
		imageView = rootView.findViewById(R.id.dialog_view_image);
		init(rootView, savedInstanceState,
				!newNote ? Resource.getNote(listIndex, noteIndex) : new Note(
						null, null, null, null));
		return rootView;
	}
	protected abstract void init(View rootView, Bundle savedInstanceState,
			Note note);
	protected abstract int getContentView();
}