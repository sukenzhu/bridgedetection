package com.suken.bridgedetection.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import com.googlecode.androidannotations.api.BackgroundExecutor;
import com.suken.bridgedetection.BridgeDetectionApplication;
import com.suken.bridgedetection.Constants;
import com.suken.bridgedetection.R;
import com.suken.bridgedetection.location.LocationManager;
import com.suken.bridgedetection.location.LocationResult;
import com.suken.bridgedetection.location.OnLocationFinishedListener;
import com.suken.bridgedetection.storage.CheckFormAndDetailDao;
import com.suken.bridgedetection.storage.CheckFormData;
import com.suken.bridgedetection.storage.HDBaseData;
import com.suken.bridgedetection.storage.HDBaseDataDao;
import com.suken.bridgedetection.storage.QLBaseData;
import com.suken.bridgedetection.storage.QLBaseDataDao;
import com.suken.bridgedetection.storage.SDBaseData;
import com.suken.bridgedetection.storage.SDBaseDataDao;
import com.suken.bridgedetection.storage.SdxcFormAndDetailDao;
import com.suken.bridgedetection.storage.SdxcFormData;
import com.suken.bridgedetection.util.UiUtil;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class BridgeDetectionListActivity extends BaseActivity implements OnClickListener, OnLocationFinishedListener {

	private ListView mList;
	private ListView mHdList;
	private int mType = R.drawable.qiaoliangjiancha;

	private ImageView gpsBtn;
	private ImageView syncBtn;
	private View mListTitleQl;
	private TextView mQlListTitleText;
	private View mListTitleHd;
	private TextView mHdListTitleText;
	private CheckFormAndDetailDao mFormDao = null;
	private View mUpdateAll = null;
	private EditText mSearchInput;
	private ImageView mSearchButton;
	private int mCurrentNum = 0;
	private int mHdCurrentNum = 0;
	private TextView row1 = null;
	private TextView row2 = null;
	private TextView row3 = null;
	private TextView row4 = null;
	private TextView row5 = null;

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mList != null){
			ListPageAdapter adapter = (ListPageAdapter) mList.getAdapter();
			if(adapter != null) {
				adapter.onDestory();
			}
			mList.setAdapter(null);
		}
		if(mHdList != null) {
			ListPageAdapter adapter = (ListPageAdapter) mHdList.getAdapter();
			if(adapter != null) {
				adapter.onDestory();
			}
			mHdList.setAdapter(null);
		}
		gpsBtn = null;
		syncBtn = null;
		mListTitleQl = null;
		mQlListTitleText = null;
		mListTitleHd = null;
		mHdListTitleText = null;
		mUpdateAll = null;
		mSearchInput = null;
		mFormDao = null;
		mUpdateAll = null;
		row1 = null;
		row2 = null;
		row3 = null;
		row4 = null;
		row5 = null;
	}

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
		row1 = (TextView) findViewById(R.id.row1);
		row2 = (TextView) findViewById(R.id.row2);
		row3 = (TextView) findViewById(R.id.row3);
		row4 = (TextView) findViewById(R.id.row4);
		row5 = (TextView) findViewById(R.id.row5);
		loadData();
	}

	private void  loadData(){
		BackgroundExecutor.execute(new Runnable() {
			@Override
			public void run() {
				showLoading("加载中...");
				switch (mType) {
					case R.drawable.qiaoliangxuncha: {
						mListArray = new SdxcFormAndDetailDao().queryAll(mType);
						break;
					}
					case R.drawable.qiaoliangjiancha: {
						mListArray = new QLBaseDataDao().queryAll();
						mListArray2 = new HDBaseDataDao().queryAll();
						break;
					}
					case R.drawable.suidaojiancha:
					case R.drawable.suidaoxuncha: {
						mListArray = new SDBaseDataDao().queryAll();
						break;
					}
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						init(false);
						if(UiUtil.isUpdating){
							showLoading("继续上传中...");
						} else {
							dismissLoading();
						}
					}
				});

			}
		});
	}

	public void switchPanel(View view) {
		if (view.getId() == R.id.qiaoliang_base_title) {
			mList.setVisibility(View.VISIBLE);
			mHdList.setVisibility(View.GONE);
			mListTitleQl.setSelected(true);
			mListTitleQl.requestFocus();
			mListTitleHd.clearFocus();
			mListTitleHd.setSelected(false);
			if(mType == R.drawable.qiaoliangjiancha){
				row2.setText("桥梁名称");
				row3.setText("桥梁编码");
			}
		} else {
			mList.setVisibility(View.GONE);
			mHdList.setVisibility(View.VISIBLE);
			mListTitleQl.setSelected(false);
			mListTitleQl.clearFocus();
			mListTitleHd.requestFocus();
			mListTitleHd.setSelected(true);
			if(mType == R.drawable.qiaoliangjiancha){
				row2.setText("涵洞名称");
				row3.setText("涵洞编码");
			}
		}
	}

	private int initStatus(ListBean bean, Object bd, int type) {
		int a = 0;
		bean.status = Constants.STATUS_CHECK;
		boolean has1 = false;
		boolean has2 = false;
		if (mType != R.drawable.suidaoxuncha) {
			if(mFormDao == null){
				mFormDao = new CheckFormAndDetailDao();
			}
			List<CheckFormData> savedFormDatas = mFormDao.queryByQHId(bean.id, type);
			if (savedFormDatas != null && savedFormDatas.size() > 0) {
				for (CheckFormData cfd : savedFormDatas) {
					if (TextUtils.equals(cfd.getStatus(), Constants.STATUS_UPDATE)) {
						bean.lastEditLocalId = cfd.getLocalId();
						has1 = true;
						break;
					} else if (TextUtils.equals(cfd.getStatus(), Constants.STATUS_AGAIN)) {
						bean.lastEditLocalId = cfd.getLocalId();
						has2 = true;
					}
				}
			}
		} else {
			List<SdxcFormData> savedFormDatas = new SdxcFormAndDetailDao().queryByQHId(bean.id, type);
			if (savedFormDatas != null && savedFormDatas.size() > 0) {
				for (SdxcFormData cfd : savedFormDatas) {
					if (TextUtils.equals(cfd.getStatus(), Constants.STATUS_UPDATE)) {
						has1 = true;
						bean.lastEditLocalId = cfd.getLocalId();
						break;
					} else if (TextUtils.equals(cfd.getStatus(), Constants.STATUS_AGAIN)) {
						has2 = true;
					}
				}
			}

		}
		if (has1) {
			bean.status = Constants.STATUS_UPDATE;
			a++;
		} else if (has2) {
			bean.status = Constants.STATUS_AGAIN;
		} else {
			bean.status = Constants.STATUS_CHECK;
		}
		bean.realBean = bd;
		return a;
	}

	private  List mListArray = null;
	private List mListArray2 = null;

	private void init(boolean isReset) {
		mCurrentNum = 0;
		mHdCurrentNum = 0;
		List<ListBean> data = new ArrayList<ListBean>();
		switch (mType) {
		case R.drawable.qiaoliangxuncha: {
			row1.setText("检查时间");
			row2.setText("巡查人员");
			row3.setText("天气");
			row4.setText("管养单位");
			row5.setText("检查人");
			List<SdxcFormData> list = mListArray;
			if (list != null && list.size() > 0) {
				for (SdxcFormData bd : list) {
					ListBean bean = new ListBean();
					bean.id = bd.getLocalId() + "";
					bean.lxmc = bd.getJcry();
					bean.lxbm = bd.getGydwName();
					bean.qhbs = bd.getWeather();
					bean.qhmc = bd.getXcry();
					bean.qhzh = bd.getJcsj();
					bean.type = mType;
					bean.status = bd.getStatus();
					bean.lastEditLocalId = bd.getLocalId();
					
					if (TextUtils.equals(bean.status, Constants.STATUS_UPDATE)) {
						mCurrentNum++;
					}
					data.add(bean);
				}
			}
			mHdList.setVisibility(View.GONE);
			mListTitleHd.setVisibility(View.GONE);
			mQlListTitleText.setText(" 桥梁巡查(" + mCurrentNum + "/" + data.size() + ")");
			switchPanel(mListTitleQl);
			break;
		}
		case R.drawable.qiaoliangjiancha: {
			List<ListBean> hdData = new ArrayList<ListBean>();
			List<HDBaseData> hdBaseData = mListArray2;
			if (hdBaseData != null && hdBaseData.size() > 0) {
				for (HDBaseData bd : hdBaseData) {
					ListBean bean = new ListBean();
					bean.id = bd.getId();
					bean.lxbm = bd.getLxbh();
					bean.lxmc = bd.getLxmc();
					bean.qhbs = bd.getHdbh();
					bean.qhmc = bd.getHdmc();
					bean.qhzh = bd.getZxzh();
					bean.type = mType;
					bean.mtimes = bd.getMtimes();
					mHdCurrentNum = initStatus(bean, bd, bean.type);
					hdData.add(bean);
				}
			}
			mHdListTitleText.setText(" 涵洞(" + mHdCurrentNum + "/" + hdData.size() + ")");
			mHdList.setAdapter(new ListPageAdapter(this, hdData, mType));
			List<QLBaseData> qlBaseData = mListArray;
			if (qlBaseData != null && qlBaseData.size() > 0) {
				for (QLBaseData bd : qlBaseData) {
					ListBean bean = new ListBean();
					bean.id = bd.getId();
					bean.lxbm = bd.getLxbh();
					bean.lxmc = bd.getLxmc();
					bean.qhbs = bd.getQlbh();
					bean.qhmc = bd.getQlmc();
					bean.qhzh = bd.getZxzh();
					bean.type = mType;
					bean.mtimes = bd.getMtimes();
					mCurrentNum = initStatus(bean, bd, bean.type);
					data.add(bean);
				}
			}
			mQlListTitleText.setText(" 桥梁(" + mCurrentNum + "/" + data.size() + ")");
			mListTitleQl.performClick();
			break;
		}
		case R.drawable.suidaojiancha:
		case R.drawable.suidaoxuncha: {
			row1.setText("隧道桩号");
			row2.setText("隧道名称");
			row3.setText("隧道标识");
			List<SDBaseData> qlBaseData = mListArray;
			if (qlBaseData != null && qlBaseData.size() > 0) {
				for (SDBaseData bd : qlBaseData) {
					ListBean bean = new ListBean();
					bean.id = bd.getId();
					bean.lxbm = bd.getLxbh();
					bean.lxmc = bd.getLxmc();
					bean.qhbs = bd.getSdbh();
					bean.qhmc = bd.getSdmc();
					bean.qhzh = bd.getZxzh();
					bean.type = mType;
					bean.mtimes = bd.getMtimes();
					mCurrentNum = initStatus(bean, bd, bean.type);
					data.add(bean);
				}
			}
			mHdList.setVisibility(View.GONE);
			mListTitleHd.setVisibility(View.GONE);
			mListTitleQl.setSelected(true);
			mListTitleQl.requestFocus();
			mQlListTitleText.setText(" 隧道"+ ((mType == R.drawable.suidaojiancha)? "检查":"巡查")+"(" + mCurrentNum + "/" + data.size() + ")");
			break;
		}
		}
		gpsBtn = (ImageView) findViewById(R.id.gps_btn);
		LocationManager.getInstance().syncLocation(this);
		syncBtn = (ImageView) findViewById(R.id.tongbu_btn);
		syncBtn.setOnClickListener(this);
		if (mType == R.drawable.qiaoliangxuncha) {
			syncBtn.setImageResource(R.drawable.jiahao);
		}
		mList.setAdapter(new ListPageAdapter(this, data, mType));
		mSearchInput = (EditText) findViewById(R.id.search_input);
		mSearchInput.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_ENTER) {
					mSearchButton.performClick();
				}
				return false;
			}
		});
		mSearchButton = (ImageView) findViewById(R.id.search_btn);
		mSearchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mList.getVisibility() == View.VISIBLE) {
					((Filterable) mList.getAdapter()).getFilter().filter(mSearchInput.getEditableText().toString());
				} else {
					((Filterable) mHdList.getAdapter()).getFilter().filter(mSearchInput.getEditableText().toString());
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == syncBtn.getId()) {
			if (mType == R.drawable.qiaoliangxuncha) {
				Intent intent = new Intent(this, BridgeFormActivity.class);
				intent.putExtra("type", mType);
				startActivityForResult(intent, 2);
				return;
			}
			final CharSequence[] charSequences = { "同步基础数据", "同步上次检查数据" };
			AlertDialog.Builder builder = new AlertDialog.Builder(this);

			builder.setTitle("同步操作").setItems(charSequences, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					UiUtil.syncData(BridgeDetectionListActivity.this, which == 1);
				}
			});
			Dialog dialog = builder.create();
			dialog.setCancelable(true);
			dialog.setCanceledOnTouchOutside(true);
			dialog.show();
		} else if (v.getId() == R.id.gps_btn) {
			toast("重新定位中...");
			LocationManager.getInstance().syncLocation(this);
		} else if (v.getId() == R.id.back) {
			finish();
		}

	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg0 == 1 && arg2 != null) {
			String id = arg2.getStringExtra("id");
			long localId = arg2.getLongExtra("localId", -1l);
			updateStatus(id, localId, Constants.STATUS_UPDATE);
		} else if (arg0 == 2 && arg2 != null) {
			long id = arg2.getLongExtra("id", -1);
			if (id != -1) {
				SdxcFormData bd = new SdxcFormAndDetailDao().queryByLocalId(id);
				if (bd == null) {
					return;
				}
				if (arg1 == 2) {
					ListPageAdapter adapter = null;
					adapter = (ListPageAdapter) mList.getAdapter();
					List<ListBean> list = adapter.getSourceData();
					for(ListBean bean : list){
						if(bean.id.equals(bd.getLocalId() + "")){
							bean.id = bd.getLocalId() + "";
							bean.lxmc = bd.getJcry();
							bean.lxbm = bd.getGydwName();
							bean.qhbs = bd.getWeather();
							bean.qhmc = bd.getXcry();
							bean.qhzh = bd.getJcsj();
							bean.type = mType;
							bean.status = bd.getStatus();
							bean.lastEditLocalId = id;
							adapter.notifyDataSetChanged();
						}
					}
				} else {
					ListBean bean = new ListBean();
					bean.id = bd.getLocalId() + "";
					bean.lxmc = bd.getJcry();
					bean.lxbm = bd.getGydwName();
					bean.qhbs = bd.getWeather();
					bean.qhmc = bd.getXcry();
					bean.qhzh = bd.getJcsj();
					bean.type = mType;
					bean.status = bd.getStatus();
					bean.lastEditLocalId = id;
					if (TextUtils.equals(bean.status, Constants.STATUS_UPDATE)) {
						ListPageAdapter adapter = (ListPageAdapter) mList.getAdapter();
						adapter.addData(bean);
						mCurrentNum++;
						mQlListTitleText.setText(" 桥梁(" + mCurrentNum + "/" + adapter.getCount() + ")");
					}
				}

			}
		} else if (arg0 == 3) {
			init(true);
		}
	}

	public void updateStatus(String id, long localId, String status) {
		if (mList.getVisibility() == View.VISIBLE) {
			ListPageAdapter adapter = (ListPageAdapter) mList.getAdapter();
			adapter.updateStatus(id, localId, status);
			mQlListTitleText.setText(" 桥梁(" + (mCurrentNum + 1) + "/" + adapter.getCount() + ")");
		} else if (mHdList.getVisibility() == View.VISIBLE) {
			ListPageAdapter adapter = (ListPageAdapter) mHdList.getAdapter();
			adapter.updateStatus(id, localId, status);
			mHdListTitleText.setText(" 涵洞(" + (mHdCurrentNum + 1) + "/" + adapter.getCount() + ")");
		}
	}
	
	public static List<ListBean> updateAllList = null;;

	private void updateAll() {
		Intent intent = new Intent(this, UpdateAllActivity.class);
		updateAllList = new ArrayList<ListBean>();
		if (mList.getVisibility() == View.VISIBLE) {
			ListPageAdapter adapter = (ListPageAdapter) mList.getAdapter();
			List<ListBean> data = adapter.getSourceData();
			for(ListBean bean : data){
				if(TextUtils.equals(bean.status, Constants.STATUS_UPDATE)){
					updateAllList.add(bean);
				}
			}
		}

		if (mType == R.drawable.qiaoliangjiancha) {
			ListPageAdapter adapter = (ListPageAdapter) mHdList.getAdapter();
			List<ListBean> data = adapter.getSourceData();
			for(ListBean bean : data){
				if(TextUtils.equals(bean.status, Constants.STATUS_UPDATE)){
					updateAllList.add(bean);
				}
			}
		}
		intent.putExtra("type", mType);
		startActivityForResult(intent, 3);
	}

	@Override
	public void onLocationFinished(LocationResult result) {
		if(isFinishing() || isDestroyed()){
			BridgeDetectionApplication.getInstance().write(this.getComponentName() + "Activity finished!");
			return;
		}
		if (result != null  && result.isSuccess) {
			gpsBtn.setImageResource(R.drawable.list_gps);
		} else {
			gpsBtn.setImageResource(R.drawable.gps_red);
			gpsBtn.setOnClickListener(this);
		}
	}


	public int getCurrentType(){
		return mType;
	}

}
