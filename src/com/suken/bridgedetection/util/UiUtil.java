package com.suken.bridgedetection.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.googlecode.androidannotations.api.BackgroundExecutor;
import com.suken.bridgedetection.BridgeDetectionApplication;
import com.suken.bridgedetection.Constants;
import com.suken.bridgedetection.R;
import com.suken.bridgedetection.RequestType;
import com.suken.bridgedetection.activity.BaseActivity;
import com.suken.bridgedetection.activity.BridgeDetectionListActivity;
import com.suken.bridgedetection.http.HttpTask;
import com.suken.bridgedetection.http.OnReceivedHttpResponseListener;
import com.suken.bridgedetection.storage.CheckDetail;
import com.suken.bridgedetection.storage.CheckFormAndDetailDao;
import com.suken.bridgedetection.storage.CheckFormData;
import com.suken.bridgedetection.storage.FileDesc;
import com.suken.bridgedetection.storage.GXLuXianInfo;
import com.suken.bridgedetection.storage.GXLuXianInfoDao;
import com.suken.bridgedetection.storage.HDBaseData;
import com.suken.bridgedetection.storage.HDBaseDataDao;
import com.suken.bridgedetection.storage.QHYHZeRenInfoDao;
import com.suken.bridgedetection.storage.QHYangHuZeRenInfo;
import com.suken.bridgedetection.storage.QLBaseData;
import com.suken.bridgedetection.storage.QLBaseDataDao;
import com.suken.bridgedetection.storage.SDBaseData;
import com.suken.bridgedetection.storage.SDBaseDataDao;
import com.suken.bridgedetection.storage.SDYHZeRenInfoDao;
import com.suken.bridgedetection.storage.SDYangHuZeRenInfo;
import com.suken.bridgedetection.storage.SdxcFormAndDetailDao;
import com.suken.bridgedetection.storage.SdxcFormData;
import com.suken.bridgedetection.storage.SdxcFormDetail;
import com.suken.bridgedetection.storage.SharePreferenceManager;
import com.suken.bridgedetection.storage.YWDictionaryDao;
import com.suken.bridgedetection.storage.YWDictionaryInfo;
import com.suken.bridgedetection.util.NetWorkUtil.ConnectType;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

public class UiUtil {

	private static float DP = -1f;

	public static float getDp(Activity context) {
		if (DP == -1f) {
			DisplayMetrics dm = new DisplayMetrics();
			context.getWindowManager().getDefaultDisplay().getMetrics(dm);
			DP = dm.density;
		}
		return DP;
	}

	public static void syncData(final BaseActivity activity) {
		syncData(activity, false);
	}

	public static void syncData(final BaseActivity activity, final boolean isJustLastUpdate) {
		ConnectType type = NetWorkUtil.getConnectType(activity);
		if (type == ConnectType.CONNECT_TYPE_DISCONNECT) {
			activity.toast("当前无网络，无法同步数据");
			return;
		}
		if (type != ConnectType.CONNECT_TYPE_WIFI) {
			activity.toast("当前网络不是WiFi，不同步数据");
			return;
		}
		final StringBuilder builder = new StringBuilder();
		final OnReceivedHttpResponseListener listener = new OnReceivedHttpResponseListener() {

			@Override
			public void onRequestSuccess(RequestType type, String result) {
				JSONObject obj = JSON.parseObject(result);
				switch (type) {
				case gxlxInfo: {
					List<GXLuXianInfo> list = JSON.parseArray(obj.getString("datas"), GXLuXianInfo.class);
					new GXLuXianInfoDao().create(list);
					break;
				}
				case qlBaseData: {
					List<QLBaseData> list = JSON.parseArray(obj.getString("datas"), QLBaseData.class);
					new QLBaseDataDao().create(list);
					break;
				}
				case hdBaseData: {
					List<HDBaseData> list = JSON.parseArray(obj.getString("datas"), HDBaseData.class);
					new HDBaseDataDao().create(list);
					break;
				}
				case sdBaseData: {
					List<SDBaseData> list = JSON.parseArray(obj.getString("datas"), SDBaseData.class);
					new SDBaseDataDao().create(list);
					break;
				}
				case qhyhzrInfo: {
					List<QHYangHuZeRenInfo> list = JSON.parseArray(obj.getString("datas"), QHYangHuZeRenInfo.class);
					new QHYHZeRenInfoDao().create(list);
					break;
				}
				case sdyhzrInfo: {
					List<SDYangHuZeRenInfo> list = JSON.parseArray(obj.getString("datas"), SDYangHuZeRenInfo.class);
					new SDYHZeRenInfoDao().create(list);
					break;
				}
				case ywzddmInfo: {
					List<YWDictionaryInfo> list = JSON.parseArray(obj.getString("datas"), YWDictionaryInfo.class);
					new YWDictionaryDao().create(list);
					break;
				}
				case lastqhjcInfo:
				case lastsdjcInfo: {
					List<CheckFormData> list = JSON.parseArray(obj.getString("datas"), CheckFormData.class);
					if (list != null && list.size() > 0) {
						for (CheckFormData data : list) {
							data.setType(type == RequestType.lastqhjcInfo ? R.drawable.qiaoliangjiancha : R.drawable.suidaojiancha);
							data.setLastUpdate(true);
						}
						CheckFormAndDetailDao dao = new CheckFormAndDetailDao();
						dao.deleteLastUpdate();
						dao.create(list);
					}
					break;
				}

				default:
					break;
				}
			}

			@Override
			public void onRequestFail(RequestType type, String resultCode, String result) {
				builder.append(type.getDesc() + "、");
			}
		};
		BackgroundExecutor.execute(new Runnable() {

			@Override
			public void run() {
				activity.showLoading("同步数据中...");
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				BasicNameValuePair pair = new BasicNameValuePair("userId", BridgeDetectionApplication.mCurrentUser.getUserId());
				list.add(pair);
				pair = new BasicNameValuePair("token", BridgeDetectionApplication.mCurrentUser.getToken());
				list.add(pair);
				pair = new BasicNameValuePair("did", BridgeDetectionApplication.mDeviceId);
				list.add(pair);
				if (!isJustLastUpdate) {
					new HttpTask(listener, RequestType.gxlxInfo).executePost(list);
					new HttpTask(listener, RequestType.qlBaseData).executePost(list);
					new HttpTask(listener, RequestType.hdBaseData).executePost(list);
					new HttpTask(listener, RequestType.sdBaseData).executePost(list);
					new HttpTask(listener, RequestType.qhyhzrInfo).executePost(list);
					new HttpTask(listener, RequestType.sdyhzrInfo).executePost(list);
					new HttpTask(listener, RequestType.ywzddmInfo).executePost(list);
				}
				new HttpTask(listener, RequestType.lastqhjcInfo).executePost(list);
				new HttpTask(listener, RequestType.lastsdjcInfo).executePost(list);
				String msg = builder.toString();
				if (TextUtils.isEmpty(msg)) {
					SharePreferenceManager.getInstance().updateString("lastSyncTime", System.currentTimeMillis() + "");
					activity.toast("数据同步成功");
				} else {
					msg = msg.substring(0, msg.length() - 1);
					activity.toast(msg + "数据同步失败，请在设置中重试同步！");
				}
				activity.dismissLoading();
			}
		});
	}

	public static String formatNowTime() {
		Date nowTime = new Date();
		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return time.format(nowTime);
	}

	public static String formatNowTime(String format) {
		Date nowTime = new Date();
		SimpleDateFormat time = new SimpleDateFormat(format);
		return time.format(nowTime);
	}

	public static String[] concat(String[] a, String[] b) {
		String[] c = new String[a.length + b.length];
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length, b.length);
		return c;
	}

	public static String genDeviceId() {
		Context context = BridgeDetectionApplication.getInstance();
		TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		int hash = (manager.getDeviceId().hashCode() + "").hashCode();
		return hash + "";
	}

	public static void updateSingle(final String qhId, final int type, final boolean handleDialog, final BaseActivity activity) {
		BackgroundExecutor.execute(new Runnable() {
			public void run() {
				if(handleDialog){
					activity.showLoading("上传中...");
				}
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				BasicNameValuePair pair = new BasicNameValuePair("userId", BridgeDetectionApplication.mCurrentUser.getUserId());
				list.add(pair);
				pair = new BasicNameValuePair("token", BridgeDetectionApplication.mCurrentUser.getToken());
				list.add(pair);
				if (type == R.drawable.suidaoxuncha || type == R.drawable.qiaoliangxuncha) {
					updateXunchaData(qhId, type, list, activity);
				} else {
					updateCheckData(qhId, type, list, activity);
				}
				if(handleDialog){
					activity.dismissLoading();
				}
			}
		});
	}

	private static String[] updateAttachment(final String picattach, final String vidattach, List<NameValuePair> list) {
		final String[] strs = new String[2];

		OnReceivedHttpResponseListener listener = new OnReceivedHttpResponseListener() {

			@Override
			public void onRequestSuccess(RequestType type, String result) {
				JSONObject obj = JSON.parseObject(result);
				String re = obj.getString("datas");
				List<FileDesc> files = JSON.parseArray(re, FileDesc.class);
				String[] pics = new String[] {};
				int i = 0;
				if (!TextUtils.isEmpty(picattach)) {
					pics = picattach.split(",");
					StringBuilder sb = new StringBuilder();
					for (String s : pics) {
						if (!TextUtils.isEmpty(s)) {
							sb.append(files.get(i).fileId);
							i++;
						}
					}
					strs[0] = sb.toString();
				}
				String[] vdos = new String[] {};
				if (!TextUtils.isEmpty(vidattach)) {
					vdos = vidattach.split(",");
					StringBuilder sb = new StringBuilder();
					for (String s : vdos) {
						if (!TextUtils.isEmpty(s)) {
							if (!TextUtils.isEmpty(s)) {
								sb.append(files.get(i).fileId);
								i++;
							}
						}
					}
					strs[1] = sb.toString();
				}

			}

			@Override
			public void onRequestFail(RequestType type, String resultCode, String result) {

			}
		};

		String[] pics = new String[] {};
		if (!TextUtils.isEmpty(picattach)) {
			pics = picattach.split(",");
		}
		String[] vdos = new String[] {};
		if (!TextUtils.isEmpty(vidattach)) {
			vdos = vidattach.split(",");
		}
		String[] attaches = UiUtil.concat(pics, vdos);
		if (attaches.length > 0) {
			new HttpTask(listener, RequestType.uploadFile).uploadFile(list, attaches);
		}

		return strs;

	}

	private static void updateXunchaData(final String qhId, final int type, List<NameValuePair> list, final BaseActivity activity) {
		final SdxcFormData data = new SdxcFormAndDetailDao().queryByQHIdAndStatus(qhId, "1", type);
		if (data != null) {
			for (final SdxcFormDetail detail : data.getInspectLogDetailList()) {
				String[] strs = updateAttachment(detail.getPicattachment(), detail.getVidattachment(), list);
				detail.setPicattachment(strs[0]);
				detail.setVidattachment(strs[1]);
			}
			OnReceivedHttpResponseListener listener = new OnReceivedHttpResponseListener() {

				@Override
				public void onRequestSuccess(RequestType type1, String result) {
					new SdxcFormAndDetailDao().create(data);
					activity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							if (activity instanceof BridgeDetectionListActivity) {
								String dataId = data.getLocalId() + "";
								if(type == R.drawable.suidaoxuncha){
									dataId = data.getSdid();
								}
								((BridgeDetectionListActivity) activity).updateStatus(dataId, Constants.STATUS_AGAIN);
							}
						}
					});
					activity.toast("上传成功");
				}

				@Override
				public void onRequestFail(RequestType type, String resultCode, String result) {
					activity.toast(result);
				}
			};
			data.setStatus("2");
			String json = new String(JSON.toJSONString(data));
			list.add(new BasicNameValuePair("json", json));
			new HttpTask(listener, type == R.drawable.suidaoxuncha ? RequestType.updatesdxcInfo : RequestType.updateqhxcInfo).executePost(list);
		}
	}

	private static void updateCheckData(final String qhId, final int type, List<NameValuePair> list, final BaseActivity activity) {
		final CheckFormData data = new CheckFormAndDetailDao().queryByQHIdAndStatus(qhId, "1", type);
		if (data != null) {
			for (final CheckDetail detail : data.getOftenCheckDetailList()) {
				String[] strs = updateAttachment(detail.getPicattachment(), detail.getVidattachment(), list);
				detail.setPicattachment(strs[0]);
				detail.setVidattachment(strs[1]);
			}
			OnReceivedHttpResponseListener listener = new OnReceivedHttpResponseListener() {

				@Override
				public void onRequestSuccess(RequestType type1, String result) {
					new CheckFormAndDetailDao().create(data);
					activity.runOnUiThread(new Runnable() {
						public void run() {
							if(activity instanceof BridgeDetectionListActivity){
								String dataId = data.getQhid();
								if(type == R.drawable.suidaojiancha){
									dataId = data.getSdid();
								}
								((BridgeDetectionListActivity) activity).updateStatus(dataId, Constants.STATUS_AGAIN);
							}
						}
					});
					activity.toast("上传成功");
				}

				@Override
				public void onRequestFail(RequestType type, String resultCode, String result) {
					activity.toast(result);
				}
			};
			data.setStatus("2");
			String json = new String(JSON.toJSONString(data));
			list.add(new BasicNameValuePair("json", json));
			new HttpTask(listener, type == R.drawable.suidaojiancha ? RequestType.updatesdjcInfo : RequestType.updateqhjcInfo).executePost(list);
		}
	}

}
