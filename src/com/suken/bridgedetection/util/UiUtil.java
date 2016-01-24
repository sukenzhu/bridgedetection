package com.suken.bridgedetection.util;

import android.app.Activity;
import android.util.DisplayMetrics;

public class UiUtil {

	private static float DP = -1f;

	public static float getDp(Activity context) {
		if (DP == -1f) {
			DisplayMetrics dm = new DisplayMetrics();
			context.getWindowManager().getDefaultDisplay().getMetrics(dm);
			DP = dm.density;
		}
		return DP;
	}
}
