package com.example.anotherapp;

import java.util.ArrayList;

public class NoteList {
	public String name;
	public int id;
	public ArrayList<String> notes = new ArrayList<String>();
	public boolean hasChanged = false;
	
	public NoteList(String name, int id, ArrayList<String> notes) {
		this.name = name;
		this.id = id;
		this.notes = notes;
	}
	@Override
	public String toString() {
		return name + " " + id + " " + notes.get(0) + " " + notes.size();
	}
}
