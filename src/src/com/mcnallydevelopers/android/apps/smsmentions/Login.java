package com.mcnallydevelopers.android.apps.smsmentions;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.facebook.android.Facebook.DialogListener;
import com.mcnallydevelopers.android.apps.smsmentions.database.Database;
import com.mcnallydevelopers.android.apps.smsmentions.tools.Config;
import com.mcnallydevelopers.android.apps.smsmentions.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class Login extends Activity {
	Facebook facebook = new Facebook("393253210732381");
	private String log = "FacebookConnect";

	private Database db = null;
	private Config config = null;
	private JSONObject obj = null;
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		db = new Database(getApplicationContext());

		pd = new ProgressDialog(this);

		facebook.authorize(this, new String[] { "offline_access",
				"publish_stream", "publish_actions", "email" },

		new DialogListener() {
			@Override
			public void onComplete(Bundle values) {

				String response = "";
				try {
					response = facebook.request("me/friends");
				} catch (MalformedURLException e1) {
					Log.e(log, e1.getMessage());
				} catch (IOException e1) {
					Log.e(log, e1.getMessage());
				}
				try {
					obj = Util.parseJson(response);
				} catch (FacebookError e) {
					Log.e(log, e.getMessage());
				} catch (JSONException e) {
					Log.e(log, e.getMessage());
				}

				Async async = new Async();
				async.execute("Save");

			}

			@Override
			public void onFacebookError(FacebookError error) {
				Toast.makeText(getBaseContext(), error.getMessage(),
						Toast.LENGTH_LONG).show();
				backToMain();

			}

			@Override
			public void onError(DialogError e) {
				Toast.makeText(getBaseContext(), e.getMessage(),
						Toast.LENGTH_LONG).show();
				backToMain();
			}

			@Override
			public void onCancel() {
				backToMain();

			}
		});
	}

	private void backToMain() {
		moveTaskToBack(true);
	}

	private void runMainActivity() {
		Intent intent = new Intent();
		intent.setClass(this, Main.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		finish();
	}

	public class Async extends AsyncTask<String, String, String> {

		protected void onPreExecute() {

			pd.setTitle("Guardando");
			pd.setMessage("Guardando contactos de Facebook...");
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(false);
			pd.show();
		}

		protected String doInBackground(String... params) {

			try {

				if (obj.getJSONArray("data").length() > 0) {
					

					for (int i = 0; i < obj.getJSONArray("data").length(); i++) {

						db.insertFriend(
								obj.getJSONArray("data").getJSONObject(i)
										.getString("id"),
								obj.getJSONArray("data").getJSONObject(i)
										.getString("name"));
					}
					config = new Config(getApplicationContext(), db);
					config.set("facebook", "true");
				}

			} catch (JSONException e) {
				Log.e(log, e.getMessage());
			}
			return "Ok";
		}

		@Override
		protected void onPostExecute(String result) {

			runMainActivity();
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		pd.dismiss();
		if (db != null) {
			db.sqlite.close();
		}
	}
}
