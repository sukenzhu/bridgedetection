package com.suken.bridgedetection.fragment;

import android.os.Environment;
import com.suken.bridgedetection.R;
import com.suken.bridgedetection.activity.HomePageActivity;
import com.suken.bridgedetection.activity.IpSettingActivity;
import com.suken.bridgedetection.http.HttpTask;
import com.suken.bridgedetection.storage.FileDesc;
import com.suken.bridgedetection.storage.FileDescDao;
import com.suken.bridgedetection.storage.SharePreferenceManager;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

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
		view.findViewById(R.id.delPng).setOnClickListener(this);
		view.findViewById(R.id.delVdo).setOnClickListener(this);
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onClick(final View v) {
		if(v.getId() == R.id.delPng || v.getId() == R.id.delVdo){
			String path = Environment.getExternalStorageDirectory().toString() + File.separator + getActivity().getPackageName();
			File file =  new File(path);
			if(file.exists()){
				File[] listFiles = file.listFiles(new FileFilter() {
					@Override
					public boolean accept(File file) {
						String name = file.getName();
						if(v.getId() == R.id.delPng){
							return name.endsWith("png");
						} else {
							return name.endsWith(".mp4");
						}
					}
				});

				List<FileDesc> descs = new FileDescDao().queryAll();
				if(listFiles != null){
					for(File f : listFiles){
						if(descs != null){
							for(FileDesc desc : descs){
								String delName = f.getName();
								if(desc.fileName.contains(delName)){
									f.delete();
								}
							}
						}
					}
					toast("清空成功");
				}
			}
		} else {
			String ip = mIpEv.getText().toString();
			if (!TextUtils.isEmpty(ip)) {
				SharePreferenceManager.getInstance().updateString("ip", ip);
			}
			String port = mPortEv.getText().toString();
			SharePreferenceManager.getInstance().updateString("port", port);
			toast("保存成功");
		}
		if(getActivity() instanceof IpSettingActivity){
			getActivity().finish();
		} else {
			HomePageActivity act = (HomePageActivity) getActivity();
			act.selectHome();
		}
	}

}
