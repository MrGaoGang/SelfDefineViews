package mrgao.com.mrgaoviews.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import mrgao.com.mrgaoviews.R;
import mrgao.com.mrgaoviews.utils.DensityUtils;

/**
 * Created by mr.gao on 2017/12/24.
 * Package:    mrgao.com.mrgaoviews.views
 * Create Date:2017/12/24
 * Project Name:SelfDefineViews
 * Description:
 */

public class PanelCircleView extends View {

    private final static String LOG = "PanelCircleView";
    private Context mContext;
    private int mWidth;
    private int mHeight;

    private Paint mPaint;
    private Paint mPaintText;
    private Rect mRectText;

    private int mOutArcColor;//最外面的圆弧颜色
    private int mFillColor;//百分比颜色
    private int mEmptyColor;//非百分比颜色
    private int mPointerColor;//指针的颜色
    private int mCenterTextColor;//文字的颜色
    private int mMinCircleRaduis;//内部最小的圆的大小
    private int mScaleLength;//刻度的长度
    private int mScaleCount;//刻度的个数
    private int mTextSize;
    private String mContent = "30%";
    private float mPercent = 0.3f;


    private int outArcStrokeWidth = 6;//最外面的圆弧的线宽
    private int secondStrokeWidth = 40;//第二个圆弧的线宽

    private int defaultStrokeWidth = 3;//其他默认的线宽

    private int mStartAngle;//圆弧开始的角度
    private int mTotalAngle;//整个界面总的角度
    private int mOutToSecondWidth = 60;//最外面的圆弧和第二个圆弧之间的宽度间隔


    public PanelCircleView(Context context) {
        this(context, null);
    }

    public PanelCircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PanelCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PanelCircleView, defStyleAttr, 0);

        mMinCircleRaduis = (int) typedArray.getDimension(R.styleable.PanelCircleView_minCenterCircleRadius, DensityUtils.dp2px(context, 10));
        mOutArcColor = typedArray.getColor(R.styleable.PanelCircleView_outArcColor, Color.YELLOW);
        mEmptyColor = typedArray.getColor(R.styleable.PanelCircleView_emptyColor, Color.WHITE);
        mTextSize = (int) typedArray.getDimension(R.styleable.PanelCircleView_centerTextSize, DensityUtils.dp2px(context, 10));

        mCenterTextColor = typedArray.getColor(R.styleable.PanelCircleView_centerTextColor, Color.BLACK);
        mFillColor = typedArray.getColor(R.styleable.PanelCircleView_fillColor, Color.YELLOW);
        mScaleLength = (int) typedArray.getDimension(R.styleable.PanelCircleView_scaleLength, DensityUtils.dp2px(context, 10));
        mPointerColor = typedArray.getColor(R.styleable.PanelCircleView_pointerColor, Color.BLUE);
        mScaleCount = typedArray.getInt(R.styleable.PanelCircleView_scaleCount, 14);
        mTotalAngle = typedArray.getInt(R.styleable.PanelCircleView_totalAngle, 250);


        typedArray.recycle();
        mStartAngle = (int) ((360f - mTotalAngle) / 2 + 90);

        mPaint = new Paint();
        mPaintText = new Paint();
        mPaintText.setStyle(Paint.Style.FILL);
        mPaintText.setTextSize(mTextSize);
        mPaintText.setColor(mCenterTextColor);

        mRectText = new Rect();
        mPaintText.getTextBounds(mContent, 0, mContent.length(), mRectText);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
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
            mHeight = mWidth;//保证宽高一致
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制最外面的圆弧
        mPaint.setColor(mOutArcColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(outArcStrokeWidth);
        mPaint.setAntiAlias(true);

        canvas.drawArc(new RectF(outArcStrokeWidth, outArcStrokeWidth,
                        mWidth - outArcStrokeWidth, mHeight - outArcStrokeWidth),
                mStartAngle, mTotalAngle,
                false, mPaint);

        //绘制第二个圆弧
        //绘制空的部分
        mPaint.setColor(mEmptyColor);
        mPaint.setStrokeWidth(secondStrokeWidth);
        RectF secondF = new RectF(outArcStrokeWidth + mOutToSecondWidth,
                outArcStrokeWidth + mOutToSecondWidth,
                mWidth - outArcStrokeWidth - mOutToSecondWidth,
                mWidth - outArcStrokeWidth - mOutToSecondWidth);

        canvas.drawArc(secondF, mStartAngle, mTotalAngle,
                false, mPaint);


        //绘制进度部分
        //绘制进度
        mPaint.setColor(mFillColor);
        canvas.drawArc(new RectF(outArcStrokeWidth + mOutToSecondWidth,
                        outArcStrokeWidth + mOutToSecondWidth,
                        mWidth - outArcStrokeWidth - mOutToSecondWidth,
                        mWidth - outArcStrokeWidth - mOutToSecondWidth),
                mStartAngle,
                mTotalAngle * mPercent,//进度*百分比
                false, mPaint);


        //绘制第三个小圆
        mPaint.setColor(mOutArcColor);
        mPaint.setStrokeWidth(defaultStrokeWidth);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mMinCircleRaduis + 20, mPaint);
        //绘制圆心小圆
        mPaint.setColor(mFillColor);
        mPaint.setStrokeWidth(defaultStrokeWidth + 10);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mMinCircleRaduis, mPaint);


        //绘制刻度
        mPaint.setStrokeWidth(defaultStrokeWidth);
        mPaint.setColor(mOutArcColor);
        //绘制第一个刻度
        canvas.drawLine(
                mWidth / 2,
                outArcStrokeWidth + getPaddingTop(),
                mWidth / 2,
                outArcStrokeWidth + getPaddingTop() + mScaleLength,
                mPaint);
        float angle = mTotalAngle / mScaleCount;//刻度需要旋转的角度

        //绘制右边的刻度
        for (int i = 0; i < mScaleCount / 2; i++) {
            canvas.rotate(angle, mWidth / 2, mHeight / 2);
            canvas.drawLine(
                    mWidth / 2,
                    outArcStrokeWidth + getPaddingTop(),
                    mWidth / 2,
                    outArcStrokeWidth + getPaddingTop() + mScaleLength,
                    mPaint);

        }
        //把画布旋转回来
        canvas.rotate(-angle * mScaleCount / 2, mWidth / 2, mHeight / 2);

        //绘制左边的刻度
        for (int i = 0; i < mScaleCount / 2; i++) {
            canvas.rotate(-angle, mWidth / 2, mHeight / 2);
            canvas.drawLine(mWidth / 2,
                    outArcStrokeWidth + getPaddingTop(),
                    mWidth / 2,
                    outArcStrokeWidth + getPaddingTop() + mScaleLength,
                    mPaint);

        }
        //把画布旋转回来
        canvas.rotate(angle * mScaleCount / 2, mWidth / 2, mHeight / 2);


        //绘制指针
        mPaint.setColor(mPointerColor);
        canvas.rotate((mTotalAngle * mPercent - mTotalAngle / 2), mWidth / 2, mHeight / 2);
        //指针的Y轴末尾的坐标是，小圆外面
        float startY = mHeight / 2 - secondF.height() / 2 + secondStrokeWidth / 2;
        float endY = mHeight / 2 - outArcStrokeWidth - mMinCircleRaduis - getPaddingTop();
        canvas.drawLine(mWidth / 2, startY, mWidth / 2, endY, mPaint);
        //旋转回来
        canvas.rotate(-(mTotalAngle * mPercent - mTotalAngle / 2), mWidth / 2, mHeight / 2);


        //绘制文本
        mPaintText.getTextBounds(mContent, 0, mContent.length(), mRectText);
        canvas.drawText(mContent,
                (mWidth - mRectText.width()) / 2,
                mHeight / 2 + mMinCircleRaduis + 20 + mOutToSecondWidth,
                mPaintText);

    }


    public void setScaleLength(int scaleLength) {
        mScaleLength = scaleLength;
        invalidate();
    }

    public void setScaleCount(int scaleCount) {
        mScaleCount = scaleCount;
        invalidate();
    }

    public void setContent(String content) {
        mContent = content;
        invalidate();
    }

    public void setPercent(float percent) {
        if (percent < 1 && percent > 0) {
            mPercent = percent;
        } else if (percent > 1 && percent < 100) {
            mPercent = percent / 100;
        } else {
            Log.i(LOG, "invalid number  percent");
            return;
        }

        invalidate();
    }


}
