package com.suken.bridgedetection.fragment;

import java.util.ArrayList;
import java.util.List;

import com.suken.bridgedetection.Constants;
import com.suken.bridgedetection.R;
import com.suken.bridgedetection.activity.BridgeFormActivity;
import com.suken.bridgedetection.storage.CheckDetail;
import com.suken.bridgedetection.storage.SdxcFormDetail;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class FormItemController implements OnClickListener {

	private View mFormItem;
	private View mImgVideoLayout;
	private View mEditLayout;
	private ImageView mArrowImgView;
	private boolean mIsShowing = false;
	private String mTitle = "";
	private BridgeFormActivity mContext;
	private int type = 1;
	private String mDefaultValue;
	private String mQhId;
	private boolean mIsHandong;

	public FormItemController(Activity context, View view, OnClickListener listener, String text, int type, String defaultValue, String[] itemTexts,
			CheckDetail formDetail, String blank1, String blank2, String qhId, boolean isHandong) {
		this.mIsHandong = isHandong;
		mContext = (BridgeFormActivity) context;
		mFormItem = view;
		mQhId = qhId;
		mImgVideoLayout = mFormItem.findViewById(R.id.img_video_layout);
		mArrowImgView = (ImageView) mFormItem.findViewById(R.id.arrow_img);
		mEditLayout = mFormItem.findViewById(R.id.form_item_edit_layout);
		mArrowImgView.setImageResource(R.drawable.xia);
		TextView qslxText = (TextView) mFormItem.findViewById(R.id.qslx_title);
		TextView qsfwText = (TextView) mFormItem.findViewById(R.id.qsfw_title);
		TextView byyjText = (TextView) mFormItem.findViewById(R.id.byyj_title);
		qslxText.setText(itemTexts[0]);
		qsfwText.setText(itemTexts[1]);
		byyjText.setText(itemTexts[2]);
		if(type == R.drawable.suidaojiancha){
			TextView item4 = (TextView) mFormItem.findViewById(R.id.item4_title);
			item4.setText(itemTexts[3]);
			TextView item5 = (TextView) mFormItem.findViewById(R.id.item5_title);
			item5.setText(itemTexts[4]);
		}
		TextView formTitle = (TextView) mFormItem.findViewById(R.id.form_column);
		mTitle = text;
		this.type = type;
		formTitle.setText(text);
		formTitle.setOnClickListener(listener);
		formTitle.setTag(this);
		mArrowImgView.setTag(this);
		mArrowImgView.setOnClickListener(listener);
		this.mDefaultValue = defaultValue;
		initImageAndVideo(formDetail, blank1, blank2);
	}

	private ImageView xiangji;
	private TextView imageNum;
	private Spinner spinner;
	private ImageView sxj;
	private TextView sxjNum;
	private EditText qslxEv = null;
	private EditText qsfwEv = null;
	private EditText byyjEv = null;
	private EditText item4Ev = null;
	private EditText item5Ev = null;

	public static class ImageDesc {
		public String name;
		public String path;
	}

	public static class VideoDesc {
		public String name;
		public String path;
	}

	private List<ImageDesc> mImages = new ArrayList<ImageDesc>();
	private List<VideoDesc> mVedios = new ArrayList<VideoDesc>();

	private class SpinnerAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mImages.size();
		}

		@Override
		public ImageDesc getItem(int position) {
			return mImages.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView view = new TextView(mFormItem.getContext());
			ImageDesc desc = getItem(position);
			view.setText(mTitle + "-" + desc.name);
			view.setTag(desc);
			view.setTextColor(Color.RED);
			view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					ImageDesc desc = (ImageDesc) v.getTag();
					mContext.jumpToMedia(FormItemController.this, Constants.REQUEST_CODE_EDIT_IMG, desc);
				}
			});
			return view;
		}

	}

	private SpinnerAdapter mAdapter = null;

	private void initImageAndVideo(CheckDetail formDetail, String blank1, String blank2) {
		qslxEv = (EditText) mFormItem.findViewById(R.id.qslx_edit);
		qsfwEv = (EditText) mFormItem.findViewById(R.id.qsfw_edit);
		byyjEv = (EditText) mFormItem.findViewById(R.id.byyj_edit);
		if(type == R.drawable.suidaojiancha){
			mFormItem.findViewById(R.id.item4_layout).setVisibility(View.VISIBLE);
			mFormItem.findViewById(R.id.item5_layout).setVisibility(View.VISIBLE);
			item4Ev = (EditText) mFormItem.findViewById(R.id.item4_edit);
			item5Ev = (EditText) mFormItem.findViewById(R.id.item5_edit);
		}

		if (!mIsHandong && (type == R.drawable.qiaoliangjiancha || type == R.drawable.qiaoliangxuncha)) {
			if (formDetail != null && !TextUtils.isEmpty(formDetail.getQslx())) {
				qslxEv.setText(formDetail.getQslx());
			} else {
				qslxEv.setText(mDefaultValue);
			}
			if (formDetail != null && !TextUtils.isEmpty(formDetail.getQsfw())) {
				qsfwEv.setText(formDetail.getQsfw());
			} else {
				qsfwEv.setHint(blank1);
			}
			if (formDetail != null && !TextUtils.isEmpty(formDetail.getBycsyj())) {
				byyjEv.setText(formDetail.getBycsyj());
			} else {
				byyjEv.setHint(blank2);
			}
		} else if(mIsHandong){

			if (formDetail != null && !TextUtils.isEmpty(formDetail.getQkms())) {
				qslxEv.setText(formDetail.getQkms());
			} else {
				qslxEv.setText(mDefaultValue);
			}
			if (formDetail != null && !TextUtils.isEmpty(formDetail.getBycsyj())) {
				qsfwEv.setText(formDetail.getBycsyj());
			} else {
				qsfwEv.setHint(blank1);
			}
			if (formDetail != null && !TextUtils.isEmpty(formDetail.getRemark())) {
				byyjEv.setText(formDetail.getRemark());
			} else {
				byyjEv.setHint(blank2);
			}
		
		}
	
		xiangji = (ImageView) mImgVideoLayout.findViewById(R.id.xiangji);
		imageNum = (TextView) mImgVideoLayout.findViewById(R.id.img_num);
		imageNum.setText("0");
		spinner = (Spinner) mImgVideoLayout.findViewById(R.id.img_spinner);
		sxj = (ImageView) mImgVideoLayout.findViewById(R.id.video);
		sxjNum = (TextView) mImgVideoLayout.findViewById(R.id.video_num);
		sxjNum.setText("0");
		mAdapter = new SpinnerAdapter();
		spinner.setAdapter(new SpinnerAdapter());
		xiangji.setOnClickListener(this);
		sxj.setOnClickListener(this);
	}

	public boolean isShowing() {
		return mIsShowing;
	}

	public void show() {
		mIsShowing = true;
		mEditLayout.setVisibility(View.VISIBLE);
		mImgVideoLayout.setVisibility(View.VISIBLE);
		mArrowImgView.setImageResource(R.drawable.shang);
	}

	public void hide() {
		mIsShowing = false;
		mEditLayout.setVisibility(View.GONE);
		mImgVideoLayout.setVisibility(View.GONE);
		mArrowImgView.setImageResource(R.drawable.xia);
	}

	public String generateMediaName(boolean isImg) {
		if (isImg) {
			return "pic-" + mQhId + "-" + (mImages.size() + 1) + ".png";
		} else {
			return "vdo-"+ mQhId + "-" + (mVedios.size() + 1) + ".mp4";
		}
	}

	public void updateImg(ImageDesc desc) {
		mImages.add(desc);
		imageNum.setText(mImages.size() + "");
		mAdapter.notifyDataSetChanged();
		spinner.setSelection(0);
	}

	public void updateVideo(VideoDesc desc) {
		mVedios.add(desc);
		sxjNum.setText(mVedios.size() + "");
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == xiangji.getId()) {
			mContext.jumpToMedia(this, Constants.REQUEST_CODE_CAPTURE, null);
		} else if (v.getId() == sxj.getId()) {
			mContext.jumpToMedia(this, Constants.REQUEST_CODE_VIDEO, null);
		}
	}
	
	public SdxcFormDetail packageSxDetail(){
		SdxcFormDetail detail = new SdxcFormDetail();
		detail.setJcnr(mTitle);
		detail.setQkms(qslxEv.getText().toString());
		detail.setDealwith(qsfwEv.getText().toString());
		detail.setJcsj(byyjEv.getText().toString());
		StringBuilder builder = new StringBuilder();
		for (ImageDesc desc : mImages) {
			builder.append(desc.path + ",");
		}
		detail.setPicattachment(builder.toString());
		builder = new StringBuilder();
		for (VideoDesc desc : mVedios) {
			builder.append(desc.path + ",");
		}
		detail.setVidattachment(builder.toString());
		return detail;
	}

	public CheckDetail packageCheckDetail() {
		CheckDetail detail = new CheckDetail();
		detail.setBjmc(mTitle);
		if (!mIsHandong && (type == R.drawable.qiaoliangjiancha || type == R.drawable.qiaoliangxuncha)) {
			detail.setBycsyj(byyjEv.getText().toString());
			detail.setQsfw(qsfwEv.getText().toString());
			detail.setQslx(qslxEv.getText().toString());
		} else if(mIsHandong){
			detail.setQkms(qslxEv.getText().toString());
			detail.setBycsyj(qsfwEv.getText().toString());
			detail.setRemark(byyjEv.getText().toString());
		} else if(type == R.drawable.suidaojiancha){
			detail.setYcwz(qslxEv.getText().toString());
			detail.setQsnr(qsfwEv.getText().toString());
			detail.setYcms(byyjEv.getText().toString());
			detail.setPd(item4Ev.getText().toString());
			detail.setBycsyj(item5Ev.getText().toString());
		}
		StringBuilder builder = new StringBuilder();
		for (ImageDesc desc : mImages) {
			builder.append(desc.path + ",");
		}
		detail.setPicattachment(builder.toString());
		builder = new StringBuilder();
		for (VideoDesc desc : mVedios) {
			builder.append(desc.path + ",");
		}
		detail.setVidattachment(builder.toString());
		return detail;
	}

}
