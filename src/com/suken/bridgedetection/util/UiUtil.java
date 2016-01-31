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
import com.suken.bridgedetection.RequestType;
import com.suken.bridgedetection.activity.BaseActivity;
import com.suken.bridgedetection.http.HttpTask;
import com.suken.bridgedetection.http.OnReceivedHttpResponseListener;
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
				new HttpTask(listener, RequestType.gxlxInfo).executePost(list);
				new HttpTask(listener, RequestType.qlBaseData).executePost(list);
				new HttpTask(listener, RequestType.hdBaseData).executePost(list);
				new HttpTask(listener, RequestType.sdBaseData).executePost(list);
				new HttpTask(listener, RequestType.qhyhzrInfo).executePost(list);
				new HttpTask(listener, RequestType.sdyhzrInfo).executePost(list);
				new HttpTask(listener, RequestType.ywzddmInfo).executePost(list);
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
	
	public static String genDeviceId(){
		Context context = BridgeDetectionApplication.getInstance();
		TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		int hash = (manager.getDeviceId().hashCode() + "").hashCode();
		return hash + "";
	}
}
