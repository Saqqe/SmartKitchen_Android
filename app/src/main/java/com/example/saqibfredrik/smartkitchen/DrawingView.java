package com.example.saqibfredrik.smartkitchen;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Saqib Sarker on 2015-08-31.
 */
public class DrawingView extends View{

    private Bitmap mBitmap;
    private Canvas mCanvas;
    Context context;
    private Paint mPaint;
    private boolean isDrawing;
    private float mx, my, mStartX, mStartY, right, left, top, bottom;
    private int screenWidth, picHeight;


    public DrawingView(Context c) {
        super(c);
        init(c, null, 0);
    }//End of construct

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public DrawingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }


    public void init(Context c, AttributeSet attrs, int defStyleAttr) {
        this.context = c;
        initStuff();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initStuff();

    }//End of onSizeChanged

    public void initStuff(){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display  = wm.getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);

        screenWidth   = size.x;
        mBitmap       = BitmapFactory.decodeResource(getResources(), R.drawable.greenmilk);
        picHeight     = mBitmap.getHeight();

        Log.d("Debug", "Inside initStuff");
        mBitmap          = Bitmap.createBitmap(mBitmap, 0, 0, screenWidth, picHeight);
        mCanvas          = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(mBitmap, 0, 0, mPaint);

        if (isDrawing){
            onDrawRectangle(canvas);
        }
    }//End of onDraw

    @Override
    public boolean onTouchEvent(MotionEvent event) {
         mx = event.getX();
         my = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onTouchEventRectangle(event);
                break;
            case MotionEvent.ACTION_UP:
                onTouchEventRectangle(event);
                break;
            case MotionEvent.ACTION_MOVE:
                onTouchEventRectangle(event);
                break;
        }
        return true;
    }//End of onTouchEvent


    //------------------------------------------------------------------
    // Rectangle
    //------------------------------------------------------------------
    private void onTouchEventRectangle(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initStuff();
                isDrawing = true;
                mStartX = mx;
                mStartY = my;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                isDrawing = false;
                drawRectangle(mCanvas);
                invalidate();
                break;
        }
    }//End of onTouchEventRectangle

    private void onDrawRectangle(Canvas canvas) {
        drawRectangle(canvas);
    }//End of DrawRectangle

    private void drawRectangle(Canvas canvas) {
        right   = mStartX > mx ? mStartX : mx;
        left    = mStartX > mx ? mx : mStartX;
        bottom  = mStartY > my ? mStartY : my;
        top     = mStartY > my ? my : mStartY;

        Log.d("Debug", "Inside drawRectangle" +
                "\nRight: " + right +
                "\nLeft: " + left +
                "\nTop: " + top +
                "\nBottom: " + bottom);
        canvas.drawRect(left, top, right, bottom, mPaint);
    }//End of drawRectangle

    //Show a toast on this activity for a shot duration
    public void toast (String msg)
    {
        Toast toast = Toast.makeText(this.getContext(), msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void cropBitmap(){
        toast("Cropping");
        Log.d("Debug", "Inside cropBitmap" +
                "\nRight: " + right +
                "\nLeft: " + left +
                "\nTop: " + top +
                "\nBottom: " + bottom);
    }


}//End of DrawingView
