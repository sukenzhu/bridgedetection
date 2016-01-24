package com.suken.bridgedetection.fragment;

import com.suken.bridgedetection.R;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class FormItemController {

	private View mFormItem;
	private View mImgVideoLayout;
	private View mEditLayout;
	private ImageView mArrowImgView;
	private int mResId;
	private boolean mIsShowing = false;

	public FormItemController(Activity context, int resId, OnClickListener listener, String text) {
		mResId = resId;
		mFormItem = context.findViewById(resId);
		mImgVideoLayout = mFormItem.findViewById(R.id.img_video_layout);
		mArrowImgView = (ImageView) mFormItem.findViewById(R.id.arrow_img);
		mEditLayout = mFormItem.findViewById(R.id.form_item_edit_layout);
		mArrowImgView.setImageResource(R.drawable.xia);
		TextView formTitle = (TextView) mFormItem.findViewById(R.id.form_column);
		formTitle.setText(text);
		if (resId == R.id.yiqiang_layout) {
			show();
		} else {
			hide();
		}
		mArrowImgView.setTag(this);
		mArrowImgView.setOnClickListener(listener);
	}
	
	public boolean isShowing(){
		return mIsShowing;
	}

	public void show() {
		mIsShowing = true;
		mEditLayout.setVisibility(View.VISIBLE);
		mImgVideoLayout.setVisibility(View.VISIBLE);
		mArrowImgView.setImageResource(R.drawable.shang);
	}

	public void hide() {
		mIsShowing =  false;
		mEditLayout.setVisibility(View.GONE);
		mImgVideoLayout.setVisibility(View.GONE);
		mArrowImgView.setImageResource(R.drawable.xia);
	}

}
