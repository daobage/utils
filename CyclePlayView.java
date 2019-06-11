package shix.camerap2p.client.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import shix.camerap2p.client.R;

public class CyclePlayView extends View {
    Paint mPaintOutSide;
    Paint mPaintInSide;
    Paint mPaintText;
    int mWidth , mHeigth;
    RectF rect;
    String initStr;
    long time;
    float textWidth;
    float textHeight;
    float max=10;
    float deg;
    Paint.FontMetrics fontMetrics;
    boolean isDown = true;
    boolean isClear = false;

    public CyclePlayView(Context context) {
        super(context);
    }

    public CyclePlayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initData();
    }

    public CyclePlayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    void initData(){
        mPaintOutSide = new Paint();
        mPaintOutSide.setAntiAlias(true);
        mPaintOutSide.setStyle(Paint.Style.STROKE);
        mPaintOutSide.setStrokeWidth(dp2px(2));
        mPaintOutSide.setColor(Color.parseColor("#ff8c00"));

        mPaintInSide = new Paint();
        mPaintInSide.setStyle(Paint.Style.STROKE);
        mPaintInSide.setAntiAlias(true);
        mPaintInSide.setStrokeWidth(dp2px(6));
        mPaintInSide.setColor(Color.parseColor("#f5deb3"));

        mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintText.setAntiAlias(true);
        mPaintText.setTextSize(dp2px(10));
        mPaintText.setColor(Color.parseColor("#ffa500"));
        fontMetrics = mPaintText.getFontMetrics();
        textHeight = fontMetrics.bottom-fontMetrics.top;
        initStr = getResources().getString(R.string.audition);
        deg = 3.6f;
    }



    public void setMax(float ma) {
        this.max = ma;
        deg = 36/(max);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeigth = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mWidth,mHeigth);
    }


    public void reset(){
        initStr = getResources().getString(R.string.audition);
        time = 0;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        drawOutSideCycle(canvas);
        drawAra(canvas);
        drawText(canvas);
    }

    void drawOutSideCycle(Canvas canvas){
        canvas.drawCircle(mWidth/2,mHeigth/2,mWidth/2-dp2px(3),mPaintOutSide);
    }

    void drawAra(Canvas canvas){
        if (rect == null){
            rect = new RectF(dp2px(9),dp2px(9),mWidth-dp2px(9),mHeigth-dp2px(9));
        }
        canvas.drawArc(rect,-90f,time*deg,false,mPaintInSide);
    }
    void drawText(Canvas canvas){
        textWidth = mPaintText.measureText(initStr);
        canvas.drawText(initStr,mWidth/2-textWidth/2,mHeigth/2+ textHeight/2,mPaintText);
    }



    public void setText(String text){
        initStr = text;
        postInvalidate();
    }

    public void setArc(){
        isDown = true;
        time = 0;
        new Thread(){
            @Override
            public void run() {
                while (isDown) {
                    if (time <= (max+1)*10) {
                        postInvalidate();
                        try {
                            sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        time++;
                    }else {
                        isDown = false;
                        time = 0;
                        postInvalidate();
                    }
                }
            }
        }.start();
    }

    public void stopDraw(){
        isDown = false;
    }

    float dp2px(float dp){
        float scale = getResources().getDisplayMetrics().density;
        return dp*scale+0.5f;
    }

}
