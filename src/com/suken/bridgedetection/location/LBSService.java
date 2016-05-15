package com.suken.bridgedetection.location;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.*;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suken.bridgedetection.Constants;
import com.suken.bridgedetection.storage.SharePreferenceManager;
import com.suken.bridgedetection.util.UiUtil;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

public class LBSService extends Service {

    public static final String TAG = "LBSService";
    // 30000ms --minimum time interval between location updates, in milliseconds
    private static  long minTime = Integer.MAX_VALUE;
    private static float minDistance = 0;

    String tag = this.toString();
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location location;
    private GpsStatus gStatus;
    private GpsSatelliteListener gpsSatelliteListener;
    private final IBinder mBinder = new LBSServiceBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        startService();
        Log.i(TAG, "in onCreate method.");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopService();
        Log.i(TAG, "in onDestroy method.");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        boolean gpsswitch =SharePreferenceManager.getInstance().readBoolean(Constants.GPS_SWITCH, false);
//        if(gpsswitch){
//            minTime = SharePreferenceManager.getInstance().readInt(Constants.INTERVAL, 50);
//            minTime = minTime * 1000;
//            minDistance = 10;
//        } else {
//            minDistance = 0;
//            minTime = Integer.MAX_VALUE;
//        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                -1, 10, locationListener);
        Log.i(TAG, "in startService method.");
        return super.onStartCommand(intent, flags, startId);
    }

    public class LBSServiceBinder extends Binder {
        LBSService getService() {
            return LBSService.this;
        }
    }

    public void startService() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LBSServiceListener(){
            @Override
            public void onLocationChanged(Location location) {
                super.onLocationChanged(location);
                handleLocationChanged();
            }
        };

        gpsSatelliteListener = new GpsSatelliteListener();
        locationManager.addGpsStatusListener(gpsSatelliteListener);
    }

    private void  handleLocationChanged(){
        Intent intent = new Intent();

        intent.setAction("com.exams.demo10_lbs");

        location = locationManager
                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
        gStatus = locationManager.getGpsStatus(null);
        int maxSatellites = gStatus.getMaxSatellites();
        Iterable<GpsSatellite> iterable = gStatus.getSatellites();
        Iterator<GpsSatellite> iterator = iterable.iterator();
        int x = 0;

        while (iterator != null && iterator.hasNext()
                && x <= maxSatellites) {
            GpsSatellite gpsSatellite = (GpsSatellite) iterator
                    .next();
            if (gpsSatellite.usedInFix())
                x++;

        }
        double latitude, longitude, accuracy, speed, altitude;
        long time;
        String wz = "";
        boolean isSuccess = false;
        if (location != null) {
            isSuccess = true;
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            accuracy = location.getAccuracy();
            speed = location.getSpeed();
            time = location.getTime();
            altitude = location.getAltitude() > 0 ? location.getAltitude() : 0;
            String url = "http://maps.google.cn/maps/api/geocode/json?latlng=%s,%s&sensor=true&language=zh_CN";
            url = String.format(url, latitude, longitude);

            Geocoder geocoder = new Geocoder(this);
            try {
                List<Address> address = geocoder.getFromLocation(latitude, longitude, 1); 
                if(address != null  && address.size() > 0){
                    wz = address.get(0).getAddressLine(0);
                }
            } catch (Exception e) {
            }

        } else {
            latitude = 0.0;
            longitude = 0.0;
            accuracy = 0.0;
            speed = 0.0;
            altitude = 0.0;
            time = System.currentTimeMillis();
            wz = "";
        }
        Bundle bundle = new Bundle();
        bundle.putDouble("latitude", latitude);
        bundle.putDouble("longitude", longitude);
        bundle.putDouble("accuracy", accuracy);
        bundle.putDouble("speed", speed);
        bundle.putDouble("Satenum", x);
        bundle.putDouble("altitude", altitude);
        bundle.putString("date", UiUtil.formatTime(time));
        bundle.putString("wz", wz);
        bundle.putBoolean("success", isSuccess);
        intent.putExtras(bundle);
        sendBroadcast(intent);// 锟斤拷锟酵广播
    }

    public void stopService() {
        if (locationManager != null && locationListener != null
                && gpsSatelliteListener != null) {
            locationManager.removeUpdates(locationListener);
            locationManager.removeGpsStatusListener(gpsSatelliteListener);
        }
        Log.i(TAG, "in stopService method.");
    }

}
