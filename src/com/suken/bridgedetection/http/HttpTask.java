package com.suken.bridgedetection.http;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.suken.bridgedetection.Constants;
import com.suken.bridgedetection.RequestType;

import android.text.TextUtils;

public class HttpTask {

	private static String REQUEST_IP = "121.28.74.58";

	private static String REQUEST_PORT = "8080";
	
	private RequestType mRequestType = null;

	private OnReceivedHttpResponseListener mResponseListener;

	public HttpTask(OnReceivedHttpResponseListener listener, RequestType type) {
		super();
		mResponseListener = listener;
		mRequestType = type;
	}

	public void executePost(List<NameValuePair> parameters) {
		// 和GET方式一样，先将参数放入List
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpPost postMethod = new HttpPost("http://" + REQUEST_IP + ":" + REQUEST_PORT + mRequestType.getUrl());
			postMethod.setEntity(new UrlEncodedFormEntity(parameters)); // 将参数填入POST Entity中
			HttpResponse response = httpClient.execute(postMethod); // 执行POST方法
			int resultCode = response.getStatusLine().getStatusCode();
			String result = EntityUtils.toString(response.getEntity(), "utf-8");
			if (resultCode == 200) {
				JSONObject obj = JSON.parseObject(result);
				String errorCode = obj.getString(Constants.ERRORCODE);
				String errorMsg = obj.getString(Constants.ERRORMSG);
				if (TextUtils.equals(errorCode, "200")) {
					mResponseListener.onRequestSuccess(mRequestType, result);
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

}
