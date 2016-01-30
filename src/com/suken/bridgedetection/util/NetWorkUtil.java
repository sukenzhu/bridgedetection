package com.suken.bridgedetection.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 */
public class NetWorkUtil {

	// 精简连接类型
	public enum ConnectType {
		CONNECT_TYPE_WIFI, CONNECT_TYPE_MOBILE, CONNECT_TYPE_OTHER, CONNECT_TYPE_DISCONNECT,
	}

	// 精简网络类型
	public enum MobileNetworkType {
		MOBILE_NETWORK_TYPE_2G, MOBILE_NETWORK_TYPE_3G, MOBILE_NETWORK_TYPE_4G, MOBILE_NETWORK_TYPE_UNKNOWN,
	}

	public static final int CONN_TYPE_2G = 1;
	public static final int CONN_TYPE_3G = 2;
	public static final int CONN_TYPE_4G = 4;
	public static final int CONN_TYPE_WIFI = 10;

	public static int getNetWorkStatus(Context context) {
		ConnectType netConnType = getConnectType(context);
		if (netConnType == ConnectType.CONNECT_TYPE_WIFI) {
			return CONN_TYPE_WIFI;
		} else if (netConnType == ConnectType.CONNECT_TYPE_MOBILE) {
			MobileNetworkType mobileNetworkType = getMobileNetworkType(context);
			switch (mobileNetworkType) {
			case MOBILE_NETWORK_TYPE_2G:
				return CONN_TYPE_2G;
			case MOBILE_NETWORK_TYPE_3G:
				return CONN_TYPE_3G;
			case MOBILE_NETWORK_TYPE_4G:
				return CONN_TYPE_4G;
			default:
				return CONN_TYPE_2G;
			}
		}
		return CONN_TYPE_2G;
	}

	/**
	 * 判断网络连接类型
	 *
	 * @param context
	 * @return boolean
	 */
	public static ConnectType getConnectType(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null && activeNetInfo.isConnected()) {
			switch (activeNetInfo.getType()) {
			case ConnectivityManager.TYPE_MOBILE: {
				return ConnectType.CONNECT_TYPE_MOBILE;
			}
			case ConnectivityManager.TYPE_WIFI: {
				return ConnectType.CONNECT_TYPE_WIFI;
			}
			default: {
				return ConnectType.CONNECT_TYPE_OTHER;
			}
			}
		} else {
			return ConnectType.CONNECT_TYPE_DISCONNECT;
		}
	}

	/**
	 * 判断mobile网络连接类型
	 *
	 * @param context
	 * @return boolean
	 */
	public static MobileNetworkType getMobileNetworkType(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE && activeNetInfo.isConnected()) {
			return getNetWorkType(activeNetInfo.getSubtype());
		} else {
			return MobileNetworkType.MOBILE_NETWORK_TYPE_UNKNOWN;
		}
	}

	private static MobileNetworkType getNetWorkType(int networkType) {
		switch (networkType) {
		case TelephonyManager.NETWORK_TYPE_GPRS:
		case TelephonyManager.NETWORK_TYPE_EDGE:
		case TelephonyManager.NETWORK_TYPE_CDMA:
		case TelephonyManager.NETWORK_TYPE_1xRTT:
		case TelephonyManager.NETWORK_TYPE_IDEN:
			return MobileNetworkType.MOBILE_NETWORK_TYPE_2G;
		case TelephonyManager.NETWORK_TYPE_UMTS:
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
		case TelephonyManager.NETWORK_TYPE_HSDPA:
		case TelephonyManager.NETWORK_TYPE_HSUPA:
		case TelephonyManager.NETWORK_TYPE_HSPA:
		case TelephonyManager.NETWORK_TYPE_EVDO_B:
		case TelephonyManager.NETWORK_TYPE_EHRPD:
		case TelephonyManager.NETWORK_TYPE_HSPAP:
			return MobileNetworkType.MOBILE_NETWORK_TYPE_3G;
		case TelephonyManager.NETWORK_TYPE_LTE:
			return MobileNetworkType.MOBILE_NETWORK_TYPE_4G;
		default:
			return MobileNetworkType.MOBILE_NETWORK_TYPE_UNKNOWN;
		}
	}
}
