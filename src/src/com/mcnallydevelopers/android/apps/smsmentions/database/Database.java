package com.mcnallydevelopers.android.apps.smsmentions.database;

import java.util.Date;

import com.mcnallydevelopers.android.apps.smsmentions.database.DatabaseHelper;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class Database extends Activity {
	public static String log = "Database";
	public Context myContext;
	public DatabaseHelper databaseHelper;
	public SQLiteDatabase sqlite = null;

	public static final int DB_WRITE_AND_READ = 1;
	public static final int DB_READ = 2;

	public Database(Context context) {
		super();
		myContext = context;

		databaseHelper = new DatabaseHelper(myContext);

		databaseHelper.initializeDataBase();

		connect(DB_READ);

	}

	/*
	 * connect
	 * 
	 * @description Open connection
	 * 
	 * @return none
	 */
	public void connect(int type) {

		try {
			switch (type) {

			case DB_WRITE_AND_READ:
				sqlite = databaseHelper.getWritableDatabase();
				break;
			case DB_READ:
				sqlite = databaseHelper.getReadableDatabase();
				break;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {

		}
	}

	/*
	 * disconnect
	 * 
	 * @description Close connection
	 * 
	 * @return none
	 */
	public void disconnect() {

		try {
			databaseHelper.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			sqlite.close();
		}
	}

	/*
	 * insertFriend
	 * 
	 * @description Insert new row in table
	 * 
	 * @return Cursor
	 */
	public void insertFriend(int uid, String name) {
		connect(DB_WRITE_AND_READ);
		ContentValues values = new ContentValues();
		values.put("uid", uid);
		values.put("name", name);

		sqlite.insert(databaseHelper.tblFriends, null, values);
		disconnect();
	}


	/*
	 * getAllFriends
	 * 
	 * @description Return all friends rows
	 * 
	 * @return Cursor
	 */
	public Cursor getAllFriends() {
		connect(DB_READ);

		String[] fields = new String[] { "uid", "name" };

		Cursor cursor = sqlite.query(databaseHelper.tblFriends, fields,
				null, null, null, null, null);
		startManagingCursor(cursor);
		return cursor;

	}

	
	/*
	 * getConfig
	 * 
	 * @description Return a String value based in config_key
	 * 
	 * @return String
	 */
	public String getConfig(String[] where) {
		connect(DB_READ);
		String returnData = "";
		String query = "SELECT config_value FROM tblConfig WHERE config_key = ?";
		Cursor cursor = sqlite.rawQuery(query, where);
		startManagingCursor(cursor);

		if (cursor.getCount() == 0) {
			cursor.close();
			return returnData;
		}
		while (cursor.moveToNext()) {
			returnData = cursor.getString(0);
		}
		cursor.close();
		return returnData;
	}
	
	
	/*
	 * setConfig
	 * 
	 * @description Set a config based in config_key
	 * 
	 * @return none
	 */
	public void setConfig(String config_key, String config_value) {
		connect(DB_READ);
		String query = "INSERT INTO tblConfig VALUES ( null, '" + config_key
				+ "','" + config_value + "')";

		sqlite.execSQL(query);

		disconnect();
	}
}
