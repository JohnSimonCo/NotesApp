package com.example.anotherapp;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NoteListAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<Note> notes;

	// private Context context;

	public NoteListAdapter(Context context, List<Note> notes) {
		// this.context = context;
		this.notes = notes;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return notes.size();
		// return
	}

	@Override
	public Object getItem(int pos) {
		return notes.get(pos);
	}

	@Override
	public long getItemId(int arg0) {
		// Won't be needed
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Note note = notes.get(position);
		NoteViewHolder holder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.note, parent, false);
			holder = new NoteViewHolder();
			// This is just for calling findViewById() and saving it to the
			// holder object

			holder.dateTextView = (TextView) convertView
					.findViewById(R.id.list_item_date);
			holder.titleTextView = (TextView) convertView
					.findViewById(R.id.list_item_title);
			holder.noteTextView = (TextView) convertView
					.findViewById(R.id.list_item_note);

			convertView.setTag(holder);
		} else {
			holder = (NoteViewHolder) convertView.getTag();
		}

		holder.dateTextView.setText(note.generateDateString());
		holder.titleTextView.setText(note.generateTitle());
		holder.noteTextView.setText(note.note);

		// This is where we'll do all the setup for the list item

		return convertView;
	}

	public class NoteViewHolder {
		// Just keep a list of all the views here, it'll give ~30% performence
		// boost
		private TextView dateTextView;
		private TextView titleTextView;
		private TextView noteTextView;
	}

}