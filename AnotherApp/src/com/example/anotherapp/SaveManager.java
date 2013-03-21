package com.example.anotherapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import android.content.SharedPreferences;

//Thought to be sort of a library class, which can be added to any project
//Could extend SharedPreferences, but i like it more this way, might create conflicts with SharedPreferences functions and variables otherwise
public class SaveManager {
	private SharedPreferences.Editor editor;
	private HashMap<String, Object> saves = new HashMap<String, Object>();

	public SaveManager(SharedPreferences preference) {
		editor = preference.edit();
		Map<String, ?> all = preference.getAll();
		for (Entry<String, ?> entry : all.entrySet()) {
			saves.put(entry.getKey(), entry.getValue());
		}
	}

	public int getSize() {
		return saves.size();
	}

	public int getInt(String key, int defaultValue) {
		return saves.containsKey(key) && saves.get(key) instanceof Integer ? (Integer) saves
				.get(key) : defaultValue;
	}

	public int getInt(String key) {
		return getInt(key, 0);
	}
	
	public long getLong(String key, long defaultValue) {
		return saves.containsKey(key) && saves.get(key) instanceof Long ? (Long) saves
				.get(key) : defaultValue;
	}

	public long getLong(String key) {
		return getLong(key, 0);
	}

	public String getString(String key, String defaultValue) {
		return saves.containsKey(key) && saves.get(key) instanceof String ? (String) saves
				.get(key) : defaultValue;
	}

	public String getString(String key) {
		return getString(key, null);
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		return saves.containsKey(key) && saves.get(key) instanceof Boolean ? (Boolean) saves
				.get(key) : defaultValue;
	}

	public boolean getBoolean(String key) {
		return getBoolean(key, false);
	}

	public SaveManager save(String key, int value) {
		editor.putInt(key, value);
		saves.put(key, value);
		return this;
	}
	
	public SaveManager save(String key, long value) {
		editor.putLong(key, value);
		saves.put(key, value);
		return this;
	}

	public SaveManager save(String key, boolean value) {
		editor.putBoolean(key, value);
		saves.put(key, value);
		return this;
	}

	public SaveManager save(String key, String value) {
		editor.putString(key, value);
		saves.put(key, value);
		return this;
	}

	public void remove(String key) {
		editor.remove(key);
		saves.remove(key);
	}

	public void removeFromQuery(String query) {
		ArrayList<String> remove = new ArrayList<String>();
		for (Entry<String, Object> entry : saves.entrySet())
			if (entry.getKey().contains(query))
				remove.add(entry.getKey());
		for (String key : remove)
			saves.remove(key);
	}

	public HashMap<String, Object> getFractionFromQuery(String query) {
		HashMap<String, Object> newMap = new HashMap<String, Object>();
		for (Entry<String, Object> entry : saves.entrySet())
			if (entry.getKey().contains(query))
				newMap.put(entry.getKey(), entry.getValue());
		return newMap;
	}

	public int getFractionSizeFromQuery(String query) {
		int count = 0;
		for (Entry<String, Object> entry : saves.entrySet())
			if (entry.getKey().contains(query))
				count++;
		return count;
	}

	public void commit() {
		editor.commit();
	}

	public String toString() {
		StringBuilder sz = new StringBuilder("Amount of variales: "
				+ saves.size() + "\n");
		for (Entry<String, Object> entry : saves.entrySet())
			sz.append(entry.getKey() + ", " + entry.getValue() + "\n");
		return sz.toString();
	}

}