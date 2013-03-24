package com.example.anotherapp;

import java.text.DateFormat;
import java.util.Date;

public class Note {
	private final static int TITLE_WORD_COUNT = 2;
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

	public String generateTitle() {
		if (title.isEmpty()) {
			StringBuilder sz = new StringBuilder();
			String[] words = this.note.split("\n")[0].split(" ");
			for(int i = 0; i < words.length && i < TITLE_WORD_COUNT; i++)
				sz.append(words[i] + " ");
			return sz.toString();
		}
		return title;
	}

	public String generateDateString() {
		return DateFormat.getDateInstance().format(date).toUpperCase();
	}

	@Override
	public String toString() {
		return generateTitle() + "\n" + note + "\n" + image + "\n"
				+ generateDateString();
	}

}