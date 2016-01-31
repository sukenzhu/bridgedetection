package com.suken.bridgedetection.activity;

import java.util.List;

import com.suken.bridgedetection.storage.YWDictionaryInfo;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DictionarySpinnerAdapter extends BaseAdapter {
	private List<YWDictionaryInfo> infos;
	private Context mContext;

	public DictionarySpinnerAdapter(Context context, List<YWDictionaryInfo> infos) {
		super();
		this.mContext = context;
		this.infos = infos;
	}

	@Override
	public int getCount() {
		return infos.size();
	}

	@Override
	public YWDictionaryInfo getItem(int position) {
		return infos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView view = new TextView(mContext);
		view.setPadding(5, 0, 0, 0);
		YWDictionaryInfo desc = getItem(position);
		view.setText(desc.getItemValue());
		view.setTag(desc);
		view.setTextColor(Color.RED);
		view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
		return view;
	}

}
