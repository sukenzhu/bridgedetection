package com.suken.bridgedetection.activity;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.suken.bridgedetection.BridgeDetectionApplication;
import com.suken.bridgedetection.Constants;
import com.suken.bridgedetection.R;
import com.suken.bridgedetection.fragment.FormItemController;
import com.suken.bridgedetection.fragment.FormItemController.ImageDesc;
import com.suken.bridgedetection.fragment.FormItemController.VideoDesc;
import com.suken.bridgedetection.location.LocationManager;
import com.suken.bridgedetection.location.LocationResult;
import com.suken.bridgedetection.location.OnLocationFinishedListener;
import com.suken.bridgedetection.storage.CheckDetail;
import com.suken.bridgedetection.storage.CheckFormAndDetailDao;
import com.suken.bridgedetection.storage.CheckFormData;
import com.suken.bridgedetection.storage.GpsData;
import com.suken.bridgedetection.storage.GpsDataDao;
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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

public class BridgeFormActivity extends BaseActivity implements OnClickListener {

	private String[] detailNames = null;
	private String[] mItemTexts = null;
	private String[] detailValues = null;
	private String[] et1blanks = null;
	private String[] et2blanks = null;
	private Object bean = null;
	private List<FormItemController> mDetailMaps = new ArrayList<FormItemController>();
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

	private TextView extra1Tv = null;
	private EditText extra1Ev = null;
	private View extraLayout = null;
	private TextView weatherTv = null;
	private Spinner weatherEv = null;

	private EditText fzr = null;
	private EditText jlr = null;

	private Spinner lastPddj = null;
	private Spinner pddj = null;

	private CheckBox qlhz = null;
	private TextView mFormTitle;
	private int mType = -1;
	private View mOperateLayout = null;
	private boolean mIsHanDong = false;
	private RadioGroup mRadioGroup = null;
	// 仅为桥梁巡查
	private EditText mDealWithEv = null;

	private EditText mJcrEv = null;

	private TextView mGpsTv = null;

	private boolean mIsGpsSuccess = false;

	private Object lastEditForm = null;
	private LastEditBaseClass lastEditBaseClass;

	private long localId = -1l;

	private boolean isEdit = false;
	private boolean isCheckAgain = false;
	private boolean isLastUpdate = false;
	private Map<String, FormBaseDetail> mFormBaseDetails = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_form_page);
		init();
	}

	private void init() {
		mOperateLayout = findViewById(R.id.operateLayout);
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
		extraLayout = findViewById(R.id.extra_layout);
		extra1Tv = (TextView) findViewById(R.id.form_extra1_tv);
		extra1Ev = (EditText) findViewById(R.id.form_extra1_ev);
		weatherTv = (TextView) findViewById(R.id.form_weather_tv);
		weatherEv = (Spinner) findViewById(R.id.form_weather_sp);
		weatherEv.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_item, Constants.weatherStrs));
		weatherEv.setSelection(0);
		save1 = (TextView) findViewById(R.id.save1);
		save2 = (Button) findViewById(R.id.save2);
		save1.setOnClickListener(this);
		save2.setOnClickListener(this);
		mFormContent = (LinearLayout) findViewById(R.id.form_content);
		isEdit = getIntent().getBooleanExtra("isEdit", false);
		isCheckAgain = getIntent().getBooleanExtra("isCheckAgain", false);
		bean = getIntent().getSerializableExtra("qhInfo");
		mType = getIntent().getIntExtra("type", R.drawable.qiaoliangjiancha);
		mGpsTv = (TextView) findViewById(R.id.gps_text);
		syncLocation();
		if (mType == R.drawable.suidaojiancha) {
			TextView fzrTv = (TextView) findViewById(R.id.fzrtv);
			fzrTv.setText("检查人员：");
		}
		if (isEdit || isCheckAgain) {
			localId = getIntent().getLongExtra("localId", -1l);
			switch (mType) {
			case R.drawable.qiaoliangjiancha:
			case R.drawable.suidaojiancha: {
				lastEditForm = new CheckFormAndDetailDao().queryByLocalId(localId);
				break;
			}
			case R.drawable.suidaoxuncha:
			case R.drawable.qiaoliangxuncha: {
				lastEditForm = new SdxcFormAndDetailDao().queryByLocalId(localId);
				break;
			}
			default:
				break;
			}
		
			lastEditBaseClass = getBaseDataFromLastEdit();
		}
		mFormBaseDetails = getFormBaseDetail();

		if (mType == R.drawable.qiaoliangxuncha) {
			initQhxc();
		} else {
			initQhjc();
		}

	}

	private void syncLocation() {
		LocationManager.getInstance().syncLocation(new OnLocationFinishedListener() {

			@Override
			public void onLocationFinished(LocationResult result) {
				if (!result.isSuccess) {
					mGpsTv.setText("gps定位失败");
					mGpsTv.setTextColor(Color.RED);
				} else {
					mIsGpsSuccess = true;
					if (bean != null && mType == R.drawable.qiaoliangjiancha) {

						String qhlx = "b";
						double baseX = -1d;
						double baseY = -1d;

						if (bean instanceof QLBaseData) {
							qhlx = "b";
							baseX = ((QLBaseData) bean).getGpsX();
							baseY = ((QLBaseData) bean).getGpsY();
							boolean needUpdateGps = false;
							String kjfl = ((QLBaseData) bean).getQlkjfl();
							int kjflInt = 4;
							if (!TextUtils.isEmpty(kjfl)) {
								kjflInt = Integer.parseInt(kjfl);
							}
							double distance = 50;
							switch (kjflInt) {
							case 1:
								distance = 1000;
								break;
							case 2:
								distance = 350;
								break;
							case 3:
								distance = 100;
								break;
							case 4:
								distance = 50;
								break;
							default:
								break;
							}

							if (baseX <= 0 || baseY <= 0) {
								needUpdateGps = true;
							} else {
								double distance1 = UiUtil.getDistance(baseX, baseY, result.latitude, result.longitude);
								if (distance1 > distance) {
									needUpdateGps = true;
									toast("当前误差为：" + distance1 + "千米，允许误差范围为：" + distance + "米");
								}
							}
							if (needUpdateGps) {
								GpsData gpsData = new GpsData();
								gpsData.setId(Long.parseLong(qhId));
								gpsData.setQhlx(qhlx);
								gpsData.setGpsX(result.latitude);
								gpsData.setGpsY(result.longitude);
								new GpsDataDao().create(gpsData);
							}
						}
					}
				}
			}
		});
	}

	private void initQhxc() {
		mOperateLayout.setVisibility(View.VISIBLE);
		mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		mRadioGroup.setVisibility(View.GONE);
		extraLayout.setVisibility(View.VISIBLE);
		extra1Tv.setText("巡查人员：");
		mFormTitle.setText("高速公路桥梁巡查日志");
		qlhz.setVisibility(View.GONE);
		detailNames = new String[] { "桥梁桩号：" };
		mItemTexts = Constants.qhxcformDetailItemTexts;
		findViewById(R.id.form_qlbh).setVisibility(View.GONE);
		findViewById(R.id.form_lxbh).setVisibility(View.GONE);
//		findViewById(R.id.form_zxzh).setVisibility(View.GONE);
		zxzhTv.setText("管养单位：");
		zxzhEv.setText(BridgeDetectionApplication.mCurrentUser.getDefgqName());
		yhdwTv.setText("检查时间：");
		yhdwEv.setText(UiUtil.formatNowTime());
		yhdwEv.setEnabled(false);
		zxzhEv.setEnabled(false);
		findViewById(R.id.pddj_layout).setVisibility(View.GONE);
		findViewById(R.id.lastEditLayout).setVisibility(View.GONE);
		findViewById(R.id.qlxcLayout).setVisibility(View.VISIBLE);
		mDealWithEv = (EditText) findViewById(R.id.dealwithEv);
		mJcrEv = (EditText) findViewById(R.id.jiancharenEv);
		if ((isEdit || isCheckAgain) && lastEditBaseClass != null) {
			qlhz.setChecked(TextUtils.equals(lastEditBaseClass.qlhz, "1"));
			extra1Ev.setText(lastEditBaseClass.xcry);
			mDealWithEv.setText(lastEditBaseClass.dealwith);
			resetWeather();
			mJcrEv.setText(lastEditBaseClass.jcr);
			if (TextUtils.equals("b", lastEditBaseClass.qhlx)) {
				RadioButton rb = (RadioButton) findViewById(R.id.radioql);
				rb.setChecked(true);
			} else {
				RadioButton rb = (RadioButton) findViewById(R.id.radiohd);
				rb.setChecked(true);
			}
			if (mFormBaseDetails != null) {
				if(mFormBaseDetails.size() > 0){
					findViewById(R.id.operateDelete).setEnabled(true);
				}
				for (int i = 0; i < mFormBaseDetails.size(); i++) {
					generateController(i, mIsHanDong, false);
				}
			}
		}

	}
	
	private void resetWeather(){
		List<String> weathes = Arrays.asList(Constants.weatherStrs);
		int index = weathes.indexOf(lastEditBaseClass.weather);
		if(index > 0){
			weatherEv.setSelection(index);
		}
	}

	private class LastEditBaseClass {
		String fzr;
		String jlr;
		String prepddj;
		String pddj;
		String sdfx;
		String weather;
		String qhlx;
		String xcry;
		String dealwith;
		String jcr;
		String qlhz = "0";

	}

	private LastEditBaseClass getBaseDataFromLastEdit() {
		LastEditBaseClass data = new LastEditBaseClass();
		if (lastEditForm != null) {
			if (lastEditForm instanceof CheckFormData) {
				data.fzr = ((CheckFormData) lastEditForm).getFzry();
				data.jlr = ((CheckFormData) lastEditForm).getJlry();
				data.prepddj = ((CheckFormData) lastEditForm).getPrePddj();
				data.pddj = ((CheckFormData) lastEditForm).getPddj();
				data.sdfx = ((CheckFormData) lastEditForm).getSdfx();
				data.weather = ((CheckFormData) lastEditForm).getWeather();
				data.qlhz = ((CheckFormData) lastEditForm).getHzf();
			} else if (lastEditForm instanceof SdxcFormData) {
				data.fzr = ((SdxcFormData) lastEditForm).getFzry();
				data.jlr = ((SdxcFormData) lastEditForm).getJlry();
				data.qhlx = ((SdxcFormData) lastEditForm).getQhlx();
				data.xcry = ((SdxcFormData) lastEditForm).getXcry();
				data.dealwith = ((SdxcFormData) lastEditForm).getDealwith();
				data.jcr = ((SdxcFormData) lastEditForm).getJcry();
				data.weather = ((SdxcFormData) lastEditForm).getWeather();
			}
			return data;
		}
		return null;
	}

	public class FormBaseDetail {
		public String picAttach = null;
		public String vdoAttach = null;
		public String picAttachNames = null;
		public String vdoAttachNames = null;
		public String item1;
		public String item2;
		public String item3;
		public String item4;
		public String item5;
		public String item6;
		public String title;
		public long localId;
	}

	private Map<String, FormBaseDetail> getFormBaseDetail() {
		Map<String, FormBaseDetail> maps = new HashMap<String, BridgeFormActivity.FormBaseDetail>();
		if ((isEdit || isCheckAgain) && lastEditForm != null) {
			if (lastEditForm instanceof CheckFormData) {
				List<CheckDetail> details = ((CheckFormData) lastEditForm).getOftenCheckDetailList();
				for (CheckDetail detail : details) {
					FormBaseDetail baseDetail = new FormBaseDetail();
					baseDetail.picAttach = detail.getPicattachment();
					baseDetail.vdoAttach = detail.getVidattachment();
					baseDetail.picAttachNames = detail.getPicattachmentNames();
					baseDetail.vdoAttachNames = detail.getVidattachmentNames();
					baseDetail.localId = detail.getLocalId();
					if (mType == R.drawable.qiaoliangjiancha) {
						baseDetail.title = detail.getBjmc();
						baseDetail.item1 = detail.getQslx();
						baseDetail.item2 = detail.getQsfw();
						baseDetail.item3 = detail.getBlcsyj();
					} else if (mType == R.drawable.suidaojiancha) {
						baseDetail.title = detail.getJgmc();
						baseDetail.item1 = detail.getYcwz();
						baseDetail.item2 = detail.getQsnr();
						baseDetail.item3 = detail.getYcms();
						baseDetail.item4 = detail.getPd();
						baseDetail.item5 = detail.getYhcsyj();
					}
					maps.put(baseDetail.title, baseDetail);

				}
				return maps;
			} else if (lastEditForm instanceof SdxcFormData) {
				List<SdxcFormDetail> details = ((SdxcFormData) lastEditForm).getInspectLogDetailList();
				for (SdxcFormDetail detail : details) {
					FormBaseDetail baseDetail = new FormBaseDetail();
					baseDetail.picAttach = detail.getPicattachment();
					baseDetail.vdoAttach = detail.getCreatetime();
					baseDetail.localId = detail.getLocalId();
					if (mType == R.drawable.suidaoxuncha) {
						baseDetail.title = detail.getJcnr();
						baseDetail.item1 = detail.getQkms();
						baseDetail.item2 = detail.getDealwith();
						baseDetail.item3 = detail.getJcsj();
					} else if (mType == R.drawable.qiaoliangxuncha) {
						baseDetail.item1 = detail.getQhzh();
						baseDetail.item2 = detail.getFx();
						baseDetail.item3 = detail.getFxbh();
						baseDetail.item4 = detail.getShwz();
						baseDetail.item5 = detail.getDx();
						baseDetail.item6 = detail.getYs();
					}
					maps.put(baseDetail.title, baseDetail);
				}
				return maps;
			}
		}
		return null;
	}

	private void initQhjc() {
		if (bean instanceof QLBaseData) {
			if (mType == R.drawable.qiaoliangjiancha) {
				mFormTitle.setText("桥梁经常检查记录表");
				qlhz.setText("桥梁会诊");
				mItemTexts = Constants.qlformDetailItemTexts;
				detailNames = Constants.qlformDetailNames;
				detailValues = Constants.qlformDetailValues;
				et1blanks = Constants.qlformDetailEt2Blanks;
				et2blanks = Constants.qlformDetailEt3Blanks;
			}

			qhId = ((QLBaseData) bean).getId();
			qlbhEv.setText(((QLBaseData) bean).getQlbh());
			qlmcEv.setText(((QLBaseData) bean).getQlmc());
			lxbhEv.setText(((QLBaseData) bean).getLxbh());
			lxmcEv.setText(((QLBaseData) bean).getLxmc());
			zxzhEv.setText(((QLBaseData) bean).getZxzh());
			yhdwEv.setText(((QLBaseData) bean).getGydwName());
		} else if (bean instanceof HDBaseData) {
			if (mType == R.drawable.qiaoliangjiancha) {
				mFormTitle.setText("涵洞经常检查记录表");
				qlhz.setText("涵洞会诊");
				mItemTexts = Constants.hdformDetailItemTexts;
				detailNames = Constants.hdformDetailNames;
				detailValues = Constants.hdformDetailValues;
				et1blanks = Constants.hdformDetailEt2Blanks;
				et2blanks = Constants.hdformDetailEt3Blanks;
				extraLayout.setVisibility(View.VISIBLE);
				extra1Tv.setText("涵洞类型：");
				extra1Ev.setEnabled(false);
				extra1Ev.setText(((HDBaseData) bean).getHdlx());
				findViewById(R.id.form_weather_layout).setVisibility(View.INVISIBLE);
			}
			qlbhTv.setText("涵洞编号：");
			qlmcTv.setText("涵洞名称：");
			mIsHanDong = true;
			qhId = ((HDBaseData) bean).getId();
			qlbhEv.setText(((HDBaseData) bean).getHdbh());
			qlmcEv.setText(((HDBaseData) bean).getHdmc());
			lxbhEv.setText(((HDBaseData) bean).getLxbh());
			lxmcEv.setText(((HDBaseData) bean).getLxmc());
			zxzhEv.setText(((HDBaseData) bean).getZxzh());
			yhdwEv.setText(((HDBaseData) bean).getGydwName());
		} else if (bean instanceof SDBaseData) {
			if (mType == R.drawable.suidaojiancha) {
				mItemTexts = Constants.sdformDetailItemTexts;
				detailNames = Constants.sdformDetailNames;
				mFormTitle.setText("隧道经常检查记录表");
				qlhz.setText("隧道会诊");
				extraLayout.setVisibility(View.VISIBLE);
			} else {
				mItemTexts = Constants.sdxcformDetailItemTexts;
				detailNames = Constants.sdxcformDetailNames;
				mFormTitle.setText("隧道日常巡查记录表");
				qlhz.setVisibility(View.GONE);
			}
			qlbhTv.setText("隧道编号：");
			qlmcTv.setText("隧道名称：");
			qhId = ((SDBaseData) bean).getId();
			qlbhEv.setText(((SDBaseData) bean).getSdbh());
			qlmcEv.setText(((SDBaseData) bean).getSdmc());
			lxbhEv.setText(((SDBaseData) bean).getLxbh());
			lxmcEv.setText(((SDBaseData) bean).getLxmc());
			zxzhEv.setText(((SDBaseData) bean).getZxzh());
			yhdwEv.setText(((SDBaseData) bean).getGydwName());
		}
		if (mType == R.drawable.qiaoliangjiancha) {
			QHYangHuZeRenInfo qhyhzrInfo = new QHYHZeRenInfoDao().queryByQhId(qhId);
			if (qhyhzrInfo != null) {
				fzr.setText(qhyhzrInfo.getYhgcs());
			}
		} else {
			SDYangHuZeRenInfo sdyhzrInfo = new SDYHZeRenInfoDao().queryById(qhId);
			if (sdyhzrInfo != null) {
				fzr.setText(sdyhzrInfo.getYhgcs());
			}
		}

		if (mType != R.drawable.qiaoliangjiancha) {
			findViewById(R.id.pddj_layout).setVisibility(View.GONE);
		}
		jlr.setText(BridgeDetectionApplication.mCurrentUser.getUserName());
		List<YWDictionaryInfo> dinfos = new YWDictionaryDao().queryByTypeId("10000001160070");
		lastPddj.setAdapter(new DictionarySpinnerAdapter(this, dinfos));
		pddj.setAdapter(new DictionarySpinnerAdapter(this, dinfos));

		if ((isEdit || isCheckAgain) && lastEditBaseClass != null) {
			qlhz.setChecked(TextUtils.equals(lastEditBaseClass.qlhz, "1"));
			if (!TextUtils.isEmpty(lastEditBaseClass.fzr)) {
				fzr.setText(lastEditBaseClass.fzr);
			}
			if (!TextUtils.isEmpty(lastEditBaseClass.jlr)) {
				jlr.setText(lastEditBaseClass.jlr);
			}
			if (mType == R.drawable.suidaojiancha) {
				extra1Ev.setText(lastEditBaseClass.sdfx);
				resetWeather();
			} else {
				if (mType == R.drawable.qiaoliangjiancha) {
					for (YWDictionaryInfo info : dinfos) {
						if (!TextUtils.isEmpty(lastEditBaseClass.prepddj) && TextUtils.equals(info.getItemValue() + "", lastEditBaseClass.prepddj)) {
							int index = dinfos.indexOf(info);
							lastPddj.setSelection(index);
						}
						if (!TextUtils.isEmpty(lastEditBaseClass.pddj) && TextUtils.equals(info.getItemValue() + "", lastEditBaseClass.pddj)) {
							int index = dinfos.indexOf(info);
							pddj.setSelection(index);
						}
					}
				}

			}
		}

		for (int index = 0; index < detailNames.length; index++) {
			generateController(index, mIsHanDong, true);
		}
	}

	private void generateController(int index, boolean mIsHanDong, boolean isNew) {

		View view = LayoutInflater.from(this).inflate(R.layout.activity_form_item, null);
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(-1, -1);
		param.leftMargin = (int) (10 * UiUtil.getDp(this));
		param.rightMargin = (int) (10 * UiUtil.getDp(this));
		param.topMargin = (int) (5 * UiUtil.getDp(this));
		view.setPadding(0, 0, 0, param.topMargin);
		view.setBackgroundColor(Color.parseColor("#80ffffff"));
		mFormContent.addView(view, param);
		String blank2 = "";
		if (et2blanks != null) {
			if (et2blanks.length == 1) {
				blank2 = et2blanks[0];
			} else {
				blank2 = et2blanks[index];
			}
		}
		FormBaseDetail fbd = null;
		if ((isEdit || isCheckAgain) && mFormBaseDetails != null) {
			if (mType == R.drawable.qiaoliangxuncha) {
				if(!isNew){
					fbd = (FormBaseDetail) (mFormBaseDetails.values().toArray())[index];
				}
			} else {
				fbd = mFormBaseDetails.get(detailNames[index]);
			}
		}
		String detailName = "";
		if (mType == R.drawable.qiaoliangxuncha) {
			detailName = detailNames[0];
		} else {
			detailName = detailNames[index];
		}
		FormItemController con = new FormItemController(this, view, this, detailName, mType, detailValues != null ? detailValues[index] : "", mItemTexts,
				et1blanks != null ? et1blanks[index] : "", blank2, qhId, mIsHanDong, fbd, isCheckAgain);
		if (index == 0) {
			con.show();
		} else {
			con.hide();
		}
		mDetailMaps.add(con);

	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.save1 || view.getId() == R.id.save2) {
			if (mIsGpsSuccess) {
				saveToLocal();
				toast("Gps定位成功，保存成功！");
			} else {
				LocationManager.getInstance().syncLocation(new OnLocationFinishedListener() {

					@Override
					public void onLocationFinished(LocationResult result) {
						if (result.isSuccess) {
							saveToLocal();
							toast("Gps定位成功，保存成功！");
						} else {
							toast("Gps定位失败，不能离开！");
						}
					}
				});
			}
			return;
		}
		for (FormItemController fic : mDetailMaps) {
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
		long savedId = -1l;
		String time = LocationManager.getInstance().getLastLocationResult().time;
		if (mType == R.drawable.suidaoxuncha || mType == R.drawable.qiaoliangxuncha) {
			SdxcFormData data = new SdxcFormData();
			if (isEdit) {
				data.setLocalId(localId);
			}
			data.setGydwId(BridgeDetectionApplication.mCurrentUser.getDefgqId());
			data.setGydwName(BridgeDetectionApplication.mCurrentUser.getDefgqName());
			data.setYhdwId(BridgeDetectionApplication.mCurrentUser.getDefgqId());
			data.setYhdwName(BridgeDetectionApplication.mCurrentUser.getDefgqName());
			if (bean instanceof SDBaseData) {
				data.setLxid(((SDBaseData) bean).getLxid());
				data.setYhjgId(((SDBaseData) bean).getGydwId());
				data.setYhjgName(((SDBaseData) bean).getGydwName());
				data.setSdmc(qlmcEv.getText().toString());
				data.setSdid(qhId);
				data.setSdbh(qlbhEv.getText().toString());
				data.setSdzh(zxzhEv.getText().toString());
				data.setLxmc(lxmcEv.getText().toString());
				data.setZxzh(zxzhEv.getText().toString());
				data.setLxbh(lxbhEv.getText().toString());
			} else {

				data.setQhlx(mRadioGroup.getCheckedRadioButtonId() == R.id.radioql ? "b" : "c");
				data.setXcry(extra1Ev.getText().toString());
				data.setJcry(mJcrEv.getText().toString());
				data.setDealWith(mDealWithEv.getText().toString());
				data.setWeather(weatherEv.getSelectedItem().toString());
			}
			
			data.setJcsj(time);
			data.setJcsd(time);
			data.setType(mType);
			data.setFzry(fzr.getText().toString());
			data.setJlry(jlr.getText().toString());
			data.setStatus(Constants.STATUS_UPDATE);
			data.setCreator(BridgeDetectionApplication.mCurrentUser.getUserName());
			SdxcFormAndDetailDao dao = new SdxcFormAndDetailDao();
			if (mType == R.drawable.suidaoxuncha && !isEdit) {
				dao.deleteByQhId(qhId, mType);
			}
			dao.create(data);
			for (int i = 0; i < mDetailMaps.size(); i++) {
				FormItemController con = mDetailMaps.get(i);
				SdxcFormDetail detail = con.packageSxDetail();
				detail.setFormId(data.getLocalId());
				dao.create(detail);
			}
			savedId = data.getLocalId();
			if (mType == R.drawable.qiaoliangxuncha) {
				Intent intent = new Intent();
				intent.putExtra("id", data.getLocalId());
				setResult(isEdit ? 2 : 1, intent);
				finish();
			}
		} else {
			CheckFormData data = new CheckFormData();
			if (isEdit) {
				data.setLocalId(localId);
			}
			data.setGldwId(BridgeDetectionApplication.mCurrentUser.getDefgqId());
			data.setGldwName(BridgeDetectionApplication.mCurrentUser.getDefgqName());
			data.setYhdwId(BridgeDetectionApplication.mCurrentUser.getDefgqId());
			data.setYhdwName(BridgeDetectionApplication.mCurrentUser.getDefgqName());
			if (mType == R.drawable.qiaoliangjiancha) {
				if (bean instanceof QLBaseData) {
					data.setQhlx("b");
					data.setLxid(((QLBaseData) bean).getLxid());
				} else if (bean instanceof HDBaseData) {
					data.setQhlx("c");
					data.setLxid(((HDBaseData) bean).getLxid());
					data.setCus2(((HDBaseData) bean).getHdlx());
				}
				data.setQhmc(qlmcEv.getText().toString());
				data.setQhid(qhId);
				data.setQhbm(qlbhEv.getText().toString());
				data.setPrePddj(((YWDictionaryInfo) lastPddj.getSelectedItem()).getItemValue() + "");
				data.setPddj(((YWDictionaryInfo) pddj.getSelectedItem()).getItemValue() + "");
				LocationResult re = LocationManager.getInstance().getLastLocationResult();
				if(re != null){
					data.setCus1(re.latitude + "");
					data.setCus3(re.longitude + "");
				}
			} else if (mType == R.drawable.suidaojiancha) {
				if (bean instanceof SDBaseData) {
					data.setLxid(((SDBaseData) bean).getLxid());
					data.setYhjgId(((SDBaseData) bean).getGydwId());
					data.setYhjgName(((SDBaseData) bean).getGydwName());
				}
				data.setSdmc(qlmcEv.getText().toString());
				data.setSdid(qhId);
				data.setSdbm(qlbhEv.getText().toString());
				data.setSdzh(zxzhEv.getText().toString());
				data.setSdfx(extra1Ev.getText().toString());
				data.setWeather(weatherEv.getSelectedItem().toString());
			}
			data.setType(mType);
			data.setLxmc(lxmcEv.getText().toString());
			data.setZxzh(zxzhEv.getText().toString());
			data.setLxbm(lxbhEv.getText().toString());
			data.setFzry(fzr.getText().toString());
			data.setJcry(fzr.getText().toString());
			data.setJlry(jlr.getText().toString());
			data.setStatus(Constants.STATUS_UPDATE);
			data.setHzf(qlhz.isChecked() ? "1" : "0");
			data.setJcsj(time);
			data.setCreator(BridgeDetectionApplication.mCurrentUser.getUserName());
			CheckFormAndDetailDao dao = new CheckFormAndDetailDao();
			if (!isEdit) {
				dao.deleteByQhId(qhId, mType);
			}
			dao.create(data);
			savedId = data.getLocalId();
			for (int i = 0; i < mDetailMaps.size(); i++) {
				FormItemController con = mDetailMaps.get(i);
				CheckDetail detail = con.packageCheckDetail();
				detail.setFormId(data.getLocalId());
				dao.create(detail);
			}
		}
		if (mType != R.drawable.qiaoliangxuncha) {
			Intent intent = new Intent();
			intent.putExtra("id", qhId);
			intent.putExtra("localId", savedId);
			setResult(1, intent);
			finish();
		}

	}

	private Uri mOutPutFileUri = null;
	private FormItemController mEditController;

	public void jumpToMedia(FormItemController con, int requestCode, ImageDesc desc) {
		mEditController = con;
		String path = Environment.getExternalStorageDirectory().toString() + File.separator + getPackageName();
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
			if (!f.exists()) {
				return;
			}
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

	public void operate(View view) {
		if (view.getId() == R.id.operateAdd) {
			generateController(mDetailMaps.size(), mIsHanDong, true);
			findViewById(R.id.operateDelete).setEnabled(true);
			mDetailMaps.get(mDetailMaps.size() - 1).performArrowImgClick();
		} else if (view.getId() == R.id.operateDelete) {
			FormItemController controller = mDetailMaps.remove(mDetailMaps.size() - 1);
			mFormContent.removeView(controller.getItemView());
			if (mDetailMaps.size() == 0) {
				findViewById(R.id.operateDelete).setEnabled(false);
			} else {
				mDetailMaps.get(mDetailMaps.size() - 1).performArrowImgClick();
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			back(null);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void back(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("提醒");
		builder.setMessage("返回将丢失当前未保存信息");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

}
