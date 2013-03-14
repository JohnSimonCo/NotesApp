package com.example.anotherapp;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class Resource {
	private final static String prefix = "com.android.anotherapp.";
	public final static String SEND_NOTE_INDEX = prefix + "SEND_NOTE_INDEX";
	public final static String SEND_EDITED_NOTE = prefix + "SEND_EDITED_NOTE";
	public final static String SEND_DID_EDIT = prefix + "SEND_DID_EDIT";
	public final static String SEND_NOTE_ARRAYLIST = prefix
			+ "SEND_NOTE_ARRAYLIST";
	public final static String SEND_LIST_INDEX = prefix + "SEND_SAVED_LIST_INDEX";
	public final static String SEND_CHANGED_LIST_INDEX = prefix + "SEND_EDITED_LIST_INDEX";
	public final static String SEND_CURRENT_PAGE = prefix + "SEND_CURRENT_PAGE";

	private static int tests = 0;
	private static SharedPreferences saves;
	public static ArrayList<NoteList> lists = new ArrayList<NoteList>();

	public final static String SAVE_LIST = prefix + "SAVE_LIST_NUMBER";
	public final static String SAVE_LIST_AMOUNT = prefix + "SAVE_LIST_AMOUNT";
	public final static String SAVE_LIST_NAME = prefix + "SAVE_LIST_NAME";
	public final static String SAVE_LIST_ID = prefix + "SAVE_LIST_ID";
	public final static String SAVE_NOTE_AMOUNT = prefix + "SAVE_NOTE_AMOUNT";
	public final static String SAVE_NOTE_TEXT = prefix + "SAVE_NOTE_TEXT";
	public final static String SAVE_MAKE_ID = prefix + "SAVE_MAKE_ID";
	
	public final static String STRING_UNNAMED_LIST = "Unnamed list";
	public final static String STRING_ADD_LIST = "Add new list";

	public static void gatherListsFromSave(SharedPreferences preferences) {
		saves = preferences;
		printSavedVariables();

		int listAmount = saves.getInt(SAVE_LIST_AMOUNT, 0);
		if (listAmount == 0)
			return;

		int pc = -1;
		int[] ids = new int[listAmount];
		while (++pc < listAmount) {
			ids[pc] = saves.getInt(SAVE_LIST_ID + pc, -1);
		}

		int c = -1;
		while (++c < ids.length) {
			int id = ids[c];
			ArrayList<String> notes = new ArrayList<String>();
			String name = saves.getString(SAVE_LIST_NAME + id, "No Name");
			int noteAmount = saves.getInt(SAVE_LIST + id + SAVE_NOTE_AMOUNT, 0);
			int nc = -1;
			while (++nc < noteAmount) {
				notes.add(saves.getString(SAVE_LIST + id + SAVE_NOTE_TEXT + nc,
						"No Note"));
			}
			while (lists.size() <= c)
				lists.add(null);
			lists.set(c, new NoteList(name, id, notes));
		}
	}

	public static int addNote(int listIndex, String note) {
		NoteList list = lists.get(listIndex);
		list.hasChanged = true;
		list.notes.add(note);

		return list.notes.size() - 1;
	}

	public static void editNoteText(int listIndex, int noteIndex, String newValue) {
		NoteList list = lists.get(listIndex);
		list.hasChanged = true;
		list.notes.set(noteIndex, newValue);
	}
	
	public static int moveNote(int oldListIndex, int oldNoteIndex, int newListIndex) {
		lists.get(oldListIndex).hasChanged = true;
		lists.get(newListIndex).hasChanged = true;
		
		String note = lists.get(oldListIndex).notes.get(oldNoteIndex);
		lists.get(oldListIndex).notes.remove(oldNoteIndex);
		lists.get(newListIndex).notes.add(note);
		
		return lists.get(newListIndex).notes.size() - 1;
	}
	
	public static void deleteNote(int listIndex, int noteIndex) {
		NoteList list = lists.get(listIndex);
		list.hasChanged = true;
		list.notes.remove(noteIndex);
	}
	
	public static void deleteNotes(int[] listIndexes, int[][] noteIndexes) {
		int i = -1;
		while(++i < listIndexes.length) {
			int listIndex = listIndexes[i];
			lists.get(listIndex).hasChanged = true;
			for(int c = 0; c < noteIndexes[i].length; c++) {
				lists.get(listIndex).notes.remove(noteIndexes[i][c]);
			}
		}
	}

	public static String getNote(int listIndex, int noteIndex) {
		return lists.get(listIndex).notes.get(noteIndex);
	}
	
	public static int addList() {
		return addList("");
	}

	public static int addList(String name) {
		int listIndex = lists.size();
		System.out.println(saves == null);
		int id = saves.getInt(SAVE_MAKE_ID, 0);
		lists.add(new NoteList(name, id, new ArrayList<String>()));

		saves.edit().putInt(SAVE_LIST_AMOUNT, listIndex + 1)
				.putInt(SAVE_LIST_ID + listIndex, id)
				.putString(SAVE_LIST_NAME + id, name)
				.putInt(SAVE_MAKE_ID, id + 1).commit();

		return listIndex;
	}

	public static void editListName(int index, String newValue) {
		lists.get(index).name = newValue;

		saves.edit().putString(SAVE_LIST_NAME + lists.get(index).id, newValue)
				.commit();
	}

	public static void deleteList(int index) {
		int id = lists.get(index).id;
		lists.remove(index);

		int listAmount = lists.size();
		int oldListAmount = listAmount + 1;
		SharedPreferences.Editor editor = saves.edit();
		editor.putInt(SAVE_LIST_AMOUNT, listAmount);

		// remove list
		int noteAmount = saves.getInt(SAVE_LIST + id + SAVE_NOTE_AMOUNT, 0);
		editor.remove(SAVE_LIST_NAME + id).remove(
				SAVE_LIST + id + SAVE_NOTE_AMOUNT);

		int i = -1;
		while (++i < noteAmount) {
			editor.remove(SAVE_LIST + id + SAVE_NOTE_TEXT + i);
		}

		editor.remove(SAVE_LIST_ID + listAmount);
		if (listAmount > 0) {
			// read old ids
			ArrayList<Integer> ids = new ArrayList<Integer>();
			i = -1;
			while (++i < oldListAmount) {
				int indexId = saves.getInt(SAVE_LIST_ID + i, 0);
				if (indexId != id)
					ids.add(indexId);
			}

			// rewrite ids
			i = -1;
			while (++i < ids.size()) {
				editor.putInt(SAVE_LIST_ID + i, ids.get(i));
			}
		}

		editor.commit();
		printSavedVariables();
	}

	public static void applyNoteChanges() {
		for (NoteList list : lists) {
			if (list.hasChanged) {
				SharedPreferences.Editor editor = saves.edit();
				int oldNoteAmount = saves.getInt(SAVE_LIST + list.id
						+ SAVE_NOTE_AMOUNT, 0);
				int newNoteAmount = list.notes.size();
				if (oldNoteAmount > newNoteAmount) {
					int c = newNoteAmount - 1;
					while (++c < oldNoteAmount) {
						editor.remove(SAVE_LIST + list.id + SAVE_NOTE_TEXT + c);
					}
				}

				int i = -1;
				while (++i < newNoteAmount) {
					editor.putString(SAVE_LIST + list.id + SAVE_NOTE_TEXT + i,
							list.notes.get(i));
				}
				editor.putInt(SAVE_LIST + list.id + SAVE_NOTE_AMOUNT,
						newNoteAmount);

				editor.commit();
			}
		}
	}

	public static void printSavedVariables() {
		ArrayList<Integer> ints = new ArrayList<Integer>();
		ArrayList<String> strings = new ArrayList<String>();
		ArrayList<String> intKeys = new ArrayList<String>();
		ArrayList<String> keys = new ArrayList<String>();
		Map<String, ?> all = saves.getAll();
		for (Entry<String, ?> x : all.entrySet()) {
			if (x.getValue().getClass().equals(Integer.class)) {
				ints.add((Integer) x.getValue());
				intKeys.add(x.getKey());
			} else if (x.getValue().getClass().equals(String.class)) {
				strings.add((String) x.getValue());
				keys.add(x.getKey());
			}
		}
		System.out.println("Amount of variales: "
				+ (ints.size() + strings.size()));
		int i = -1;
		while (++i < ints.size()) {
			System.out.println(ints.get(i));
			System.out.println(intKeys.get(i));
		}
		i = -1;
		while (++i < strings.size()) {
			System.out.println(strings.get(i));
			System.out.println(keys.get(i));
		}
	}

	public Resource() {
	}

	public static void toast(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	public static void toast(String text) {
		toast(MainActivity.context, text);
	}

	public static void toast(Context context, Boolean bool) {
		toast(context, Boolean.toString(bool));
	}

	public static void toast(Boolean bool) {
		toast(MainActivity.context, Boolean.toString(bool));
	}

	public static void toast(Context context, int i) {
		toast(context, Integer.toString(i));
	}

	public static void toast(int i) {
		toast(MainActivity.context, Integer.toString(i));
	}

	public static void toast(Context context) {
		toast(context, "test" + ++tests);
	}

	public static void toast() {
		toast(MainActivity.context, "test" + ++tests);
	}
}