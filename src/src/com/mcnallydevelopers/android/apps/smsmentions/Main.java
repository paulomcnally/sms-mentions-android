package com.mcnallydevelopers.android.apps.smsmentions;

import com.mcnallydevelopers.android.apps.smsmentions.database.Database;
import com.mcnallydevelopers.android.apps.smsmentions.tools.Config;
import com.mcnallydevelopers.android.apps.smsmentions.tools.CustomeListAdapter;
import com.mcnallydevelopers.android.apps.smsmentions.tools.Friends;
import com.mcnallydevelopers.android.apps.smsmentions.tools.CustomeList;
import com.mcnallydevelopers.android.apps.smsmentions.tools.Sms;
import com.mcnallydevelopers.android.apps.smsmentions.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity {

	private Config config = null;
	private Database db = null;
	private Sms sms = null;
	private Friends friends;
	private CustomeList[] data = null;
	private EditText editTextSMS;
	private TextView countSMS;
	private Button buttonSend;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		this.initClass();

		if (!this.config.is_login()) {
			this.runLoginActivity();
		} else {
			this.editTextSMSListener();
			this.buttonAction();
			this.addList();

		}
	}

	private void initClass() {
		this.db = new Database(this);
		this.config = new Config(this, db);
		this.friends = new Friends(db);
		this.sms = new Sms(this);
	}
	
	private void buttonAction(){
		buttonSend = (Button)findViewById(R.id.buttonSend);
		buttonSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String txt = editTextSMS.getText().toString();
                if(txt.length() == 0){
                	Toast.makeText(getApplicationContext(), "No has escrito un mensaje.", Toast.LENGTH_LONG).show();
                }
                else{
                	sms.setMessage(txt);
                	sms.setNumber( config.getShortcode() );
                	sms.send();
                	Toast.makeText(getApplicationContext(), "Mensaje enviado.", Toast.LENGTH_LONG).show();
                	moveTaskToBack(true);
                }
            }
        });
		
	}

	private void editTextSMSListener() {
		editTextSMS = (EditText) findViewById(R.id.editTextSMS);
		countSMS = (TextView) findViewById(R.id.textViewCount);
		editTextSMS.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				countSMS.setText(""+editTextSMS.getText().length());
			}
		});

	}

	private void runLoginActivity() {
		Intent intent = new Intent();
		intent.setClass(this, Login.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		finish();
	}

	private void addList() {
		Cursor rows = this.friends.getRows();
		if (rows.getCount() == 0) {
			return;
		} else {
			data = new CustomeList[this.friends.getCount()];

			int i = 0;
			while (rows.moveToNext()) {
				String id = rows.getString(0);
				String name = rows.getString(1);

				data[i] = new CustomeList(id, name);

				i++;
			}

			CustomeListAdapter adapter = new CustomeListAdapter(this,
					R.layout.listview_item_row, data);

			ListView listView = (ListView) findViewById(R.id.listViewFriend);

			listView.setAdapter(adapter);

			// Click Events
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					String txt = editTextSMS.getText().toString();

					txt = txt + " @[" + data[position].id + "::"
							+ data[position].name + "] ";

					editTextSMS.setText(txt);
					editTextSMS.setSelection(editTextSMS.getText().length());
					txt = "";

				}
			});
		}
		rows.close();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (db != null) {
			db.sqlite.close();
		}
	}

}