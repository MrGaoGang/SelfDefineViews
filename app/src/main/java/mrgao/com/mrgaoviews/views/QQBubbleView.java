package mrgao.com.mrgaoviews.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;

import mrgao.com.mrgaoviews.R;
import mrgao.com.mrgaoviews.utils.DensityUtils;
import mrgao.com.mrgaoviews.utils.QQBubbleEvaluator;

/**
 * Created by mr.gao on 2018/1/7.
 * Package:    mrgao.com.mrgaoviews.views
 * Create Date:2018/1/7
 * Project Name:SelfDefineViews
 * Description:
 */

public class QQBubbleView extends View {
    //气泡的状态，分别表示：默认，拖动，移动，消失
    private final int STATE_DEFAULT = 0;
    private final int STATE_DRAG = 1;
    private final int STATE_MOVE = 2;
    private final int STATE_DISMISS = 3;


    private int mTextColor;
    private int mBubbleColor;


    //拖动气泡的坐标
    private float mBubbleCenterX;
    private float mBubbleCenterY;

    //默认显示圆的中心点坐标
    private float mCenterX;
    private float mCenterY;

    //控制点的坐标
    private float mControllX;
    private float mControllY;
    //表示中心点与曲线相切的那两个点的坐标
    private float mCenterStartX;
    private float mCenterStartY;
    private float mCenterEndX;
    private float mCenterEndY;
    //表示拖动的那个气派与曲线相切的那两个点的坐标
    private float mBubbleStartX;
    private float mBubbleStartY;
    private float mBubbleEndX;
    private float mBubbleEndY;
    //表示气泡的半径和起初那个点的半径
    private float mBubbleRadius;
    private float mCenterRadius;

    private int mMaxLen = 200;//表示最大拖动100像素就消失
    private int mState = STATE_DEFAULT;
    //拖动的距离
    private double mDistance;
    //分别是贝塞尔曲线的路径，以及对应的画笔
    private Path mPath;
    private Paint mPaint;
    //文字的画笔
    private Paint mTextPaint;
    private Rect mTextRect;
    //文字的数量
    private String mCount = "12";


    private BubbleStateListener mBubbleStateListener;

    public QQBubbleView(Context context) {
        this(context, null);
    }

    public QQBubbleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QQBubbleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.QQBubbleView, defStyleAttr, 0);
        mBubbleColor = typedArray.getColor(R.styleable.QQBubbleView_bubbleColor, Color.RED);
        mTextColor = typedArray.getColor(R.styleable.QQBubbleView_bubbleTextColor, Color.WHITE);
        mMaxLen = (int) typedArray.getDimension(R.styleable.QQBubbleView_maxDistance, DensityUtils.dp2px(context, 100));
        mCenterRadius = (int) typedArray.getDimension(R.styleable.QQBubbleView_bubbleRadius, DensityUtils.dp2px(context, 10));
        mBubbleRadius = (int) mCenterRadius;
        typedArray.recycle();
        init();
    }

    //初始化一些画笔
    private void init() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setColor(mBubbleColor);
        mPaint.setAntiAlias(true);


        mTextPaint = new Paint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStrokeWidth(3);
        mTextPaint.setFakeBoldText(true);
        mTextRect = new Rect();
        mTextPaint.getTextBounds(mCount, 0, mCount.length(), mTextRect);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measuredDimension(widthMeasureSpec), measuredDimension(heightMeasureSpec));
    }

    /**
     * Author: MrGao
     * CreateTime: 2018/1/8 14:15
     * MethodName:
     * Des：当没有设置组件大小的时候，就设置组件的大小为半径的2倍
     * Params：
     * Return:
     **/
    private int measuredDimension(int measureSpec) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = (int) (2 * mBubbleRadius);
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initPosition(w, h);
    }

    /**
     * 初始化坐标
     *
     * @param w
     * @param h
     */
    private void initPosition(int w, int h) {
        mCenterX = w / 2;
        mCenterY = h / 2;

        mBubbleCenterX = mCenterX;
        mBubbleCenterY = mCenterY;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mState != STATE_DISMISS) {
            //画出拖拽的气泡
            canvas.drawCircle(mBubbleCenterX, mBubbleCenterY, mBubbleRadius, mPaint);
        }

        //当处于拖动状态的时候，就画出贝塞尔曲线
        if (mState == STATE_DRAG && mDistance < mMaxLen - mMaxLen / 6) {
            canvas.drawCircle(mCenterX, mCenterY, mCenterRadius, mPaint);
            caculateBeiSarPath();
            mPath.reset();
            mPath.moveTo(mCenterStartX, mCenterStartY);
            mPath.quadTo(mControllX, mControllY, mBubbleStartX, mBubbleStartY);

            mPath.lineTo(mBubbleEndX, mBubbleEndY);
            mPath.quadTo(mControllX, mControllY, mCenterEndX, mCenterEndY);
            mPath.close();

            canvas.drawPath(mPath, mPaint);

        }

        //绘制文本
        if (mState != STATE_DISMISS && !TextUtils.isEmpty(mCount)) {
            mTextPaint.getTextBounds(mCount, 0, mCount.length(), mTextRect);
            canvas.drawText(mCount, mBubbleCenterX - mTextRect.width() / 2,
                    mBubbleCenterY + mTextRect.height() / 2, mTextPaint);
        }


    }

    /**
     * Author: MrGao
     * CreateTime: 2018/1/7 17:11
     * MethodName:
     * Des：计算贝塞尔曲线以及各个点的坐标
     * Params：
     * Return:
     **/
    public void caculateBeiSarPath() {
        //控制点中心的坐标，是两个点中心的坐标值
        mControllX = (mBubbleCenterX + mCenterX) / 2;
        mControllY = (mBubbleCenterY + mCenterY) / 2;

        mDistance = Math.hypot(mBubbleCenterX - mCenterX, mBubbleCenterY - mCenterY);
        float sin = (float) ((mBubbleCenterY - mCenterY) / mDistance);
        float cos = (float) ((mBubbleCenterX - mCenterX) / mDistance);

        mCenterStartX = mCenterX - mCenterRadius * sin;
        mCenterStartY = mCenterY - mCenterRadius * cos;

        mCenterEndX = mCenterX + mCenterRadius * sin;
        mCenterEndY = mCenterY + mCenterRadius * cos;

        mBubbleStartX = mBubbleCenterX - mBubbleRadius * sin;
        mBubbleStartY = mBubbleCenterY - mBubbleRadius * cos;

        mBubbleEndX = mBubbleCenterX + mBubbleRadius * sin;
        mBubbleEndY = mBubbleCenterY + mBubbleRadius * cos;


    }

    /**
     * 点击的时候：首先明白在不可见的时候是不可以点击的
     * 否则计算出当前手指的位置和起始点的坐标值，当拖动的距离在一个比较小的范围内，说明是在拖动的
     * <p>
     * 拖动的时候：当不是在默认的情况下，计算出当前拖动的点和起始点的距离从而得到拖动的距离，当拖动的距离小于最大的距离的时候：
     * 也是出于拖拽状态，但是此时起始点的半径应该逐渐变小；
     * 相反出于拖动状态
     * <p>
     * 手指抬起的时候：
     * 当出于拖拽状态的时候，就弹回原来的位置
     * 当处于移动状态，但是移动的位置最后却在起始点的位置附近，也弹回原来的位置
     * 否则：让组件消失
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mState != STATE_DISMISS) {
                    getParent().requestDisallowInterceptTouchEvent(true);

                    mDistance = Math.hypot(event.getX() - mBubbleCenterX, event.getY() - mBubbleCenterX);
                    if (mDistance < mBubbleRadius + mMaxLen / 4) {//当点击了气泡周围部分就说明是在拖拽
                        mState = STATE_DRAG;
                        if (mBubbleStateListener != null) {
                            mBubbleStateListener.onDrag();
                        }
                    } else {
                        mState = STATE_DEFAULT;
                        if (mBubbleStateListener != null) {
                            mBubbleStateListener.onDefault();
                        }
                    }
                }

                break;

            case MotionEvent.ACTION_MOVE:
                if (mState != STATE_DEFAULT) {
                    getParent().requestDisallowInterceptTouchEvent(true);

                    mBubbleCenterX = event.getX();
                    mBubbleCenterY = event.getY();
                    mDistance = Math.hypot(mBubbleCenterX - mCenterX, mBubbleCenterY - mCenterY);

                    if (mState == STATE_DRAG) {
                        if (mDistance < mMaxLen - mMaxLen / 5) {//mMaxLen/5是误差
                            //随着拖的距离越远，那么起初的那个气泡的半径就越小
                            mCenterRadius = (float) (mBubbleRadius - mDistance / 10);

                        } else {
                            mState = STATE_MOVE;
                            if (mBubbleStateListener != null) {
                                mBubbleStateListener.onMove();
                            }
                        }
                    }
                    invalidate();
                }
                break;

            case MotionEvent.ACTION_UP:
                getParent().requestDisallowInterceptTouchEvent(false);

                if (mState == STATE_DRAG) {//当拖动距离较短的时候，为拖动，此时气泡恢复原来位置，
                    restoreAnimation();
                } else if (mState == STATE_MOVE) {
                    if (mDistance < mBubbleRadius * 2) {//表示移动到起初的位置周围的话，就是回去
                        restoreAnimation();
                    } else {
                        dismissAnimation();
                    }
                }

                break;
        }
        return true;
    }

    /**
     * Author: MrGao
     * CreateTime: 2018/1/7 16:55
     * MethodName:
     * Des：//当拖动距离较短的时候，为拖动，此时气泡恢复原来位置，使用插值器
     * Params：
     * Return:
     **/
    private void restoreAnimation() {
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new QQBubbleEvaluator(),
                new Point((int) mBubbleCenterX, (int) mBubbleCenterY),
                new Point((int) mCenterX, (int) mCenterY));
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new BounceInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Point point = (Point) valueAnimator.getAnimatedValue();
                mBubbleCenterX = point.x;
                mBubbleCenterY = point.y;
                invalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mState = STATE_DEFAULT;
                if (mBubbleStateListener != null) {
                    mBubbleStateListener.onDefault();
                }

            }
        });
        valueAnimator.start();
    }

    /**
     * Author: MrGao
     * CreateTime: 2018/1/7 16:58
     * MethodName:
     * Des：消失动画
     * Params：
     * Return:
     **/
    private void dismissAnimation() {
        mState = STATE_DISMISS;
        if (mBubbleStateListener != null) {
            mBubbleStateListener.onDismiss();
        }

        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 2f, 1f, 2f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0f);
        animationSet.setFillAfter(false);
        animationSet.setDuration(1000);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        startAnimation(animationSet);


    }


    /**
     * Author: MrGao
     * CreateTime: 2018/1/8 14:13
     * MethodName: 设置气泡的内容
     * Des：
     * Params：
     * Return:
     **/
    public void setText(String count) {
        if (!TextUtils.isEmpty(count)) {
            mCount = count;
            invalidate();
        }

    }

    /**
     * 重置气泡
     */
    public void resetView() {
        mState = STATE_DEFAULT;
        this.clearAnimation();
        initPosition(getWidth(), getHeight());
        invalidate();
    }


    /**
     * 设置监听器
     *
     * @param bubbleStateListener
     */
    public void setBubbleStateListener(BubbleStateListener bubbleStateListener) {
        mBubbleStateListener = bubbleStateListener;
    }

    /**
     * Author: MrGao
     * CreateTime: 2018/1/8 14:05
     * MethodName:
     * Des：气泡状态接口回调
     * Params：
     * Return:
     **/
    public interface BubbleStateListener {
        void onDefault();//默认状态

        void onMove();//移动状态

        void onDrag();//拖动状态

        void onDismiss();//消失状态
    }

}
