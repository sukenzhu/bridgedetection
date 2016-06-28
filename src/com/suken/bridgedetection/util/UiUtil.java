package com.suken.bridgedetection.util;

import android.app.*;
import android.content.*;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
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
import com.suken.bridgedetection.storage.*;
import com.suken.bridgedetection.util.NetWorkUtil.ConnectType;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.*;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class UiUtil {

    public static float DP = -1f;
    public static int densityDpi = 480;
    public static int width = 0;
    public static int height = 0;

    public static boolean isUpdating = false;

    public static float getDp(Activity context) {
        if (DP == -1f) {
            DisplayMetrics dm = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getMetrics(dm);
            DP = dm.density;
            densityDpi = (int) (dm.densityDpi * DP);
            width = dm.widthPixels;
            height = dm.heightPixels;
        }
        return DP;
    }

    public static void reInitWidthHeight(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;
    }

    private static final double PI = 3.14159265358979323; //圆周率
    private static final double R1 = 6371229;             //地球的半径


    /**
     * 根据两个位置的经纬度，来计算两地的距离（单位为KM）
     * 参数为String类型
     *
     * @return
     */
    public static double getDistance(double longt1, double lat1, double longt2, double lat2) {

        double x, y, distance;
        x = (longt2 - longt1) * PI * R1 * Math.cos(((lat1 + lat2) / 2) * PI / 180) / 180;
        y = (lat2 - lat1) * PI * R1 / 180;
        distance = Math.hypot(x, y) / 1000;

        return distance;
    }

    public static void syncData(final BaseActivity activity) {
        syncData(activity, false);
    }

    public static void syncData(final BaseActivity activity, final boolean isJustLastUpdate, final OnSyncDataFinishedListener syncListener) {

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
                        int type1 = type == RequestType.lastqhjcInfo ? R.drawable.qiaoliangjiancha : (type == RequestType.lastsdxcinfo ? R.drawable.suidaoxuncha : R.drawable.suidaojiancha);
                        if (list != null && list.size() > 0) {
                            for (CheckFormData data : list) {
                                data.setType(type1);
                                data.setLastUpdate(true);
                                if (data.getZxzh().equals("297.014")) {
                                    Log.e("", data.toString());
                                }
                                if (TextUtils.isEmpty(data.getGldwId())) {
                                    data.setGldwId(data.getYhjgId());
                                }
                            }
                            CheckFormAndDetailDao dao = new CheckFormAndDetailDao();
                            dao.deleteLastUpdateByType(type1);
                            dao.create(list);
                        }
                        break;
                    }
                    case lastsdxcinfo: {
                        List<SdxcFormData> list = JSON.parseArray(obj.getString("datas"), SdxcFormData.class);
                        if (list != null && list.size() > 0) {
                            for (SdxcFormData data : list) {
                                data.setType(R.drawable.suidaoxuncha);
                                data.setLastUpdate(true);
                                if (TextUtils.isEmpty(data.getGldwId())) {
                                    data.setGldwId(data.getYhjgId());
                                }
                            }
                            SdxcFormAndDetailDao dao = new SdxcFormAndDetailDao();
                            dao.deleteLastUpdateByType(R.drawable.suidaoxuncha);
                            dao.create(list);
                        }
                        break;
                    }
                    case syncData: {
                        // 桥梁 bridges 后面就是之前的data
                        // culverts 涵洞的key
                        // tunnels 隧道
                        // dictionarys 系统字典的
                        // brgengineers 桥涵工程师的 tunengineers 隧道的

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
                builder.append(result + "(" + resultCode + "),");
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
                // if (!isJustLastUpdate) {
                // new HttpTask(listener,
                // RequestType.gxlxInfo).executePost(list);
                // new HttpTask(listener,
                // RequestType.qlBaseData).executePost(list);
                // new HttpTask(listener,
                // RequestType.hdBaseData).executePost(list);
                // new HttpTask(listener,
                // RequestType.sdBaseData).executePost(list);
                // new HttpTask(listener,
                // RequestType.qhyhzrInfo).executePost(list);
                // new HttpTask(listener,
                // RequestType.sdyhzrInfo).executePost(list);
                // new HttpTask(listener,
                // RequestType.ywzddmInfo).executePost(list);
                // }
                // new HttpTask(listener,
                // RequestType.lastqhjcInfo).executePost(list);
                // new HttpTask(listener,
                // RequestType.lastsdjcInfo).executePost(list);
                if (isJustLastUpdate) {
                    int currentType = R.drawable.qiaoliangjiancha;
                    if (BridgeDetectionApplication.mCurrentActivity instanceof BridgeDetectionListActivity) {
                        currentType = ((BridgeDetectionListActivity) (activity)).getCurrentType();
                    }
                    if (currentType == R.drawable.qiaoliangjiancha || currentType == R.drawable.qiaoliangxuncha) {
                        new HttpTask(listener, RequestType.lastqhjcInfo).executePost(list);
                    } else if (currentType == R.drawable.suidaojiancha) {
                        new HttpTask(listener, RequestType.lastsdjcInfo).executePost(list);
                    } else if (currentType == R.drawable.suidaoxuncha) {
                        new HttpTask(listener, RequestType.lastsdxcinfo).executePost(list);
                    }
                } else {
                    new HttpTask(listener, RequestType.syncData).executePost(list);
                }
                String msg = builder.toString();
                if (TextUtils.isEmpty(msg)) {
                    SharePreferenceManager.getInstance().updateString("lastSyncTime", System.currentTimeMillis() + "");
                    activity.toast("数据同步成功");
                    if (syncListener != null) {
                        syncListener.onSyncFinished(true);
                    }
                } else {
                    msg = msg.substring(0, msg.length() - 1);
                    activity.toast(msg + "数据同步失败，请在设置中重试同步！");
                    if (syncListener != null) {
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

    public static String formatNowHourTime() {
        Date nowTime = new Date();
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
        return time.format(nowTime);
    }

    public static String formatNowTime(String format) {
        Date nowTime = new Date();
        SimpleDateFormat time = new SimpleDateFormat(format);
        return time.format(nowTime);
    }

    public static String formatTime(long time) {
        SimpleDateFormat str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return str.format(time);
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
        if (manager != null) {
            int hash = ((manager.getDeviceId() == null ? (manager.getSubscriberId() == null ? "123" : manager.getSubscriberId()) : manager.getDeviceId()).hashCode() + "").hashCode();
            return hash + "";
        }
        return "123";
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
        if (handleDialog && !(activity.isFinishing() || activity.isDestroyed())) {
            activity.dismissLoading();
        } else if (handleDialog && BridgeDetectionApplication.mCurrentActivity != null) {
            BridgeDetectionApplication.mCurrentActivity.dismissLoading();
        }
    }

    public static void updateSingle(final String qhId, final int type, final boolean handleDialog, final BaseActivity activity) {
        BackgroundExecutor.execute(new Runnable() {
            public void run() {
                isUpdating = true;
                updateSingleNotPost(qhId, type, handleDialog, activity);
                isUpdating = false;
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
                String[] pics = new String[]{};
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
                String[] vdos = new String[]{};
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

        String[] pics = new String[]{};
        if (!TextUtils.isEmpty(picattach)) {
            pics = picattach.split(",");
        }
        String[] vdos = new String[]{};
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


                                if (activity.isFinishing() || activity.isDestroyed()) {
                                    if (BridgeDetectionApplication.mCurrentActivity != null && BridgeDetectionApplication.mCurrentActivity instanceof BridgeDetectionListActivity) {
                                        ((BridgeDetectionListActivity) BridgeDetectionApplication.mCurrentActivity).updateStatus(dataId, data.getLocalId(), Constants.STATUS_AGAIN);
                                    }
                                } else {
                                    ((BridgeDetectionListActivity) activity).updateStatus(dataId, data.getLocalId(), Constants.STATUS_AGAIN);
                                }
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
                    if (type1 != RequestType.updateGps) {
                        new CheckFormAndDetailDao().create(data);
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                if (activity instanceof BridgeDetectionListActivity) {
                                    String dataId = data.getQhid();
                                    if (type == R.drawable.suidaojiancha) {
                                        dataId = data.getSdid();
                                    }
                                    if (activity.isFinishing() || activity.isDestroyed()) {
                                        if (BridgeDetectionApplication.mCurrentActivity != null && BridgeDetectionApplication.mCurrentActivity instanceof BridgeDetectionListActivity) {
                                            ((BridgeDetectionListActivity) BridgeDetectionApplication.mCurrentActivity).updateStatus(dataId, data.getLocalId(), Constants.STATUS_AGAIN);
                                        }
                                    } else {
                                        ((BridgeDetectionListActivity) activity).updateStatus(dataId, data.getLocalId(), Constants.STATUS_AGAIN);
                                    }
                                }
                            }
                        });
                        activity.toast("上传成功");
                    }
                }

                @Override
                public void onRequestFail(RequestType type, String resultCode, String result) {
                    if (type != RequestType.updateGps) {
                        activity.toast(result);
                    }
                }
            };
            data.setStatus("2");
            //data.setTjsj(UiUtil.formatNowTime());
            String json = new String(JSON.toJSONString(data));
            if (type == R.drawable.qiaoliangjiancha) {
                GpsData gpsData = new GpsDataDao().queryGpsData(Long.parseLong(qhId), data.getQhlx());
                if (gpsData != null) {
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
            ByteArrayInputStream bais = new ByteArrayInputStream(content);
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

    public static File getFileFromServer(String path, ProgressDialog pd) throws Exception {
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            //获取到文件的大小
            pd.setMax(conn.getContentLength());
            InputStream is = conn.getInputStream();
            File file = new File(Environment.getExternalStorageDirectory(), "updata.apk");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                //获取当前下载量
                pd.setProgress(total);
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            return null;
        }
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
                            final ProgressDialog pd;    //进度条对话框
                            pd = new ProgressDialog(activity);
                            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            pd.setMessage("正在下载更新");
                            pd.show();
                            BackgroundExecutor.execute(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        File file = getFileFromServer(url, pd);
                                        if (file != null) {
                                            Intent intent = new Intent();
                                            //执行动作
                                            intent.setAction(Intent.ACTION_VIEW);
                                            //执行的数据类型
                                            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                                            activity.startActivity(intent);
                                        }
                                    } catch (Exception e) {
                                        activity.toast("下载文件失败");
                                    }
                                    pd.dismiss(); //结束掉进度条对话框
                                }
                            });
                        } else {
                            dialog.dismiss();
                            if (activity instanceof HomePageActivity) {
                                ((HomePageActivity) activity).selectHome();
                            }
                        }
                    }
                };
                try {
                    builder.setTitle("更新").setMessage("检测到新版本，是否更新？").setPositiveButton("确定", listener).setNegativeButton("取消", listener).show();
                } catch (Throwable e) {

                }
            }
        });

    }

    private static boolean mIsCheckUpdate = false;

    public static void update(final BaseActivity activity) {
        if (mIsCheckUpdate) {
            return;
        }
        mIsCheckUpdate = true;
        BackgroundExecutor.execute(new Runnable() {

            @Override
            public void run() {
                OnReceivedHttpResponseListener listener = new OnReceivedHttpResponseListener() {

                    @Override
                    public void onRequestSuccess(RequestType type, JSONObject result) {
                        activity.dismissLoading();
                        int versioncode = result.getInteger("versioncode");
                        final String url = result.getString("apkurl");
                        try {
                            PackageInfo info = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
                            if (versioncode > info.versionCode) {
                                todownload(url, activity);
                            } else {
                                activity.toast("当前版本为最新版本，无需更新");
                                if (activity instanceof HomePageActivity) {
                                    ((HomePageActivity) activity).selectHome();
                                }
                            }
                        } catch (NameNotFoundException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onRequestFail(RequestType type, String resultCode, String result) {
                        activity.toast(result);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                activity.dismissLoading();
                            }
                        });

                    }
                };
                activity.showLoading("检查更新中...");
                List<NameValuePair> list = new ArrayList<NameValuePair>();
                if (BridgeDetectionApplication.mCurrentUser != null) {
                    BasicNameValuePair pair = new BasicNameValuePair("userId", BridgeDetectionApplication.mCurrentUser.getUserId());
                    list.add(pair);
                    pair = new BasicNameValuePair("token", BridgeDetectionApplication.mCurrentUser.getToken());
                    list.add(pair);
                }
                new HttpTask(listener, RequestType.update).executePost(list);
                mIsCheckUpdate = false;
            }
        });
    }

    public static void setAlarm(Context context) {
        // 操作：发送一个广播，广播接收后Toast提示定时操作完成
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setAction("android.suken.action.checkdata");
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        // 设定一个五秒后的时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 50);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
    }

    public static boolean checkValid(long lastTime) {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(lastTime);

        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(System.currentTimeMillis());

        if (c2.get(Calendar.YEAR) == c1.get(Calendar.YEAR)) {
            if (c2.get(Calendar.MONTH) == c1.get(Calendar.MONTH)) {
                return true;
            }
        }
        return false;
    }

}
