package com.example.xinlai_001.camerademo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;

public class ZoomFrameLayout extends FrameLayout implements ScaleGestureDetector.OnScaleGestureListener {

    ScaleGestureDetector scaleGestureDetector;  // 手势缩放
    GestureDetector gestureDetector; // 多点触碰处理


    Matrix mMatrix; // 缩放矩阵
    float maxScale = 4.0f; // 最大缩放比例
    float minScale = 0.5f; // 最小缩放比例
    RectF rect;
    int lastPointerCount;
    float lastX , lastY;
    float delX, delY;
    View childView;
    public ZoomFrameLayout(Context context) {
        super(context);
        init();
    }

    public ZoomFrameLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ZoomFrameLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    // 初始化参数
    void init(){
        scaleGestureDetector = new ScaleGestureDetector(new WeakReference<Context>(getContext()).get(),
                new WeakReference<ZoomFrameLayout>(this).get());
        gestureDetector = new GestureDetector(getContext(),new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (getPreScale() != 1.0f){
                    ZoomFrameLayout.this.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mMatrix.reset();
//                            setImageMatrix(mMatrix);
                            invalidate();
                            makeDrableCenter();
                        }
                    },20);
                }
                return true;
            }
        });
        mMatrix = new Matrix();
        rect = new RectF();
    }


    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float scale = detector.getScaleFactor();
        float preScale = getPreScale();
        if (preScale * scale < maxScale
                && preScale * scale > minScale){
            mMatrix.postScale(scale,scale,getWidth()/2,getHeight()/2);
//            setImageMatrix(mMatrix);
            postInvalidate();
            makeDrableCenter();
        }
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        if (gestureDetector.onTouchEvent(event)){
            return true;
        }
        float x = 0 , y= 0;
        final int pointCount = event.getPointerCount(); // 获取手指个数
        for (int i = 0; i< pointCount;i++){
            x += event.getX(i);
            y += event.getY(i);
        }
        x = x/pointCount;
        y = y/pointCount;
        if (pointCount != lastPointerCount){
            lastX = x;
            lastY = y;
        }
        lastPointerCount = pointCount;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                delX = x - lastX;
                delY = y - lastY;
                if ((rect.left >= 0 && delX > 0) ||(rect.right <= getWidth() && delX < 0)){
                    delX = 0;
                }
                if ((rect.top >= 0&& delY >0)||(rect.bottom <= getHeight() && delY <0 )){
                    delY = 0;
                }
                mMatrix.postTranslate(delX,delY);
//                setImageMatrix(mMatrix);
                postInvalidate();
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_UP:
                lastPointerCount = 0;
                break;
        }
        return true;
    }

    private float getPreScale(){
        float[] matrix = new float[9];
        mMatrix.getValues(matrix);
        return matrix[Matrix.MSCALE_X];
    }

    void makeDrableCenter(){
        if (childView != null) {
            rect.set(0, 0, childView.getMeasuredWidth(), childView.getMeasuredHeight());
            mMatrix.mapRect(rect);
        }

        int width = getWidth();
        int height = getHeight();
        float dx = 0, dy = 0 ;
        if (rect.width() >= width){
            if (rect.left > 0){
                dx = -rect.left;
            }
            if (rect.right < width){
                dx = width - rect.right;
            }
        }

        if (rect.height() >= height){
            if (rect.top > 0){
                dy = -rect.top;
            }

            if (rect.bottom < height){
                dy = height-rect.bottom;
            }
        }

        if (rect.width() < width ){
            dx = width/2-(rect.right-rect.width()/2);
        }
        if (rect.height() < height){
            dy = height/2-(rect.bottom-rect.height()/2);
        }

        if (dx !=0 || dy != 0 ){
            mMatrix.postTranslate(dx,dy);
            postInvalidate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        scaleGestureDetector = null;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        childView = getChildAt(0);
        canvas.save();
        canvas.concat(mMatrix);
        childView.draw(canvas);
        canvas.restore();
    }
}
