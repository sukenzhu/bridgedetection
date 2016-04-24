package com.suken.bridgedetection.location;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import com.suken.bridgedetection.activity.BaseActivity;
import com.suken.bridgedetection.activity.HomePageActivity;
import com.suken.bridgedetection.storage.GpsGjData;
import com.suken.bridgedetection.storage.GpsGjDataDao;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.location.Poi;
import com.googlecode.androidannotations.api.BackgroundExecutor;
import com.suken.bridgedetection.BridgeDetectionApplication;
import com.suken.bridgedetection.Constants;
import com.suken.bridgedetection.RequestType;
import com.suken.bridgedetection.http.HttpTask;
import com.suken.bridgedetection.http.OnReceivedHttpResponseListener;
import com.suken.bridgedetection.storage.SharePreferenceManager;
import com.suken.bridgedetection.util.NetWorkUtil;
import com.suken.bridgedetection.util.NetWorkUtil.ConnectType;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class LocationManager implements OnReceivedHttpResponseListener {

	private static LocationManager mInstance;
	private LocationClient mLocationClient;
	private BDLocationListener myListener = new MyLocationListener();
	private LocationResult mLastBDLocation = null;
	private List<OnLocationFinishedListener> mListenerArray = new ArrayList<OnLocationFinishedListener>();
	private Object lock = new Object();
	private GpsGjDataDao mGpsDao = new GpsGjDataDao();

	private OnLocationFinishedListener recordListener = new OnLocationFinishedListener() {

		@Override
		public void onLocationFinished(LocationResult result) {
			if (result.isSuccess) {
				mGpsDao.create(new GpsGjData(Double.toString(result.longitude), Double.toString(result.latitude), Double.toString(result.altitude), result.time, result.wz));
				updateGps(false, false, null);
			}
			int b = SharePreferenceManager.getInstance().readInt(Constants.INTERVAL, 50);
			mLocationHandler.sendEmptyMessageAtTime(0, b);
		}
	};
	private Handler mLocationHandler = new Handler(Looper.getMainLooper()) {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Calendar calendar  = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			int  hour = calendar.get(Calendar.HOUR_OF_DAY);
			if(hour > 7 && hour < 19 ){
				syncLocation(recordListener);
			} else {
				int b = SharePreferenceManager.getInstance().readInt(Constants.INTERVAL, 50);
				mLocationHandler.sendEmptyMessageAtTime(0, b);
			}
		}
	};

	public void startRecordLocation() {
		mLocationHandler.sendEmptyMessage(0);
	}

	public void stopRecordLocation() {
		mLocationHandler.removeMessages(0);
		// 这里传一次
		updateGps(true, false, null);
	}

	private  BaseActivity activity;

	public void updateGps(final boolean force, final  boolean showLoading, final BaseActivity activity) {
		if(BridgeDetectionApplication.mCurrentUser == null){
			return;
		}
		this.activity = activity;
		if(showLoading && activity != null){
			activity.showLoading("上传中...");
		}
		BackgroundExecutor.execute(new Runnable() {
			@Override
			public void run() {
				ConnectType type = NetWorkUtil.getConnectType(BridgeDetectionApplication.getInstance());
				int count = mGpsDao.countQueryGpsData();
				if (type == ConnectType.CONNECT_TYPE_WIFI && count > 0 && (force || count > 50)) {
					List<NameValuePair> list = new ArrayList<NameValuePair>();
					BasicNameValuePair pair = new BasicNameValuePair("userId", BridgeDetectionApplication.mCurrentUser.getUserId());
					list.add(pair);
					pair = new BasicNameValuePair("token", BridgeDetectionApplication.mCurrentUser.getToken());
					list.add(pair);
					pair = new BasicNameValuePair("did", BridgeDetectionApplication.mDeviceId);
					list.add(pair);
					List recordList = mGpsDao.queryGpsData();
					pair = new BasicNameValuePair("json", JSON.toJSONString(recordList));
					list.add(pair);
					new HttpTask(LocationManager.this, RequestType.updateGpsgjInfo).executePost(list);
				}
				if(showLoading && activity != null){
					activity.dismissLoading();
				}
			}
		});
	}

	private LocationManager() {
		mLocationClient = new LocationClient(BridgeDetectionApplication.getInstance()); // 声明LocationClient类
		initLocation();
		mLocationClient.registerLocationListener(myListener); // 注册监听函数
	}

	public static LocationManager getInstance() {
		if (mInstance == null) {
			mInstance = new LocationManager();
		}
		return mInstance;
	}

	public LocationResult getLastLocationResult() {
		return mLastBDLocation;
	}

	public void syncLocation(OnLocationFinishedListener listener) {
		if (mLocationClient.isStarted()) {
			synchronized (lock) {
				mListenerArray.add(listener);
			}
		} else {
			synchronized (lock) {
				mListenerArray.add(listener);
			}
			mLocationClient.start();
		}
	}

	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		int span = 1000;
		option.setTimeOut(30000);
		option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
		option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
		option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
		mLocationClient.setLocOption(option);
	}

	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {

			LocationResult result = new LocationResult();
			// Receive Location
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			result.latitude = location.getLatitude();
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			result.longitude = location.getLongitude();
			result.altitude = location.getAltitude() > 0 ? location.getAltitude() : 0;
			result.time = location.getTime();
			result.wz = location.getAddrStr();
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			sb.append("\ntime : ");
			sb.append(location.getTime());
			if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());// 单位：公里每小时
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append("\nheight : ");
				sb.append(location.getAltitude());// 单位：米
				sb.append("\ndirection : ");
				sb.append(location.getDirection());// 单位度
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				sb.append("\ndescribe : ");
				sb.append("gps定位成功");
				result.message = "gps定位成功";
				result.isSuccess = true;

			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				// 运营商信息
				sb.append("\noperationers : ");
				sb.append(location.getOperators());
				sb.append("\ndescribe : ");
				sb.append("网络定位成功");
				result.message = "网络定位成功";
				android.location.LocationManager locationManager = (android.location.LocationManager) BridgeDetectionApplication.getInstance().getSystemService(Context.LOCATION_SERVICE);
				if (locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
					result.isSuccess = true;
				} else {
					result.isSuccess = false;
				}
			} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
				sb.append("\ndescribe : ");
				sb.append("离线定位成功，离线定位结果也是有效的");
				result.message = "离线定位成功，离线定位结果也是有效的";
				android.location.LocationManager locationManager = (android.location.LocationManager) BridgeDetectionApplication.getInstance().getSystemService(Context.LOCATION_SERVICE);
				if (locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
					result.isSuccess = true;
				} else {
					result.isSuccess = false;
				}
			} else if (location.getLocType() == BDLocation.TypeServerError) {
				sb.append("\ndescribe : ");
				sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
				result.message = "服务端网络定位失败";
				result.isSuccess = false;
			} else if (location.getLocType() == BDLocation.TypeNetWorkException) {
				sb.append("\ndescribe : ");
				sb.append("网络不同导致定位失败，请检查网络是否通畅");
				result.message = "网络不同导致定位失败，请检查网络是否通畅";
				result.isSuccess = false;
			} else if (location.getLocType() == BDLocation.TypeCriteriaException) {
				sb.append("\ndescribe : ");
				sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
				result.message = "无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机";
				result.isSuccess = false;
			}
			sb.append("\nlocationdescribe : ");
			sb.append(location.getLocationDescribe());// 位置语义化信息
			List<Poi> list = location.getPoiList();// POI数据
			if (list != null) {
				sb.append("\npoilist size = : ");
				sb.append(list.size());
				for (Poi p : list) {
					sb.append("\npoi= : ");
					sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
				}
			}
			while (!isListEmpty()) {
				synchronized (lock) {
					OnLocationFinishedListener listener = mListenerArray.remove(0);
					listener.onLocationFinished(result);
				}
			}
			mLastBDLocation = result;
			Log.i("BaiduLocationApiDem", sb.toString());
			mLocationClient.stop();
		}

	}

	private boolean isListEmpty() {
		synchronized (lock) {
			return mListenerArray.isEmpty();
		}
	}

	@Override
	public void onRequestSuccess(RequestType type, JSONObject result) {
		//上传成功删掉数据
		mGpsDao.deleteAll();
		SharePreferenceManager.getInstance().updateBoolean(BridgeDetectionApplication.mCurrentUser.getUserId()+ "gpsfail", false);
		if(activity != null){
			activity.toast("上传gps轨迹成功");
			if(activity instanceof HomePageActivity){
				((HomePageActivity) activity).hidden();
			}
		}
	}

	@Override
	public void onRequestFail(RequestType type, String resultCode, String result) {
		if(activity != null){
			activity.toast("上传gps轨迹失败");
		}
		SharePreferenceManager.getInstance().updateBoolean(BridgeDetectionApplication.mCurrentUser.getUserId()+ "gpsfail", true);
	}
}
