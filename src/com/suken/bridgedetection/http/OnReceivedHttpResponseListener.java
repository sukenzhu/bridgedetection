package com.suken.bridgedetection.http;

import com.suken.bridgedetection.RequestType;

public interface OnReceivedHttpResponseListener {

	public abstract void onRequestSuccess(RequestType type, String result);

	public abstract void onRequestFail(RequestType type, String resultCode, String result);
}
