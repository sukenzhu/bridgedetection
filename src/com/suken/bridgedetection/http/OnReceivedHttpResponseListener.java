package com.suken.bridgedetection.http;

import com.alibaba.fastjson.JSONObject;
import com.suken.bridgedetection.RequestType;

public interface OnReceivedHttpResponseListener {

	public abstract void onRequestSuccess(RequestType type, JSONObject result);

	public abstract void onRequestFail(RequestType type, String resultCode, String result);
}
