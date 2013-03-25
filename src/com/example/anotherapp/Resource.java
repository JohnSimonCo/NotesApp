package com.example.anotherapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class Resource {
	private final static String prefix = "com.android.anotherapp.";
	public final static String SEND_NOTE_INDEX = prefix + "SEND_NOTE_INDEX";
	public final static String SEND_NOTE_TITLE = prefix + "SEND_NOTE_TITLE";
	public final static String SEND_NOTE_TEXT = prefix + "SEND_NOTE_TEXT";
	public final static String SEND_NOTE_IMAGE = prefix + "SEND_NOTE_IMAGE";
	public final static String SEND_NOTE_DATE = prefix + "SEND_NOTE_DATE";
	public final static String SEND_EDITED_NOTE = prefix + "SEND_EDITED_NOTE";
	public final static String SEND_DID_EDIT = prefix + "SEND_DID_EDIT";
	public final static String SEND_NOTE_ARRAYLIST = prefix
			+ "SEND_NOTE_ARRAYLIST";
	public final static String SEND_LIST_INDEX = prefix
			+ "SEND_SAVED_LIST_INDEX";
	public final static String SEND_CHANGED_LIST_INDEX = prefix
			+ "SEND_EDITED_LIST_INDEX";
	public final static String SEND_CURRENT_PAGE = prefix + "SEND_CURRENT_PAGE";

	public final static String SAVE_LIST = "SAVE_LIST";
	public final static String SAVE_LIST_NAME = "SAVE_LIST_NAME";
	public final static String SAVE_LIST_ID = "SAVE_LIST_ID";
	public final static String SAVE_NOTE_TITLE = "SAVE_NOTE_TITLE";
	public final static String SAVE_NOTE_TEXT = "SAVE_NOTE_TEXT";
	public final static String SAVE_NOTE_IMAGE = "SAVE_NOTE_IMAGE";
	public final static String SAVE_NOTE_DATE = "SAVE_NOTE_DATE";
	public final static String SAVE_NOTE_AMOUNT = "SAVE_NOTE_AMOUNT";
	public final static String SAVE_MAKE_ID = "SAVE_MAKE_ID";

	public final static String STRING_UNNAMED_LIST = "Unnamed list";
	public final static String STRING_ADD_LIST = "Add new list";

	public static boolean portrait;

	private static int tests = 0;
	private static SaveManager saveManager;
	public static ArrayList<NoteList> lists = new ArrayList<NoteList>();

	public static void gatherListsFromSave(SharedPreferences preference) {
		saveManager = new SaveManager(preference);
		System.out.println(saveManager);

		// Because MAKE_ID will always be present
		if (saveManager.getSize() <= 1)
			return;

		HashMap<String, Object> ids = saveManager
				.getFractionFromQuery(SAVE_LIST_ID);
		for (Entry<String, Object> entry : ids.entrySet()) {
			int id = (Integer) entry.getValue();
			String name = saveManager.getString(SAVE_LIST_NAME + id,
					STRING_UNNAMED_LIST);
			ArrayList<Note> notes = new ArrayList<Note>();
			int i = -1;
			int size = saveManager.getInt(SAVE_LIST + id + SAVE_NOTE_AMOUNT);
			while (++i < size) {
				notes.add(new Note(saveManager.getString(SAVE_LIST + id
						+ SAVE_NOTE_TITLE + i), saveManager.getString(SAVE_LIST
						+ id + SAVE_NOTE_TEXT + i), saveManager
						.getString(SAVE_LIST + id + SAVE_NOTE_IMAGE + i),
						new Date(saveManager.getLong(SAVE_LIST + id
								+ SAVE_NOTE_DATE + i))));
			}
			lists.add(new NoteList(name, id, notes));
		}

	}

	public static int addNote(int listIndex, Note newNote) {
		NoteList list = lists.get(listIndex);
		list.changed = true;
		list.notes.add(newNote);

		return list.notes.size() - 1;
	}

	public static void editNote(int listIndex, int noteIndex, Note newNote) {
		NoteList list = lists.get(listIndex);
		list.changed = true;
		list.notes.set(noteIndex, newNote);
	}

	// public static void editNoteTitle(int listIndex, int noteIndex,
	// String newTitle) {
	// editNote(listIndex, noteIndex).title = newTitle;
	// }
	//
	// public static void editNoteText(int listIndex, int noteIndex, String
	// newNote) {
	// editNote(listIndex, noteIndex).note = newNote;
	// }
	//
	// public static void editNoteImage(int listIndex, int noteIndex,
	// String newImage) {
	// editNote(listIndex, noteIndex).image = newImage;
	// }
	//
	// public static void editNoteDate(int listIndex, int noteIndex, Date
	// newDate) {
	// editNote(listIndex, noteIndex).date = newDate;
	// }
	//
	// private static Note editNote(int listIndex, int noteIndex) {
	// NoteList list = lists.get(listIndex);
	// list.changed = true;
	// return list.notes.get(noteIndex);
	// }

	public static int moveNote(int oldListIndex, int oldNoteIndex,
			int newListIndex) {
		lists.get(oldListIndex).changed = true;
		lists.get(newListIndex).changed = true;

		Note note = lists.get(oldListIndex).notes.get(oldNoteIndex);
		lists.get(oldListIndex).notes.remove(oldNoteIndex);
		lists.get(newListIndex).notes.add(note);

		return lists.get(newListIndex).notes.size() - 1;
	}

	public static void deleteNote(int listIndex, int noteIndex) {
		NoteList list = lists.get(listIndex);
		list.changed = true;
		list.notes.remove(noteIndex);
	}

	public static void deleteNotes(int[] listIndexes, int[][] noteIndexes) {
		int i = -1;
		while (++i < listIndexes.length) {
			int listIndex = listIndexes[i];
			lists.get(listIndex).changed = true;
			for (int c = 0; c < noteIndexes[i].length; c++) {
				lists.get(listIndex).notes.remove(noteIndexes[i][c]);
			}
		}
	}

	public static Note getNote(int listIndex, int noteIndex) {
		return lists.get(listIndex).notes.get(noteIndex);
	}

	public static int addList(String name) {
		int listIndex = lists.size();
		int id = saveManager.getInt(SAVE_MAKE_ID, 0);
		lists.add(new NoteList(name, id, new ArrayList<Note>()));

		saveManager.save(SAVE_LIST_ID + listIndex, id)
				.save(SAVE_LIST_NAME + id, name).save(SAVE_MAKE_ID, id + 1)
				.commit();

		return listIndex;
	}

	public static void editListName(int index, String newValue) {
		lists.get(index).name = newValue;
		saveManager.save(SAVE_LIST_NAME + lists.get(index).id, newValue)
				.commit();
	}

	public static void moveList(int oldListIndex, int newListIndex) {
		int direction = oldListIndex < newListIndex ? 1 : -1;
		int i = -1;
		while (++i < Math.abs(oldListIndex - newListIndex)) {
			Collections.swap(lists, i * direction + oldListIndex, i * direction
					+ direction + oldListIndex);
		}
		i = -1;
		while (++i < lists.size())
			saveManager.save(SAVE_LIST_ID + i, lists.get(i).id);
		saveManager.commit();
	}

	public static void deleteList(int index) {
		int id = lists.get(index).id;
		lists.remove(index);
		int listAmount = lists.size();

		// remove list
		saveManager.removeFromQuery(SAVE_LIST + id);
		saveManager.remove(SAVE_LIST_NAME + id);
		saveManager.remove(SAVE_LIST_ID + listAmount);
		if (listAmount > 0) {
			// rewrite ids
			int c = -1;
			int i = -1;
			while (++c < listAmount) {
				i++;
				int oldId = saveManager.getInt(SAVE_LIST_ID + c, -1);
				if (oldId != id)
					saveManager.save(SAVE_LIST_ID + i, oldId);
			}
		}
		saveManager.commit();
		System.out.println(saveManager);
	}

	public static void applyNoteChanges() {
		for (NoteList list : lists) {
			if (list.changed) {
				saveManager.removeFromQuery(SAVE_LIST + list.id);
				for (int i = 0; i < list.notes.size(); i++) {
					String title = list.notes.get(i).title;
					String note = list.notes.get(i).note;
					String image = list.notes.get(i).image;
					long date = list.notes.get(i).date.getTime();
					if (!title.isEmpty())
						saveManager.save(SAVE_LIST + list.id + SAVE_NOTE_TITLE
								+ i, title);
					if (!note.isEmpty())
						saveManager.save(SAVE_LIST + list.id + SAVE_NOTE_TEXT
								+ i, note);
					if (!image.isEmpty())
						saveManager.save(SAVE_LIST + list.id + SAVE_NOTE_IMAGE
								+ i, image);
					if (date > 0)
						saveManager.save(SAVE_LIST + list.id + SAVE_NOTE_DATE
								+ i, date);
				}
				saveManager.save(SAVE_LIST + list.id + SAVE_NOTE_AMOUNT,
						list.notes.size());
			}
		}
		saveManager.commit();
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