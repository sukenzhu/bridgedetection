package com.suken.bridgedetection.activity;

import com.suken.bridgedetection.R;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ListPageAdapter extends BaseAdapter {
	
	private Activity mContext ;

	public ListPageAdapter(Activity mContext) {
		super();
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		return 163;
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		if(arg1 == null){
			arg1 = mContext.getLayoutInflater().inflate(R.layout.activity_list_page_item, arg2, false);
		}
		return arg1;
	}

}
