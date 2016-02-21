package com.suken.bridgedetection.fragment;

import java.util.ArrayList;
import java.util.List;

import com.suken.bridgedetection.Constants;
import com.suken.bridgedetection.R;
import com.suken.bridgedetection.activity.BridgeFormActivity;
import com.suken.bridgedetection.storage.CheckDetail;
import com.suken.bridgedetection.storage.SdxcFormDetail;
import com.suken.bridgedetection.util.UiUtil;

import android.app.Activity;import android.content.res.ColorStateList;
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
	private EditText xczh;

	public FormItemController(Activity context, View view, OnClickListener listener, String text, int type, String defaultValue, String[] itemTexts,
			CheckDetail formDetail, String blank1, String blank2, String qhId, boolean isHandong) {
		this.mIsHandong = isHandong;
		mContext = (BridgeFormActivity) context;
		mFormItem = view;
		mQhId = qhId;
		xczh = (EditText) view.findViewById(R.id.form_qlzh);
		if(type == R.drawable.qiaoliangxuncha){
			xczh.setVisibility(View.VISIBLE);
		}
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
		if(type == R.drawable.suidaojiancha || type == R.drawable.qiaoliangxuncha){
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
	
	private Spinner item4Spinner = null;
	
	private Spinner item5Spinner = null;

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
		if(type == R.drawable.suidaojiancha || type == R.drawable.qiaoliangxuncha){
			mFormItem.findViewById(R.id.item4_layout).setVisibility(View.VISIBLE);
			mFormItem.findViewById(R.id.item5_layout).setVisibility(View.VISIBLE);
			item4Ev = (EditText) mFormItem.findViewById(R.id.item4_edit);
			item5Ev = (EditText) mFormItem.findViewById(R.id.item5_edit);
			item4Spinner = (Spinner) mFormItem.findViewById(R.id.item4_spinner);
			item5Spinner = (Spinner) mFormItem.findViewById(R.id.item5_spinner);
			if(type == R.drawable.suidaojiancha){
				item4Ev.setVisibility(View.GONE);
				item5Ev.setVisibility(View.GONE);
				item4Spinner.setVisibility(View.VISIBLE);
				item5Spinner.setVisibility(View.VISIBLE);
				item4Spinner.setAdapter(new TextSpinnerAdapter(new String[]{"正常","一般异常","严重异常"}));
				item5Spinner.setAdapter(new TextSpinnerAdapter(new String[]{"跟踪监测","维修处置","定期或专项检查"}));
				item4Spinner.setSelection(0);
				item5Spinner.setSelection(0);
			}
		}

		if (!mIsHandong && (type == R.drawable.qiaoliangjiancha)) {
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
			if (formDetail != null && !TextUtils.isEmpty(formDetail.getBlcsyj())) {
				byyjEv.setText(formDetail.getBlcsyj());
			} else {
				byyjEv.setHint(blank2);
			}
		} else if(mIsHandong){

			if (formDetail != null && !TextUtils.isEmpty(formDetail.getQkms())) {
				qslxEv.setText(formDetail.getQkms());
			} else {
				qslxEv.setText(mDefaultValue);
			}
			if (formDetail != null && !TextUtils.isEmpty(formDetail.getBlcsyj())) {
				qsfwEv.setText(formDetail.getBlcsyj());
			} else {
				qsfwEv.setHint(blank1);
			}
			if (formDetail != null && !TextUtils.isEmpty(formDetail.getRemark())) {
				byyjEv.setText(formDetail.getRemark());
			} else {
				byyjEv.setHint(blank2);
			}
		
		}
		if(type == R.drawable.suidaoxuncha){
			byyjEv.setText(UiUtil.formatNowTime());
			byyjEv.setEnabled(false);
			byyjEv.setTextColor(Color.BLACK);
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
	
	public void performArrowImgClick(){
		mArrowImgView.performClick();
	}

	public void hide() {
		mIsShowing = false;
		mEditLayout.setVisibility(View.GONE);
		mImgVideoLayout.setVisibility(View.GONE);
		mArrowImgView.setImageResource(R.drawable.xia);
	}

	public String generateMediaName(boolean isImg) {
		if (isImg) {
			return "pic-" + System.currentTimeMillis() + "-" + (mImages.size() + 1) + ".png";
		} else {
			return "vdo-" + System.currentTimeMillis() + "-" + (mVedios.size() + 1) + ".mp4";
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
		if(type == R.drawable.suidaoxuncha){
			detail.setJcnr(mTitle);
			detail.setQkms(qslxEv.getText().toString());
			detail.setDealwith(qsfwEv.getText().toString());
			detail.setJcsj(byyjEv.getText().toString());
		} else {
			detail.setQhzh(xczh.getText().toString());
			detail.setFx(qslxEv.getText().toString());
			detail.setFxbh(qsfwEv.getText().toString());
			detail.setShwz(byyjEv.getText().toString());
			detail.setDx(item4Ev.getText().toString());
			detail.setYs(item5Ev.getText().toString());
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

	public CheckDetail packageCheckDetail() {
		CheckDetail detail = new CheckDetail();
		detail.setBjmc(mTitle);
		if (!mIsHandong && (type == R.drawable.qiaoliangjiancha || type == R.drawable.qiaoliangxuncha)) {
			detail.setBlcsyj(byyjEv.getText().toString());
			detail.setQsfw(qsfwEv.getText().toString());
			detail.setQslx(qslxEv.getText().toString());
		} else if(mIsHandong){
			detail.setQkms(qslxEv.getText().toString());
			detail.setBlcsyj(qsfwEv.getText().toString());
			detail.setRemark(byyjEv.getText().toString());
		} else if(type == R.drawable.suidaojiancha){
			detail.setYcwz(qslxEv.getText().toString());
			detail.setQsnr(qsfwEv.getText().toString());
			detail.setYcms(byyjEv.getText().toString());
			detail.setPd(item4Spinner.getSelectedItem().toString());
			detail.setYhcsyj(item5Spinner.getSelectedItem().toString());
			detail.setJgmc(mTitle);
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
	
	public View getItemView(){
		return mFormItem;
	}
	
	
	private class TextSpinnerAdapter extends BaseAdapter{
		
		private String[] array = null;

		public TextSpinnerAdapter(String[] array) {
			super();
			this.array = array;
		}

		@Override
		public int getCount() {
			return array.length;
		}

		@Override
		public String getItem(int position) {
			return array[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView view = new TextView(mContext);
			view.setPadding(5, 0, 0, 0);
			view.setText(getItem(position));
			view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
			return view;
		}
		
	}

}
