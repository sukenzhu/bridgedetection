package com.suken.bridgedetection.http;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.suken.bridgedetection.BridgeDetectionApplication;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.suken.bridgedetection.Constants;
import com.suken.bridgedetection.RequestType;
import com.suken.bridgedetection.storage.SharePreferenceManager;
import com.suken.bridgedetection.util.UiUtil;

import android.text.TextUtils;
import android.util.Log;

public class HttpTask {
//	 public static String REQUEST_IP = "binglee.wicp.net";

    public static String REQUEST_IP = "qljc.hbgsyh.com";

    public static String REQUEST_PORT = "";

    private RequestType mRequestType = null;

    private OnReceivedHttpResponseListener mResponseListener;

    private String getUrl(String suffix) {
        String ip = SharePreferenceManager.getInstance().readString("ip", REQUEST_IP);
        String port = SharePreferenceManager.getInstance().readString("port", REQUEST_PORT);
        String portStr = "";
        if (!TextUtils.isEmpty(port)) {
            portStr = ":" + port;
        }
        return "http://" + ip + portStr + suffix;
    }

    public HttpTask(OnReceivedHttpResponseListener listener, RequestType type) {
        super();
        mResponseListener = listener;
        mRequestType = type;
    }

    public void uploadFile(List<NameValuePair> parameters, String... files) {

        // 和GET方式一样，先将参数放入List
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpPost postMethod = new HttpPost(getUrl(mRequestType.getUrl()));
            MultipartEntity entity = new MultipartEntity();
            for (String s : files) {
                if (!TextUtils.isEmpty(s)) {
                    File f = new File(s);
                    ContentBody cbFile = new FileBody(new File(s));
                    entity.addPart(f.getName(), cbFile);
                }
            }
            for (NameValuePair pair : parameters) {
                entity.addPart(pair.getName(), new StringBody(pair.getValue()));
            }
            postMethod.setEntity(entity);
            HttpResponse response = httpClient.execute(postMethod); // 执行POST方法
            int resultCode = response.getStatusLine().getStatusCode();
            String result = EntityUtils.toString(response.getEntity(), "utf-8");
            if (resultCode == 200) {
                JSONObject obj = JSON.parseObject(result);
                String errorCode = obj.getString(Constants.ERRORCODE);
                String errorMsg = obj.getString(Constants.ERRORMSG);
                if (TextUtils.equals(errorCode, "200")) {
                    mResponseListener.onRequestSuccess(mRequestType, obj);
                } else {
                    mResponseListener.onRequestFail(mRequestType, errorCode, errorMsg);
                }
            } else {
                mResponseListener.onRequestFail(mRequestType, resultCode + "", result);
            }
        } catch (Exception e) {
            mResponseListener.onRequestFail(mRequestType, "-100", e.getMessage());
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

    }

    public void executePostBackground(final List<NameValuePair> parameters) {

        new Thread(new Runnable() {

            @Override
            public void run() {
                executePost(parameters);
            }
        }).start();
    }

    public void executePost(List<NameValuePair> parameters) {

        long start = System.currentTimeMillis();
        // 和GET方式一样，先将参数放入List
        HttpClient httpClient = new DefaultHttpClient();
        int resultCode = -100;
        try {
            // 请求超时
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
            // 读取超时
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
            HttpPost postMethod = new HttpPost(getUrl(mRequestType.getUrl()));
            postMethod.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            postMethod.setEntity(new UrlEncodedFormEntity(parameters, "utf-8")); // 将参数填入POST
            HttpResponse response = httpClient.execute(postMethod); // 执行POST方法
            long postTime = System.currentTimeMillis();
            BridgeDetectionApplication.getInstance().write("RequestType : " + mRequestType.getDesc() + " : postTime " + (postTime - start));
            resultCode = response.getStatusLine().getStatusCode();
            String result = "";
            if (mRequestType == RequestType.syncData) {
                byte[] reArray = EntityUtils.toByteArray(response.getEntity());
                reArray = UiUtil.unGZip(reArray);
                reArray = Base64.decode(new String(reArray, "utf-8"));
                result = new String(reArray, "utf-8");
            } else {
                result = EntityUtils.toString(response.getEntity(), "utf-8");
            }
            BridgeDetectionApplication.getInstance().write("result length : " + result.length());

            if (resultCode == 200) {
                JSONObject obj = JSON.parseObject(result);
                BridgeDetectionApplication.getInstance().write("RequestType : " + mRequestType.getDesc() + " : parseObject " + (System.currentTimeMillis() - postTime));
                if (mRequestType == RequestType.update) {
                    mResponseListener.onRequestSuccess(mRequestType, obj);
                    return;
                }
                String errorCode = obj.getString(Constants.ERRORCODE);
                String errorMsg = obj.getString(Constants.ERRORMSG);
                if (TextUtils.equals(errorCode, "200")) {
                    mResponseListener.onRequestSuccess(mRequestType, obj);
                } else {
                    mResponseListener.onRequestFail(mRequestType, errorCode, errorMsg);
                }
            } else {
                mResponseListener.onRequestFail(mRequestType, resultCode + "", result);
            }
            BridgeDetectionApplication.getInstance().write("RequestType : " + mRequestType.getDesc() + " : handleResultTime " + (System.currentTimeMillis() - postTime));
        } catch (Exception e) {
            e.printStackTrace();
            if (resultCode != 200) {
                mResponseListener.onRequestFail(mRequestType, "-100", e.getMessage());
            }
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        BridgeDetectionApplication.getInstance().write("RequestType : " + mRequestType.getDesc() + " : totalTime " + (System.currentTimeMillis() - start));
    }

}
