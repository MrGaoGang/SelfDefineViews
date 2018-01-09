package mrgao.com.mrgaoviews.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.List;
import java.util.Random;

import mrgao.com.mrgaoviews.R;
import mrgao.com.mrgaoviews.model.Bubble;
import mrgao.com.mrgaoviews.model.BubbleFactory;
import mrgao.com.mrgaoviews.model.Circle;
import mrgao.com.mrgaoviews.utils.DensityUtils;

/**
 * Created by mr.gao on 2018/1/9.
 * Package:    mrgao.com.mrgaoviews.views
 * Create Date:2018/1/9
 * Project Name:SelfDefineViews
 * Description:
 */

public class ChargeBubbleView extends View {
    private String TAG = "ChargeBubbleView";

    //气泡到达一周的时间
    private int BUBBLE_FLOAT_TIME = 2000;
    // 中等振幅大小
    private static final int MIDDLE_AMPLITUDE = 20;
    // 不同类型之间的振幅差距
    private static final int AMPLITUDE_DISPARITY = 20;
    //气泡最大的半径大小
    private static final int BUBBLE_MAR_RADIUS = 30;
    // 中等振幅大小
    private int mMiddleAmplitude = MIDDLE_AMPLITUDE;
    // 振幅差
    private int mAmplitudeDisparity = AMPLITUDE_DISPARITY;

    // 叶子飘动一个周期所花的时间
    private long mBubbleFloatTime = BUBBLE_FLOAT_TIME;

    private Random mRandom = new Random();
    private int mWidth;
    private int mHeight;
    //气泡的颜色
    private int mBubbleColor = Color.RED;
    //组件中心宽
    private int mMidWidth;
    //组件中心高
    private int mMidHeight;
    //当前的进度
    private int mProgress = 0;
    private int mcurrentProgress;
    //气泡结束的位置
    private int mBubbleEndHeight;
    //下方的那个出气泡位置的半径
    private int mBottomArcRadius = 40;
    //中心那个圆形进度条的半径
    private int mCenterCircleRadius = 150;
    //产生气泡的工具类
    private BubbleFactory mBubbleFactory;
    //所以气泡的数量
    private List<Bubble> mBubbleList;
    //下方气泡的画笔
    private Paint mPaint;
    //中心处圆的画笔
    private Paint mCenterPaint;
    //进度动画
    private ValueAnimator mValueAnimator;//进度动画
    //进度显示的
    private Rect mTextRect;
    private Paint mTextPaint;
    private int mTextColor;
    private int mTextSize;

    //外层包围的原点的个数的集合
    private Circle[] mCircles;
    //每个原点需要旋转的角度
    private float[] mRotates;
    //需要设置的原点的个数
    private int mCircleCount = 20;

    public ChargeBubbleView(Context context) {
        this(context, null);

    }

    public ChargeBubbleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChargeBubbleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ChargeBubbleView, defStyleAttr, 0);

        mBubbleColor = typedArray.getColor(R.styleable.ChargeBubbleView_bubbleColor, Color.BLUE);
        mTextSize = (int) typedArray.getDimension(R.styleable.ChargeBubbleView_textSize, DensityUtils.dp2px(context, 16));
        mTextColor = typedArray.getColor(R.styleable.ChargeBubbleView_textColor, Color.BLACK);

        typedArray.recycle();
        init();
    }

    private void init() {
        mBubbleFactory = new BubbleFactory((int) BUBBLE_FLOAT_TIME);
        mBubbleList = mBubbleFactory.getBubbles(mProgress, BUBBLE_MAR_RADIUS);
        mPaint = new Paint();
        mPaint.setColor(mBubbleColor);
        mPaint.setAntiAlias(true);

        mCenterPaint = new Paint();
        mCenterPaint.setAntiAlias(true);
        mCenterPaint.setColor(mBubbleColor);
        mCenterPaint.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextRect = new Rect();
        mTextPaint.getTextBounds(mProgress + "%", 0, (mProgress + "%").length(), mTextRect);

        mCircles = new Circle[mCircleCount];
        mRotates = new float[mCircleCount];
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
            mHeight = getMeasuredHeight();//保证宽高一致
        }
        mMidWidth = mWidth / 2;
        mMidHeight = mHeight / 2;
        mBubbleEndHeight = mMidHeight - mCenterCircleRadius;

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
        mWidth = w;
        mMidWidth = mWidth / 2;
        mMidHeight = mHeight / 2;
        mBubbleEndHeight = mMidHeight - mCenterCircleRadius;
        initCircle();
        circleAnimation();
    }

    /**
     * 初始化外层的圆,分别为两个部分的圆，设置位置和半径以及颜色
     */
    private void initCircle() {
        mCircles = new Circle[mCircleCount];
        float circleRadius = mCenterCircleRadius * 2 / mCircleCount;
        for (int i = 0; i < mCircleCount; i++) {
            mCircles[i] = new Circle();
            if (i < mCircleCount / 2) {
                mCircles[i].setCenter(mMidWidth, mMidHeight - mCenterCircleRadius);
                mCircles[i].setColor(mBubbleColor);
                mCircles[i].setRadius(circleRadius - circleRadius * i / (mCircleCount / 2 + 1));
            } else {
                mCircles[i].setCenter(mMidWidth, mMidHeight + mCenterCircleRadius);
                mCircles[i].setColor(mBubbleColor);
                mCircles[i].setRadius(circleRadius - circleRadius * (i - mCircleCount / 2) / (mCircleCount / 2 + 1));
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mcurrentProgress < mProgress && mProgress > 0) {
            drawBottomArc(canvas);
            drawBubbles(canvas);
            postInvalidate();
        }

        drawCenterCircle(canvas);
        drawTextView(canvas);
    }

    /**
     * 绘制最底部的半圆
     *
     * @param canvas
     */
    @SuppressLint("NewApi")
    private void drawBottomArc(Canvas canvas) {
        canvas.drawArc(mMidWidth - mBottomArcRadius,
                mHeight - 2 * mBottomArcRadius,
                mMidWidth + mBottomArcRadius,
                mHeight,
                180,
                180,
                true,
                mPaint);
    }


    /**
     * 绘制下方的气泡
     *
     * @param canvas
     */
    private void drawBubbles(Canvas canvas) {
        mBubbleFloatTime = mBubbleFloatTime <= 0 ? BUBBLE_FLOAT_TIME : mBubbleFloatTime;
        long current = System.currentTimeMillis();
        for (int i = 0; i < mBubbleList.size(); i++) {
            Bubble bubble = mBubbleList.get(i);

            if (current > bubble.getTime() && bubble.getTime() != 0) {
                canvas.save();
                getBubbleLoaction(bubble, current);

                canvas.drawCircle(bubble.getX(), bubble.getY(), bubble.getRadius(), mPaint);

                canvas.restore();
            } else {
                continue;
            }
        }


    }

    /**
     * 绘制
     *
     * @param canvas
     */
    private void drawCenterCircle(Canvas canvas) {
        for (int i = 0; i < mCircleCount; i++) {
            canvas.save();
            canvas.rotate(mRotates[i], mMidWidth, mMidHeight);
            canvas.drawCircle(mCircles[i].getCenter().x, mCircles[i].getCenter().y, mCircles[i].getRadius(), mCenterPaint);
            canvas.restore();
        }
    }

    /**
     * 绘制文字
     *
     * @param canvas
     */
    private void drawTextView(Canvas canvas) {
        mTextPaint.getTextBounds(mProgress + "%", 0, (mProgress + "%").length(), mTextRect);
        canvas.drawText(mProgress + "%", mMidWidth - mTextRect.width() / 2, mMidHeight, mTextPaint);
    }

    /**
     * 获取到气泡的Y轴的坐标值
     *
     * @param bubble
     * @param currentTime
     */
    private void getBubbleLoaction(Bubble bubble, long currentTime) {
        long internal = currentTime - bubble.getTime();
        mBubbleFloatTime = mBubbleFloatTime <= 0 ? BUBBLE_FLOAT_TIME : mBubbleFloatTime;
        if (internal < 0) {
            return;
        } else if (internal > mBubbleFloatTime) {

            bubble.setTime(System.currentTimeMillis() + mRandom.nextInt((int) (mBubbleFloatTime / 3)) + mBubbleFloatTime);
        }

        float fraction = (float) internal / mBubbleFloatTime;
        bubble.setY((int) (mHeight - mBubbleEndHeight * fraction));
        bubble.setX(getBubbleLocationX(bubble));

    }

    /**
     * 获取到气泡的X轴的值
     *
     * @param bubble
     * @return
     */
    private int getBubbleLocationX(Bubble bubble) {
        // x = A(wy+Q)+h
        float w = (float) ((float) 2 * Math.PI / mBubbleEndHeight);
        float a = mMiddleAmplitude;
        switch (bubble.getType()) {
            case LITTLE:
                // 小振幅 ＝ 中等振幅 － 振幅差
                a = mMiddleAmplitude - mAmplitudeDisparity;
                break;
            case MIDDLE:
                a = mMiddleAmplitude;
                break;
            case BIG:
                // 小振幅 ＝ 中等振幅 + 振幅差
                a = mMiddleAmplitude + mAmplitudeDisparity;
                break;
            default:
                break;
        }
        return (int) (a * Math.sin(w * bubble.getY())) + mMidWidth;
    }


    /**
     * 进度动画
     */
    private void progressAnimation() {
        if (mValueAnimator == null) {
            mValueAnimator = ValueAnimator.ofInt(0, mProgress);
        }
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.setDuration(mProgress * mBubbleFloatTime);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mcurrentProgress = (int) valueAnimator.getAnimatedValue();
            }
        });
        mValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mcurrentProgress = mProgress;
            }
        });
        mValueAnimator.start();
    }

    /**
     * 外层的圆的动画
     */
    private void circleAnimation() {
        for (int i = 0; i < mCircleCount; i++) {
            final int index = i;
            ValueAnimator fadeAnimator = ValueAnimator.ofFloat(0, 360);
            fadeAnimator.setRepeatCount(ValueAnimator.INFINITE);
            fadeAnimator.setDuration(3000);
            fadeAnimator.setStartDelay((index >= mCircleCount / 2 ? index - mCircleCount / 2 : index) * 120);
            fadeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mRotates[index] = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });

            fadeAnimator.start();
        }
    }

    /**
     * 设置进度
     *
     * @param progress
     */
    public void setProgress(int progress) {
        if (progress > 1 && progress <= 100) {
            mProgress = progress;
            mBubbleList.clear();
            mBubbleList = mBubbleFactory.getBubbles(progress, BUBBLE_MAR_RADIUS);
            postInvalidate();
            progressAnimation();
        }
    }

    /**
     * Created by mr.gao on 2018/1/9.
     * Package:    mrgao.com.mrgaoviews.model
     * Create Date:2018/1/9
     * Project Name:SelfDefineViews
     * Description:
     */

    public enum BubbleStartType {
        LITTLE, MIDDLE, BIG
    }
}
