package com.suken.bridgedetection.fragment;

import java.util.ArrayList;
import java.util.List;

import com.suken.bridgedetection.Constants;
import com.suken.bridgedetection.R;
import com.suken.bridgedetection.activity.BridgeFormActivity;
import com.suken.bridgedetection.storage.CheckDetail;

import android.R.color;
import android.app.Activity;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class FormItemController implements OnClickListener{

	private View mFormItem;
	private View mImgVideoLayout;
	private View mEditLayout;
	private ImageView mArrowImgView;
	private boolean mIsShowing = false;
	private String mTitle = "";
	private BridgeFormActivity mContext ;
	private static final String qxlx_default = "完好或有无开裂、倾斜、滑移、沉降、风化剥落或异常变形等";
	private static final String qxfw_default = "描述翼墙位置（上下行方向、左右位置）和缺损程度";
	private static final String byyj_default = "注明继续观察或修复时限和采取的交通管制措施及向上级报告情况";
	private int type = 1;

	public FormItemController(Activity context, View view, OnClickListener listener, String text, int type) {
		mContext = (BridgeFormActivity) context;
		mFormItem = view;
		mImgVideoLayout = mFormItem.findViewById(R.id.img_video_layout);
		mArrowImgView = (ImageView) mFormItem.findViewById(R.id.arrow_img);
		mEditLayout = mFormItem.findViewById(R.id.form_item_edit_layout);
		mArrowImgView.setImageResource(R.drawable.xia);
		TextView formTitle = (TextView) mFormItem.findViewById(R.id.form_column);
		mTitle = text;
		formTitle.setText(text);
		mArrowImgView.setTag(this);
		mArrowImgView.setOnClickListener(listener);
		initImageAndVideo();
	}

	private ImageView xiangji;
	private TextView imageNum;
	private Spinner spinner;
	private ImageView sxj;
	private TextView sxjNum;
	private EditText qslxEv = null;
	private EditText qsfwEv = null;
	private EditText byyjEv = null;

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

	private void initImageAndVideo() {
		qslxEv = (EditText) mFormItem.findViewById(R.id.qslx_edit);
		qsfwEv = (EditText) mFormItem.findViewById(R.id.qsfw_edit);
		byyjEv = (EditText) mFormItem.findViewById(R.id.byyj_edit);
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
		if(isImg){
			return "pic-" + (mImages.size() + 1) + ".png";
		} else {
			return "vdo-" + (mVedios.size() + 1) + ".mp4";
		}
	}
	
	public void updateImg(ImageDesc desc){
		mImages.add(desc);
		imageNum.setText(mImages.size() + "");
		mAdapter.notifyDataSetChanged();
		spinner.setSelection(0);
	}
	
	public void updateVideo(VideoDesc desc){
		mVedios.add(desc);
		sxjNum.setText(mVedios.size() + "");
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == xiangji.getId()){
			mContext.jumpToMedia(this, Constants.REQUEST_CODE_CAPTURE, null);
		} else if(v.getId() == sxj.getId()){
			mContext.jumpToMedia(this, Constants.REQUEST_CODE_VIDEO, null);
		}
	}
	
	
	public CheckDetail packageCheckDetail(){
		CheckDetail detail = new CheckDetail();
		detail.setBjmc(mTitle);
		if(type == 1){
			detail.setBycsyj(byyjEv.getText().toString());
			detail.setQsfw(qsfwEv.getText().toString());
			detail.setQslx(qslxEv.getText().toString());
		}
		StringBuilder builder = new StringBuilder();
		for(ImageDesc desc : mImages){
			builder.append(desc.path + ",");
		}
		detail.setPicattachment(builder.toString());
		builder = new StringBuilder();
		for(VideoDesc desc : mVedios){
			builder.append(desc.path + ",");
		}
		detail.setVidattachment(builder.toString());
		return detail;
	}

}
