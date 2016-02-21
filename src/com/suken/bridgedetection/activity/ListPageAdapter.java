package com.suken.bridgedetection.activity;

import java.util.ArrayList;
import java.util.List;

import com.suken.bridgedetection.Constants;
import com.suken.bridgedetection.R;
import com.suken.bridgedetection.storage.HDBaseData;
import com.suken.bridgedetection.storage.QLBaseData;
import com.suken.bridgedetection.storage.SDBaseData;
import com.suken.bridgedetection.util.UiUtil;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class ListPageAdapter extends BaseAdapter implements Filterable {

	private BridgeDetectionListActivity mContext;
	private List<ListBean> mSourceData;
	private List<ListBean> mUnfilteredData = null;
	private int mType = R.drawable.qiaoliangjiancha;
	private ListFilter mFilter;
	
	public List<ListBean> getSourceData(){
		return mUnfilteredData;
	}
	private OnClickListener mItemClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.operator) {
				ListBean bean = (ListBean) v.getTag();
				String status = bean.status;
				if (TextUtils.equals(status, "0") || TextUtils.equals(status, "2")) {
					
					// 去检查
					Intent intent = new Intent(mContext, BridgeFormActivity.class);
					if (mType == R.drawable.qiaoliangjiancha || mType == R.drawable.qiaoliangxuncha) {
						if (bean.realBean instanceof QLBaseData) {
							intent.putExtra("qhInfo", (QLBaseData) bean.realBean);
						} else if (bean.realBean instanceof HDBaseData) {
							intent.putExtra("qhInfo", (HDBaseData) bean.realBean);
						}
					} else {
						intent.putExtra("qhInfo", (SDBaseData) bean.realBean);
					}
					bean.mLastFormData = mContext.findLastSyncData(bean.id);
					intent.putExtra("type", mType);
					intent.putExtra("formData", bean.mLastFormData);
					mContext.startActivityForResult(intent, 1);
				} else if (TextUtils.equals(status, "1")) {
					// 去上传
					UiUtil.updateSingle(bean.id, bean.type, true, mContext);
				}
			} else {
				ViewHolder holder = (ViewHolder) v.getTag();
				holder.operate.setVisibility(View.VISIBLE);
				String status = holder.bean.status;
				changeView(status, holder);
			}
		}
	};

	private void changeView(String status, ViewHolder holder) {
		holder.operate.setVisibility(View.VISIBLE);
		holder.colorCircle.setVisibility(View.VISIBLE);
		if (TextUtils.equals(status, Constants.STATUS_CHECK)) {
			holder.operate.setBackgroundColor(Color.parseColor("#c0c0c0"));
			holder.operate.setText(mType == R.drawable.suidaoxuncha ? "开始巡查":"开始检查");
			holder.colorCircle.setBackgroundResource(R.drawable.circle_view_bg_gray);
		} else if (TextUtils.equals(status, Constants.STATUS_UPDATE)) {
			holder.operate.setBackgroundColor(Color.parseColor("#ff9900"));
			holder.operate.setText("上传");
			holder.colorCircle.setBackgroundResource(R.drawable.circle_view_bg_yellow);
		} else if (TextUtils.equals(status, Constants.STATUS_AGAIN)) {
			holder.operate.setBackgroundColor(Color.parseColor("#199847"));
			if(mType == R.drawable.qiaoliangxuncha){
				holder.operate.setText("查看");
				holder.operate.setVisibility(View.GONE);
			} else {
				holder.operate.setText(mType == R.drawable.suidaoxuncha ? "再次巡查":"再次检查");
				holder.operate.setVisibility(View.VISIBLE);
			}
			holder.colorCircle.setBackgroundResource(R.drawable.circle_view_bg_green);
		}
	}

	public ListPageAdapter(BridgeDetectionListActivity mContext, List<ListBean> data, int type) {
		super();
		this.mType = type;
		this.mContext = mContext;
		mSourceData = data;
		mUnfilteredData = data;
	}

	@Override
	public int getCount() {
		return mSourceData.size();
	}

	@Override
	public ListBean getItem(int arg0) {
		return mSourceData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	private class ViewHolder {
		TextView zxzh;
		TextView qhmc;
		TextView qhbs;
		TextView lxbm;
		TextView lxmc;
		View colorCircle;
		Button operate;
		ListBean bean;

	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		ViewHolder holder;
		if (view == null) {
			view = mContext.getLayoutInflater().inflate(R.layout.activity_list_page_item, arg2, false);
			holder = new ViewHolder();
			holder.zxzh = (TextView) view.findViewById(R.id.zxzh);
			holder.qhmc = (TextView) view.findViewById(R.id.name);
			holder.qhbs = (TextView) view.findViewById(R.id.qhbs);
			holder.lxbm = (TextView) view.findViewById(R.id.lxbm);
			holder.lxmc = (TextView) view.findViewById(R.id.lxmc);
			holder.operate = (Button) view.findViewById(R.id.operator);
			holder.colorCircle = view.findViewById(R.id.color_circle);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.bean = getItem(position);
		view.setTag(holder);
		holder.zxzh.setText(holder.bean.qhzh);
		holder.qhmc.setText(holder.bean.qhmc);
		holder.qhbs.setText(holder.bean.qhbs);
		holder.lxbm.setText(holder.bean.lxbm);
		holder.lxmc.setText(holder.bean.lxmc);
		view.setOnClickListener(mItemClickListener);
		holder.operate.setTag(holder.bean);
		holder.operate.setOnClickListener(mItemClickListener);
		if (!TextUtils.equals(holder.bean.status, "0")) {
			changeView(holder.bean.status, holder);
		} else {
			holder.operate.setVisibility(View.GONE);
			holder.colorCircle.setVisibility(View.INVISIBLE);
		}
		return view;
	}

	public void updateStatus(String id, String status) {
		for (ListBean bean : mSourceData) {
			if (TextUtils.equals(bean.id, id)) {
				bean.status = status;
				notifyDataSetChanged();
			}
		}
	}

	@Override
	public Filter getFilter() {
		if (mFilter == null) {
			mFilter = new ListFilter();
		}
		return mFilter;
	}

	private class ListFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			FilterResults results = new FilterResults();
			if (mUnfilteredData == null) {
				mUnfilteredData = new ArrayList<ListBean>(mSourceData);
			}
			if (prefix == null || prefix.length() == 0) {
				List<ListBean> list = mUnfilteredData;
				results.values = list;
				results.count = list.size();
			} else {
				String prefixString = prefix.toString().toLowerCase();
				int count = mUnfilteredData.size();
				List<ListBean> newValues = new ArrayList<ListBean>();
				for (int i = 0; i < count; i++) {
					ListBean bean = mUnfilteredData.get(i);
					boolean a = TextUtils.indexOf(bean.lxmc.toLowerCase(), prefixString) != -1;
					boolean b = TextUtils.indexOf(bean.lxbm.toLowerCase(), prefixString) != -1;
					boolean c = TextUtils.indexOf(bean.qhmc.toLowerCase(), prefixString) != -1;
					boolean d = TextUtils.indexOf(bean.qhbs.toLowerCase(), prefixString) != -1;
					boolean e = TextUtils.indexOf(bean.qhzh.toLowerCase(), prefixString) != -1;
					if (a || b || c || d || e) {
						newValues.add(bean);
					}
				}
				results.values = newValues;
				results.count = newValues.size();
			}
			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
            mSourceData = ((List<ListBean>) results.values);
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

	}
	
	public void addData(ListBean bean){
		mUnfilteredData.add(bean);
		mSourceData = mUnfilteredData;
		notifyDataSetChanged();
	}

}
