package com.mcnallydevelopers.android.apps.smsmentions.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mcnallydevelopers.android.apps.smsmentions.database.Database;


import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

public class Config {

	private static String log = "Config";

	Context myContext;

	private Database db = null;

	private boolean is_debug = false;

	public Config(Context context, Database database) {
		super();
		myContext = context;

		db = database;
		
	}

	public void debug(boolean b) {
		if (b) {
			Log.i(log, "Debug: True");
		} else {
			Log.i(log, "Debug: False");
		}
		is_debug = b;
	}

	/*
	 * get
	 * 
	 * @description search in tblConfig row based in config_key and return
	 * conffig_value
	 * 
	 * @return String
	 */
	public String get(String config_key) {
		Log.i(log, "Get: " + config_key);
		String[] where = new String[] { config_key };
		String result = db.getConfig(where);
		// db.disconnect();
		return result;
	}

	/*
	 * set
	 * 
	 * @description Insert o update config row
	 * 
	 * @return none
	 */
	public void set(String config_key, String config_value) {
		db.setConfig(config_key, config_value);
		// db.disconnect();
	}




	/*
	 * getShortcode
	 * 
	 * @description Replace in json string values (movie_id, cinema_id and
	 * movie_date) this sms is a request to recibe days and hour by movie
	 * 
	 * @return String
	 */
	public String getShortcode() {
		String shortcode = "";
		TelephonyManager telephonyManager = ((TelephonyManager) myContext
				.getSystemService(Context.TELEPHONY_SERVICE));
		String operatorName = telephonyManager.getNetworkOperatorName();

		Pattern pattern_claro = Pattern.compile("[cC][lL][aA][rR][oO]");
		Matcher matcher_claro = pattern_claro.matcher(operatorName);
		if (matcher_claro.matches()) {
			shortcode = this.get("shortcode_claro");
		}

		Pattern pattern_movistar = Pattern
				.compile("[mM][oO][vV][iI][sS][tT][aA][rR]");
		Matcher matcher_movistar = pattern_movistar.matcher(operatorName);
		if (matcher_movistar.matches()) {
			shortcode = this.get("shortcode_movistar");
		}
		return shortcode;
	}
	
	/*
	 * is_login
	 * 
	 * @description Confirm if user_token is saved
	 * 
	 * @return boolean
	 */
	public boolean is_login() {
		String row = this.get("facebook");
		if( row.equals("") ){
			return false;
		}
		else{
			return true;
		}
	}
	
	/*
	 * is_login
	 * 
	 * @description Confirm if user_token is saved
	 * 
	 * @return boolean
	 */
	public boolean logout() {
		if( this.is_login() ){
			db.deleteConfig("user_token");
			db.deleteConfig("user_first_name");
			db.deleteConfig("user_last_name");
			db.deleteConfig("user_email");
		}
		return true;
	}

}
