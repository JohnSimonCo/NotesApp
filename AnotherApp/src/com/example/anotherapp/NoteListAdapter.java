package com.example.anotherapp;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NoteListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<Note> mNotes;
	public static NoteViewHolder holder;
	
	private Context context;

	public NoteListAdapter(Context ctx) {
		context = ctx;
		mInflater = LayoutInflater.from(context);
	}

	public void setListItems(List<Note> noteList) {
		mNotes = noteList;
	}

	@Override
	public int getCount() {
		return mNotes.size();
	}

	@Override
	public Object getItem(int pos) {
		return mNotes.get(pos);
	}

	@Override
	public long getItemId(int arg0) {
		// Won't be needed
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Note currentNote = mNotes.get(position);

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.note, parent, false);

			holder = new NoteViewHolder();

			// This is just for calling findViewById() and saving it to the
			// holder object

			holder.titleTextView = (TextView) convertView
					.findViewById(R.id.list_item_title);

			convertView.setTag(holder);
		} else {
			holder = (NoteViewHolder) convertView.getTag();
		}

		holder.titleTextView.setText(currentNote.getTitle());

		// This is where we'll do all the setup for the list item

		return convertView;
	}

	public class NoteViewHolder {
		// Just keep a list of all the views here, it'll give ~30% performence
		// boost
		private TextView titleTextView;
	}

}