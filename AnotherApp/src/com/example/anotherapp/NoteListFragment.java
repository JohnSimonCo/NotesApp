package com.example.anotherapp;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

public class NoteListFragment extends ListFragment {
	public ArrayList<Note> notes = new ArrayList<Note>();
	public int index;

	public ListView listView;
	public NoteListAdapter adapter;
	public Spinner spinner;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		index = getArguments().getInt(Resource.SEND_LIST_INDEX);
	//	notes = getArguments().getStringArrayList(Resource.SEND_NOTE_ARRAYLIST);
		setHasOptionsMenu(true);
		return inflater.inflate(R.layout.fragment_note_list, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		loadData();
		
		adapter = new NoteListAdapter(getActivity());
		adapter.setListItems(notes);
		
		setListAdapter(adapter);
		
		getListView().setOnItemClickListener(onItemClick);
		getListView().setOnItemLongClickListener(onLongClick);
		
		
		
//		getListView().setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
	}

	private OnItemClickListener onItemClick = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			
			//TODO Spring clean
			
//			if(((MainActivity)getActivity()).actionMode == null) {
//				Intent intent = new Intent(getActivity(), NoteDetailActivity.class);
//				intent.putExtra(Resource.SEND_NOTE_INDEX, position);
//				intent.putExtra(Resource.SEND_LIST_INDEX, index);
//				startActivity(intent);
			// } else {
			// v.setSelected(!v.isSelected());
			// }
		}
	};
	
	public void loadData() {
		//TODO Follow this example but make it work
		for (int i = 0; i < 5; i++) {
			Note n = new Note("Hej");
			notes.add(n);
		}
	}

	AdapterView.OnItemLongClickListener onLongClick = new AdapterView.OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> adapterView, View view,
				int i, long l) {
//			MainActivity parent = (MainActivity)getActivity();
//			if (parent.actionMode != null) {
//				return false;
//			}
//
//			parent.actionMode = getActivity().startActionMode(parent.actionModeCallback);
//			view.setSelected(true);
//			return true;
			return false;
		}
	};
	
}
