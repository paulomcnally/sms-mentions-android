package com.mcnallydevelopers.android.apps.smsmentions.tools;

import android.database.Cursor;

import com.mcnallydevelopers.android.apps.smsmentions.database.Database;

public class Friends {

	private Database db = null;

	private Cursor rows = null;

	public Friends(Database database) {
		super();

		this.db = database;
	}

	public void getAll() {
		this.rows = db.getAllFriends();
	}

	public int getCount() {
		if (this.rows == null) {
			this.getAll();
		}
		return this.rows.getCount();
	}

	public Cursor getRows() {
		if (this.rows == null) {
			this.getAll();
		}
		return this.rows;
	}

}
