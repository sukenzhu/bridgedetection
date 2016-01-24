package com.suken.imageditor;

import com.suken.bridgedetection.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImageditorActivity extends Activity implements View.OnClickListener {

	private GraffitiView graffitiView;
	private TextView iconGraffiti, iconClose, iconWenben, iconModify;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		graffitiView = (GraffitiView) findViewById(R.id.top_panel);
		Intent intent = getIntent();
		String path = intent.getStringExtra("imgPath");
		graffitiView.setBitmapPath(path);
		iconGraffiti = (TextView) findViewById(R.id.icon_doodle);
		iconClose = (TextView) findViewById(R.id.icon_close);
		iconWenben = (TextView) findViewById(R.id.icon_wenben);
		iconWenben.setOnClickListener(this);
		iconClose.setOnClickListener(this);
		iconGraffiti.setOnClickListener(this);
		iconGraffiti.performClick();
		iconModify = (TextView) findViewById(R.id.icon_modify);
		iconModify.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		int id = v.getId();
		switch (id) {
		case R.id.icon_close:
			finish();
			break;
		case R.id.icon_wenben: {
			graffitiView.setTopView(GraffitiView.TOP_TITLE_VIE);
			iconWenben.setBackgroundColor(getResources().getColor(R.color.active));
			iconGraffiti.setBackgroundColor(getResources().getColor(R.color.none));
			break;
		}
		case R.id.icon_doodle: {
			graffitiView.setTopView(GraffitiView.TOP_DOODLE);
			iconGraffiti.setBackgroundColor(getResources().getColor(R.color.active));
			iconWenben.setBackgroundColor(getResources().getColor(R.color.none));
			break;
		}
		case R.id.icon_modify: {
			DisplayMetrics dm = new DisplayMetrics();
			ImageditorActivity.this.getWindowManager().getDefaultDisplay().getMetrics(dm);
			float dp = dm.density;
			final AlertDialog dialog = new Builder(ImageditorActivity.this).create();
			LinearLayout ll = new LinearLayout(ImageditorActivity.this);
			LinearLayout l = new LinearLayout(ImageditorActivity.this);
			ll.setOrientation(LinearLayout.VERTICAL);
			TextView tv = new TextView(ImageditorActivity.this);
			tv.setText("更新文本");
			tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
			int padding = (int) (20 * dp);
			tv.setPadding(padding, padding, padding, padding);
			ll.addView(tv);
			View line = new View(ImageditorActivity.this);
			line.setLayoutParams(new LinearLayout.LayoutParams(-1, 2));
			line.setBackgroundColor(Color.BLUE);
			ll.addView(line);
			final EditText et = new EditText(ImageditorActivity.this);
			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(-1, -2);
			param.leftMargin = padding;
			param.rightMargin = padding;
			param.topMargin = padding;
			param.bottomMargin = padding;
			param.gravity = Gravity.CENTER;
			et.setLayoutParams(param);
			et.setText(graffitiView.getTitleText());
			ll.addView(et);
			line = new View(ImageditorActivity.this);
			line.setLayoutParams(new LinearLayout.LayoutParams(-1, 2));
			line.setBackgroundColor(Color.BLUE);
			ll.addView(line);
			l.setGravity(Gravity.CENTER);
			l.addView(ll, param);
			LinearLayout bl = new LinearLayout(ImageditorActivity.this);
			TextView cancel = new TextView(ImageditorActivity.this);
			cancel.setPadding(0, padding, 0, padding);
			cancel.setGravity(Gravity.CENTER);
			param = new LinearLayout.LayoutParams(-1, -2, 1);
			cancel.setLayoutParams(param);
			cancel.setText("取消");
			cancel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
			cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					dialog.dismiss();
				}
			});
			View line1 = new View(ImageditorActivity.this);
			line1.setLayoutParams(new LinearLayout.LayoutParams(2, -1));
			line1.setBackgroundColor(Color.BLUE);

			TextView confirm = new TextView(ImageditorActivity.this);
			confirm.setPadding(15, 15, 15, 15);
			confirm.setText("确定");
			confirm.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
			confirm.setPadding(0, padding, 0, padding);
			confirm.setGravity(Gravity.CENTER);
			confirm.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					graffitiView.setTitleText(et.getEditableText().toString());
					dialog.dismiss();
				}
			});
			param = new LinearLayout.LayoutParams(-1, -2, 1);
			confirm.setLayoutParams(param);
			bl.addView(cancel);
			bl.addView(line1);
			bl.addView(confirm);

			ll.addView(bl, new LinearLayout.LayoutParams(-1, -2));
			dialog.show();
			dialog.getWindow().setLayout(-1, -1);
			dialog.getWindow().setGravity(Gravity.CENTER);
			dialog.getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
			dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
			dialog.setContentView(l, new ViewGroup.LayoutParams(-1, -1));
			ll.setBackgroundColor(Color.WHITE);

			break;
		}
		}
	}
}
