package com.example.anotherapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class DialogViewFragment extends DialogFragment {

	// TODO fix this shit up (the image)
	private TextView dateView;
	private TextView titleView;
	private TextView noteView;

	@Override
	protected void init(View rootView, Bundle savedInstanceState, Note note) {
		dateView = (TextView) rootView.findViewById(R.id.dialog_view_date);
		titleView = (TextView) rootView.findViewById(R.id.dialog_view_title);
		noteView = (TextView) rootView.findViewById(R.id.dialog_view_note);
		((Button) rootView.findViewById(R.id.dialog_edit)).setOnClickListener(onEditClick);
		((Button) rootView.findViewById(R.id.dialog_delete)).setOnClickListener(onDeleteClick);

		dateView.setText(note.generateDateString());
		titleView.setText(note.generateTitle());
		noteView.setText(note.note);
	}
	
	public View.OnClickListener onEditClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			activity.changeFragment(getArguments());
		}
	};
	
	public View.OnClickListener onDeleteClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Resource.deleteNote(listIndex, noteIndex);
			Resource.applyNoteChanges();
			getActivity().finish();
		}
	};

	@Override
	protected int getContentView() {
		return R.layout.fragment_dialog_view;
	}
}
