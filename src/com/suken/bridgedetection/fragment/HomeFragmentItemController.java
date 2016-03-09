package com.suken.bridgedetection.fragment;

import com.suken.bridgedetection.R;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeFragmentItemController {
	
	private ImageView mImageView;
	private TextView mTextView;
	private View mItemView;
	private Activity mActivity;
	
	public HomeFragmentItemController(HomePageFragment fragment, View contentView, int imgRes, String textStr){
		mItemView = contentView.findViewById(imgRes);
		mImageView = (ImageView) mItemView.findViewById(R.id.home_frag_item_img);
		mImageView.setImageResource(imgRes);
		mTextView = (TextView) mItemView.findViewById(R.id.home_frag_item_text);
		mTextView.setText(textStr);
		mItemView.setOnClickListener(fragment);
	}

	public  void destory(){
		mItemView.setOnClickListener(null);
		mImageView = null;
		mTextView = null;
		mItemView = null;
		mActivity = null;
	}

}
