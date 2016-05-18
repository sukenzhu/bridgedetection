package com.suken.bridgedetection.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.*;
import com.baidu.location.LocationClientOption.LocationMode;
import com.googlecode.androidannotations.api.BackgroundExecutor;
import com.suken.bridgedetection.BridgeDetectionApplication;
import com.suken.bridgedetection.Constants;
import com.suken.bridgedetection.RequestType;
import com.suken.bridgedetection.activity.BaseActivity;
import com.suken.bridgedetection.activity.HomePageActivity;
import com.suken.bridgedetection.http.HttpTask;
import com.suken.bridgedetection.http.OnReceivedHttpResponseListener;
import com.suken.bridgedetection.storage.GpsGjData;
import com.suken.bridgedetection.storage.GpsGjDataDao;
import com.suken.bridgedetection.storage.SharePreferenceManager;
import com.suken.bridgedetection.util.NetWorkUtil;
import com.suken.bridgedetection.util.NetWorkUtil.ConnectType;
import com.suken.bridgedetection.util.UiUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LocationManager implements OnReceivedHttpResponseListener {

    private static LocationManager mInstance;
    private LocationClient mLocationClient;
    private BDLocationListener myListener = new MyLocationListener();
    private LocationResult mLastBDLocation = null;
    private List<OnLocationFinishedListener> mListenerArray = new ArrayList<OnLocationFinishedListener>();
    private Object lock = new Object();
    private GpsGjDataDao mGpsDao = new GpsGjDataDao();


    private long lastRetryTime = -1;

    private boolean mIsBaiduLocation = false;

    private OnLocationFinishedListener recordListener = new OnLocationFinishedListener() {

        @Override
        public void onLocationFinished(LocationResult result) {
            if (result.isSuccess) {
                mGpsDao.create(new GpsGjData(Double.toString(result.longitude), Double.toString(result.latitude), Double.toString(result.altitude), result.time, result.wz));
                updateGps(false, false, null);
            }
            int b = SharePreferenceManager.getInstance().readInt(Constants.INTERVAL, 50);
            mLocationHandler.sendEmptyMessageDelayed(0, b * 1000);
        }
    };

    private Handler mLocationHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            if (hour > 7 && hour < 19) {
                syncLocation(recordListener);
            } else {
                int b = SharePreferenceManager.getInstance().readInt(Constants.INTERVAL, 50);
                mLocationHandler.sendEmptyMessageDelayed(0, b * 1000);
                if (mIsBaiduLocation) {
//                    if (mLocationClient.isStarted()) {
//                        mLocationClient.stop();
//                    }
                } else {
                    Intent i = new Intent(BridgeDetectionApplication.getInstance().getApplicationContext(), LBSService.class);
                    BridgeDetectionApplication.getInstance().getApplicationContext().stopService(i);
                    if (dataReceiver != null) {
                        BridgeDetectionApplication.getInstance().getApplicationContext().unregisterReceiver(dataReceiver);// 取消注册Broadcast Receiver
                        dataReceiver = null;
                    }
                }
            }
        }
    };
    private DataReceiver dataReceiver;

    public void startRecordLocation() {
        mLocationHandler.sendEmptyMessage(0);
    }

    public void stopRecordLocation() {
        mLocationHandler.removeMessages(0);
        // 这里传一次
        updateGps(true, false, null);
        if (mIsBaiduLocation) {
            if (mLocationClient.isStarted()) {
                mLocationClient.stop();
            }
        } else {
            Intent i = new Intent(BridgeDetectionApplication.getInstance().getApplicationContext(), LBSService.class);
            BridgeDetectionApplication.getInstance().getApplicationContext().stopService(i);
            if (dataReceiver != null) {
                BridgeDetectionApplication.getInstance().getApplicationContext().unregisterReceiver(dataReceiver);// 取消注册Broadcast Receiver
                dataReceiver = null;
            }
        }
    }

    private class DataReceiver extends BroadcastReceiver {// 继承自BroadcastReceiver的子类

        @Override
        public void onReceive(Context context, Intent intent) {// 重写onReceive方法

            Bundle bundledata = intent.getExtras();
            LocationResult result = new LocationResult();
            if (bundledata != null) {
                boolean isSuccess = bundledata.getBoolean("success");
                result.isSuccess = isSuccess;
                if (isSuccess) {
                    double latitude = bundledata.getDouble("latitude");
                    double longitude = bundledata.getDouble("longitude");
                    double accuracy = bundledata.getDouble("accuracy");
                    double speed = bundledata.getDouble("speed");
                    double Satenum = bundledata.getDouble("Satenum");
                    double altitude = bundledata.getDouble("altitude");
                    String dateString = bundledata.getString("date");
                    String wz = bundledata.getString("wz");
                    result.latitude = latitude;
                    result.longitude = longitude;
                    result.altitude = altitude;
                    result.time = dateString;
                    result.wz = wz;

                    while (!isListEmpty()) {
                        synchronized (lock) {
                            OnLocationFinishedListener listener = mListenerArray.remove(0);
                            listener.onLocationFinished(result);
                        }
                    }
                    BridgeDetectionApplication.getInstance().write("\t卫星在用数量:" + Satenum + "\n\t纬度:" + latitude
                            + "\t经度:" + longitude + "\n\t精度:" + accuracy
                            + "\n\t速度:" + speed + "\n\t更新时间:" + dateString);
                    mLastBDLocation = result;
                } else {
                    Log.w("LocationManager", "syncLocation Failed!");
                }
            }

        }
    }

    private BaseActivity activity;
    private int count;

    public void updateGps(final boolean force, final boolean showLoading, final BaseActivity activity) {
        if (BridgeDetectionApplication.mCurrentUser == null) {
            return;
        }
        this.activity = activity;
        if (showLoading && activity != null) {
            activity.showLoading("上传中...");
        }
        BackgroundExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ConnectType type = NetWorkUtil.getConnectType(BridgeDetectionApplication.getInstance());
                count = mGpsDao.countQueryGpsData();
                BridgeDetectionApplication.getInstance().write("触发上传:" + count + "   " + force);

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
                    BridgeDetectionApplication.getInstance().write("开始上传gps轨迹:" + count + "   " + force);
                    new HttpTask(LocationManager.this, RequestType.updateGpsgjInfo).executePost(list);
                }
                if (showLoading && activity != null) {
                    activity.dismissLoading();
                }
            }
        });
    }

    private LocationManager() {
//        mLocationClient = new LocationClient(BridgeDetectionApplication.getInstance()); // 声明LocationClient类
//        initLocation();
//        mLocationClient.registerLocationListener(myListener); // 注册监听函数
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
        synchronized (lock) {
            if (listener != null) {
                mListenerArray.add(listener);
            }
        }

        if (mIsBaiduLocation) {
//            if (!mLocationClient.isStarted()) {
//                mLocationClient.start();
//            } else {
//                mLocationClient.requestLocation();
//            }
        } else {
            Intent i = new Intent(BridgeDetectionApplication.getInstance().getApplicationContext(), LBSService.class);
            BridgeDetectionApplication.getInstance().getApplicationContext().startService(i);
            if (dataReceiver == null) {
                dataReceiver = new DataReceiver();
                IntentFilter filter = new IntentFilter();// 创建IntentFilter对象
                filter.addAction("com.exams.demo10_lbs");
                BridgeDetectionApplication.getInstance().getApplicationContext().registerReceiver(dataReceiver, filter);// 注册Broadcast Receiver
            }
        }


    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
        option.setTimeOut(60000);
        option.setScanSpan(0);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);// 可选，默认false,设置是否使用gps
        option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

//            if(mLocationClient.getLocOption().getScanSpan() == 0){
//            mLocationClient.stop();
//            }
            LocationResult result = new LocationResult();
            // Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append(System.currentTimeMillis());
            sb.append("当前时间 : " + UiUtil.formatNowTime());
            sb.append(" ;time : ");
            sb.append(location.getTime());
            sb.append(" ;error code : ");
            sb.append(location.getLocType());
            sb.append(";latitude : ");
            result.latitude = location.getLatitude();
            sb.append(location.getLatitude());
            sb.append(" ;lontitude : ");
            result.longitude = location.getLongitude();
            result.altitude = location.getAltitude() > 0 ? location.getAltitude() : 0;
            result.time = location.getTime();
            result.wz = location.getAddrStr();
            sb.append(location.getLongitude());
            sb.append(" ;radius : ");
            sb.append(location.getRadius());
            sb.append(" ;time : ");
            sb.append(location.getTime());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append(" ;speed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append(" ;satellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append(" ;height : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append(" ;direction : ");
                sb.append(location.getDirection());// 单位度
                sb.append(" ;addr : ");
                sb.append(location.getAddrStr());
                sb.append(";describe : ");
                sb.append(" ;gps定位成功");
                result.message = "gps定位成功";
                result.isSuccess = true;

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append(" ;addr : ");
                sb.append(location.getAddrStr());
                // 运营商信息
                sb.append(" ;operationers : ");
                sb.append(location.getOperators());
                sb.append(" ;describe : ");
                sb.append("网络定位成功");
                result.message = "网络定位成功";
                android.location.LocationManager locationManager = (android.location.LocationManager) BridgeDetectionApplication.getInstance().getSystemService(Context.LOCATION_SERVICE);
                if (locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
                    result.isSuccess = true;
                } else {
                    result.isSuccess = false;
                }
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append(" ;describe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
                result.message = "离线定位成功，离线定位结果也是有效的";
                android.location.LocationManager locationManager = (android.location.LocationManager) BridgeDetectionApplication.getInstance().getSystemService(Context.LOCATION_SERVICE);
                if (locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
                    result.isSuccess = true;
                } else {
                    result.isSuccess = false;
                }
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append(" ;describe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                result.message = "服务端网络定位失败";
                result.isSuccess = false;
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append(" ;describe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
                result.message = "网络不同导致定位失败，请检查网络是否通畅";
                result.isSuccess = false;
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append(" ;describe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                result.message = "无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机";
                result.isSuccess = false;
            }
            sb.append(" ;locationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append(" ;poilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append(" ;poi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            mLastBDLocation = result;
            long now = System.currentTimeMillis() - lastRetryTime;
            if (now > 2000 * 60 && !result.isSuccess) {
                syncLocation(null);
                lastRetryTime = System.currentTimeMillis();
            } else {
                while (!isListEmpty()) {
                    synchronized (lock) {
                        OnLocationFinishedListener listener = mListenerArray.remove(0);
                        listener.onLocationFinished(result);
                    }
                }
            }
            BridgeDetectionApplication.getInstance().write("\n" + sb.toString() + "\n");
            Log.i("BaiduLocationApiDem", sb.toString());
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
        BridgeDetectionApplication.getInstance().write("上传成功gps轨迹条数:" + count);
        count = 0;
        SharePreferenceManager.getInstance().updateBoolean(BridgeDetectionApplication.mCurrentUser.getUserId() + "gpsfail", false);
        if (activity != null) {
            activity.toast("上传gps轨迹成功");
            if (activity instanceof HomePageActivity) {
                ((HomePageActivity) activity).hidden();
            }
            activity = null;
        }
    }

    @Override
    public void onRequestFail(RequestType type, String resultCode, String result) {
        BridgeDetectionApplication.getInstance().write("上传失败gps轨迹条数:" + count + "  " + result);
        if (activity != null) {
            activity.toast(result);
            activity = null;
        }
        SharePreferenceManager.getInstance().updateBoolean(BridgeDetectionApplication.mCurrentUser.getUserId() + "gpsfail", true);
    }
}
