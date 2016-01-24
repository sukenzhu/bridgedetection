package com.suken.bridgedetection.fragment;

import com.suken.bridgedetection.R;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LeftFragmentItemController{

	private View mLeftFragmentView;

	private View mItemView;

	private ImageView mImageView;

	private TextView mTextView;

	private TextView mNumView;
	
	private Activity mHomeActivity;
	
	private View mLineView;

	public LeftFragmentItemController(LeftFragment fragment, View leftFragView, int itemId, String textStr, int imgDrawable) {
		mHomeActivity = fragment.getActivity();
		mLeftFragmentView = leftFragView;
		mItemView = mLeftFragmentView.findViewById(itemId);
		mItemView.setOnClickListener(fragment);
		mImageView = (ImageView) mItemView.findViewById(R.id.left_frag_item_img);
		if(imgDrawable > 0){
			mImageView.setImageResource(imgDrawable);
		}
		if(!TextUtils.isEmpty(textStr)){
			mTextView = (TextView) mItemView.findViewById(R.id.left_frag_item_text);
			mTextView.setText(textStr);
		}
		mNumView = (TextView) mItemView.findViewById(R.id.left_frag_item_num);
		mLineView = mItemView.findViewById(R.id.left_frag_item_line);
		mLineView.setVisibility(View.GONE);
	}

	public void updateNum(int num) {
		if(num > 0){
			mNumView.setText(num + "");
			mNumView.setVisibility(View.VISIBLE);
		} else {
			mNumView.setVisibility(View.GONE);
		}
	}

	public void setSelected(){
		mItemView.setSelected(true);
		mItemView.setFocusable(true);
		mLineView.setVisibility(View.VISIBLE);
	}
	
	public void clearFocus(){
		mItemView.setSelected(false);
		mItemView.setFocusable(false);
		mLineView.setVisibility(View.GONE);
	}

}
