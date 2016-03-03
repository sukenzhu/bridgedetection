package com.suken.bridgedetection.activity;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.androidannotations.api.BackgroundExecutor;
import com.suken.bridgedetection.Constants;
import com.suken.bridgedetection.R;
import com.suken.bridgedetection.storage.CheckFormAndDetailDao;
import com.suken.bridgedetection.storage.CheckFormData;
import com.suken.bridgedetection.storage.HDBaseData;
import com.suken.bridgedetection.storage.QLBaseData;
import com.suken.bridgedetection.storage.SDBaseData;
import com.suken.bridgedetection.storage.SdxcFormAndDetailDao;
import com.suken.bridgedetection.storage.SdxcFormData;
import com.suken.bridgedetection.util.NetWorkUtil;
import com.suken.bridgedetection.util.NetWorkUtil.ConnectType;
import com.suken.bridgedetection.util.UiUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class UpdateAllActivity extends BaseActivity {

	private int mType;
	private List<UpdateBean> mSourceData = new ArrayList<UpdateBean>();
	private ListView mListView = null;
	private String mTypeStr = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getWindow().getAttributes().height = -1;
		getWindow().getAttributes().width = -1;
		setContentView(R.layout.activity_page_updateall);
		mListView = (ListView) findViewById(R.id.update_all_list);
		Intent intent = getIntent();
		mType = intent.getIntExtra("type", R.drawable.qiaoliangjiancha);
		switch (mType) {
		case R.drawable.qiaoliangjiancha:
			mTypeStr = "桥涵经常检查记录表";
			break;
		case R.drawable.qiaoliangxuncha:
			mTypeStr = "桥涵巡查记录表";
			break;
		case R.drawable.suidaojiancha:
			mTypeStr = "隧道经常检查记录表";
			break;
		case R.drawable.suidaoxuncha:
			mTypeStr = "隧道巡查记录表";
			break;

		default:
			break;
		}
		ImageView img = (ImageView) findViewById(R.id.image);
		if (NetWorkUtil.getConnectType(this) == ConnectType.CONNECT_TYPE_WIFI) {
			img.setImageResource(R.drawable.wifi_red);
		} else {
			img.setImageResource(R.drawable.wifi_gray);
		}
		updateInit(false);
	}

	private class UpdateBean {
		String mc;
		String jlr;
		String sj;
		String id;
		int mType;
		ListBean listBean;
		boolean isChecked = true;
	}

	private void updateInit(boolean isReset) {

		List<UpdateBean> list = new ArrayList<UpdateBean>();
		if (mType == R.drawable.qiaoliangjiancha || mType == R.drawable.suidaojiancha) {
			CheckFormAndDetailDao formDao = new CheckFormAndDetailDao();
			for (ListBean listBean : BridgeDetectionListActivity.updateAllList) {
				CheckFormData data = formDao.queryByQHIdAndStatus(listBean.id, Constants.STATUS_UPDATE, mType);
				if (data != null) {
					UpdateBean bean = new UpdateBean();
					bean.listBean = listBean;
					bean.sj = data.getJcsj();
					if (mType == R.drawable.qiaoliangjiancha) {
						bean.id = data.getQhid();
						bean.mc = data.getQhmc();
					} else {
						bean.id = data.getSdid();
						bean.mc = data.getSdmc();
					}
					bean.jlr = data.getJlry();
					bean.mType = mType;
					list.add(bean);
				}
			}
		} else {
			SdxcFormAndDetailDao dao = new SdxcFormAndDetailDao();
			for (ListBean listBean : BridgeDetectionListActivity.updateAllList) {
				SdxcFormData data = dao.queryByQHIdAndStatus(listBean.id, Constants.STATUS_UPDATE, mType);
				if (data != null) {
					UpdateBean bean = new UpdateBean();
					bean.listBean = listBean;
					if (mType == R.drawable.suidaoxuncha) {
						bean.id = data.getSdid();
						bean.mc = data.getSdmc();
					} else {
						bean.id = data.getLocalId() + "";
						bean.mc = "";
					}
					bean.sj = data.getJcsj();
					bean.jlr = data.getJlry();
					bean.mType = mType;
					list.add(bean);
				}
			}
		}
		mSourceData = list;
		TextView title = (TextView) findViewById(R.id.title);
		title.setText(mTypeStr + mSourceData.size() + "份未上传");
		if (isReset) {
			UpdateAdapter adapter = (UpdateAdapter) mListView.getAdapter();
			adapter.notifyDataSetChanged();
		} else {
			mListView.setAdapter(new UpdateAdapter());
		}

	}

	private class UpdateAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mSourceData.size();
		}

		@Override
		public UpdateBean getItem(int position) {
			return mSourceData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			if (convertView == null) {
				view = LayoutInflater.from(UpdateAllActivity.this).inflate(R.layout.updateall_item, null);
			} else {
				view = convertView;
			}
			CheckBox box = (CheckBox) view.findViewById(R.id.checkbox);
			UpdateBean bean = getItem(position);
			TextView tv = (TextView) view.findViewById(R.id.textview);
			String text = "记录人：" + bean.jlr + "," + "时间：" + bean.sj;
			if(bean.mType == R.drawable.qiaoliangxuncha){
				tv.setText(text);
			} else {
				tv.setText("名称：" + bean.mc + "," + text);
			}
			box.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
			box.setTag(bean);
			box.setChecked(bean.isChecked);
			view.setTag(bean);
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {

					UpdateBean bean = (UpdateBean) v.getTag();
					ListBean listBean = bean.listBean;
					Intent intent = new Intent(UpdateAllActivity.this, BridgeFormActivity.class);
					intent.putExtra("localId", listBean.lastEditLocalId);
					intent.putExtra("isEdit", true);
					intent.putExtra("type", mType);
					if (mType == R.drawable.qiaoliangjiancha || mType == R.drawable.qiaoliangxuncha) {
						if (listBean.realBean instanceof QLBaseData) {
							intent.putExtra("qhInfo", (QLBaseData) listBean.realBean);
						} else if (listBean.realBean instanceof HDBaseData) {
							intent.putExtra("qhInfo", (HDBaseData) listBean.realBean);
						}
					} else {
						intent.putExtra("qhInfo", (SDBaseData) listBean.realBean);
					}
					startActivityForResult(intent, mType == R.drawable.qiaoliangxuncha ? 2 : 1);
				
				}
			});
			box.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					UpdateBean tag = (UpdateBean) buttonView.getTag();
					tag.isChecked = isChecked;
				}
			});
			return view;
		}

	}

	public void toUpdate(View view) {
		if (mSourceData.size() > 0) {
			BackgroundExecutor.execute(new Runnable() {
				
				@Override
				public void run() {
					showLoading("上传中...");
					for (UpdateBean bean : mSourceData) {
						if (bean.isChecked) {
							UiUtil.updateSingleNotPost(bean.id + "", bean.mType, false, UpdateAllActivity.this);
						}
					}
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							updateInit(true);
							if(mSourceData == null || mSourceData.size() == 0){
								finish();
							}
						}
					});
					dismissLoading();
				}
			});
		}
	}
	
	public void close(View view){
		finish();
	}

}
