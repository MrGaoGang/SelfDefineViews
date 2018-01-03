package mrgao.com.mrgaoviews.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import mrgao.com.mrgaoviews.R;

/**
 * Created by mr.gao on 2018/1/3.
 * Package:    mrgao.com.mrgaoviews.views
 * Create Date:2018/1/3
 * Project Name:SelfDefineViews
 * Description:
 */

public class My360JumpView extends View {
    private int mWidth;
    private int mHeight;

    private Bitmap mBitmap;
    private int mLineColor;
    private int mPointColor;

    private Point mStartPoint;
    private Point mEndPoint;
    private Point mControllPoint;

    private Path mPath;

    private Paint mPointPaint;
    private Paint mLinePaint;
    private Paint mPaint;

    private int mBitmapX;//图片的X轴
    private int mBitmapY;//图片的Y轴

    private boolean mIsJumpBack = false;

    private int mJumpPercent = 100;

    private int mJumpCount = 2;//这个表示来回的跳跃次数
    private int mCurrentJumpCount = 0;

    public My360JumpView(Context context) {
        this(context, null);
    }

    public My360JumpView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public My360JumpView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.My360JumpView, defStyleAttr, 0);
        mLineColor = typedArray.getColor(R.styleable.My360JumpView_centerLineColor, Color.BLUE);
        mPointColor = typedArray.getColor(R.styleable.My360JumpView_pointrColor, Color.BLACK);
        typedArray.recycle();

        mStartPoint = new Point();
        mEndPoint = new Point();
        mControllPoint = new Point();


        mPath = new Path();


        mPointPaint = new Paint();
        mLinePaint = new Paint();
        mPointPaint.setColor(mPointColor);
        mPointPaint.setAntiAlias(true);
        mPointPaint.setStyle(Paint.Style.FILL);

        mLinePaint.setColor(mLineColor);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(10);

        mPaint = new Paint();


        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        } else {
            mWidth = getMeasuredWidth();
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            mHeight = heightSize;
        }
        mStartPoint.set(mWidth / 4, mHeight / 2);
        mControllPoint.set(mWidth / 2, mHeight / 2);
        mEndPoint.set(mWidth * 3 / 4, mHeight / 2);

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制开始点和结束点
        canvas.drawCircle(mStartPoint.x, mStartPoint.y, 10, mPointPaint);
        canvas.drawCircle(mEndPoint.x, mEndPoint.y, 10, mPointPaint);
        if (mIsJumpBack) {
            jumpBack(canvas);
        } else {
            noJumpBack(canvas);
        }
    }

    /**
     * Author: MrGao
     * CreateTime: 2018/1/3 21:34
     * MethodName:
     * Des：有回跳
     * Params：
     * Return:
     **/
    private void jumpBack(Canvas canvas) {
        mPath.moveTo(mStartPoint.x, mStartPoint.y);

        mPath.quadTo(mControllPoint.x, mHeight / 2 + (mControllPoint.y - mHeight / 2) * mJumpPercent / 100, mEndPoint.x, mEndPoint.y);
        canvas.drawPath(mPath, mLinePaint);
        mPath.reset();//一定要加这一句，不然移动的时候回保留痕迹

        if (mJumpPercent > 0) {
            canvas.drawBitmap(mBitmap, mBitmapX, mBitmapY * mJumpPercent / 100, mPaint);
            mJumpPercent = mJumpPercent - 5;
            postInvalidateDelayed(20);
        } else {//表示已经到了顶部了
            mIsJumpBack = false;
            mJumpPercent = 100;
        }


    }

    /**
     * Author: MrGao
     * CreateTime: 2018/1/3 21:34
     * MethodName:
     * Des：没有回跳
     * Params：
     * Return:
     **/
    private void noJumpBack(Canvas canvas) {

        mPath.moveTo(mStartPoint.x, mStartPoint.y);
        mPath.quadTo(mControllPoint.x, mControllPoint.y, mEndPoint.x, mEndPoint.y);
        canvas.drawPath(mPath, mLinePaint);
        mPath.reset();//一定要加这一句，不然移动的时候回保留痕迹


        mBitmapX = mControllPoint.x - mBitmap.getWidth() / 2;
        //Y轴的值是：默认下拉的时候图片的位置是在两个点和控制点之间，那么Y就是......
        mBitmapY = (mControllPoint.y - mHeight / 2) / 2 + mHeight / 2 - mBitmap.getHeight();

        canvas.drawBitmap(mBitmap, mBitmapX, mBitmapY, mPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int y = (int) event.getY();

                int x = (int) event.getX();
                if (x < mEndPoint.x && x > mStartPoint.x) {
                    mControllPoint.x = x;
                }
                if (y > mHeight / 2) {
                    mControllPoint.y = y;
                }
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                mIsJumpBack = true;
                invalidate();
                break;
        }
        return true;
    }
}
