package com.mcnallydevelopers.android.apps.smsmentions.tools;

import com.mcnallydevelopers.android.apps.smsmentions.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomeListAdapter extends ArrayAdapter<CustomeList> {
	Context context;
	int layoutResourceId;
	CustomeList data[] = null;

	public CustomeListAdapter(Context context, int layoutResourceId,
			CustomeList[] data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		CustomeListHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new CustomeListHolder();
			holder.name = (TextView) row.findViewById(R.id.name);

			row.setTag(holder);
		} else {
			holder = (CustomeListHolder) row.getTag();
		}

		CustomeList customlist = data[position];
		holder.name.setText(customlist.name);
		

		return row;
	}

	static class CustomeListHolder {
		TextView name;
	}

}
