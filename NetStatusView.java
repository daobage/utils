package shix.camerap2p.client.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class NetStatusView extends View {
    int width;
    int height;
    Paint mPaintStroke;  // 信号量没有达到
    Paint mPaintSolid;  // 信号量达到


    Rect rect1;
    Rect rect2;
    Rect rect3;
    Rect rect4;
    Rect rect5;
    int index = -1;
    int bottom;
    int left;
    int right;
    int top;
    int net_2g[][] = {
            {0,5},
            {6,12},
            {13,19},
            {20,25},
            {26,31}
    };
    int net_3g[][]={
            {0,17},
            {18,35},
            {36,53},
            {54,71},
            {72,91}
    };
    int net_4g[][]={
            {0,19},
            {20,39},
            {40,59},
            {60,79},
            {80,97}
    };
    public NetStatusView(Context context) {
        super(context);
    }

    public NetStatusView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initData();
    }

    public NetStatusView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    void initData(){
        mPaintStroke = new Paint();
        mPaintStroke.setStyle(Paint.Style.FILL);
        mPaintStroke.setAntiAlias(true);
        mPaintStroke.setColor(Color.parseColor("#dcdcdc"));
        mPaintSolid = new Paint();
        mPaintSolid.setAntiAlias(true);
        mPaintSolid.setStyle(Paint.Style.FILL);
        mPaintSolid.setColor(Color.parseColor("#333333"));
        rect1 = new Rect();
        left = dp2px(6);
        right = left+dp2px(2);
        bottom = dp2px(20);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawNetStatus(canvas);
    }

    public void getIndexforValue(int value,NetStatueEnum netStatue){
       switch (netStatue){
           case NET_2G:
               index = getValueIndex(value,net_2g);
               break;
           case NET_3G:
               index = getValueIndex(value,net_3g);
               break;
           case NET_4G:
               index = getValueIndex(value,net_4g);
               break;
               default:
                   index = -1;
                   break;
       }
       postInvalidate();
    }

    private int getValueIndex(int value,int[][] values){
        for (int i=0;i<5;i++){
            if (values[i][0] <= value && values[i][1] >= value){
                return i;
            }
        }
        return -1;
    }

    private void drawNetStatus(Canvas canvas){

       for (int i=0;i<=index;i++){
           left = dp2px(6+i*6);
           right = left+dp2px(4);
           top = dp2px(24-3*i);
           bottom = dp2px(30);
           rect1.top = top;
           rect1.left = left;
           rect1.right = right;
           rect1.bottom = bottom;
           canvas.drawRect(rect1,mPaintSolid);
       }
       for (int j=index+1;j<5;j++){
           left = dp2px(6+j*6);
           right = left+dp2px(4);
           top = dp2px(24-3*j);
           bottom = dp2px(30);
           rect1.top = top;
           rect1.left = left;
           rect1.right = right;
           rect1.bottom = bottom;
           canvas.drawRect(rect1,mPaintStroke);
       }
    }

    int dp2px(long dp){
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dp*scale+0.5f);
    }

}
