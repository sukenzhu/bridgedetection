/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.suken.bridgedetection.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.suken.bridgedetection.R;
import com.suken.bridgedetection.fragment.IpConfigFragment;

/**
 * @author kaifuzdl
 * @version $$Id: IpSettingActivity.java, v 0.1 16/4/24 上午10:09 kaifuzdl Exp $$
 */
public class IpSettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ip_act_layout);

    }
}
