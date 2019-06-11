package shix.camerap2p.client.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import shix.camerap2p.client.R;

public class CycleView extends View {
    Paint mPaintOutSide;
    Paint mPaintInSide;
    Paint mPaintText;
    int mWidth , mHeigth;
    RectF rect;
    String initStr;
    long time;
    float tim = 0;
    float textWidth;
    float textHeight;
    int max=10;
    float deg;
    OnToucherLister lister;
    Paint.FontMetrics fontMetrics;
    boolean isDown = true;
    long downtime = 0;
    boolean canClick = true;
    public CycleView(Context context) {
        super(context);
    }

    public CycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initData();
    }

    public CycleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        mPaintInSide.setStrokeWidth(dp2px(8));
        mPaintInSide.setColor(Color.parseColor("#f5deb3"));

        mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintText.setAntiAlias(true);
        mPaintText.setTextSize(dp2px(14));
        mPaintText.setColor(Color.parseColor("#ffa500"));
        fontMetrics = mPaintText.getFontMetrics();
        textHeight = fontMetrics.bottom-fontMetrics.top;
        initStr = getResources().getString(R.string.start_record_voice);
        deg = 3.6f;
    }

    public void setLister(OnToucherLister lister) {
        this.lister = lister;
    }


    public void setMax(int max) {
        this.max = max;
        deg = 36/max;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeigth = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mWidth,mHeigth);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
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

    private void setText(){
        isDown = true;
       tim = 0;
        new Thread(){
            @Override
            public void run() {
                while (isDown) {
                    if (tim <= max) {
                        initStr = String.format("%.1f",tim) + "S";
                        postInvalidate();
                        try {
                            sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        tim +=0.1;
                    }else {
                        isDown = false;
                        if (lister != null){
                            Log.e("tag____","stop");
                            lister.onActionUpOrTime(tim);
                        }
                    }
                }
            }
        }.start();
    }


    private void setArc(){
        isDown = true;
        time = 0;
        new Thread(){
            @Override
            public void run() {
                while (isDown) {
                    if (time <= max*10) {
                        postInvalidate();
                        try {
                            sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        time++;
                    }else {
                        isDown = false;
                    }
                }
            }
        }.start();
    }

   private void stopDraw(){
        isDown = false;
   }

   float dp2px(float dp){
        float scale = getResources().getDisplayMetrics().density;
        return dp*scale+0.5f;
   }

    public void setCanClick(boolean canClick) {
        this.canClick = canClick;
    }

    public void reset(){
        time = 0;
        initStr = getResources().getString(R.string.start_record_voice);
        postInvalidate();
   }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downtime = SystemClock.currentThreadTimeMillis();
                if (canClick) {
                    canClick = false;
                    isDown = true;
                    setArc();
                    setText();
                    if (lister != null) {
                        lister.onActionDown();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.e("time__","time   "+(SystemClock.currentThreadTimeMillis() - downtime));

                    stopDraw();
                    if (lister != null) {
                        lister.onActionUpOrTime(tim);
                    }
                break;
        }
        return true;
    }


    public interface OnToucherLister{
        void onActionDown();
        void onActionUpOrTime(float time);
    }

}
