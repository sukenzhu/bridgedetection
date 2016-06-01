package com.suken.imageditor;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import com.suken.bridgedetection.Constants;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.suken.bridgedetection.util.UiUtil;

public class ImageditorActivity extends Activity implements View.OnClickListener {

    private GraffitiView graffitiView;
    private TextView iconGraffiti, iconClose, iconWenben, iconModify, iconSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ResUtil.getLayoutId("activity_main"));
        UiUtil.reInitWidthHeight(this);
        Intent intent = getIntent();
        Uri path = intent.getParcelableExtra(MediaStore.EXTRA_OUTPUT);
        try {
            File f = new File(new URI(path.toString()));
            GraffitiView.setBitmapPath(f.getPath());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        graffitiView = (GraffitiView) findViewById(ResUtil.getId("top_panel"));
        iconGraffiti = (TextView) findViewById(ResUtil.getId("icon_doodle"));
        iconClose = (TextView) findViewById(ResUtil.getId("icon_close"));
        iconWenben = (TextView) findViewById(ResUtil.getId("icon_wenben"));
        iconSave = (TextView) findViewById(ResUtil.getId("icon_save"));
        iconSave.setOnClickListener(this);
        iconWenben.setOnClickListener(this);
        iconClose.setOnClickListener(this);
        iconGraffiti.setOnClickListener(this);
        iconGraffiti.performClick();
        iconModify = (TextView) findViewById(ResUtil.getId("icon_modify"));
        iconModify.setOnClickListener(this);
    }

    @Override
    public void finish() {
        setResult(Constants.REQUEST_CODE_EDIT_IMG);
        super.finish();
    }



    @Override
    public void onClick(View v) {

        int id = v.getId();

        if(id == ResUtil.getId("icon_close")){
            finish();
        } else if(id == ResUtil.getId("icon_wenben")){

            graffitiView.setTopView(GraffitiView.TOP_TITLE_VIE);
            iconWenben.setBackgroundColor(getResources().getColor(ResUtil.getColorId("active")));
            iconGraffiti.setBackgroundColor(getResources().getColor(ResUtil.getColorId("none")));
        }else if(id == ResUtil.getId("icon_doodle")){

            graffitiView.setTopView(GraffitiView.TOP_DOODLE);
            iconGraffiti.setBackgroundColor(getResources().getColor(ResUtil.getColorId("active")));
            iconWenben.setBackgroundColor(getResources().getColor(ResUtil.getColorId("none")));
        }else if(id == ResUtil.getId("icon_save")){

            graffitiView.saveBitmap();
            finish();
        }else if(id == ResUtil.getId("icon_modify")){

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
        }

    }
}
