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
			boolean spaceBefore = false;
			int words = 0;
			StringBuilder sz = new StringBuilder();
			for (char c : note.toCharArray()) {
				if (c == '\r' || c == '\n')
					break;
				if (spaceBefore && c != ' ') {
					spaceBefore = false;
					if (++words >= TITLE_WORD_COUNT)
						break;
				} else if (!spaceBefore)
					sz.append(c);
				if (c == ' ') {
					spaceBefore = true;
				}
			}
			return sz.toString();
		}
		return title;
	}

	public String generateDateString() {
		return DateFormat.getDateInstance().format(date);
	}

	@Override
	public String toString() {
		return generateTitle() + "\n" + note + "\n" + image + "\n"
				+ generateDateString();
	}

}