package com.suken.bridgedetection.fragment;

import com.suken.bridgedetection.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class LeftFragment extends Fragment implements OnClickListener {

	private View mContentView = null;

	private LeftFragmentItemController mHomeItem;
	private LeftFragmentItemController mIpItem;
	private LeftFragmentItemController mGpsItem;
	private LeftFragmentItemController mVideoItem;
	private LeftFragmentItemController mUpdateItem;
	private LeftFragmentItemController mExitItem;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.left_fragment_view, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mContentView = view;
		initView();
	}

	private void initView() {
//		mProfileItem = new LeftFragmentItemController(this, mContentView, R.id.left_frag_profile, "", -1);
		mHomeItem = new LeftFragmentItemController(this, mContentView, R.id.left_frag_home, "首页",
				R.drawable.left_frag_home_img_selector);
		mHomeItem.setSelected();
		mIpItem = new LeftFragmentItemController(this, mContentView, R.id.left_frag_ip, "通讯设置",
				R.drawable.left_frag_ip_img_selector);
		mVideoItem = new LeftFragmentItemController(this, mContentView, R.id.left_frag_video, "视频监控",
				R.drawable.left_frag_video_img_selector);
		mGpsItem = new LeftFragmentItemController(this, mContentView, R.id.left_frag_gps, "GPS设置",
				R.drawable.left_frag_gps_img_selector);
		mUpdateItem = new LeftFragmentItemController(this, mContentView, R.id.left_frag_update, "版本更新",
				R.drawable.left_frag_update_img_selector);
		mExitItem = new LeftFragmentItemController(this, mContentView, R.id.left_frag_exit, "安全退出",
				R.drawable.left_frag_exit_img_selector);
	}

	@Override
	public void onClick(View view) {
		mHomeItem.clearFocus();
		mIpItem.clearFocus();
		mVideoItem.clearFocus();
		mGpsItem.clearFocus();
		mUpdateItem.clearFocus();
		mExitItem.clearFocus();
		int id = view.getId();
		switch (id) {
		case R.id.left_frag_home:
			mHomeItem.setSelected();
			break;
		case R.id.left_frag_ip:
			mIpItem.setSelected();
			break;
		case R.id.left_frag_video:
			mVideoItem.setSelected();
			break;
		case R.id.left_frag_update:
			mUpdateItem.setSelected();
			break;
		case R.id.left_frag_exit:
			mExitItem.setSelected();
			break;
		case R.id.left_frag_gps:
			mGpsItem.setSelected();
			break;

		default:
			break;
		}
	}

}