package com.suken.bridgedetection.fragment;

import com.suken.bridgedetection.activity.BaseActivity;

import android.app.Activity;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {

	private BaseActivity mActivity;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = (BaseActivity) activity;
	}

	public void showLoading(String msg) {
		mActivity.showLoading(msg);
	}

	public void toast(String msg) {
		mActivity.toast(msg);
	}

	public void dismissLoading(String msg) {
		mActivity.dismissLoading();
	}

}
