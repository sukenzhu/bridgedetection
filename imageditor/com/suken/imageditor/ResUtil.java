package com.suken.imageditor;

import android.content.res.Resources;
import com.suken.bridgedetection.BridgeDetectionApplication;

/**
 * Created by gaofeng on 16/6/1.
 */
public class ResUtil {

    private static Resources getResources() {
        return BridgeDetectionApplication.getInstance().getResources();
    }

    public static int getLayoutId(String name) {
        return getResources().getIdentifier(name, "layout", BridgeDetectionApplication.getInstance().getPackageName());
    }

    public static int getId(String name) {
        return getResources().getIdentifier(name, "id", BridgeDetectionApplication.getInstance().getPackageName());
    }


    public static int getDrawableId(String name) {
        return getResources().getIdentifier(name, "drawable", BridgeDetectionApplication.getInstance().getPackageName());
    }


    public static int getColorId(String name) {
        return getResources().getIdentifier(name, "color", BridgeDetectionApplication.getInstance().getPackageName());
    }

    public static int getStringId(String name) {
        return getResources().getIdentifier(name, "String", BridgeDetectionApplication.getInstance().getPackageName());
    }


}
