package com.example.anotherapp;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import android.content.SharedPreferences;

public class SaveManager {
	private SharedPreferences saves;
	private ArrayList<String> keys = new ArrayList<String>();

	public SaveManager(SharedPreferences preference) {
		saves = preference;
		Map<String, ?> all = saves.getAll();
		for (Entry<String, ?> x : all.entrySet()) {
			keys.add(x.getKey());
		}
	}

	public int get(String key, int defValue) {
		return saves.getInt(key, defValue);
	}

	public String get(String key, String defValue) {
		return saves.getString(key, defValue);
	}

	public boolean get(String key, boolean defValue) {
		return saves.getBoolean(key, defValue);
	}

	public void commit() {
		saves.edit().commit();
	}

	public SaveManager save(String key, int value) {
		saves.edit().putInt(key, value).commit();
		keys.add(key);
		return this;
	}

	public SaveManager save(String key, boolean value) {
		saves.edit().putBoolean(key, value).commit();
		keys.add(key);
		return this;
	}

	public SaveManager save(String key, String value) {
		saves.edit().putString(key, value).commit();
		keys.add(key);
		return this;
	}

	public void remove(String key) {
		saves.edit().remove(key);
		keys.remove(key);
	}

	public void removeIfContains(String query) {
		for (String key : keys) {
			if (key.contains(query))
				remove(key);
		}
	}

	public void print() {
		Map<String, ?> all = saves.getAll();
		int i = -1;
		for (Entry<String, ?> x : all.entrySet()) {
			System.out.println(keys.get(++i) + ", " + x.getValue());
		}
		System.out.println("Amount of variales: " + i);
	}

}