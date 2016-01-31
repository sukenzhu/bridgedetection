package com.suken.bridgedetection.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.googlecode.androidannotations.api.BackgroundExecutor;
import com.suken.bridgedetection.BridgeDetectionApplication;
import com.suken.bridgedetection.R;
import com.suken.bridgedetection.RequestType;
import com.suken.bridgedetection.http.HttpTask;
import com.suken.bridgedetection.http.OnReceivedHttpResponseListener;
import com.suken.bridgedetection.location.LocationManager;
import com.suken.bridgedetection.location.LocationResult;
import com.suken.bridgedetection.location.OnLocationFinishedListener;
import com.suken.bridgedetection.storage.CheckDetail;
import com.suken.bridgedetection.storage.CheckFormAndDetailDao;
import com.suken.bridgedetection.storage.CheckFormData;
import com.suken.bridgedetection.storage.FileDesc;
import com.suken.bridgedetection.storage.HDBaseData;
import com.suken.bridgedetection.storage.HDBaseDataDao;
import com.suken.bridgedetection.storage.QLBaseData;
import com.suken.bridgedetection.storage.QLBaseDataDao;
import com.suken.bridgedetection.storage.SDBaseData;
import com.suken.bridgedetection.storage.SDBaseDataDao;
import com.suken.bridgedetection.util.UiUtil;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class BridgeDetectionListActivity extends BaseActivity implements OnReceivedHttpResponseListener, OnClickListener, OnLocationFinishedListener {

	private ListView mList;
	private ListView mHdList;
	private int mType = R.drawable.qiaoliangjiancha;

	private ImageView gpsBtn;
	private View syncBtn;
	private List<CheckFormData> mLastSyncData = null;
	private View mListTitleQl;
	private TextView mQlListTitleText;
	private View mListTitleHd;
	private TextView mHdListTitleText;
	private CheckFormAndDetailDao mFormDao = null;
	private View mUpdateAll = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mFormDao = new CheckFormAndDetailDao();
		setContentView(R.layout.activity_list_page);
		mUpdateAll = findViewById(R.id.update_all);
		mUpdateAll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				updateAll();
			}
		});
		Intent intent = getIntent();
		mType = intent.getIntExtra("type", mType);
		mList = (ListView) findViewById(R.id.activity_list_ql);
		mHdList = (ListView) findViewById(R.id.activity_list_hd);
		mQlListTitleText = (TextView) findViewById(R.id.list_ql_title);
		mHdListTitleText = (TextView) findViewById(R.id.list_hd_title);
		mListTitleQl = findViewById(R.id.qiaoliang_base_title);
		mListTitleHd = findViewById(R.id.handong_base_title);
		findViewById(R.id.back).setOnClickListener(this);
		syncData();
	}

	public void switchPanel(View view) {
		if (view.getId() == R.id.qiaoliang_base_title) {
			mList.setVisibility(View.VISIBLE);
			mHdList.setVisibility(View.GONE);
			mListTitleQl.setSelected(true);
			mListTitleQl.requestFocus();
			mListTitleHd.clearFocus();
			mListTitleHd.setSelected(false);
		} else {
			mList.setVisibility(View.GONE);
			mHdList.setVisibility(View.VISIBLE);
			mListTitleQl.setSelected(false);
			mListTitleQl.clearFocus();
			mListTitleHd.requestFocus();
			mListTitleHd.setSelected(true);
		}
	}
	
	private int initStatus(ListBean bean, Object bd){
		int a = 0;
		CheckFormData lastFormData = findLastSyncData(bean.id);
		List<CheckFormData> savedFormDatas = mFormDao.queryByQHId(bean.id);
		bean.status = "0";
		boolean has1 = false;
		boolean has2 = false;
		if(savedFormDatas != null && savedFormDatas.size() > 0){
			for(CheckFormData cfd : savedFormDatas){
				if(TextUtils.equals(cfd.getStatus(), "1")){
					has1 = true;
					break;
				} else if(TextUtils.equals(cfd.getStatus(), "2")){
					has2 = true;
				}
			}
		}	
		if(has1){
			bean.status = "1";
			a++;
		} else if(has2){
			bean.status = "2";
		} else {
			bean.status = "0";
		}
		bean.mLastFormData = lastFormData;
		bean.realBean = bd;
		return a;
	}

	private void init() {
		List<ListBean> data = new ArrayList<ListBean>();
		int a = 0;
		switch (mType) {
		case R.drawable.qiaoliangjiancha:
		case R.drawable.qiaoliangxuncha: {
			List<ListBean> hdData = new ArrayList<ListBean>();

			List<HDBaseData> hdBaseData = new HDBaseDataDao().queryAll();
			if (hdBaseData != null && hdBaseData.size() > 0) {
				for (HDBaseData bd : hdBaseData) {
					ListBean bean = new ListBean();
					bean.id = bd.getId();
					bean.lxbm = bd.getLxbh();
					bean.lxmc = bd.getLxmc();
					bean.qhbs = bd.getHdbh();
					bean.qhmc = bd.getHdmc();
					bean.qhzh = bd.getZxzh();
					a = initStatus(bean, bd);
					hdData.add(bean);
				}
			}
			mHdListTitleText.setText(" 涵洞(" + a + "/" + hdData.size() + ")");
			mHdList.setAdapter(new ListPageAdapter(this, hdData, mType));
			a = 0;
			List<QLBaseData> qlBaseData = new QLBaseDataDao().queryAll();
			if (qlBaseData != null && qlBaseData.size() > 0) {
				for (QLBaseData bd : qlBaseData) {
					ListBean bean = new ListBean();
					bean.id = bd.getId();
					bean.lxbm = bd.getLxbh();
					bean.lxmc = bd.getLxmc();
					bean.qhbs = bd.getQlbh();
					bean.qhmc = bd.getQlmc();
					bean.qhzh = bd.getZxzh();
					a = initStatus(bean, bd);
					data.add(bean);
				}
			}
			mQlListTitleText.setText(" 桥梁(" + a + "/" + data.size() + ")");
			mListTitleQl.performClick();
			break;
		}
		case R.drawable.suidaojiancha:
		case R.drawable.suidaoxuncha: {
			List<SDBaseData> qlBaseData = new SDBaseDataDao().queryAll();
			if (qlBaseData != null && qlBaseData.size() > 0) {
				for (SDBaseData bd : qlBaseData) {
					ListBean bean = new ListBean();
					bean.id = bd.getId();
					bean.lxbm = bd.getLxbh();
					bean.lxmc = bd.getLxmc();
					bean.qhbs = bd.getSdbh();
					bean.qhmc = bd.getSdmc();
					bean.qhzh = bd.getZxzh();
					a = initStatus(bean, bd);
					data.add(bean);
				}
			}
			mHdList.setVisibility(View.GONE);
			mListTitleHd.setVisibility(View.GONE);
			mListTitleQl.setSelected(true);
			mListTitleQl.requestFocus();
			mQlListTitleText.setText(" 隧道(" + a + "/" + data.size() + ")");
			break;
		}
		}
		gpsBtn = (ImageView) findViewById(R.id.gps_btn);
		LocationManager.getInstance().syncLocation(this);
		syncBtn = findViewById(R.id.tongbu_btn);
		syncBtn.setOnClickListener(this);
		mList.setAdapter(new ListPageAdapter(this, data, mType));
	}

	private void syncData() {
		if (BridgeDetectionApplication.mIsOffline) {
			return;
		}
		BackgroundExecutor.execute(new Runnable() {

			@Override
			public void run() {
				showLoading("同步中...");
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				BasicNameValuePair pair = new BasicNameValuePair("userId", BridgeDetectionApplication.mCurrentUser.getUserId());
				list.add(pair);
				pair = new BasicNameValuePair("token", BridgeDetectionApplication.mCurrentUser.getToken());
				list.add(pair);
				pair = new BasicNameValuePair("did", BridgeDetectionApplication.mDeviceId);
				list.add(pair);
				new HttpTask(BridgeDetectionListActivity.this, (mType == R.drawable.qiaoliangjiancha || mType == R.drawable.qiaoliangxuncha) ? RequestType.lastqhjcInfo : RequestType.lastsdjcInfo)
						.executePost(list);
				runOnUiThread(new Runnable() {
					public void run() {
						init();
					}
				});
				dismissLoading();
			}
		});

	}

	private CheckFormData findLastSyncData(String qhId) {
		if (mLastSyncData != null && mLastSyncData.size() > 0) {
			for (CheckFormData data : mLastSyncData) {
				if (TextUtils.equals(data.getQhid(), qhId)) {
					return data;
				}
			}
		}
		return null;
	}

	@Override
	public void onRequestSuccess(RequestType type, String result) {
		JSONObject obj = JSON.parseObject(result);
		mLastSyncData = JSON.parseArray(obj.getString("datas"), CheckFormData.class);
		toast("同步成功");
	}

	@Override
	public void onRequestFail(RequestType type, String resultCode, String result) {
		toast("同步失败(" + resultCode + ")");
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == syncBtn.getId()) {
			final CharSequence[] charSequences = { "同步基础数据", "同步上次检查数据", "同步本地检查数据" };
			AlertDialog.Builder builder = new AlertDialog.Builder(this);

			builder.setTitle("同步操作").setItems(charSequences, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (which == 0) {
						UiUtil.syncData(BridgeDetectionListActivity.this);
					} else if (which == 1) {
						syncData();
					} else if (which == 2) {
						updateAll();
					}
				}
			}).show();
		} else if (v.getId() == R.id.gps_btn) {

		} else if (v.getId() == R.id.back) {
			finish();
		}

	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg0 == 1) {
			String id = arg2.getStringExtra("id");
			if (mList.getVisibility() == View.VISIBLE) {
				ListPageAdapter adapter = (ListPageAdapter) mList.getAdapter();
				adapter.updateStatus(id);
			} else if (mHdList.getVisibility() == View.VISIBLE) {
				ListPageAdapter adapter = (ListPageAdapter) mHdList.getAdapter();
				adapter.updateStatus(id);
			}
		}
	}

	private void updateAll() {

	}

	public void updateSingle(final String qhId) {
		BackgroundExecutor.execute(new Runnable() {
			public void run() {
				showLoading("上传中...");
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				BasicNameValuePair pair = new BasicNameValuePair("userId", BridgeDetectionApplication.mCurrentUser.getUserId());
				list.add(pair);
				pair = new BasicNameValuePair("token", BridgeDetectionApplication.mCurrentUser.getToken());
				list.add(pair);
				CheckFormData data = new CheckFormAndDetailDao().queryByQHIdAndStatus(qhId, "1");
				if (data != null) {
					for (final CheckDetail detail : data.getOfenCheckDetailList()) {
						OnReceivedHttpResponseListener listener = new OnReceivedHttpResponseListener() {

							@Override
							public void onRequestSuccess(RequestType type, String result) {
								JSONObject obj = JSON.parseObject(result);
								String re = obj.getString("datas");
								List<FileDesc> files = JSON.parseArray(re, FileDesc.class);
								String[] pics = new String[] {};
								int i = 0;
								if (!TextUtils.isEmpty(detail.getPicattachment())) {
									pics = detail.getPicattachment().split(",");
									StringBuilder sb = new StringBuilder();
									for (String s : pics) {
										if (!TextUtils.isEmpty(s)) {
											sb.append(files.get(i).fileId);
											i++;
										}
									}
									detail.setPicattachment(sb.toString());
								}
								String[] vdos = new String[] {};
								if (!TextUtils.isEmpty(detail.getVidattachment())) {
									vdos = detail.getVidattachment().split(",");
									StringBuilder sb = new StringBuilder();
									for (String s : vdos) {
										if (!TextUtils.isEmpty(s)) {
											if (!TextUtils.isEmpty(s)) {
												sb.append(files.get(i).fileId);
												i++;
											}
										}
									}
									detail.setVidattachment(sb.toString());
								}

							}

							@Override
							public void onRequestFail(RequestType type, String resultCode, String result) {

							}
						};
						
						String[] pics = new String[] {};
						if (!TextUtils.isEmpty(detail.getPicattachment())) {
							pics = detail.getPicattachment().split(",");
						}
						String[] vdos = new String[] {};
						if (!TextUtils.isEmpty(detail.getVidattachment())) {
							vdos = detail.getVidattachment().split(",");
						}
						String[] attaches = UiUtil.concat(pics, vdos);
						if(attaches.length > 0){
							new HttpTask(listener, RequestType.uploadFile).uploadFile(list, attaches);
						}
					}
					OnReceivedHttpResponseListener listener = new OnReceivedHttpResponseListener() {
						
						@Override
						public void onRequestSuccess(RequestType type, String result) {
							toast("上传成功");
						}
						
						@Override
						public void onRequestFail(RequestType type, String resultCode, String result) {
							toast("上传失败");
						}
					};
					String json = new String(JSON.toJSONString(data));
					list.add(new BasicNameValuePair("json", json));
					new HttpTask(listener, RequestType.updateqhjcInfo).executePost(list);
				}
				dismissLoading();
			}
		});
	}

	@Override
	public void onLocationFinished(LocationResult result) {
		if (result.isSuccess) {
			gpsBtn.setImageResource(R.drawable.list_gps);
		} else {
			gpsBtn.setImageBitmap(null);
			gpsBtn.setOnClickListener(this);
		}
	}

}
