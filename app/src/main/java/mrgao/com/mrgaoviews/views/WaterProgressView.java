package mrgao.com.mrgaoviews.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import mrgao.com.mrgaoviews.R;
import mrgao.com.mrgaoviews.utils.DensityUtils;

/**
 * Created by mr.gao on 2018/1/4.
 * Package:    mrgao.com.mrgaoviews.views
 * Create Date:2018/1/4
 * Project Name:SelfDefineViews
 * Description:
 */

public class WaterProgressView extends View {
    private static final int mDefaultLineLen = 20;//默认的最外面的刻度长度
    private static final int mProgress_SPACE_WATER = 10;//最外层的进度条和内部水波纹的间隔

    private int mOutBaseColor;//外层刻度进度条的背景颜色
    private int mProgressColor;//外层刻度进度条的颜色
    private int mfirstWaterColor;//水波纹第一个波纹的颜色
    private int mSecondWaterColor;//第二个波纹的颜色
    private int mTextColor;

    private int mDefaultWidth = 300;//默认的宽，没有设置组件宽高的时候
    private int mPercent = 0;//当前进度
    private int mWidth;//组件的宽度
    private int mHeight;//组件的高度
    private int mTextSize;//文字的大小
    private int mWaterWidth;//水波纹圆形的宽度
    private int mWaterHeight;//水波纹圆形的高度


    /*一些画笔*/
    private Paint mOutProgressPaint;//
    private Paint mFirstWaterPaint;
    private Paint mSecondWaterPaint;
    private Paint mTextPaint;

    private Path mFirstPath;
    private Path mSecondPath;
    /*用来测量文字的*/
    private Rect mTextRect;

    private int mStep = 0;//移步的步长
    private float mWaterRange = 30;//水波纹的高度
    private int mWaveCount = 2;//水波纹的个数
    private int mRadius;//水波纹圆的半径
    private int mWaveLength;//每一个波纹的宽度

    private int mProgressY;//水波纹的中心的Y轴

    private int mSpeed = 2000;//这个是设置水波纹的时间的

    ValueAnimator mWaveValueAnimator;//水波纹的属性动画
    ValueAnimator mProgressValueAnimator;//进度的属性动画

    public WaterProgressView(Context context) {
        this(context, null);
    }

    public WaterProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaterProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaterProgressView, defStyleAttr, 0);

        mOutBaseColor = typedArray.getColor(R.styleable.WaterProgressView_outBaseColor, Color.GRAY);
        mProgressColor = typedArray.getColor(R.styleable.WaterProgressView_outProColor, Color.BLUE);
        mfirstWaterColor = typedArray.getColor(R.styleable.WaterProgressView_firstWaterColor, Color.BLUE);
        mSecondWaterColor = typedArray.getColor(R.styleable.WaterProgressView_secondWaterColor, Color.GREEN);
        mTextColor = typedArray.getColor(R.styleable.WaterProgressView_waterTextColor, Color.BLACK);
        mTextSize = (int) typedArray.getDimension(R.styleable.WaterProgressView_waterTextSize, DensityUtils.dp2px(context, 16));
        mWaterRange = (int) typedArray.getDimension(R.styleable.WaterProgressView_waveRange, DensityUtils.dp2px(context, 10));
        mWaveCount = typedArray.getInt(R.styleable.WaterProgressView_waveCount, 2);
        typedArray.recycle();
        init();
    }

    /**
     * Author: MrGao
     * CreateTime: 2018/1/4 15:25
     * MethodName:
     * Des：初始化画笔
     * Params：
     * Return:
     **/
    private void init() {

        mOutProgressPaint = new Paint();
        mOutProgressPaint.setColor(mOutBaseColor);
        mOutProgressPaint.setStyle(Paint.Style.STROKE);
        mOutProgressPaint.setAntiAlias(true);

        mFirstWaterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFirstWaterPaint.setAntiAlias(true);
        mFirstWaterPaint.setColor(mfirstWaterColor);
        mFirstWaterPaint.setStrokeWidth(3);
        mFirstWaterPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mSecondWaterPaint = new Paint();
        mSecondWaterPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mSecondWaterPaint.setColor(mSecondWaterColor);
        mSecondWaterPaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);

        mTextRect = new Rect();
        mTextPaint.getTextBounds(mPercent + "%", 0, (mPercent + "%").length(), mTextRect);

        mFirstPath = new Path();
        mSecondPath = new Path();

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int min = Math.min(widthSize, heightSize);
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = min;
        } else {
            mWidth = mDefaultWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = min;
        } else {
            mHeight = mDefaultWidth;//保证宽高一致
        }
        mWaterWidth = mWidth - 2 * (mDefaultLineLen + mProgress_SPACE_WATER);
        mWaterHeight = mHeight - 2 * (mDefaultLineLen + mProgress_SPACE_WATER);
        mWaveLength = mWaterWidth / mWaveCount;
        mRadius = mWaterWidth / 2;
        setMeasuredDimension(mWidth, mHeight);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = Math.min(w, h);
        mHeight = Math.min(w, h);
        mWaterWidth = mWidth - 2 * (mDefaultLineLen + mProgress_SPACE_WATER);
        mWaterHeight = mHeight - 2 * (mDefaultLineLen + mProgress_SPACE_WATER);
        mWaveLength = mWaterWidth / mWaveCount;
        mRadius = mWaterWidth / 2;

        runWave();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制外层的刻度进度条
        initWaveRange();
        drawOutArc(canvas);
        mProgressY = mWaterHeight + mDefaultLineLen + mProgress_SPACE_WATER - (mWaterHeight * mPercent / 100);
        drawWaterView(canvas, false);
        drawWaterView(canvas, true);
        drawTextView(canvas);
    }

    /**
     * 初始化水波纹的振幅
     */
    private void initWaveRange() {
        if (mPercent < 50) {
            if (mPercent < 20)
                mWaterRange = 30;
            else
                mWaterRange = mPercent * 0.7f;

        } else {
            if (mPercent > 80)
                mWaterRange = 30;
            else
                mWaterRange = (100 - mPercent) * 0.7f;
        }
    }

    /**
     * 绘制外层的刻度
     *
     * @param canvas
     */
    private void drawOutArc(Canvas canvas) {
        canvas.save();
        for (int i = 0; i < 100; i++) {
            if (i < mPercent) {
                mOutProgressPaint.setColor(mProgressColor);
            } else {
                mOutProgressPaint.setColor(mOutBaseColor);
            }

            canvas.rotate(3.6f, mWidth / 2, mHeight / 2);
            canvas.drawLine(mWidth / 2, getPaddingTop(), mWidth / 2,
                    getPaddingTop() + mDefaultLineLen, mOutProgressPaint);

        }
        canvas.restore();
    }

    /**
     * 绘制水波纹
     *
     * @param canvas
     * @param isRight2Left
     */
    private void drawWaterView(Canvas canvas, boolean isRight2Left) {

        //重置并绘制一个圆
        mFirstPath.reset();
        canvas.clipPath(mFirstPath);
        mFirstPath.addCircle(mWidth / 2, mHeight / 2, mRadius, Path.Direction.CCW);
        canvas.clipPath(mFirstPath, Region.Op.REPLACE);


        int distance = mDefaultLineLen + mProgress_SPACE_WATER;//中线波浪和边角进度条的距离
        //确定贝塞尔曲线各个点的位置
        mFirstPath.reset();

        mFirstPath.moveTo(distance, mProgressY);

        //从左往右绘制
        if (!isRight2Left) {

            //w为什么是mWaveCount*2，那是因为需要在不可见的区域多绘制几个曲线，这样就可以形成
            //波浪的效果，如果只有一个mWaveCount的话，那么就是一个简单的正选曲线
            for (int i = 1; i <= mWaveCount * 2; i++) {
                int x1 = distance + mWaveLength * i - mWaveLength / 2 - mStep;

                if (i % 2 != 0) {
                    mFirstPath.quadTo(x1,
                            mProgressY - mWaterRange,
                            distance + mWaveLength * i - mStep,
                            mProgressY);
                } else {
                    mFirstPath.quadTo(
                            x1,
                            mProgressY + mWaterRange,
                            distance + mWaveLength * i - mStep,
                            mProgressY);
                }
            }

            mFirstPath.lineTo(distance + mWaveCount * mWaveLength,
                    distance + mWaterHeight);

            mFirstPath.lineTo(distance,
                    distance + mWaterHeight);

            mFirstPath.close();

            canvas.drawPath(mFirstPath, mFirstWaterPaint);


        } else {
            //重置并绘制一个圆
            mSecondPath.reset();
            canvas.clipPath(mSecondPath);
            mSecondPath.addCircle(mWidth / 2, mHeight / 2, mRadius, Path.Direction.CCW);
            canvas.clipPath(mSecondPath, Region.Op.REPLACE);
            //确定贝塞尔曲线各个点的位置
            mSecondPath.reset();

            mSecondPath.moveTo(distance, mProgressY);
            for (int i = -mWaveCount; i <= mWaveCount; i++) {
                int x1 = distance + mWaveLength * i - mWaveLength / 2 + mStep;

                if (i % 2 != 0) {
                    mSecondPath.quadTo(x1,
                            mProgressY - mWaterRange,
                            distance + mWaveLength * i + mStep,
                            mProgressY);
                } else {
                    mSecondPath.quadTo(
                            x1,
                            mProgressY + mWaterRange,
                            distance + mWaveLength * i + mStep,
                            mProgressY);
                }
            }
            mSecondPath.lineTo(distance + mWaveCount * mWaveLength,
                    distance + mWaterHeight);

            mSecondPath.lineTo(0,
                    distance + mWaterHeight);

            mSecondPath.close();

            canvas.drawPath(mSecondPath, mSecondWaterPaint);
        }


    }


    /**
     * 绘制文字
     *
     * @param canvas
     */
    private void drawTextView(Canvas canvas) {
        mTextPaint.getTextBounds(mPercent + "%", 0, (mPercent + "%").length(), mTextRect);
        canvas.drawText(mPercent + "%", mWidth / 2 - mTextRect.width() / 2, mHeight / 2, mTextPaint);
    }

    /**
     * Author: MrGao
     * CreateTime: 2018/1/4 16:29
     * MethodName:
     * Des：水波纹动画
     * Params：
     * Return:
     **/
    private void runWave() {
        mWaveValueAnimator = ValueAnimator.ofInt(0, mWaveLength * mWaveCount);
        mWaveValueAnimator.setDuration(mSpeed);
        mWaveValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mWaveValueAnimator.setInterpolator(new LinearInterpolator());
        mWaveValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mStep = (int) animation.getAnimatedValue();
                invalidate(new Rect(mDefaultLineLen + mProgress_SPACE_WATER,
                        mDefaultLineLen + mProgress_SPACE_WATER,
                        mDefaultLineLen + mProgress_SPACE_WATER + mWaterWidth,
                        mDefaultLineLen + mProgress_SPACE_WATER + mWaterHeight
                ));
            }
        });
        mWaveValueAnimator.start();
    }


    /**
     * 设置进度条
     *
     * @param progress
     */
    public void setProgress(int progress) {
        if (progress > 0 && progress < 100) {
            this.mPercent = progress;
            mProgressValueAnimator = ValueAnimator.ofInt(mPercent, progress);
            mProgressValueAnimator.setDuration(2000);
            mProgressValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    mPercent = (int) valueAnimator.getAnimatedValue();
                    invalidate();
                }
            });
            mProgressValueAnimator.start();
        }

    }


    /**
     * 暂停动画
     */
    public void pause() {
        mWaveValueAnimator.pause();
    }

    /**
     * 重新开始动画
     */
    public void restart() {
        mWaveValueAnimator.resume();
    }

    /**
     * 设置水波纹动画的时间
     *
     * @param speed
     */
    public void setWaveTime(int speed) {
        mSpeed = speed;
        mWaveValueAnimator.setDuration(speed);
        invalidate();

    }
}
