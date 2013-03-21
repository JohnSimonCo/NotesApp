package com.example.anotherapp;

import java.util.ArrayList;

public class NoteList {
	public String name;
	public int id;
	//TODO some kind of date
	public ArrayList<Note> notes = new ArrayList<Note>();
	public boolean changed = false;
	
	public NoteList(String name, int id, ArrayList<Note> notes) {
		this.name = name;
		this.id = id;
		this.notes = notes;
	}
	@Override
	public String toString() {
		return name + ", " + id + ", " + notes.size();
	}
	
}
