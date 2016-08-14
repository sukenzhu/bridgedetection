package com.suken.bridgedetection.fragment;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.googlecode.androidannotations.api.BackgroundExecutor;
import com.suken.bridgedetection.BridgeDetectionApplication;
import com.suken.bridgedetection.Constants;
import com.suken.bridgedetection.R;
import com.suken.bridgedetection.RequestType;
import com.suken.bridgedetection.activity.BaseActivity;
import com.suken.bridgedetection.activity.BridgeDetectionListActivity;
import com.suken.bridgedetection.activity.WebViewActivity;
import com.suken.bridgedetection.http.HttpTask;
import com.suken.bridgedetection.http.OnReceivedHttpResponseListener;
import com.suken.bridgedetection.location.LocationManager;
import com.suken.bridgedetection.location.LocationResult;
import com.suken.bridgedetection.location.OnLocationFinishedListener;
import com.suken.bridgedetection.storage.*;
import com.suken.bridgedetection.util.OnSyncDataFinishedListener;
import com.suken.bridgedetection.util.UiUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomePageFragment extends BaseFragment implements OnClickListener, OnLocationFinishedListener, OnSyncDataFinishedListener {

    private TextView mjingdu;
    private TextView mWeidu;
    private TextView mDeviceId;
    private TextView mLastLogin = null;
    private TextView time = null;
    private TextView tipsNum1 = null;
    private TextView tipsNum2 = null;
    private TextView tipsNum3 = null;
    private TextView tipsNum4 = null;
    private View tipLayout1 = null;
    private View tipLayout2 = null;
    private View tipLayout3 = null;
    private View tipLayout4 = null;
    private View gpsGjWarnView = null;

    private View mContentView = null;
    private boolean mIsGpsSuccess = false;
    private List<HomeFragmentItemController> list = new ArrayList<HomeFragmentItemController>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            time.setText(UiUtil.formatNowTime());
            sendEmptyMessageDelayed(0, 1000);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (HomeFragmentItemController con : list) {
            con.destory();
        }
        list.clear();
        mjingdu = null;
        mWeidu = null;
        mDeviceId = null;
        mLastLogin = null;
        time = null;
        tipsNum1 = null;
        tipsNum2 = null;
        tipsNum3 = null;
        tipsNum4 = null;
        tipLayout1 = null;
        tipLayout2 = null;
        tipLayout2 = null;
        tipLayout3 = null;
        tipLayout4 = null;
        handler.removeMessages(0);
        handler = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContentView = view;
        mjingdu = (TextView) view.findViewById(R.id.home_jingdu);
        mWeidu = (TextView) view.findViewById(R.id.home_weidu);
        mDeviceId = (TextView) view.findViewById(R.id.home_deviceId);
        tipsNum1 = (TextView) view.findViewById(R.id.left_frag_item_num1);
        tipsNum2 = (TextView) view.findViewById(R.id.left_frag_item_num2);
        tipsNum3 = (TextView) view.findViewById(R.id.left_frag_item_num3);
        tipsNum4 = (TextView) view.findViewById(R.id.left_frag_item_num4);
        tipLayout1 = view.findViewById(R.id.left_frag_item_layout1);
        tipLayout2 = view.findViewById(R.id.left_frag_item_layout2);
        tipLayout3 = view.findViewById(R.id.left_frag_item_layout3);
        tipLayout4 = view.findViewById(R.id.left_frag_item_layout4);
        TextView currentVersion = (TextView) view.findViewById(R.id.currentVersion);
        PackageInfo info;
        try {
            info = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            String versionName = info.versionName;
            currentVersion.setText("当前版本：" + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        mDeviceId.setText("设备号:" + UiUtil.genDeviceId());
        mLastLogin = (TextView) view.findViewById(R.id.last_login);
        mLastLogin.setText("上次登录:" + SharePreferenceManager.getInstance().readString("上次登录", UiUtil.formatNowTime("yyyy-MM-dd")));
        time = (TextView) view.findViewById(R.id.home_time);
        time.setText(UiUtil.formatNowTime());
        gpsGjWarnView = view.findViewById(R.id.gpsgjwarn);
        gpsGjWarnView.setOnClickListener(this);
        handler.sendEmptyMessageDelayed(0, 1000);

        list.add(new HomeFragmentItemController(this, mContentView, R.drawable.richangyanghu, "日常养护"));
        list.add(new HomeFragmentItemController(this, mContentView, R.drawable.zhiliangchoujian, "质量抽检"));
        list.add(new HomeFragmentItemController(this, mContentView, R.drawable.zhuanxianggongcheng, "专项工程"));
        list.add(new HomeFragmentItemController(this, mContentView, R.drawable.qiaoliangjiancha, "桥梁检查"));
        if(BridgeDetectionApplication.isHiddenSuidao){
            View suidaojiancha = mContentView.findViewById(R.drawable.suidaojiancha);
            View suidaoxuncha = mContentView.findViewById(R.drawable.suidaoxuncha);

            View qiaoliangxuncha = mContentView.findViewById(R.drawable.qiaoliangxuncha);
            View jidianxuncha = mContentView.findViewById(R.drawable.jidianxuncha);
            View luzhengxunshi = mContentView.findViewById(R.drawable.luzhengxunshi);

            View yingjishijian = mContentView.findViewById(R.drawable.yingjishijian);


            suidaojiancha.setId(R.drawable.qiaoliangxuncha);
            qiaoliangxuncha.setId(R.drawable.suidaojiancha);


            qiaoliangxuncha.setId(R.drawable.luzhengxunshi);

            luzhengxunshi.setId(R.drawable.suidaojiancha);


            suidaoxuncha.setId(R.drawable.jidianxuncha);

            jidianxuncha.setId(R.drawable.suidaoxuncha);

            luzhengxunshi.setId(R.drawable.yingjishijian);

            yingjishijian.setId(R.drawable.suidaojiancha);


            mContentView.findViewById(R.drawable.suidaoxuncha).setVisibility(View.INVISIBLE);
            mContentView.findViewById(R.drawable.suidaojiancha).setVisibility(View.INVISIBLE);

        }
        list.add(new HomeFragmentItemController(this, mContentView, R.drawable.suidaojiancha, "隧道检查"));
        list.add(new HomeFragmentItemController(this, mContentView, R.drawable.qiaoliangxuncha, "桥梁巡查"));
        list.add(new HomeFragmentItemController(this, mContentView, R.drawable.suidaoxuncha, "隧道巡查"));
        list.add(new HomeFragmentItemController(this, mContentView, R.drawable.luzhengxunshi, "路政巡视"));
        list.add(new HomeFragmentItemController(this, mContentView, R.drawable.jidianxuncha, "桥梁抽查"));
        list.add(new HomeFragmentItemController(this, mContentView, R.drawable.yingjishijian, "应急事件"));
    }


    @Override
    public void onClick(View v) {
        int vid = v.getId();

        if (vid == R.id.gpsgjwarn) {
            LocationManager.getInstance().updateGps(true, true, (BaseActivity) getActivity());
            return;
        }
        if(BridgeDetectionApplication.mCurrentUser.getRoles() == null){
            toast("无权限");
            return;
        }

        if(vid == R.drawable.qiaoliangjiancha|| vid == R.drawable.qiaoliangxuncha){
            if(!BridgeDetectionApplication.mCurrentUser.getRoles().contains("highway_qlxc")){
                toast("无权限");
                return;
            }
        }

        if(vid == R.drawable.suidaojiancha|| vid == R.drawable.suidaoxuncha){
            if(!BridgeDetectionApplication.mCurrentUser.getRoles().contains("highway_sdxc")){
                toast("无权限");
                return;
            }
        }

        if (vid == R.drawable.qiaoliangjiancha || vid == R.drawable.qiaoliangxuncha || vid == R.drawable.suidaojiancha || vid == R.drawable.suidaoxuncha) {
            Intent intent = new Intent(getActivity(), BridgeDetectionListActivity.class);
            intent.putExtra("type", vid);
            startActivity(intent);
        } else if(vid == R.drawable.jidianxuncha) {
            startActivity(new Intent(getActivity(), WebViewActivity.class));
        } else  {
                toast("敬请期待!");
        }
    }

    @Override
    public void onLocationFinished(LocationResult result) {

        if(getActivity() == null || ((BaseActivity)getActivity()).isDestroyed() || getActivity().isFinishing()){
            return;
        }

        if (result.isSuccess) {
            mIsGpsSuccess = true;
            mjingdu.setText("经度:" + result.longitude);
            mWeidu.setText("纬度:" + result.latitude);
            TextView tv = (TextView) getActivity().findViewById(R.id.syncLocationTv);
            tv.setText("定位成功");
            tv.setTextColor(Color.WHITE);
        } else if(!mIsGpsSuccess){
            TextView tv = (TextView) getActivity().findViewById(R.id.syncLocationTv);
            tv.setText("定位失败");
            tv.setTextColor(Color.RED);
        }
    }

    public void onSelected() {

        android.location.LocationManager locationManager = (android.location.LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER) && !mIsGpsSuccess) {
            mContentView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (getActivity()!= null &&  !getActivity().isFinishing()) {
                        LocationManager.getInstance().syncLocation(HomePageFragment.this);
                    }
                }
            }, 200);
        }

        BackgroundExecutor.execute(new Runnable() {

            @Override
            public void run() {

                boolean showGpsGjWarn = SharePreferenceManager.getInstance().readBoolean(BridgeDetectionApplication.mCurrentUser.getUserId() + "gpsfail", false);
                GpsGjDataDao gjDataDao = new GpsGjDataDao();
                int failCount = gjDataDao.countQueryGpsData();
                final boolean showGpsGj = showGpsGjWarn && failCount > 0;

                getActivity().runOnUiThread(new Runnable() {

                                                @Override
                                                public void run() {

                                                    if (showGpsGj) {
                                                        gpsGjWarnView.setVisibility(View.VISIBLE);
                                                    } else {
                                                        gpsGjWarnView.setVisibility(View.GONE);
                                                    }
                                                }
                                            }
                );
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        BaseActivity activity = (BaseActivity) getActivity();
        if(activity.isDestroyed()){
            return;
        }
        onSelected();
        try {
            calculateNotUpdate();
        } catch (Throwable throwable){
            throwable.printStackTrace();
        }

    }

    private void calculateNotUpdate(){
        BackgroundExecutor.execute(new Runnable() {

            @Override
            public void run() {

                final int count1 = new CheckFormAndDetailDao().countTypeAndStatus(R.drawable.qiaoliangjiancha, Constants.STATUS_UPDATE);
                final int count2 = new CheckFormAndDetailDao().countTypeAndStatus(R.drawable.suidaojiancha, Constants.STATUS_UPDATE);
                final int count3 = new SdxcFormAndDetailDao().countTypeAndStatus(R.drawable.qiaoliangxuncha, Constants.STATUS_UPDATE);
                final int count4 = new SdxcFormAndDetailDao().countTypeAndStatus(R.drawable.suidaoxuncha, Constants.STATUS_UPDATE);

                boolean showGpsGjWarn = SharePreferenceManager.getInstance().readBoolean(BridgeDetectionApplication.mCurrentUser.getUserId() + "gpsGjFail", false);
                GpsGjDataDao gjDataDao = new GpsGjDataDao();
                int failCount = gjDataDao.countQueryGpsData();
                final boolean showGpsGj = showGpsGjWarn && failCount > 0;
                if(getActivity() == null){
                    return;
                }

                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            if (showGpsGj) {
                                gpsGjWarnView.setVisibility(View.VISIBLE);
                            } else {
                                gpsGjWarnView.setVisibility(View.GONE);
                            }

                            if (count1 > 0) {
                                tipsNum1.setText(count1 + "");
                                tipLayout1.setVisibility(View.VISIBLE);
                            } else {
                                tipLayout1.setVisibility(View.GONE);
                            }
                            if (count2 > 0) {
                                tipsNum2.setText(count2 + "");
                                tipLayout2.setVisibility(View.VISIBLE);
                            } else {
                                tipLayout2.setVisibility(View.GONE);
                            }
                            if (count3 > 0) {
                                tipsNum3.setText(count3 + "");
                                tipLayout3.setVisibility(View.VISIBLE);
                            } else {
                                tipLayout3.setVisibility(View.GONE);
                            }
                            if (count4 > 0) {
                                tipsNum4.setText(count4 + "");
                                tipLayout4.setVisibility(View.VISIBLE);
                            } else {
                                tipLayout4.setVisibility(View.GONE);
                            }

                        }catch (Throwable throwable){

                        }
                    }
                });


                OnReceivedHttpResponseListener responseListener = new OnReceivedHttpResponseListener() {
                    @Override
                    public void onRequestSuccess(RequestType type, JSONObject result) {

                        JSONArray array = result.getJSONArray("datas");
                        int qlCount = 0;
                        int hdCount = 0;
                        int sdjcNoCount = 0;

                        if(array == null){
                            return;
                        }

                        for(int i = 0;i < array.size(); i++){
                            JSONObject object = array.getJSONObject(i);
                            if("b".equals(object.getString("qhlx"))){
                                qlCount++;
                            } else if("c".equals(object.getString("qhlx"))){
                                hdCount++;
                            } else if("t".equals(object.getString("qhlx"))){
                                sdjcNoCount++;
                            }
                        }

                        if (qlCount + hdCount + sdjcNoCount > 0) {
                           final  StringBuilder sb = new StringBuilder();
                            if (qlCount > 0) sb.append("未检查桥梁" + qlCount + "座,");
                            if (hdCount > 0) sb.append("涵洞" + hdCount + "道,");
                            if (sdjcNoCount > 0) sb.append("隧道" + sdjcNoCount + "道,");

                            sb.deleteCharAt(sb.length() - 1);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String ns = Context.NOTIFICATION_SERVICE;
                                    NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(ns);
                                    // 定义通知栏展现的内容信息
                                    int icon = R.drawable.ic_launcher;
                                    CharSequence tickerText = "提醒";
                                    long when = System.currentTimeMillis();
                                    Notification notification = new Notification(icon, tickerText, when);

                                    // 定义下拉通知栏时要展现的内容信息
                                    Context context = getActivity();
                                    CharSequence contentTitle = "本月剩余未检查提醒";
                                    CharSequence contentText = sb.toString();
                                    notification.setLatestEventInfo(context, contentTitle, contentText, null);
                                    // 用mNotificationManager的notify方法通知用户生成标题栏消息通知
                                    mNotificationManager.notify(1, notification);
                                    Vibrator vib = (Vibrator) getActivity().getSystemService(Service.VIBRATOR_SERVICE);
                                    vib.vibrate(300);
                                    Toast.makeText(getActivity(), contentText, Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    }

                    @Override
                    public void onRequestFail(RequestType type, String resultCode, String result) {

                    }
                };

                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(System.currentTimeMillis());
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

                if(maxDay - day <= 7) {
                    List<NameValuePair> list = new ArrayList<NameValuePair>();
                    BasicNameValuePair pair = new BasicNameValuePair("userId", BridgeDetectionApplication.mCurrentUser.getUserId());
                    list.add(pair);
                    pair = new BasicNameValuePair("token", BridgeDetectionApplication.mCurrentUser.getToken());
                    list.add(pair);
                    new HttpTask(responseListener, RequestType.remainNotCheck).executePost(list);
                }


            }
        });
    }

    @Override
    public void onSyncFinished(boolean isSuccess) {
        if (getActivity() == null) {
            return;
        }
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                initGPS();
            }
        });
    }

    private void initGPS() {
        android.location.LocationManager locationManager = (android.location.LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        // 判断GPS模块是否开启，如果没有则开启
        if (!locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this.getActivity());
            dialog.setTitle("提醒");
            dialog.setMessage("请打开GPS");
            dialog.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // 转到手机设置界面，用户设置GPS
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, 0); // 设置完成后返回到原来的界面
                }
            });
            dialog.setNeutralButton("取消", new android.content.DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.dismiss();
                    LocationManager.getInstance().syncLocation(HomePageFragment.this);
                }
            });
            dialog.setCancelable(false);
            try {
                dialog.show();
            }catch (Throwable e){

            }
        } else {
            LocationManager.getInstance().syncLocation(this);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            onSelected();
        }
    }
}