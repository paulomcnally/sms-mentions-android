package com.mcnallydevelopers.android.apps.smsmentions.tools;

import java.util.List;


import com.mcnallydevelopers.android.apps.smsmentions.R;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.util.Log;

public class Sms {
	private static String log = "Sms";
	public Context myContext;

	private String param_number;
	private String param_message;
	private Class param_class;

	private String sent_response_message = "None";
	private String delivered_response_message;


	private boolean runIntent = false;

	public static final int FLAG_UPDATE_CURRENT = 134217728;
	public static final int FLAG_NO_CREATE = 536870912;
	public static final int FLAG_ONE_SHOT = 1073741824;
	public static final int FLAG_CANCEL_CURRENT = 268435456;

	public Sms(Context context) {
		super();
		myContext = context;
	}

	private void setSentResponseMessage(String rm) {
		Log.i(log, "Sent status: " + rm);
		sent_response_message = rm;
	}

	private void setDeliveredResponseMessage(String rm) {
		Log.i(log, "Delivered status: " + rm);
		delivered_response_message = rm;
	}

	public void setRunIntent(boolean b) {
		if(b){
			Log.i(log, "Run intent: True");
		}
		else{
			Log.i(log, "Run intent: False");
		}
		runIntent = b;
	}

	public void setNumber(String n) {
		Log.i(log, "Number: " + n);
		param_number = n;

	}

	public void setMessage(String m) {
		Log.i(log, "Message: " + m);
		param_message = m;
	}

	public void setActivityClassName(Class c) {
		Log.i(log, "Class: " + c);
		param_class = c;
	}

	public void send() {

		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";
		PendingIntent sentPI = PendingIntent.getBroadcast(myContext, 0,
				new Intent(SENT), FLAG_ONE_SHOT);
		PendingIntent deliveredPI = PendingIntent.getBroadcast(myContext, 0,
				new Intent(DELIVERED), FLAG_ONE_SHOT);

		// ---when the SMS has been sent---
		myContext.registerReceiver(new BroadcastReceiver() {
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					setSentResponseMessage("Send");
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					setSentResponseMessage(myContext.getResources().getString(
							R.string.sms_result_error_generic_failure));


					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					setSentResponseMessage(myContext.getResources().getString(
							R.string.sms_result_error_no_service));


					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					setSentResponseMessage(myContext.getResources().getString(
							R.string.sms_result_error_null_pdu));

					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					setSentResponseMessage(myContext.getResources().getString(
							R.string.sms_result_error_radio_off));

					break;
				}
			}
		}, new IntentFilter(SENT));

		// ---when the SMS has been delivered---
		myContext.registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					setDeliveredResponseMessage(myContext.getResources()
							.getString(R.string.sms_result_delivered_ok));
					break;
				case Activity.RESULT_CANCELED:

					setDeliveredResponseMessage(myContext.getResources()
							.getString(R.string.sms_result_delivered_canceled));
					break;
				}
			}
		}, new IntentFilter(DELIVERED));

		SmsManager smsmanager = SmsManager.getDefault();
		smsmanager.sendTextMessage(param_number, null, param_message, sentPI,
				deliveredPI);

		sentPI.cancel();
		deliveredPI.cancel();
	}

}
