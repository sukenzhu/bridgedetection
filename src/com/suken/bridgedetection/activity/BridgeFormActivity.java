package com.suken.bridgedetection.activity;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.suken.bridgedetection.BridgeDetectionApplication;
import com.suken.bridgedetection.Constants;
import com.suken.bridgedetection.R;
import com.suken.bridgedetection.fragment.FormItemController;
import com.suken.bridgedetection.fragment.FormItemController.ImageDesc;
import com.suken.bridgedetection.fragment.FormItemController.VideoDesc;
import com.suken.bridgedetection.storage.CheckDetail;
import com.suken.bridgedetection.storage.CheckFormAndDetailDao;
import com.suken.bridgedetection.storage.CheckFormData;
import com.suken.bridgedetection.storage.HDBaseData;
import com.suken.bridgedetection.storage.QHYHZeRenInfoDao;
import com.suken.bridgedetection.storage.QHYangHuZeRenInfo;
import com.suken.bridgedetection.storage.QLBaseData;
import com.suken.bridgedetection.storage.SDBaseData;
import com.suken.bridgedetection.storage.SDYHZeRenInfoDao;
import com.suken.bridgedetection.storage.SDYangHuZeRenInfo;
import com.suken.bridgedetection.storage.SdxcFormAndDetailDao;
import com.suken.bridgedetection.storage.SdxcFormData;
import com.suken.bridgedetection.storage.SdxcFormDetail;
import com.suken.bridgedetection.storage.YWDictionaryDao;
import com.suken.bridgedetection.storage.YWDictionaryInfo;
import com.suken.bridgedetection.util.UiUtil;
import com.suken.imageditor.ImageditorActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class BridgeFormActivity extends Activity implements OnClickListener {

	private String[] detailNames = null;
	private String[] mItemTexts = null;
	private String[] detailValues = null;
	private String[] et1blanks = null;
	private String[] et2blanks = null;
	private Object bean = null;
	private CheckFormData formData;
	private Map<String, FormItemController> mDetailMaps = new HashMap<String, FormItemController>();
	private LinearLayout mFormContent;
	private String qhId = "";
	private TextView save1 = null;
	private Button save2 = null;
	private TextView qlbhTv = null;
	private EditText qlbhEv = null;
	private TextView qlmcTv = null;
	private EditText qlmcEv = null;
	private TextView lxbhTv = null;
	private EditText lxbhEv = null;
	private TextView lxmcTv = null;
	private EditText lxmcEv = null;
	private TextView zxzhTv = null;
	private EditText zxzhEv = null;
	private TextView yhdwTv = null;
	private EditText yhdwEv = null;

	private EditText fzr = null;
	private EditText jlr = null;

	private Spinner lastPddj = null;
	private Spinner pddj = null;

	private CheckBox qlhz = null;
	private TextView mFormTitle;
	private int mType = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_form_page);
		init();
	}

	private void init() {
		mFormTitle = (TextView) findViewById(R.id.form_title);
		qlhz = (CheckBox) findViewById(R.id.form_qlhz);
		lastPddj = (Spinner) findViewById(R.id.form_scpd_spinner);
		pddj = (Spinner) findViewById(R.id.form_bcpd_spinner);
		jlr = (EditText) findViewById(R.id.form_jlr_spinner);
		fzr = (EditText) findViewById(R.id.form_fzr_tv);
		qlbhTv = (TextView) findViewById(R.id.form_qlbh_tv);
		qlbhEv = (EditText) findViewById(R.id.form_qlbh_ev);
		qlbhEv.setEnabled(false);
		qlmcTv = (TextView) findViewById(R.id.form_qlmc_tv);
		qlmcEv = (EditText) findViewById(R.id.form_qlmc_ev);
		qlmcEv.setEnabled(false);
		lxbhTv = (TextView) findViewById(R.id.form_lxbh_tv);
		lxbhEv = (EditText) findViewById(R.id.form_lxbh_ev);
		lxbhEv.setEnabled(false);
		lxmcTv = (TextView) findViewById(R.id.form_lxmc_tv);
		lxmcEv = (EditText) findViewById(R.id.form_lxmc_ev);
		lxmcEv.setEnabled(false);
		zxzhTv = (TextView) findViewById(R.id.form_zxzh_tv);
		zxzhEv = (EditText) findViewById(R.id.form_zxzh_ev);
		zxzhEv.setEnabled(false);
		yhdwTv = (TextView) findViewById(R.id.form_yhdw_tv);
		yhdwEv = (EditText) findViewById(R.id.form_yhdw_ev);
		yhdwEv.setEnabled(false);
		save1 = (TextView) findViewById(R.id.save1);
		save2 = (Button) findViewById(R.id.save2);
		save1.setOnClickListener(this);
		save2.setOnClickListener(this);
		mFormContent = (LinearLayout) findViewById(R.id.form_content);
		bean = getIntent().getSerializableExtra("qhInfo");
		mType = getIntent().getIntExtra("type", R.drawable.qiaoliangjiancha);
		formData = (CheckFormData) getIntent().getSerializableExtra("formData");
		List<YWDictionaryInfo> dinfos = new YWDictionaryDao().queryByTypeId("10000001160070");
		boolean mIsQh = mType == R.drawable.qiaoliangjiancha || mType == R.drawable.qiaoliangxuncha;
		boolean mIsHanDong = false;
		if (mIsQh && bean instanceof QLBaseData) {
			mItemTexts = Constants.qlformDetailItemTexts;
			detailNames = Constants.qlformDetailNames;
			detailValues = Constants.qlformDetailValues;
			et1blanks = Constants.qlformDetailEt2Blanks;
			et2blanks = Constants.qlformDetailEt3Blanks;
			qhId = ((QLBaseData) bean).getId();
			qlbhEv.setText(((QLBaseData) bean).getQlbh());
			qlmcEv.setText(((QLBaseData) bean).getQlmc());
			lxbhEv.setText(((QLBaseData) bean).getLxbh());
			lxmcEv.setText(((QLBaseData) bean).getLxmc());
			zxzhEv.setText(((QLBaseData) bean).getZxzh());
			yhdwEv.setText(((QLBaseData) bean).getGydwName());
		} else if (mIsQh && bean instanceof HDBaseData) {
			mFormTitle.setText("涵洞经常检查记录表");
			qlhz.setText("涵洞会诊");
			qlbhTv.setText("涵洞编号");
			qlmcTv.setText("涵洞名称");
			mIsHanDong = true;
			mItemTexts = Constants.hdformDetailItemTexts;
			detailNames = Constants.hdformDetailNames;
			detailValues = Constants.hdformDetailValues;
			et1blanks = Constants.hdformDetailEt2Blanks;
			et2blanks = Constants.hdformDetailEt3Blanks;
			qhId = ((HDBaseData) bean).getId();
			qlbhEv.setText(((HDBaseData) bean).getHdbh());
			qlmcEv.setText(((HDBaseData) bean).getHdmc());
			lxbhEv.setText(((HDBaseData) bean).getLxbh());
			lxmcEv.setText(((HDBaseData) bean).getLxmc());
			zxzhEv.setText(((HDBaseData) bean).getZxzh());
			yhdwEv.setText(((HDBaseData) bean).getGydwName());
		} else if (!mIsQh && bean instanceof SDBaseData) {
			if(mType == R.drawable.suidaojiancha){
				mItemTexts = Constants.sdformDetailItemTexts;
				detailNames = Constants.sdformDetailNames;
				mFormTitle.setText("隧道经常检查记录表");
				qlhz.setText("隧道会诊");
			} else {
				mItemTexts = Constants.sdxcformDetailItemTexts;
				detailNames = Constants.sdxcformDetailNames;
				mFormTitle.setText("隧道日常巡查记录表");
				qlhz.setVisibility(View.GONE);
			}
			qlbhTv.setText("隧道编号");
			qlmcTv.setText("隧道名称");
			qhId = ((SDBaseData) bean).getId();
			qlbhEv.setText(((SDBaseData) bean).getSdbh());
			qlmcEv.setText(((SDBaseData) bean).getSdmc());
			lxbhEv.setText(((SDBaseData) bean).getLxbh());
			lxmcEv.setText(((SDBaseData) bean).getLxmc());
			zxzhEv.setText(((SDBaseData) bean).getZxzh());
			yhdwEv.setText(((SDBaseData) bean).getGydwName());
		}
		if (mIsQh) {
			QHYangHuZeRenInfo qhyhzrInfo = new QHYHZeRenInfoDao().queryByQhId(qhId);
			if (qhyhzrInfo != null) {
				fzr.setText(qhyhzrInfo.getYhgcs());
			}
		} else {
			SDYangHuZeRenInfo sdyhzrInfo = new SDYHZeRenInfoDao().queryById(qhId);
			if (sdyhzrInfo != null) {
				fzr.setText(sdyhzrInfo.getYhgcs());
			}
			findViewById(R.id.pddj_layout).setVisibility(View.GONE);
		}
		jlr.setText(BridgeDetectionApplication.mCurrentUser.getUserName());
		lastPddj.setAdapter(new DictionarySpinnerAdapter(this, dinfos));
		pddj.setAdapter(new DictionarySpinnerAdapter(this, dinfos));
		Map<String, CheckDetail> mLastFormDetails = new HashMap<String, CheckDetail>();
		if (formData != null) {
			if (mIsQh) {
				for (YWDictionaryInfo info : dinfos) {
					if (TextUtils.equals(info.getType() + "", formData.getPrePddj())) {
						int index = dinfos.indexOf(info);
						lastPddj.setSelection(index);
						pddj.setSelection(index);
					}
				}
			}
			if (formData.getOfenCheckDetailList() != null) {
				for (CheckDetail cd : formData.getOfenCheckDetailList()) {
					mLastFormDetails.put(cd.getBjmc(), cd);
				}
			}
		}

		for (int index = 0; index < detailNames.length; index++) {
			View view = LayoutInflater.from(this).inflate(R.layout.activity_form_item, null);
			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(-1, -1);
			param.leftMargin = (int) (10 * UiUtil.getDp(this));
			param.rightMargin = (int) (10 * UiUtil.getDp(this));
			param.topMargin = (int) (5 * UiUtil.getDp(this));
			view.setPadding(0, 0, 0, param.topMargin);
			view.setBackgroundColor(Color.parseColor("#80ffffff"));
			mFormContent.addView(view, param);
			String blank2 = "";
			if (et2blanks != null && et2blanks.length == 1) {
				blank2 = et2blanks[0];
			} else {
				blank2 = et2blanks[index];
			}
			FormItemController con = new FormItemController(this, view, this, detailNames[index], mType, detailValues != null ? detailValues[index] : "",
					mItemTexts, mLastFormDetails.get(detailNames[index]), et1blanks != null ? et1blanks[index] : "", blank2, qhId, mIsHanDong);
			if (index == 0) {
				con.show();
			} else {
				con.hide();
			}
			mDetailMaps.put(detailNames[index], con);
		}
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.save1 || view.getId() == R.id.save2) {
			saveToLocal();
			return;
		}
		for (FormItemController fic : mDetailMaps.values()) {
			fic.hide();
		}
		FormItemController con = (FormItemController) view.getTag();
		if (con.isShowing()) {
			con.hide();
		} else {
			con.show();
		}
	}

	private void saveToLocal() {
		if(mType == R.drawable.suidaoxuncha){
			SdxcFormData data = new SdxcFormData();
			if (bean instanceof SDBaseData) {
				data.setGldwId(((SDBaseData) bean).getGydwId());
				data.setGldwName(((SDBaseData) bean).getGydwName());
				data.setYhdwId(((SDBaseData) bean).getGydwId());
				data.setYhdwName(((SDBaseData) bean).getGydwName());
				data.setLxid(((SDBaseData) bean).getLxid());
				data.setYhjgId(((SDBaseData) bean).getGydwId());
				data.setYhjgName(((SDBaseData) bean).getGydwName());
			}
			data.setSdmc(qlmcEv.getText().toString());
			data.setSdid(qhId);
			data.setSdbh(qlbhEv.getText().toString());
			data.setSdzh(zxzhEv.getText().toString());
			data.setType(mType);
			data.setLxmc(lxmcEv.getText().toString());
			data.setZxzh(zxzhEv.getText().toString());
			data.setFzry(fzr.getText().toString());
			data.setJlry(jlr.getText().toString());
			data.setStatus("1");
			data.setCreator(BridgeDetectionApplication.mCurrentUser.getUserName());
			SdxcFormAndDetailDao dao = new SdxcFormAndDetailDao();
			dao.create(data);
			for (String s : detailNames) {
				FormItemController con = mDetailMaps.get(s);
				SdxcFormDetail detail = con.packageSxDetail();
				detail.setFormId(data.getId());
				dao.create(detail);
			}
		} else {
			CheckFormData data = new CheckFormData();
			if (mType == R.drawable.qiaoliangjiancha || mType == R.drawable.qiaoliangxuncha) {
				if (bean instanceof QLBaseData) {
					data.setGldwId(((QLBaseData) bean).getGydwId());
					data.setGldwName(((QLBaseData) bean).getGydwName());
					data.setYhdwId(((QLBaseData) bean).getGydwId());
					data.setYhdwName(((QLBaseData) bean).getGydwName());
					data.setQhlx("b");
					data.setLxid(((QLBaseData) bean).getLxid());
				} else if (bean instanceof HDBaseData) {
					data.setGldwId(((HDBaseData) bean).getGydwId());
					data.setGldwName(((HDBaseData) bean).getGydwName());
					data.setYhdwId(((HDBaseData) bean).getGydwId());
					data.setYhdwName(((HDBaseData) bean).getGydwName());
					data.setQhlx("c");
					data.setLxid(((HDBaseData) bean).getLxid());
				}
				data.setQhmc(qlmcEv.getText().toString());
				data.setQhid(qhId);
				data.setQhbm(qlbhEv.getText().toString());
				data.setPrePddj(((YWDictionaryInfo) lastPddj.getSelectedItem()).getItemValue() + "");
				data.setPddj(((YWDictionaryInfo) pddj.getSelectedItem()).getItemValue() + "");
			} else if (mType == R.drawable.suidaojiancha) {
				if (bean instanceof SDBaseData) {
					data.setGldwId(((SDBaseData) bean).getGydwId());
					data.setGldwName(((SDBaseData) bean).getGydwName());
					data.setYhdwId(((SDBaseData) bean).getGydwId());
					data.setYhdwName(((SDBaseData) bean).getGydwName());
					data.setLxid(((SDBaseData) bean).getLxid());
					data.setYhjgId(((SDBaseData) bean).getGydwId());
					data.setYhjgName(((SDBaseData) bean).getGydwName());
				}
				data.setSdmc(qlmcEv.getText().toString());
				data.setSdid(qhId);
				data.setSdbm(qlbhEv.getText().toString());
				data.setSdzh(zxzhEv.getText().toString());
//				data.setSdfx(((SDBaseData) bean).get);
//				data.setWeather(weather);
			}
			data.setType(mType);
			data.setLxmc(lxmcEv.getText().toString());
			data.setZxzh(zxzhEv.getText().toString());
			data.setLxbm(lxbhEv.getText().toString());
			data.setFzry(fzr.getText().toString());
			data.setJlry(jlr.getText().toString());
			data.setStatus("1");
			data.setHzf(qlhz.isChecked() ? "1" : "0");
			data.setJcsj(UiUtil.formatNowTime());
			data.setCreator(BridgeDetectionApplication.mCurrentUser.getUserName());
			CheckFormAndDetailDao dao = new CheckFormAndDetailDao();
			dao.create(data);
			for (String s : detailNames) {
				FormItemController con = mDetailMaps.get(s);
				CheckDetail detail = con.packageCheckDetail();
				detail.setFormId(data.getId());
				dao.create(detail);
			}
		}
		
		Intent intent = new Intent();
		intent.putExtra("id", qhId);
		setResult(1, intent);
		finish();
	}

	private Uri mOutPutFileUri = null;
	private FormItemController mEditController;

	public void jumpToMedia(FormItemController con, int requestCode, ImageDesc desc) {
		mEditController = con;
		String path = Environment.getExternalStorageDirectory().toString() + File.separator + qhId;
		File path1 = new File(path);
		if (!path1.exists()) {
			path1.mkdirs();
		}
		String name = "";
		if (requestCode == Constants.REQUEST_CODE_CAPTURE) {
			name = path1 + File.separator + con.generateMediaName(true);
		} else if (requestCode == Constants.REQUEST_CODE_EDIT_IMG) {
			name = desc.path;
		} else {
			name = path1 + File.separator + con.generateMediaName(false);
		}
		File file = new File(name);
		mOutPutFileUri = Uri.fromFile(file);
		Intent intent = new Intent();
		intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutPutFileUri);
		if (requestCode == Constants.REQUEST_CODE_CAPTURE) {
			intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent, requestCode);
		} else if (requestCode == Constants.REQUEST_CODE_EDIT_IMG) {
			intent.setClass(this, ImageditorActivity.class);
			startActivityForResult(intent, requestCode);
		} else {
			// intent.setClass(this, RecorderActivity.class);
			intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);// 参数设置可以省略
			intent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
			startActivityForResult(intent, requestCode);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		File f = null;
		try {
			f = new File(new URI(mOutPutFileUri.toString()));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		if (requestCode == Constants.REQUEST_CODE_CAPTURE) {
			ImageDesc desc = new ImageDesc();
			desc.name = f.getName();
			desc.path = f.getPath();
			mEditController.updateImg(desc);
		} else if (requestCode == Constants.REQUEST_CODE_EDIT_IMG) {
			// 保存在原先的图片中所以不处理

		} else if (requestCode == Constants.REQUEST_CODE_VIDEO) {
			VideoDesc desc = new VideoDesc();
			desc.name = f.getName();
			desc.path = f.getPath();
			mEditController.updateVideo(desc);
		}
	}

	public CheckDetail getCheckDetail(String title) {
		if (formData != null) {
			List<CheckDetail> list = formData.getOfenCheckDetailList();
			for (CheckDetail detail : list) {
				if (TextUtils.equals(detail.getBjmc(), title)) {
					return detail;
				}
			}
		}
		return null;
	}

}
