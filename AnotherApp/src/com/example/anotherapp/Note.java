package com.example.anotherapp;

import java.util.Date;

public class Note {
	
	public String title, note, image;
	public Date date;

	public Note(String title, String note, String image, Date date) {
		this.title = title;
		this.note = note;
		this.image = image;
		this.date = date;
	}

}