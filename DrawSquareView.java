package shix.camerap2p.client.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import shix.camerap2p.client.R;

public class DrawSquareView extends View {

    Paint mPaint;
    Paint mPaintBase;
    Rect mRectBase;
    Rect mRect;
    // 屏幕的宽高
    int mWidth;
    int mHeight;

    //属性内容显示宽高 dp
    float attrWidth;
    float attrHeight;

    float marginTop;

    int showWidth;
    int showHeight;

    int attrBgColor;
    int attrBorderColor;

    Paint mPaintBorder;
    Rect mRectBorder;
    TypedArray mTypedArray;
    public DrawSquareView(Context context) {
        super(context,null);
    }

    public DrawSquareView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttri(context,attrs);
        initData();
    }

    public DrawSquareView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);
    }
    void initAttri(Context context,AttributeSet attributeSet){
       mTypedArray = context.obtainStyledAttributes(attributeSet,R.styleable.DrawSquareView);
       int count = mTypedArray.getIndexCount();
       for (int i = 0;i<count;i++){
           int attr = mTypedArray.getIndex(i);
           switch (attr){
               case R.styleable.DrawSquareView_BackGroudColor: // 蒙层颜色
                   attrBgColor = mTypedArray.getColor(attr,Color.GRAY);
                   break;
               case R.styleable.DrawSquareView_BorderColor:  // 边框颜色
                   attrBorderColor = mTypedArray.getColor(attr,Color.GREEN);
                   break;
               case  R.styleable.DrawSquareView_insideHeight:
                   attrHeight = mTypedArray.getDimension(attr,180);
                   break;
               case R.styleable.DrawSquareView_insideWidth:
                   attrWidth = mTypedArray.getDimension(attr,150);
                   break;
               case R.styleable.DrawSquareView_marginTop:
                   marginTop = mTypedArray.getDimension(attr,0);
                   break;
                   default:
                       break;
           }
       }
       mTypedArray.recycle();
       showWidth = (int) attrWidth;
       showHeight = (int) attrHeight;
        Log.e("draw",attrWidth+"---------"+attrHeight);
        Log.e("draw",showWidth+"---------"+showHeight);
    }
    void initData(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.GRAY);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
        mPaint.setStrokeWidth(2);

        mPaintBase = new Paint();
        mPaintBase.setAntiAlias(true);
        mPaintBase.setColor(Color.GRAY);
        mPaintBase.setStyle(Paint.Style.FILL);
        mPaintBorder = new Paint();
        mPaintBorder.setAntiAlias(true);
        mPaintBorder.setStyle(Paint.Style.STROKE);
        mPaintBorder.setStrokeWidth(2.5f);
        mPaintBorder.setColor(Color.GREEN);
        mPaintBorder.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth = canvas.getWidth();
        mHeight = canvas.getHeight();
        mRectBase = new Rect(0,0,mWidth,mHeight);
        canvas.drawRect(mRectBase,mPaintBase);

        mRect = new Rect(mWidth/2-showWidth/2,(int)marginTop,mWidth/2+showWidth/2,(int)marginTop+showHeight);
        canvas.drawRect(mRect,mPaint);
        mRectBorder = new Rect(mWidth/2-showWidth/2-2,(int)marginTop-2,mWidth/2+showWidth/2+2,showHeight+2+(int)marginTop);
        canvas.drawRect(mRectBorder,mPaintBorder);


    }

    int dp2px(Context context,float dipValue){
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dipValue*scale+0.5f);
    }
}
