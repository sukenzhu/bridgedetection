package com.suken.bridgedetection.activity;

import com.suken.bridgedetection.R;
import com.suken.bridgedetection.fragment.FormItemController;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class BridgeFormActivity extends Activity implements OnClickListener {

	private FormItemController yiqiang_layout;
	private FormItemController zhpo_layout;
	private FormItemController qtjc_layout;
	private FormItemController qdjc_layout;
	private FormItemController djcs_layout;
	private FormItemController zhizuo_layout;
	private FormItemController sbjg_layout;
	private FormItemController qllj_layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_form_page);
		init();
	}

	private void init() {
		yiqiang_layout = new FormItemController(this, R.id.yiqiang_layout, this, "翼墙");
		zhpo_layout = new FormItemController(this, R.id.zhpo_layout, this, "锥坡、护坡");
		qtjc_layout = new FormItemController(this, R.id.qtjc_layout, this, "桥台及基础");
		qdjc_layout = new FormItemController(this, R.id.qdjc_layout, this, "桥墩及基础");
		djcs_layout = new FormItemController(this, R.id.djcs_layout, this, "地基冲刷");
		zhizuo_layout = new FormItemController(this, R.id.zhizuo_layout, this, "支座");
		sbjg_layout = new FormItemController(this, R.id.sbjg_layout, this, "上部结构异常变形");
		qllj_layout = new FormItemController(this, R.id.qllj_layout, this, "桥与路连接");
	}

	@Override
	public void onClick(View view) {
		yiqiang_layout.hide();
		zhpo_layout.hide();
		qtjc_layout.hide();
		qdjc_layout.hide();
		djcs_layout.hide();
		zhizuo_layout.hide();
		sbjg_layout.hide();
		qllj_layout.hide();
		FormItemController con = (FormItemController) view.getTag();
		if(con.isShowing()){
			con.hide();
		} else {
			con.show();
		}

	}
}
