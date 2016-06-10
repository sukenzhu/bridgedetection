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
import com.googlecode.androidannotations.api.BackgroundExecutor;
import com.suken.bridgedetection.BridgeDetectionApplication;
import com.suken.bridgedetection.Constants;
import com.suken.bridgedetection.RequestType;
import com.suken.bridgedetection.activity.BaseActivity;
import com.suken.bridgedetection.activity.BridgeFormActivity;
import com.suken.bridgedetection.activity.HomePageActivity;
import com.suken.bridgedetection.http.HttpTask;
import com.suken.bridgedetection.http.OnReceivedHttpResponseListener;
import com.suken.bridgedetection.storage.GpsGjData;
import com.suken.bridgedetection.storage.GpsGjDataDao;
import com.suken.bridgedetection.storage.SharePreferenceManager;
import com.suken.bridgedetection.util.NetWorkUtil;
import com.suken.bridgedetection.util.NetWorkUtil.ConnectType;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LocationManager implements OnReceivedHttpResponseListener {

    private static LocationManager mInstance;
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
        Intent i = new Intent(BridgeDetectionApplication.getInstance().getApplicationContext(), LBSService.class);
        BridgeDetectionApplication.getInstance().getApplicationContext().stopService(i);
        if (dataReceiver != null) {
            BridgeDetectionApplication.getInstance().getApplicationContext().unregisterReceiver(dataReceiver);// 取消注册Broadcast Receiver
            dataReceiver = null;
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
                    if(BridgeDetectionApplication.getInstance().mCurrentActivity instanceof BridgeFormActivity){
                        ((BridgeFormActivity) BridgeDetectionApplication.getInstance().mCurrentActivity).onLocationSucess(result);
                    }
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

        Intent i = new Intent(BridgeDetectionApplication.getInstance().getApplicationContext(), LBSService.class);
        BridgeDetectionApplication.getInstance().getApplicationContext().startService(i);
        if (dataReceiver == null) {
            dataReceiver = new DataReceiver();
            IntentFilter filter = new IntentFilter();// 创建IntentFilter对象
            filter.addAction("com.exams.demo10_lbs");
            BridgeDetectionApplication.getInstance().getApplicationContext().registerReceiver(dataReceiver, filter);// 注册Broadcast Receiver
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
