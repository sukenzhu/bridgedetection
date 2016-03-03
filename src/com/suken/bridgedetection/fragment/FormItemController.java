package com.suken.bridgedetection.fragment;

import java.util.ArrayList;
import java.util.List;

import com.suken.bridgedetection.Constants;
import com.suken.bridgedetection.R;
import com.suken.bridgedetection.activity.BridgeFormActivity;
import com.suken.bridgedetection.activity.BridgeFormActivity.FormBaseDetail;
import com.suken.bridgedetection.storage.CheckDetail;
import com.suken.bridgedetection.storage.SdxcFormDetail;
import com.suken.bridgedetection.util.UiUtil;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
	private FormBaseDetail mFormBaseDetail;
	private boolean mIsCheckAgain = false;

	public FormItemController(Activity context, View view, OnClickListener listener, String text, int type, String defaultValue, String[] itemTexts,
			CheckDetail formDetail, String blank1, String blank2, String qhId, boolean isHandong, FormBaseDetail detail,boolean isCheckAgain) {
		this.mIsHandong = isHandong;
		mFormBaseDetail = detail;
		mContext = (BridgeFormActivity) context;
		mFormItem = view;
		mQhId = qhId;
		xczh = (EditText) view.findViewById(R.id.form_qlzh);
		if (type == R.drawable.qiaoliangxuncha) {
			xczh.setVisibility(View.VISIBLE);
		}
		mIsCheckAgain = isCheckAgain;
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
		if (type == R.drawable.suidaojiancha || type == R.drawable.qiaoliangxuncha) {
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

	private RadioGroup item4Rg = null;

	private LinearLayout item5Ll = null;

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
			view.setText(mTitle + "-" + (position + 1));
			view.setTag(desc);
			view.setTextColor(Color.RED);
			view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
			view.setHeight((int)(15 * UiUtil.getDp(mContext))); 
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
		} else if (mIsHandong) {

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
		if (type == R.drawable.suidaoxuncha) {
			byyjEv.setText(UiUtil.formatNowTime());
			byyjEv.setEnabled(false);
			byyjEv.setTextColor(Color.BLACK);
		}

		if (mFormBaseDetail != null) {
			if (!TextUtils.isEmpty(mFormBaseDetail.item1)) {
				qslxEv.setText(mFormBaseDetail.item1);
			}
			if (!TextUtils.isEmpty(mFormBaseDetail.item2)) {
				qsfwEv.setText(mFormBaseDetail.item2);
			}
			if (!TextUtils.isEmpty(mFormBaseDetail.item1)) {
				byyjEv.setText(mFormBaseDetail.item3);
			}
		}

		if (type == R.drawable.suidaojiancha || type == R.drawable.qiaoliangxuncha) {
			mFormItem.findViewById(R.id.item4_layout).setVisibility(View.VISIBLE);
			mFormItem.findViewById(R.id.item5_layout).setVisibility(View.VISIBLE);
			item4Ev = (EditText) mFormItem.findViewById(R.id.item4_edit);
			item5Ev = (EditText) mFormItem.findViewById(R.id.item5_edit);
			item4Rg = (RadioGroup) mFormItem.findViewById(R.id.item4Rg);
			item5Ll = (LinearLayout) mFormItem.findViewById(R.id.item5_ll);
			if (type == R.drawable.suidaojiancha) {
				item4Ev.setVisibility(View.GONE);
				item5Ev.setVisibility(View.GONE);
				item4Rg.setVisibility(View.VISIBLE);
				item5Ll.setVisibility(View.VISIBLE);
				RadioButton rb1 = (RadioButton) item4Rg.findViewById(R.id.item4_1);
				rb1.setChecked(true);
				RadioButton rb2 = (RadioButton) item4Rg.findViewById(R.id.item4_2);
				RadioButton rb3 = (RadioButton) item4Rg.findViewById(R.id.item4_3);
				if (mFormBaseDetail != null) {
					if (!TextUtils.isEmpty(mFormBaseDetail.item4)) {
						if (TextUtils.equals(mFormBaseDetail.item4, "一般异常")) {
							rb2.setChecked(true);
						} else if (TextUtils.equals(mFormBaseDetail.item4, "严重异常")) {
							rb3.setChecked(true);
						}
					}
					CheckBox cb1 = (CheckBox) item5Ll.findViewById(R.id.item5_1);
					CheckBox cb2 = (CheckBox) item5Ll.findViewById(R.id.item5_2);
					CheckBox cb3 = (CheckBox) item5Ll.findViewById(R.id.item5_3);
					if (!TextUtils.isEmpty(mFormBaseDetail.item5)) {
						if (mFormBaseDetail.item5.contains("跟踪检查")) {
							cb1.setChecked(true);
						}
						if (mFormBaseDetail.item5.contains("维修处理")) {
							cb2.setChecked(true);
						}
						if (mFormBaseDetail.item5.contains("定期或专项检测")) {
							cb3.setChecked(true);
						}
					}
				}
			} else if (type == R.drawable.qiaoliangxuncha) {
				if (mFormBaseDetail != null) {
					if(!TextUtils.isEmpty(mFormBaseDetail.item1)){
						xczh.setText(mFormBaseDetail.item1);
					}
					if (!TextUtils.isEmpty(mFormBaseDetail.item4)) {
						item4Ev.setText(mFormBaseDetail.item4);
					}
					if (!TextUtils.isEmpty(mFormBaseDetail.item5)) {
						item5Ev.setText(mFormBaseDetail.item5);
					}
				}
			}
		}

		xiangji = (ImageView) mImgVideoLayout.findViewById(R.id.xiangji);
		imageNum = (TextView) mImgVideoLayout.findViewById(R.id.img_num);
		spinner = (Spinner) mImgVideoLayout.findViewById(R.id.img_spinner);
		sxj = (ImageView) mImgVideoLayout.findViewById(R.id.video);
		sxjNum = (TextView) mImgVideoLayout.findViewById(R.id.video_num);
		imageNum.setText("0");
		sxjNum.setText("0");
		if (mFormBaseDetail != null && !mIsCheckAgain) {
			String picAttach = mFormBaseDetail.picAttach;
			if (!TextUtils.isEmpty(picAttach)) {
				String[] strs = picAttach.split(",");
				if (strs != null) {
					imageNum.setText(strs.length + "");
					String[] names = mFormBaseDetail.picAttachNames.split(",");
					if (names != null) {
						for (int i = 0; i < names.length; i++) {
							ImageDesc imgDesc = new ImageDesc();
							imgDesc.name = mTitle + "-" + (i + 1);
							imgDesc.path = names[i];
							mImages.add(imgDesc);
						}
					}
				}
			}

			String vdoAttach = mFormBaseDetail.vdoAttach;
			if (!TextUtils.isEmpty(vdoAttach)) {
				String[] strs = vdoAttach.split(",");
				if (strs != null) {
					sxjNum.setText(strs.length + "");
				}
			}

		}
		mAdapter = new SpinnerAdapter();
		spinner.setAdapter(mAdapter);
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

	public void performArrowImgClick() {
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

	public SdxcFormDetail packageSxDetail() {
		SdxcFormDetail detail = new SdxcFormDetail();
		if (mFormBaseDetail != null && mIsCheckAgain) {
			detail.setLocalId(mFormBaseDetail.localId);
		}
		if (type == R.drawable.suidaoxuncha) {
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
		detail.setPicattachmentNames(builder.toString());
		builder = new StringBuilder();
		for (VideoDesc desc : mVedios) {
			builder.append(desc.path + ",");
		}
		detail.setVidattachment(builder.toString());
		detail.setVidattachmentNames(builder.toString());
		return detail;
	}

	public CheckDetail packageCheckDetail() {
		CheckDetail detail = new CheckDetail();
		if (mFormBaseDetail != null && !mIsCheckAgain) {
			detail.setLocalId(mFormBaseDetail.localId);
		}
		detail.setBjmc(mTitle);
		if (!mIsHandong && (type == R.drawable.qiaoliangjiancha)) {
			detail.setBlcsyj(byyjEv.getText().toString());
			detail.setQsfw(qsfwEv.getText().toString());
			detail.setQslx(qslxEv.getText().toString());
		} else if (mIsHandong) {
			detail.setQkms(qslxEv.getText().toString());
			detail.setBlcsyj(qsfwEv.getText().toString());
			detail.setRemark(byyjEv.getText().toString());
		} else if (type == R.drawable.suidaojiancha) {
			detail.setYcwz(qslxEv.getText().toString());
			detail.setQsnr(qsfwEv.getText().toString());
			detail.setYcms(byyjEv.getText().toString());
			int id = item4Rg.getCheckedRadioButtonId();
			RadioButton rb = (RadioButton) item4Rg.findViewById(id);
			detail.setPd(rb.getText().toString());
			CheckBox cb1 = (CheckBox) item5Ll.findViewById(R.id.item5_1);
			CheckBox cb2 = (CheckBox) item5Ll.findViewById(R.id.item5_2);
			CheckBox cb3 = (CheckBox) item5Ll.findViewById(R.id.item5_3);
			detail.setYhcsyj(
					(cb1.isChecked() ? cb1.getText() + "," : "") + (cb2.isChecked() ? cb2.getText() + "," : "") + (cb3.isChecked() ? cb3.getText() : ""));
			detail.setJgmc(mTitle);
		}
		StringBuilder builder = new StringBuilder();
		for (ImageDesc desc : mImages) {
			builder.append(desc.path + ",");
		}
		detail.setPicattachmentNames(builder.toString());
		detail.setPicattachment(builder.toString());
		builder = new StringBuilder();
		for (VideoDesc desc : mVedios) {
			builder.append(desc.path + ",");
		}
		detail.setVidattachmentNames(builder.toString());
		detail.setVidattachment(builder.toString());
		return detail;
	}

	public View getItemView() {
		return mFormItem;
	}
}
