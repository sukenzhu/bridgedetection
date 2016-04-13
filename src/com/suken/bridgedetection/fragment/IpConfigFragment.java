package com.suken.bridgedetection.fragment;

import com.suken.bridgedetection.R;
import com.suken.bridgedetection.http.HttpTask;
import com.suken.bridgedetection.storage.SharePreferenceManager;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class IpConfigFragment extends BaseFragment implements OnClickListener {
	private EditText mIpEv;
	private EditText mPortEv;
	private Button mBtn = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.ip_fragment_view, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		mIpEv = (EditText) view.findViewById(R.id.ip);
		String port = HttpTask.REQUEST_PORT = SharePreferenceManager.getInstance().readString("port", HttpTask.REQUEST_PORT);
		String ip = HttpTask.REQUEST_IP = SharePreferenceManager.getInstance().readString("ip", HttpTask.REQUEST_IP);

		mIpEv.setText(HttpTask.REQUEST_IP);
		mPortEv = (EditText) view.findViewById(R.id.port);
		mPortEv.setText(HttpTask.REQUEST_PORT);
		mBtn = (Button) view.findViewById(R.id.save1);
		mBtn.setOnClickListener(this);
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onClick(View v) {
		String ip = mIpEv.getText().toString();
		if (!TextUtils.isEmpty(ip)) {
			SharePreferenceManager.getInstance().updateString("ip", ip);
		}
		String port = mPortEv.getText().toString();
		SharePreferenceManager.getInstance().updateString("port", port);
		toast("保存成功");
	}

}
