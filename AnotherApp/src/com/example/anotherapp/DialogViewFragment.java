package com.example.anotherapp;

import android.view.View;
import android.widget.TextView;

public class DialogViewFragment extends DialogFragment {

	// TODO fix this shit up (the image)
	View image;
	TextView dateView;
	TextView titleView;
	TextView noteView;

	@Override
	public void init(View rootView) {
		dateView = (TextView) rootView.findViewById(R.id.dialog_view_date);
		titleView = (TextView) rootView.findViewById(R.id.dialog_view_title);
		noteView = (TextView) rootView.findViewById(R.id.dialog_view_note);

		String note = getArguments().getString(Resource.SEND_NOTE_TEXT);
		dateView.setText(getArguments().getString(Resource.SEND_NOTE_DATE));
		titleView.setText(Resource.createTitle(
				getArguments().getString(Resource.SEND_NOTE_TITLE), note));
		noteView.setText(note);
	}
}
