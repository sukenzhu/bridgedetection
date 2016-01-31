package com.suken.imageditor;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GraffitiSurface extends SurfaceView implements SurfaceHolder.Callback {

	private SurfaceHolder mSurfaceHolder = null;

	// 当前操作
	private DoodleAction curAction = null;
	// 默认画笔为白色
	private int currentColor = Color.WHITE;
	// 画笔的粗细
	private int currentSize = 2;

	private Paint mPaint;
	// 记录画笔的列表
	private List<DoodleAction> mActions;

	private static Bitmap bmp;

	public GraffitiSurface(Context context) {
		super(context);
		init();
	}

	public GraffitiSurface(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public GraffitiSurface(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		setZOrderMediaOverlay(true);
		// setZOrderOnTop(true);
		mSurfaceHolder = this.getHolder();
		mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);
		mSurfaceHolder.addCallback(this);
		this.setFocusable(false);
		mPaint = new Paint();
		mPaint.setColor(Color.TRANSPARENT);
		mPaint.setStrokeWidth(currentSize);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Canvas canvas = mSurfaceHolder.lockCanvas();
		if(bmp != null){
			canvas.drawBitmap(bmp, 0, 0, null);
		}
		mSurfaceHolder.unlockCanvasAndPost(canvas);
		mActions = new ArrayList<DoodleAction>();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		bmp = null;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		if (action == MotionEvent.ACTION_CANCEL) {
			return false;
		}

		float touchX = event.getRawX();
		float touchY = event.getRawY();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			setCurAction(touchX, touchY);
			break;
		case MotionEvent.ACTION_MOVE:
			Canvas canvas = mSurfaceHolder.lockCanvas();
			if(bmp != null){
				canvas.drawBitmap(bmp, 0, 0, null);
			}
			for (DoodleAction a : mActions) {
				a.draw(canvas);
			}
			curAction.move(touchX, touchY);
			curAction.draw(canvas);
			mSurfaceHolder.unlockCanvasAndPost(canvas);
			break;
		case MotionEvent.ACTION_UP:
			mActions.add(curAction);
			curAction = null;
			break;

		default:
			break;
		}
		return true;
	}

	// 得到当前画笔的类型，并进行实例
	public void setCurAction(float x, float y) {
		curAction = new DoodleAction(x, y, currentSize, currentColor);
	}

	/**
	 * 设置画笔的颜色
	 * 
	 * @param color
	 */
	public void setColor(String color) {
		currentColor = Color.parseColor(color);
	}

	/**
	 * 设置画笔的粗细
	 * 
	 * @param size
	 */
	public void setSize(int size) {
		currentSize = size;
	}

	/**
	 * 获取画布的截图
	 * 
	 * @return
	 */
	public Bitmap getBitmap() {
		Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(bmp, 0, 0, null);
		doDraw(canvas);
		return bitmap;
	}

	public void doDraw(Canvas canvas) {
		canvas.drawColor(Color.TRANSPARENT);
		for (DoodleAction a : mActions) {
			a.draw(canvas);
		}
		canvas.drawBitmap(bmp, 0, 0, mPaint);
	}

	/**
	 * 回退
	 * 
	 * @return
	 */
	public boolean back() {
		if (mActions != null && mActions.size() > 0) {
			mActions.remove(mActions.size() - 1);
			Canvas canvas = mSurfaceHolder.lockCanvas();
			// canvas.drawColor(Color.WHITE);
			for (DoodleAction a : mActions) {
				a.draw(canvas);
			}
			mSurfaceHolder.unlockCanvasAndPost(canvas);
			return true;
		}
		return false;
	}

	class DoodleAction {
		public int color;
		private Path path;
		private int size;

		DoodleAction() {
			color = Color.RED;
			path = new Path();
			size = 1;
		}

		DoodleAction(float x, float y, int size, int color) {
			path = new Path();
			this.size = size;
			this.color = color;
			path.moveTo(x, y);
			path.lineTo(x, y);
		}

		DoodleAction(int color) {
			this.color = color;
		}

		public void draw(Canvas canvas) {
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setDither(true);
			paint.setColor(color);
			paint.setStrokeWidth(size);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeJoin(Paint.Join.ROUND);
			paint.setStrokeCap(Paint.Cap.ROUND);
			canvas.drawPath(path, paint);
		}

		public void move(float mx, float my) {
			path.lineTo(mx, my);
		}

	}

	public static void setBitmapPath(String path) {
		bmp = BitmapFactory.decodeFile(path);
	}

}
