package com.example.anotherapp;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;


public class DialogViewFragment extends DialogFragment {

	// TODO fix this shit up (the image)
	private TextView dateView;
	private TextView titleView;
	private TextView noteView;
	
	private ImageButton actionBarEdit;
	private ImageButton actionBarOverflow;
	
	public Context context;

	@Override
	protected void init(View rootView, Bundle savedInstanceState, Note note) {
		context = getActivity();
		
		dateView = (TextView) rootView.findViewById(R.id.dialog_view_date);
		titleView = (TextView) rootView.findViewById(R.id.dialog_view_title);
		noteView = (TextView) rootView.findViewById(R.id.dialog_view_note);

		dateView.setText(note.generateDateString());
		titleView.setText(note.generateTitle());
		noteView.setText(note.note);
		
		actionBarOverflow = (ImageButton) rootView.findViewById(R.id.dialog_overflow);
		actionBarEdit = (ImageButton) rootView.findViewById(R.id.dialog_edit);
		
		actionBarOverflow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				PopupMenu popup = new PopupMenu(context, v);
				popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					
					@Override
					public boolean onMenuItemClick(MenuItem item) {
						switch (item.getItemId()) {
				        case R.id.dialog_popup_delete:
				        	Resource.deleteNote(listIndex, noteIndex);
							Resource.applyNoteChanges();
							getActivity().finish();
				            return true;
				            
				        case R.id.dialog_popup_share:
				        	//TODO share current note
				        	Resource.toast("SHARE");
				            return true;
				            
				        default:
				            return false;
						}
					}
				});
				
			    MenuInflater inflater = popup.getMenuInflater();
			    inflater.inflate(R.menu.dialog_popup_actions, popup.getMenu());
			    popup.show();
				
			}
		});
		
		actionBarEdit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				activity.changeFragment(getArguments());
			}
		});
	}

	@Override
	protected int getContentView() {
		return R.layout.fragment_dialog_view;
	}
}
