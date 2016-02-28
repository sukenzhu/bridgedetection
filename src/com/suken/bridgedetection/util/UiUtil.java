package com.suken.bridgedetection.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

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
import com.suken.bridgedetection.activity.HomePageActivity;
import com.suken.bridgedetection.http.HttpTask;
import com.suken.bridgedetection.http.OnReceivedHttpResponseListener;
import com.suken.bridgedetection.storage.CheckDetail;
import com.suken.bridgedetection.storage.CheckFormAndDetailDao;
import com.suken.bridgedetection.storage.CheckFormData;
import com.suken.bridgedetection.storage.FileDesc;
import com.suken.bridgedetection.storage.FileDescDao;
import com.suken.bridgedetection.storage.GXLuXianInfo;
import com.suken.bridgedetection.storage.GXLuXianInfoDao;
import com.suken.bridgedetection.storage.GpsData;
import com.suken.bridgedetection.storage.GpsDataDao;
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
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
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
	
	
	private static final double EARTH_RADIUS = 6378.137;

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}
	
	public static void syncData(final BaseActivity activity) {
		syncData(activity, false);
	}
	
	public static void syncData(final BaseActivity activity, final boolean isJustLastUpdate, final OnSyncDataFinishedListener syncListener){

		ConnectType type = NetWorkUtil.getConnectType(activity);
		if (type == ConnectType.CONNECT_TYPE_DISCONNECT) {
			activity.toast("当前无网络，无法同步数据");
			return;
		}
		if (type != ConnectType.CONNECT_TYPE_WIFI) {
			activity.toast("当前网络不是WiFi，不同步数据");
			return;
		}
		final AtomicInteger flagInt = new AtomicInteger();
		final StringBuilder builder = new StringBuilder();
		final OnReceivedHttpResponseListener listener = new OnReceivedHttpResponseListener() {

			@Override
			public void onRequestSuccess(RequestType type, JSONObject obj) {
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
				case syncData:{
//					桥梁  bridges 后面就是之前的data
//					culverts 涵洞的key
//					tunnels  隧道
//					dictionarys 系统字典的
//					brgengineers 桥涵工程师的  tunengineers 隧道的
					
					List<GXLuXianInfo> list = JSON.parseArray(obj.getString("luxians"), GXLuXianInfo.class);
					new GXLuXianInfoDao().create(list);
					List<QLBaseData> list1 = JSON.parseArray(obj.getString("bridges"), QLBaseData.class);
					new QLBaseDataDao().create(list1);
					List<HDBaseData> list2 = JSON.parseArray(obj.getString("culverts"), HDBaseData.class);
					new HDBaseDataDao().create(list2);
					List<SDBaseData> list3 = JSON.parseArray(obj.getString("tunnels"), SDBaseData.class);
					new SDBaseDataDao().create(list3);
					List<QHYangHuZeRenInfo> list4 = JSON.parseArray(obj.getString("brgengineers"), QHYangHuZeRenInfo.class);
					new QHYHZeRenInfoDao().create(list4);
					List<SDYangHuZeRenInfo> list5 = JSON.parseArray(obj.getString("tunengineers"), SDYangHuZeRenInfo.class);
					new SDYHZeRenInfoDao().create(list5);
					List<YWDictionaryInfo> list6 = JSON.parseArray(obj.getString("dictionarys"), YWDictionaryInfo.class);
					new YWDictionaryDao().create(list6);
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
//				if (!isJustLastUpdate) {
//					new HttpTask(listener, RequestType.gxlxInfo).executePost(list);
//					new HttpTask(listener, RequestType.qlBaseData).executePost(list);
//					new HttpTask(listener, RequestType.hdBaseData).executePost(list);
//					new HttpTask(listener, RequestType.sdBaseData).executePost(list);
//					new HttpTask(listener, RequestType.qhyhzrInfo).executePost(list);
//					new HttpTask(listener, RequestType.sdyhzrInfo).executePost(list);
//					new HttpTask(listener, RequestType.ywzddmInfo).executePost(list);
//				}
//				new HttpTask(listener, RequestType.lastqhjcInfo).executePost(list);
//				new HttpTask(listener, RequestType.lastsdjcInfo).executePost(list);
				if(isJustLastUpdate){
					new HttpTask(listener, RequestType.lastqhjcInfo).executePost(list);
					new HttpTask(listener, RequestType.lastsdjcInfo).executePost(list);
				} else {
					new HttpTask(listener, RequestType.syncData).executePost(list);
				}
				String msg = builder.toString();
				if (TextUtils.isEmpty(msg)) {
					SharePreferenceManager.getInstance().updateString("lastSyncTime", System.currentTimeMillis() + "");
					activity.toast("数据同步成功");
					if(syncListener != null){
						syncListener.onSyncFinished(true);
					}
				} else {
					msg = msg.substring(0, msg.length() - 1);
					activity.toast(msg + "数据同步失败，请在设置中重试同步！");
					if(syncListener != null){
						syncListener.onSyncFinished(false);
					}
				}
				activity.dismissLoading();
			}
		});
	
	}
	

	public static void syncData(final BaseActivity activity, final boolean isJustLastUpdate) {
		syncData(activity, isJustLastUpdate, null);
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

	public static void updateSingleNotPost(final String qhId, final int type, final boolean handleDialog, final BaseActivity activity) {
		if (handleDialog) {
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
		if (handleDialog) {
			activity.dismissLoading();
		}
	}

	public static void updateSingle(final String qhId, final int type, final boolean handleDialog, final BaseActivity activity) {
		BackgroundExecutor.execute(new Runnable() {
			public void run() {
				updateSingleNotPost(qhId, type, handleDialog, activity);
			}
		});
	}

	private static String[] updateAttachment(final String picattach, final String vidattach, List<NameValuePair> list) {
		final String[] strs = new String[2];

		OnReceivedHttpResponseListener listener = new OnReceivedHttpResponseListener() {

			@Override
			public void onRequestSuccess(RequestType type, JSONObject obj) {
				String re = obj.getString("datas");
				List<FileDesc> files = JSON.parseArray(re, FileDesc.class);
				String[] pics = new String[] {};
				int i = 0;
				if (!TextUtils.isEmpty(picattach)) {
					pics = picattach.split(",");
					StringBuilder sb = new StringBuilder();
					for (String s : pics) {
						if (!TextUtils.isEmpty(s)) {
							sb.append(files.get(i).fileId + ",");
							i++;
						}
					}
					strs[0] = sb.toString().substring(0, sb.length() - 1);
				}
				String[] vdos = new String[] {};
				if (!TextUtils.isEmpty(vidattach)) {
					vdos = vidattach.split(",");
					StringBuilder sb = new StringBuilder();
					for (String s : vdos) {
						if (!TextUtils.isEmpty(s)) {
							sb.append(files.get(i).fileId + ",");
							i++;
						}
					}
					strs[1] = sb.toString().substring(0, sb.length() - 1);
				}
				new FileDescDao().create(files);
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
				public void onRequestSuccess(RequestType type1, JSONObject obj) {
					new SdxcFormAndDetailDao().create(data);
					activity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							if (activity instanceof BridgeDetectionListActivity) {
								String dataId = data.getLocalId() + "";
								if (type == R.drawable.suidaoxuncha) {
									dataId = data.getSdid();
								}
								((BridgeDetectionListActivity) activity).updateStatus(dataId, data.getLocalId(), Constants.STATUS_AGAIN);
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
			data.setTjsj(UiUtil.formatNowTime());
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
				public void onRequestSuccess(RequestType type1, JSONObject obj) {
					if(type1 != RequestType.updateGps){
						new CheckFormAndDetailDao().create(data);
						activity.runOnUiThread(new Runnable() {
							public void run() {
								if (activity instanceof BridgeDetectionListActivity) {
									String dataId = data.getQhid();
									if (type == R.drawable.suidaojiancha) {
										dataId = data.getSdid();
									}
									((BridgeDetectionListActivity) activity).updateStatus(dataId, data.getLocalId(), Constants.STATUS_AGAIN);
								}
							}
						});
						activity.toast("上传成功");
					}
				}

				@Override
				public void onRequestFail(RequestType type, String resultCode, String result) {
					if(type != RequestType.updateGps){
						activity.toast(result);
					}
				}
			};
			data.setStatus("2");
			data.setTjsj(UiUtil.formatNowTime());
			String json = new String(JSON.toJSONString(data));
			if(type == R.drawable.qiaoliangjiancha){
				GpsData gpsData = new GpsDataDao().queryGpsData(Long.parseLong(qhId), data.getQhlx());
				if(gpsData != null){
					BasicNameValuePair jsonPair = new BasicNameValuePair("json", JSON.toJSONString(gpsData));
					list.add(jsonPair);
					new HttpTask(listener, RequestType.updateGps).executePost(list);
					list.remove(jsonPair);
				}
			}
			list.add(new BasicNameValuePair("json", json));
			new HttpTask(listener, type == R.drawable.suidaojiancha ? RequestType.updatesdjcInfo : RequestType.updateqhjcInfo).executePost(list);
		}
	}
	
	
	public static byte[] toGzip(byte[] content) throws IOException {
		byte[] re = null;
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(
					content);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			GZIPOutputStream gzip = new GZIPOutputStream(bos);
			byte[] buf = new byte[4096];
			int num = -1;
			while ((num = bais.read(buf)) != -1) {
				gzip.write(buf, 0, num);
			}
			gzip.flush();
			gzip.finish();
			re = bos.toByteArray();
			bais.close();
			bos.close();
			gzip.close();
		} catch (IOException e) {
			throw e;
		}
		return re;
	}

	public static byte[] unGZip(byte[] data) throws IOException {
		byte[] re = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(data);
			GZIPInputStream gzip = new GZIPInputStream(bis);
			byte[] buf = new byte[4096];
			int num = -1;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((num = gzip.read(buf, 0, buf.length)) != -1) {
				baos.write(buf, 0, num);
			}
			re = baos.toByteArray();
			baos.flush();
			baos.close();
			gzip.close();
			bis.close();
		} catch (UnsupportedEncodingException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}
		return re;
	}
	
	private static void todownload(final String url, final BaseActivity activity) {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(activity);
				OnClickListener listener = new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == Dialog.BUTTON_POSITIVE) {
							// 下载
							final DownloadManager downloadManager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
							DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
							request.setDestinationInExternalPublicDir("bridgedetection",  "bridgedetection.apk");
							final long downloadId = downloadManager.enqueue(request);
							activity.toast("下载中...");
							IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
							final BroadcastReceiver receiver = new BroadcastReceiver() {
								@Override
								public void onReceive(Context context, Intent intent) {
									long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
									if (downloadId == reference) {
										Uri uri = downloadManager.getUriForDownloadedFile(downloadId);
										Intent a = new Intent();
										a.setAction(Intent.ACTION_VIEW);
										a.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
										a.setDataAndType(uri, "application/vnd.android.package-archive");
										activity.startActivity(a);
									}
								}
							};
							activity.registerReceiver(receiver, filter);
						} else {
							dialog.dismiss();
							if(activity instanceof HomePageActivity){
								((HomePageActivity) activity).selectHome();
							}
						}
					}
				};
				builder.setTitle("更新").setMessage("检测到新版本，是否更新？").setPositiveButton("确定", listener).setNegativeButton("取消", listener).show();
			}
		});

	}
	
	
	public static  void update(final BaseActivity activity) {
		BackgroundExecutor.execute(new Runnable() {

			@Override
			public void run() {
				OnReceivedHttpResponseListener listener = new OnReceivedHttpResponseListener() {
					
					@Override
					public void onRequestSuccess(RequestType type, JSONObject result) {

						int versioncode = result.getInteger("versioncode");
						final String url = result.getString("apkurl");
						try {
							PackageInfo info = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
							if (versioncode > info.versionCode) {
								todownload(url, activity);
							} else {
								activity.toast("当前版本为最新版本，无需更新");
								if(activity instanceof HomePageActivity){
									((HomePageActivity) activity).selectHome();
								}
							}
						} catch (NameNotFoundException e) {
							e.printStackTrace();
						}
					}
					
					@Override
					public void onRequestFail(RequestType type, String resultCode, String result) {
						
					}
				};
				activity.showLoading("检查更新中...");
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				if(BridgeDetectionApplication.mCurrentUser != null){
					BasicNameValuePair pair = new BasicNameValuePair("userId", BridgeDetectionApplication.mCurrentUser.getUserId());
					list.add(pair);
					pair = new BasicNameValuePair("token", BridgeDetectionApplication.mCurrentUser.getToken());
					list.add(pair);
				}
				new HttpTask(listener, RequestType.update).executePost(list);
				activity.dismissLoading();
			}
		});
	}

}
