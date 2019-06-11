package shix.camerap2p.client.widget;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.IllegalFormatCodePointException;
import java.util.List;

import shix.camerap2p.client.mode.RecordInfo;


/**
 * 视频时间轴 控件
 */

public class VideoRulerView extends View {

    /**，
     * 图片放大状态常量
     */
    public static final int STATUS_ZOOM_OUT = 1;

    /**
     * 图片缩小状态常量
     */
    public static final int STATUS_ZOOM_IN = 2;

    /**
     * 记录两指同时放在屏幕上时，中心点的横坐标值
     */
    private float centerPointX;

    /**
     * 记录两指同时放在屏幕上时，中心点的纵坐标值
     */
    private float centerPointY;

    /**
     * 记录按下两指之间的距离
     */
    private double lastFingerDis;
    boolean isMoving = false;
    boolean isScaling = false;

    /**
     * 记录当前操作的状态，可选值为STATUS_ZOOM_OUT、STATUS_ZOOM_IN
     */
    private int currentStatus;


    private static final float TEXT_SIZE = 10;

    public float mWidth,mHeight,mlastX,mMove;

    int mValue;  //当前时间轴中点所在得刻度尺value
    int minValue = 0;


    public static  final float LINE_DIVIDER = 2f; //初始化每个刻度间距为2

    float mLineDivider = LINE_DIVIDER ;//记录一刻度屏幕上得距离

    static float lastItemDivider = LINE_DIVIDER;//缩放后一刻度得屏幕距离

    private float mDensity;
    private float ITEM_MAX_HEIGHT = 30f;
    private float ITEM_MIN_HEIGHT = 15f;
    private float textWidth;
    int mode = 0; // 0 表示拖拽 1 表示缩放

    Calendar calendar1 = Calendar.getInstance();
    Calendar calendar= Calendar.getInstance();
    Calendar selectCalendar = Calendar.getInstance();
    long minTimestamp;
    long maxTimestamp;

    List<RecordInfo> list = new ArrayList<>();

    private String[] timeString0 = {
            "00:00", "00:10","00:20","00:30","00:40","00:50", "01:00","01:10","01:20", "01:30","01:40","01:50",
            "02:00", "02:10","02:20","02:30","02:40","02:50", "03:00","03:10","03:20", "03:30","03:40","03:50",
            "04:00", "04:10","04:20","04:30","04:40","04:50", "05:00","05:10","05:20", "05:30","05:40","05:50",
            "06:00", "06:10","06:20","06:30","06:40","06:50", "07:00","07:10","07:20", "07:30","07:40","07:50",
            "08:00", "08:10","08:20","08:30","08:40","08:50", "09:00","09:10","09:20", "09:30","09:40","09:40",
            "10:00", "10:10","10:20","10:30","10:40","10:50", "11:00","11:10","11:20", "11:30","11:40","11:50",
            "12:00", "12:10","12:20","12:30","12:40","12:50", "13:00","13:10","13:20", "13:30","13:40","13:50",
            "14:00", "14:10","14:20","14:30","14:40","14:50", "15:00","15:10","15:20", "15:30","15:40","15:50",
            "16:00", "16:10","16:20","16:30","16:40","16:50", "17:00","17:10","17:20", "17:30","17:40","17:50",
            "18:00", "18:10","18:20","18:30","18:40","18:50", "19:00","19:10","19:20", "19:30","19:40","19:50",
            "20:00", "20:10","20:20","20:30","20:40","20:50", "21:00","21:10","21:20", "21:30","21:40","21:50",
            "22:00", "22:10","22:20","22:30","22:40","22:50", "23:00","23:10","23:20", "23:30","23:40","23:50"};

    private String[] timeString1 = {
            "00:00", "00:30", "01:00", "01:30","02:00","02:30", "03:00", "03:30",
            "04:00", "04:30", "05:00", "05:30","06:00","06:30", "07:00", "07:30",
            "08:00", "08:30", "09:00", "09:30","10:00","10:30", "11:00", "11:30",
            "12:00", "12:30", "13:00", "13:30","14:00","14:30", "15:00", "15:30",
            "16:00", "16:30", "17:00", "17:30","18:00","18:30", "19:00", "19:30",
            "20:00", "20:30", "21:00", "21:30","22:00","22:30", "23:00", "23:30"};

    private String[] timeString2 = {
            "00:00",  "01:00",  "02:00", "03:00",  "04:00", "05:00",
            "06:00",  "07:00",  "08:00", "09:00",  "10:00", "11:00",
            "12:00",  "13:00", "14:00",  "15:00",  "16:00", "17:00",
            "18:00",  "19:00",  "20:00",  "21:00", "22:00", "23:00"};



    public int Mode = Mode_2 ;  // 当前的刻度尺缩放模式，默认模式一
    public static final int Mode_0 = 0; //模式一,当前刻度尺显示timeString0
    public static final int Mode_1 = 1; //模式一,当前刻度尺显示timeString1
    public static final int Mode_2 = 2; //模式一,当前刻度尺显示timeString2

    /**
     * 代表value值增加60,刻度尺对应多少（一个大刻度对应多少value）
     * Mode_0代表半个小时
     * Mode_1代表1个小时
     * Mode_2代表3个小时
     * Mode_3代表12个小时
     * Mode_4代表24个小时
     */
//    int largeScaleValue = 60;
    int largeScaleValue = 60;

    /**
     * 代表value值增加10,刻度尺对应多少（一个小刻度对应多少value）
     * Mode_0代表5分钟
     * Mode_1代表10分钟
     * Mode_2代表15分钟
     * Mode_3代表60分钟
     * Mode_4代表120分钟
     */
    int smallScaleValue = 10;

    public static int valueToSencond ;  // 代表mvalue加1，刻度尺增加多少秒

    private int REFRESH_TIME =  valueToSencond*1000;

    public String[] useString;


    private float scaledRatio;

    private OnValueChangeListener mListener;

    public OnValueChangeListener getmListener() {
        return mListener;
    }

    public void setmListener(OnValueChangeListener mListener) {
        this.mListener = mListener;
    }

    Paint linePaint = new Paint();
    Paint shadowPaint = new Paint();
    Paint alarmPaint = new Paint();
    Paint faceWhitePaint = new Paint();

    TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    public VideoRulerView(Context context) {
        super(context);
    }

    public VideoRulerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData();
    }

    void initData(){
        mMove = 0;
        useString = timeString2;
        valueToSencond = 60 ;  //当选择timeString1时,mvlaue加一,刻度尺增加60秒
//        valueToSencond = 10 ;  //当选择timeString1时,mvlaue加一,刻度尺增加10秒

        mDensity = getContext().getResources().getDisplayMetrics().density;
//        setBackgroundResource(R.drawable.bg_wheel);

        shadowPaint.setStyle(Paint.Style.FILL);
        shadowPaint.setColor(Color.parseColor("#3087CEFA"));

        alarmPaint.setStyle(Paint.Style.FILL);
        alarmPaint.setColor(Color.parseColor("#33ff0000"));

        linePaint.setStrokeWidth(2);
        linePaint.setColor(Color.BLACK);

        textPaint.setTextSize(TEXT_SIZE * mDensity);
        textWidth = Layout.getDesiredWidth("0", textPaint);

    }


    public void setVedioArea(List<RecordInfo> datas){
        list.clear();
        list.addAll(datas);
        postInvalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawScaleLine(canvas);
        drawMiddleLine(canvas);
        drawShadow(canvas);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getWidth();
        mHeight = getHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    //划刻度线
    public void drawScaleLine(Canvas canvas) {


        float width = mWidth, drawCount = 0;

        float xPosition;

        for (int i = 0; drawCount < width * 2; i++) {
            xPosition = (mWidth / 2 + mMove) + i * mDensity * (mLineDivider);
            if (((mValue + i)) % largeScaleValue == 0) {
                canvas.drawLine(xPosition, 0, xPosition, mDensity * ITEM_MAX_HEIGHT, linePaint);
                if ((((mValue + i)) / largeScaleValue % useString.length) < 0) {
                    canvas.drawText(useString[useString.length + (((mValue + i)) / largeScaleValue % useString.length)], countLeftStart(mValue + i, xPosition, textWidth), getHeight() - textWidth, textPaint);
                } else {
                    canvas.drawText(useString[((mValue + i)) / largeScaleValue % useString.length], countLeftStart(mValue + i, xPosition, textWidth), getHeight() - textWidth, textPaint);
                }

            } else {
                if ((mValue + i) % smallScaleValue == 0) {
                    canvas.drawLine(xPosition, 0, xPosition, mDensity * ITEM_MIN_HEIGHT, linePaint);
                }
            }

            xPosition = (mWidth / 2 + mMove) - i * mDensity * (mLineDivider);
            if (( (mValue - i)) % largeScaleValue == 0) {
                canvas.drawLine(xPosition, 0, xPosition, mDensity * ITEM_MAX_HEIGHT, linePaint);
                if ((( (mValue - i)) / largeScaleValue % useString.length) < 0) {
                    canvas.drawText(useString[useString.length + (((mValue - i)) / largeScaleValue % useString.length)], countLeftStart(mValue - i, xPosition, textWidth), getHeight() - textWidth, textPaint);
                } else {
                    canvas.drawText(useString[( (mValue - i)) / largeScaleValue % useString.length], countLeftStart(mValue - i, xPosition, textWidth), getHeight() - textWidth, textPaint);
                }
            } else {
                if ( (mValue - i) % smallScaleValue == 0) {
                    canvas.drawLine(xPosition, 0, xPosition, mDensity * ITEM_MIN_HEIGHT, linePaint);
                }
            }
            drawCount += mDensity * (mLineDivider);

        }
    }

    /**
     * 绘制视频区域
     * @param canvas
     */
    public void drawShadow(Canvas canvas){
        float startXPosition ;
        float endXPosition ;
        for (int j = 0; j < list.size(); j++) {
            RecordInfo info = list.get(j);
            if (info.getStartTime() < minTimestamp){
                info.setStartTime(minTimestamp);
            }

            if (info.getEndTime() > maxTimestamp){
                info.setEndTime(maxTimestamp);
            }
            //获取当前得value

            startXPosition = mWidth/2 - (mValue-getmValue(info.getStartTime()) ) * mDensity * (mLineDivider) +mMove * 99/100f ;
            endXPosition = mWidth/2 -( mValue-getmValue(info.getEndTime()) ) * mDensity * (mLineDivider) +mMove * 99/100f;
           if (info.getType() == 2){
               canvas.drawRect(startXPosition,0,endXPosition,mHeight-TEXT_SIZE * mDensity*2,alarmPaint);
           }else {
               canvas.drawRect(startXPosition, 0, endXPosition, mHeight - TEXT_SIZE * mDensity * 2, shadowPaint);
           }


        }
    }

    /**
     * 计算数字显示位置的辅助方法
     *
     * @param value
     * @param xPosition
     * @param textWidth
     * @return
     */
    private float countLeftStart(int value, float xPosition, float textWidth) {
        float xp;
        xp = xPosition - (textWidth * 5 / 2); //从2.5个字开始写
        return xp;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int xPosition = (int) event.getX();
        int xMove;
        switch (event.getActionMasked() & event.getAction()) {
            case MotionEvent.ACTION_POINTER_DOWN:
                if (event.getPointerCount() == 2) {
                    // 当有两个手指按在屏幕上时，计算两指之间的距离
                    lastFingerDis = distanceBetweenFingers(event);
                    mode = 1;
                }
                break;
            case MotionEvent.ACTION_DOWN:
                if (event.getPointerCount() == 1){
                    isMoving = true;
                    isScaling = false;
                    mode = 0;
                }else {
                    mode = 1;
                    isMoving = false;
                    isScaling = true;
                }


                mlastX = xPosition;
                mMove = 0;
                break;
            case MotionEvent.ACTION_UP:
                if (event.getPointerCount() == 1 && isMoving && !isScaling) {
                    isMoving = false;
                    //当滑动到停止，改变当前刻度值
                    xMove = (int) (mMove / (mLineDivider * mDensity));
                    mValue -= xMove;
                    if (mValue <= 1){
                        mValue = 1;
                    }
                    if (mValue >= 86389/valueToSencond){
                        mValue = 86389/valueToSencond;
                    }
                    notifyValueChange();

                }else if (isScaling){

                    isMoving = false;
                    //当滑动到停止，改变当前刻度值
                    xMove = (int) (mMove / (mLineDivider * mDensity));
                    mValue -= xMove;

                }
                mMove = 0;
                mLineDivider = 2;
                lastItemDivider = 2;
                postInvalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() == 1 && mode == 0) {
                    isMoving = true;
                    mMove = xPosition - mlastX;
                    xMove = (int) (mMove / (mLineDivider * mDensity * 100));
                    mValue -= xMove;
                    Log.e("value","value  "+mValue);
//                    if (mValue <= 20){
//                        mValue = 20;
//                    }
//                    if (mValue >= 4900/valueToSencond){
//                        mValue = 4900/valueToSencond;
//                    }
                    postInvalidate();
                    if (Math.abs(mMove)> 10) {
                        onMoving();
                    }
//                        notifyValueChange();
                } else if (event.getPointerCount() == 2) {
                    // 有两个手指按在屏幕上移动时，为缩放状态
                    centerPointBetweenFingers(event);
                    double fingerDis = distanceBetweenFingers(event);

                    if (fingerDis > lastFingerDis) {
                        currentStatus = STATUS_ZOOM_OUT;
                    } else {
                        currentStatus = STATUS_ZOOM_IN;
                    }

                    if(Mode==Mode_0&&currentStatus==STATUS_ZOOM_OUT||Mode==Mode_2&&currentStatus==STATUS_ZOOM_IN){
                        Log.e("wzytest","Mode_0 时放大 和 Mode_4 时候缩小不做任何处理");
                        break;
                    }else{
                        scaledRatio = (float) (fingerDis / lastFingerDis  );
                        shadowPaint.setStrokeWidth(6*scaledRatio);
                        mLineDivider =  lastItemDivider * scaledRatio; //缩放后一刻度在屏幕上的距离
                        isScaling = true;
                    }

                    if(currentStatus==STATUS_ZOOM_IN&&Mode==Mode_0){
                        if(1.5*mLineDivider<LINE_DIVIDER){
                            // 复位，转变刻度尺模式
                            mLineDivider = 2;
                            lastItemDivider = 2;
                            useString = timeString1;
                            Mode = Mode_1;
                            valueToSencond = 3*valueToSencond;
                            //重新获取当前value
                            //重新计算蓝色录像时间轴
                        }
                    }
                    else if(currentStatus==STATUS_ZOOM_IN&&Mode==Mode_1){
                        if(2*mLineDivider<LINE_DIVIDER){
                            // 复位，转变刻度尺模式
                            mLineDivider = 2;
                            lastItemDivider = 2;
                            useString = timeString2;
                            Mode = Mode_2;
                            valueToSencond = 2*valueToSencond;
                            //重新获取当前value
                        }
                    }

                   else if(currentStatus==STATUS_ZOOM_OUT&&Mode==Mode_2){
                        if(mLineDivider/1.5>LINE_DIVIDER){
                            mLineDivider = 2;
                            lastItemDivider = 2;
                            useString = timeString1;
                            Mode = Mode_1;
                            valueToSencond = valueToSencond /2 ;
                            //重新计算蓝色录像时间轴
                        }
                    }else if(currentStatus==STATUS_ZOOM_OUT&&Mode==Mode_1){
                        if(mLineDivider/2>LINE_DIVIDER){
                            mLineDivider = 2;
                            lastItemDivider = 2;
                            useString = timeString0;
                            Mode = Mode_0;
                            valueToSencond = valueToSencond/3;
                        }
                    }

                    postInvalidate();

                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                if (event.getPointerCount() == 2) {
                    //lastValueTosecond = valueToSencond;
                    lastItemDivider = mLineDivider;

                }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }

        return true;
//        return super.onTouchEvent(event);
    }

    private void notifyValueChange() {

        if (null != mListener) {
            mListener.onValueChange(mValue);
        }
    }

    private void onMoving(){
        if (null != mListener){
            mListener.onMoving();
        }
    }


    /**
     * 获取当前刻度值
     *
     * @return
     */
    public float getValue() {
        return mValue;
    }



    /**
     * 画中间的红色指示线
     *
     * @param canvas
     */
    private void drawMiddleLine(Canvas canvas) {
        int indexWidth = 5;
        String color = "#66999999";
        Paint redPaint = new Paint();
        redPaint.setStrokeWidth(indexWidth);
        redPaint.setColor(Color.RED);
        // canvas.drawLine(mWidth / 2, 0, mWidth / 2 , mHeight, shadowPaint);
        canvas.drawLine(mWidth / 2, 0, mWidth / 2, mHeight, redPaint);

    }

    public static int getNowValue() {
        Calendar c = Calendar.getInstance();//可以对每个时间域单独修改

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);


        return ((hour * 60 + minute) * 60 + second) / valueToSencond ;

    }

    public void setCurrentIime(long tamstamp){
        if (isMoving)return;
        ;//可以对每个时间域单独修改

        calendar.setTimeInMillis(tamstamp);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        mValue = ((hour * 60 + minute) * 60 + second) / valueToSencond ;
        invalidate();
    }


    public void setSelectTime(int year,int month,int dayforMonth){
        selectCalendar.set(Calendar.YEAR,year);
        selectCalendar.set(Calendar.MONTH,month-1);
        selectCalendar.set(Calendar.DAY_OF_MONTH,dayforMonth);
        selectCalendar.set(Calendar.HOUR_OF_DAY,0);
        selectCalendar.set(Calendar.MINUTE,0);
        selectCalendar.set(Calendar.SECOND,10);
        minTimestamp = selectCalendar.getTimeInMillis();
        selectCalendar.set(Calendar.HOUR_OF_DAY,23);
        selectCalendar.set(Calendar.MINUTE,59);
        selectCalendar.set(Calendar.SECOND,50);
        maxTimestamp = selectCalendar.getTimeInMillis();
        Log.e("timesample ","min  "+minTimestamp+"  max    "+maxTimestamp);
    }
    /**
     * 获取mValue 所对应的时间
     * @param mValue
     * @return
     */
    public  String getTime(float mValue) {
        // TODO: 2017/10/23  超过24小时 和 少于0小时的处理

        int day = (int) (mValue *  valueToSencond / (3600 * 24));  // 天数
        int hour = (int) ((mValue *  valueToSencond - (60 * 60 * 24) * day) / 3600);
        int minute = (int) (mValue * valueToSencond - 3600 * hour - (60 * 60 * 24) * day) / 60;
        int second = (int) mValue * valueToSencond - hour * 3600 - minute * 60 - (60 * 60 * 24) * day;

        Calendar calendar1 = (Calendar) calendar.clone();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        /**
         *  从00:00:00开始设置时间
         */
        calendar1.add(Calendar.DATE, day);
        calendar1.set(Calendar.HOUR_OF_DAY, hour);
        calendar1.set(Calendar.MINUTE, minute);
        calendar1.set(Calendar.SECOND, second);

        String moveDate = sdf1.format(calendar1.getTime());
        return moveDate;
    }

    public  long getTimeInMillis(float mValue){
        int day = (int) (mValue *  valueToSencond / (3600 * 24));  // 天数
        int hour = (int) ((mValue *  valueToSencond - (60 * 60 * 24) * day) / 3600);
        int minute = (int) (mValue * valueToSencond - 3600 * hour - (60 * 60 * 24) * day) / 60;
        int second = (int) mValue * valueToSencond - hour * 3600 - minute * 60 - (60 * 60 * 24) * day;

        Calendar calendar1 = (Calendar) calendar.clone();

        /**
         *  从00:00:00开始设置时间
         */
//        calendar1.add(Calendar.DATE, day);
        calendar1.set(Calendar.HOUR_OF_DAY, hour);
        calendar1.set(Calendar.MINUTE, minute);
        calendar1.set(Calendar.SECOND, second);

        return calendar1.getTimeInMillis();
    }


    /**
     * 获取时间戳所对应的mvalue
     * @param time 录像时间段time
     * @return
     */
    private int getmValue(long time) {
        /**
         *  从00:00:00开始设置时间
         */
        calendar1.setTimeInMillis(time);
        calendar1.set(Calendar.HOUR_OF_DAY, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        long l1 = calendar1.getTimeInMillis();
        return (int) (time - l1) / (1000 * valueToSencond);
    }



    /**
     * 计算两个手指之间的距离。
     *
     * @param event
     * @return 两个手指之间的距离
     */
    private float distanceBetweenFingers(MotionEvent event) {
        float disX = event.getX(0) - event.getX(1);
        float disY = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(Math.pow(disX,2)+Math.pow(disY,2));
    }

    /**
     * 计算两个手指之间中心点的坐标。
     *
     * @param event
     */
    private void centerPointBetweenFingers(MotionEvent event) {
        float xPoint0 = event.getX(0);
        float yPoint0 = event.getY(0);
        float xPoint1 = event.getX(1);
        float yPoint1 = event.getY(1);
        centerPointX = (xPoint0 + xPoint1) / 2;
        centerPointY = (yPoint0 + yPoint1) / 2;
    }


}