package mrgao.com.mrgaoviews.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import mrgao.com.mrgaoviews.R;
import mrgao.com.mrgaoviews.utils.DensityUtils;

/**
 * Created by mr.gao on 2018/1/3.
 * Package:    mrgao.com.mrgaoviews.views
 * Create Date:2018/1/3
 * Project Name:SelfDefineViews
 * Description:
 */

public class HuaWeiProgressView extends View {

    private int mWidth;
    private int mHeight;

    private int mProgressColor;//进度条的颜色
    private int mBaseColor;//背景颜色
    private int mTextColor;//文字的颜色
    private int mDotColor;//点的颜色
    private int mTextSize;

    private Paint mPaint;
    private Paint mTextPaint;
    private Paint mDotPain;

    private Rect mRect;//用来测量文字宽高的

    private int mDefaultStrokeWidth = 3;//刻度的宽度
    private int mPercent = 0;//进度
    private float mDotProgress = 0;//原点的进度
    private int mLineLength = 40;//刻度的长度

    private ValueAnimator mValueAnimator;//原点的动画
    private ValueAnimator mProgressAnimator;//进度条的动画

    private boolean isStop = false;

    public HuaWeiProgressView(Context context) {
        this(context, null);
    }

    public HuaWeiProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HuaWeiProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HuaWeiProgressView, defStyleAttr, 0);
        mProgressColor = typedArray.getColor(R.styleable.HuaWeiProgressView_progressColor, Color.BLUE);
        mBaseColor = typedArray.getColor(R.styleable.HuaWeiProgressView_baseColor, Color.GRAY);
        mTextColor = typedArray.getColor(R.styleable.HuaWeiProgressView_textColor, Color.RED);
        mDotColor = typedArray.getColor(R.styleable.HuaWeiProgressView_dotColor, Color.GREEN);
        mTextSize = (int) typedArray.getDimension(R.styleable.HuaWeiProgressView_textSize, DensityUtils.dp2px(context, 16));
        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setColor(mBaseColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mDefaultStrokeWidth);
        mPaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);

        mRect = new Rect();
        mTextPaint.getTextBounds(mPercent + "%", 0, (mPercent + "%").length(), mRect);


        mDotPain = new Paint();
        mDotPain.setColor(mDotColor);
        mDotPain.setStyle(Paint.Style.FILL);
        mDotPain.setAntiAlias(true);

        mValueAnimator = ValueAnimator.ofFloat(0, 100);
        mProgressAnimator = ValueAnimator.ofInt(0, mPercent);//这个是进度条的动画
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
            mHeight = mWidth;//保证宽高一致
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawProgress(canvas);
        drawText(canvas);
        drawDot(canvas);
    }


    /**
     * Author: MrGao
     * CreateTime: 2018/1/3 19:12
     * MethodName:
     * Des：绘制进度条
     * Params：
     * Return:
     **/
    private void drawProgress(Canvas canvas) {
        canvas.save();//因为有旋转

        int top = getPaddingTop();
        for (int i = 0; i < 100; i++) {
            if (mPercent < i) {
                mPaint.setColor(mBaseColor);
            } else {
                mPaint.setColor(mProgressColor);
            }
            canvas.drawLine(mWidth / 2, top, mWidth / 2, mLineLength + top, mPaint);
            canvas.rotate(3.6f, mWidth / 2, mHeight / 2);//设置刻度有100个
        }

        canvas.restore();
    }

    /**
     * Author: MrGao
     * CreateTime: 2018/1/3 19:12
     * MethodName:
     * Des：绘制文字
     * Params：
     * Return:
     **/
    private void drawText(Canvas canvas) {

        mTextPaint.getTextBounds(mPercent + "%", 0, (mPercent + "%").length(), mRect);
        int width = mWidth / 2 - (mRect.right - mRect.left) / 2;
        int height = mHeight / 2;
        canvas.drawText(mPercent + "%", width, height, mTextPaint);

    }

    /**
     * Author: MrGao
     * CreateTime: 2018/1/3 19:13
     * MethodName:
     * Des：绘制原点
     * Params：
     * Return:
     **/
    private void drawDot(Canvas canvas) {

        if (!isStop) {
            canvas.save();
            canvas.rotate(3.6f * mDotProgress, mWidth / 2, mHeight / 2);
            canvas.drawCircle(mWidth / 2, mLineLength + 20, 10, mDotPain);
            canvas.restore();
        }

    }

    /**
     * Author: MrGao
     * CreateTime: 2018/1/3 19:38
     * MethodName:
     * Des：开始动画
     * Params：
     * Return:
     **/

    private void startAnimation() {

        mValueAnimator.setDuration(1500);
        mValueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());//设置加速减速
        mValueAnimator.setRepeatCount(2);
        mValueAnimator.setRepeatMode(ValueAnimator.RESTART);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                mDotProgress = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }


        });
        mValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isStop = true;
                invalidate();
            }
        });

        mValueAnimator.start();

    }


    /**
     * Author: MrGao
     * CreateTime: 2018/1/3 19:43
     * MethodName:
     * Des：设置进度
     * Params：
     * Return:
     **/
    public void setProgress(int percent) {

        mProgressAnimator = ValueAnimator.ofInt(0, percent);
        mProgressAnimator.setDuration(2000);
        mProgressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mPercent = (int) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });

        mProgressAnimator.start();
        startAnimation();

    }
}
