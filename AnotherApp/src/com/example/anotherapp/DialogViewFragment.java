package com.example.anotherapp;

import android.view.View;
import android.widget.TextView;

public class DialogViewFragment extends DialogFragment {

	// TODO fix this shit up (the image)
	View imageView;
	TextView dateView;
	TextView titleView;
	TextView noteView;

	@Override
	public void init(View rootView) {
		dateView = (TextView) rootView.findViewById(R.id.dialog_view_date);
		titleView = (TextView) rootView.findViewById(R.id.dialog_view_title);
		noteView = (TextView) rootView.findViewById(R.id.dialog_view_note);

		dateView.setText(note.generateDateString());
		titleView.setText(note.generateTitle());
		noteView.setText(note.note);
	}
}
