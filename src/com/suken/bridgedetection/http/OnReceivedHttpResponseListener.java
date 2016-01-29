package com.suken.bridgedetection.http;

public interface OnReceivedHttpResponseListener {

	public abstract void onRequestSuccess(String result);

	public abstract void onRequestFail(String resultCode, String result);
}
