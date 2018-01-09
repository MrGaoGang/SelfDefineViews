package mrgao.com.mrgaoviews.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import mrgao.com.mrgaoviews.model.Circle;

/**
 * Created by mr.gao on 2018/1/9.
 * Package:    mrgao.com.mrgaoviews.views
 * Create Date:2018/1/9
 * Project Name:SelfDefineViews
 * Description:
 */

public class RotateProgressView extends View {

    private int mPointCount = 20;
    private Paint mPaint;
    private int mHeight;
    private int mWidth;
    private int mPointColor = Color.RED;
    private float[] rotates;


    private Circle[] mCircles;

    public RotateProgressView(Context context) {
        this(context, null);
    }

    public RotateProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RotateProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(mPointColor);
        mPaint.setAntiAlias(true);
        rotates = new float[mPointCount];
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
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initCircle();
        radiusAnimation();
    }

    /**
     * 初始化圆的个数和数据
     */
    private void initCircle() {
        mCircles = new Circle[mPointCount];

        int circleRadius = Math.min(mHeight, mWidth) / 20;
        for (int i = 0; i < mPointCount; i++) {
            mCircles[i] = new Circle();
            mCircles[i].setCenter(mWidth / 2, circleRadius);
            mCircles[i].setColor(mPointColor);
            mCircles[i].setRadius(circleRadius - circleRadius * i / 20);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawPoint(canvas);
    }

    /**
     * 绘制点
     * @param canvas
     */
    private void drawPoint(Canvas canvas) {


        for (int i = 0; i < mPointCount; i++) {
            canvas.save();
            canvas.rotate(rotates[i], mWidth / 2, mHeight / 2);
            canvas.drawCircle(mCircles[i].getCenter().x, mCircles[i].getCenter().y, mCircles[i].getRadius(), mPaint);
            canvas.restore();
        }


    }

    /**
     * 旋转的动画
     */
    private void radiusAnimation() {

        for (int i = 0; i < mPointCount; i++) {
            final int index = i;
            ValueAnimator mRadiusValueAnimator = ValueAnimator.ofFloat(0, 360);
            mRadiusValueAnimator.setDuration(1700);
            mRadiusValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mRadiusValueAnimator.setStartDelay(i * 100);
            mRadiusValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    rotates[index] = (float) valueAnimator.getAnimatedValue();
                    invalidate();
                }
            });
            mRadiusValueAnimator.start();
        }

    }
}
