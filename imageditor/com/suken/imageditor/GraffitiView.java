package com.suken.imageditor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.suken.bridgedetection.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

public class GraffitiView extends FrameLayout {

    private GraffitiSurface doodleView;
    private TextView titleView;
    private View mTextFrame;
    public static final int TOP_DOODLE = 0, TOP_TITLE_VIE = 1;
    private int topView = TOP_DOODLE;

    public int getTopView() {
        return topView;
    }

    public void setTopView(int topView) {
        this.topView = topView;
        if (topView == TOP_DOODLE) {
            bringChildToFront(doodleView);
        } else
            bringChildToFront(mTextFrame);
    }

    public GraffitiView(Context context) {
        super(context);
        init(context);
    }

    public GraffitiView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GraffitiView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void toggleTop() {
        if (topView == TOP_TITLE_VIE) {
            bringChildToFront(doodleView);
            topView = TOP_DOODLE;
        } else {
            bringChildToFront(mTextFrame);
            topView = TOP_TITLE_VIE;
        }
    }

    private void init(Context context) {
        setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        inflate(getContext(), R.layout.graffiti_layout, this);
        mTextFrame = findViewById(R.id.title_view_frame);
        titleView = (TextView) findViewById(R.id.title_view);
        titleView.setTextColor(Color.RED);
        titleView.setOnTouchListener(new MultiTouchListener());
        doodleView = (GraffitiSurface) findViewById(R.id.doodle_view);
    }
    private static String path = "";

    public  static void setBitmapPath(String path){
        GraffitiView.path = path;
        GraffitiSurface.setBitmapPath(path);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    public Bitmap toBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        draw(canvas);
        return bitmap;
    }

    public String saveBitmap() {
        // this.setDrawingCacheEnabled(true);
        Bitmap b = toBitmap();
        Bitmap bitmapdoodle = doodleView.getBitmap();
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        try {
            Canvas cv = new Canvas(bitmap);
            cv.drawBitmap(b, 0, 0, null);
            cv.drawBitmap(bitmapdoodle, 0, 0, null);
            int[] location = new int[2];
            titleView.getLocationInWindow(location);
            cv.drawText(titleView.getText().toString(), location[0], location[1], titleView.getPaint());
            cv.save(Canvas.ALL_SAVE_FLAG);
            cv.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }

        File file = new File(path);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 95, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return file.toString();
    }

    public void setTitleText(String string) {
        titleView.setText(string);
    }

    public CharSequence getTitleText() {
        return titleView.getText();
    }
}
