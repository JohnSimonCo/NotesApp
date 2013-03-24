package com.example.anotherapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class NoteListFragment extends Fragment {
	// public ArrayList<Note> notes = new ArrayList<Note>();
	public int index;

	public ListView listView;
	public NoteListAdapter adapter;
	public Spinner spinner;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		index = getArguments().getInt(Resource.SEND_LIST_INDEX);
		// notes =
		// getArguments().getStringArrayList(Resource.SEND_NOTE_ARRAYLIST);
		setHasOptionsMenu(true);
		return inflater.inflate(R.layout.fragment_note_list, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		listView = (ListView) view.findViewById(R.id.adapter_view);
		// loadData();

		// Fixing top and bottom padding of list items which are not included in
		// invisible dividers. Using invisible header views, only minor
		// performence hit
		
		TextView v = new TextView(getActivity());
		v.setHeight(8);
		
		listView.addHeaderView(v);
		listView.addFooterView(v);

		adapter = new NoteListAdapter(getActivity(),
				Resource.lists.get(index).notes);
		listView.setAdapter(adapter);
		listView.setEmptyView(view.findViewById(R.id.empty));

		listView.setOnItemClickListener(onItemClick);
		listView.setOnItemLongClickListener(onLongClick);

		// getListView().setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
	}

	private OnItemClickListener onItemClick = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			Intent intent = new Intent(getActivity(), DialogActivity.class);
			intent.putExtra(Resource.SEND_NOTE_INDEX, position);
			intent.putExtra(Resource.SEND_LIST_INDEX, index);
			startActivity(intent);
		}
	};

	AdapterView.OnItemLongClickListener onLongClick = new AdapterView.OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> adapterView, View view,
				int i, long l) {
			return false;
		}
	};

}
