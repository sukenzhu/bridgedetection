package com.suken.bridgedetection.fragment;

import com.suken.bridgedetection.R;
import com.suken.bridgedetection.activity.BridgeDetectionListActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class HomePageFragment extends BaseFragment implements OnClickListener {

	private HomeFragmentItemController mRiChangYangHu = null;
	private HomeFragmentItemController mZhiLiangChouJian = null;
	private HomeFragmentItemController mZhuanXiangGC = null;
	private HomeFragmentItemController mQiaoLJianCha = null;
	private HomeFragmentItemController mSuiDaoJianCha = null;
	private HomeFragmentItemController mQiaoLXunCha = null;
	private HomeFragmentItemController mSuiDaoXunCha = null;
	private HomeFragmentItemController mLuZXunShi = null;
	private HomeFragmentItemController mJiDianXunCha = null;
	private HomeFragmentItemController mYingJiShiJian = null;

	private View mContentView = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.home_fragment_view, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mContentView = view;
		mRiChangYangHu = new HomeFragmentItemController(this, mContentView, R.drawable.richangyanghu, "日常养护");
		mZhiLiangChouJian = new HomeFragmentItemController(this, mContentView, R.drawable.zhiliangchoujian, "质量抽检");
		mZhuanXiangGC = new HomeFragmentItemController(this, mContentView, R.drawable.zhuanxianggongcheng, "专项工程");
		mQiaoLJianCha = new HomeFragmentItemController(this, mContentView, R.drawable.qiaoliangjiancha, "桥梁检查");
		mSuiDaoJianCha = new HomeFragmentItemController(this, mContentView, R.drawable.suidaojiancha, "隧道检查");
		mQiaoLXunCha = new HomeFragmentItemController(this, mContentView, R.drawable.qiaoliangxuncha, "桥梁巡查");
		mSuiDaoXunCha = new HomeFragmentItemController(this, mContentView, R.drawable.suidaoxuncha, "隧道巡查");
		mLuZXunShi = new HomeFragmentItemController(this, mContentView, R.drawable.luzhengxunshi, "路政巡视");
		mJiDianXunCha = new HomeFragmentItemController(this, mContentView, R.drawable.jidianxuncha, "机电巡查");
		mYingJiShiJian = new HomeFragmentItemController(this, mContentView, R.drawable.yingjishijian, "应急事件");
	}

	@Override
	public void onClick(View v) {
		int vid = v.getId();
		if (vid == R.drawable.qiaoliangjiancha || vid == R.drawable.qiaoliangxuncha || vid == R.drawable.suidaojiancha || vid == R.drawable.suidaoxuncha) {
			Intent intent = new Intent(getActivity(), BridgeDetectionListActivity.class);
			intent.putExtra("type", vid);
			startActivity(intent);
		} else {
			toast("敬请期待!");
		}
	}
}