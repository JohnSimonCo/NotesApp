package com.example.anotherapp;

import java.util.Date;

public class Note {
	
	public String title, note, image;
	public Date date;

	public Note(String title, String note, String image, Date date) {
		set(title, note, image, date);
	}
	
	public void set(String title, String note, String image, Date date) {
		this.title = title;
		this.note = note;
		this.image = image;
		this.date = date;
	}
	
	@Override
	public String toString() {
		return title + "\n" + note + "\n" + image + "\n" + date.toString();
	}

}