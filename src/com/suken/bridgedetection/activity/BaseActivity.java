package com.suken.bridgedetection.activity;

import com.suken.bridgedetection.R;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("InflateParams")
public class BaseActivity extends FragmentActivity {

	private Dialog mLoadingDialog = null;

	public void toast(final String msg) {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				Toast toast = Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		});
	}

	/**
	 * 得到自定义的progressDialog
	 * 
	 * @param context
	 * @param msg
	 * @return
	 */
	public void showLoading(final String msg) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				LayoutInflater inflater = LayoutInflater.from(BaseActivity.this);
				View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
				LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局
				// main.xml中的ImageView
				ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
				TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
				// 加载动画
				Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(BaseActivity.this,
						R.anim.loading_animation);
				// 使用ImageView显示动画
				spaceshipImage.startAnimation(hyperspaceJumpAnimation);
				tipTextView.setText(msg);// 设置加载信息

				Dialog loadingDialog = new Dialog(BaseActivity.this, R.style.loading_dialog);// 创建自定义样式dialog

				loadingDialog.setCancelable(false);// 不可以用“返回键”取消
				loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
				mLoadingDialog = loadingDialog;
				loadingDialog.show();
			}
		});

	}

	public void dismissLoading() {
		if (mLoadingDialog != null) {
			mLoadingDialog.dismiss();
			mLoadingDialog = null;
		}
	}

}
