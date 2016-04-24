package com.suken.bridgedetection.fragment;

import com.suken.bridgedetection.Constants;
import com.suken.bridgedetection.R;
import com.suken.bridgedetection.activity.BaseActivity;
import com.suken.bridgedetection.activity.HomePageActivity;
import com.suken.bridgedetection.location.LocationManager;
import com.suken.bridgedetection.storage.SharePreferenceManager;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;

public class GpsFragment extends BaseFragment {

	private Switch gpsSwitch = null;
	private EditText gpsInterval = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		return inflater.inflate(R.layout.gps_fragment_view, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		int b = SharePreferenceManager.getInstance().readInt(Constants.INTERVAL, 50);
		boolean f = SharePreferenceManager.getInstance().readBoolean(Constants.GPS_SWITCH, false);
		gpsSwitch = (Switch) view.findViewById(R.id.kaiguan);
		gpsSwitch.setChecked(f);
		gpsSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					LocationManager.getInstance().startRecordLocation();
				} else {
					LocationManager.getInstance().stopRecordLocation();
				}
			}
		});
		gpsInterval = (EditText) view.findViewById(R.id.interval);
		gpsInterval.setText(b + "");
		gpsInterval.setInputType(InputType.TYPE_CLASS_NUMBER);
		view.findViewById(R.id.syncGps).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				LocationManager.getInstance().updateGps(true,true, (BaseActivity) getActivity());
			}
		});
		view.findViewById(R.id.save1).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String interval = gpsInterval.getEditableText().toString();
				int a = 50;
				if (!TextUtils.isEmpty(interval)) {
					try {
						a = Integer.parseInt(interval);
						if (a < 10 ){
							a = 10;
						}
						if(a > 300){
							a = 300;
						}
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
				SharePreferenceManager.getInstance().updateInt(Constants.INTERVAL, a);
				SharePreferenceManager.getInstance().updateBoolean(Constants.GPS_SWITCH, gpsSwitch.isChecked());
				toast("保存成功");
				HomePageActivity act = (HomePageActivity) getActivity();
				act.selectHome();
			}
		});
	}
}
