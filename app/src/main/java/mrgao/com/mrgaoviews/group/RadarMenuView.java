package mrgao.com.mrgaoviews.group;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import mrgao.com.mrgaoviews.R;
import mrgao.com.mrgaoviews.utils.DensityUtils;

/**
 * Created by mr.gao on 2017/12/23.
 * Package:    mrgao.com.mrgaoviews.views
 * Create Date:2017/12/23
 * Project Name:
 * Description:雷达菜单
 */

public class RadarMenuView extends ViewGroup implements View.OnClickListener {

    //菜单放置的位置
    private final int LEFT_TOP = 0;
    private final int LEFT_BOTTOM = 1;
    private final int RIGHT_TOP = 2;
    private final int RIGHT_BOTTOM = 3;

    //菜单半径
    private int mMenuRadius;
    //菜单的状态，初始状态为关闭，且默认的位置在右下角
    private Status mCurrentStatus = Status.CLOSE;
    private Position mPosition = Position.RIGHT_BOTTOM;

    //菜单项点击事件监听器
    private onMenuItemClickListener mMenuItemClickListener;
    //中心的按钮
    private View mCenterButton;
    //默认的动画持续时间
    private int mAnimationDuration = 500;//默认为500ms

    //位置枚举
    private enum Position {
        LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM
    }

    //状态枚举
    private enum Status {
        CLOSE, OPEN
    }


    public RadarMenuView(Context context) {
        this(context, null);
    }

    public RadarMenuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /**
         * 加载自定义属性的值
         */
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RadarMenuView,
                defStyleAttr, 0);

        mMenuRadius = (int) typedArray.getDimension(R.styleable.RadarMenuView_radius,
                DensityUtils.dp2px(context, 100));
        int postion = typedArray.getInt(R.styleable.RadarMenuView_position, RIGHT_BOTTOM);
        switch (postion) {
            case LEFT_TOP:
                mPosition = Position.LEFT_TOP;
                break;
            case LEFT_BOTTOM:
                mPosition = Position.LEFT_BOTTOM;
                break;
            case RIGHT_TOP:
                mPosition = Position.RIGHT_TOP;
                break;
            case RIGHT_BOTTOM:
                mPosition = Position.RIGHT_BOTTOM;
                break;

        }
        typedArray.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        if (changed) {
            mCenterButton = getChildAt(0);
            onLayoutCenterButton();
            int count = getChildCount();
            for (int i = 0; i < count - 1; i++) {
                final View childView = getChildAt(i + 1);//不在获取主按钮了

                childView.setVisibility(GONE);//默认每个菜单不可见
                childView.setFocusable(false);
                childView.setClickable(false);

                //此处画一个图，两个按钮之间的夹角为90°/(count-2),除开了一个主控件按钮
                double degree = (Math.PI / 2) / (count - 2) * i;
                int left = (int) (mMenuRadius * Math.sin(degree));
                int top = (int) (mMenuRadius * Math.cos(degree));

                int width = childView.getMeasuredWidth();
                int height = childView.getMeasuredHeight();

                //如果菜单在左下或者右下，top就应该变化
                if (mPosition == Position.LEFT_BOTTOM || mPosition == Position.RIGHT_BOTTOM) {
                    top = getMeasuredHeight() - height - top;
                }
                //右上，右下
                if (mPosition == Position.RIGHT_TOP || mPosition == Position.RIGHT_BOTTOM) {
                    left = getMeasuredWidth() - width - left;
                }
                childView.layout(left, top, left + width, top + height);

                final int position = i + 1;
                childView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mMenuItemClickListener != null) {
                            mMenuItemClickListener.onClick(childView, position);
                            menuItemClickAnimation(position);
                            changeMenuStatus();

                        }
                    }
                });
            }
        }

    }


    /**
     * Author: MrGao
     * CreateTime: 2017/12/23 20:36
     * MethodName:
     * Des：布局菜单点击按钮
     * Params：
     * Return:
     **/
    private void onLayoutCenterButton() {
        mCenterButton.setOnClickListener(this);
        int width = mCenterButton.getMeasuredWidth();
        int height = mCenterButton.getMeasuredHeight();

        int left = 0;
        int top = 0;
        if (mPosition == Position.LEFT_TOP) {
            left = 0;
            top = 0;
        } else if (mPosition == Position.LEFT_BOTTOM) {
            left = 0;
            top = getMeasuredHeight() - height;
        } else if (mPosition == Position.RIGHT_TOP) {
            left = getMeasuredWidth() - width;
            top = 0;
        } else if (mPosition == Position.RIGHT_BOTTOM) {
            left = getMeasuredWidth() - width;
            top = getMeasuredHeight() - height;
        }
        mCenterButton.layout(left, top, left + width, top + height);
    }

    /**
     * Author: MrGao
     * CreateTime: 2017/12/23 21:30
     * MethodName:
     * Des：每个菜单的测量大校
     * Params：
     * Return:
     **/
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    /**
     * Author: MrGao
     * CreateTime: 2017/12/23 21:30
     * MethodName:
     * Des：主按钮的点击事件
     * Params：
     * Return:
     **/
    @Override
    public void onClick(View view) {
        rotateCenterButton(mAnimationDuration);
        menuItemAnimation(mAnimationDuration);

    }


    /**
     * Author: MrGao
     * CreateTime: 2017/12/23 21:28
     * MethodName:
     * Des：旋转主按钮
     * Params：
     * Return:
     **/
    private void rotateCenterButton(int duration) {
        //参数：旋转360度，按照自身中心旋转
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(duration);
        rotateAnimation.setFillAfter(true);
        mCenterButton.startAnimation(rotateAnimation);
    }

    /**
     * Author: MrGao
     * CreateTime: 2017/12/23 21:31
     * MethodName:
     * Des：打开菜单项的动画
     * Params：
     * Return:
     **/
    public void menuItemAnimation(int duration) {
        for (int i = 0; i < getChildCount() - 1; i++) {
            final View childView = getChildAt(i + 1);

            childView.setVisibility(VISIBLE);
            childView.setFocusable(true);
            childView.setClickable(true);

            //此处画一个图，两个按钮之间的夹角为90°/(count-2),除开了一个主控件按钮
            double degree = (Math.PI / 2) / (getChildCount() - 2) * i;
            int left = (int) (mMenuRadius * Math.sin(degree));
            int top = (int) (mMenuRadius * Math.cos(degree));
            int xflag = 1;
            int yflag = 1;

            //当放的位置是在左上角和左下角的时候，当在关闭menuItem的时候x是在减小，也就是为负值了
            if (mPosition == Position.LEFT_BOTTOM || mPosition == Position.LEFT_TOP) {
                xflag = -1;
            }
            //左上角和右上角在关闭的时候，y值也变为了负数
            if (mPosition == Position.LEFT_TOP || mPosition == Position.RIGHT_TOP) {
                yflag = -1;
            }

            AnimationSet animationSet = new AnimationSet(true);
            TranslateAnimation translateAnimation = null;

            if (mCurrentStatus == Status.CLOSE) {
                translateAnimation = new TranslateAnimation(xflag * left,
                        0, yflag * top, 0);

            } else {

                translateAnimation = new TranslateAnimation(0, xflag * left,
                        0, yflag * top);

            }

            translateAnimation.setFillAfter(true);
            translateAnimation.setDuration(duration);
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (mCurrentStatus == Status.CLOSE) {
                        childView.setVisibility(GONE);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            //旋转两圈
            RotateAnimation rotateAnimation = new RotateAnimation(0, 720f,
                    Animation.RELATIVE_TO_SELF, 0.5f,//参数分别是起始地角度，结束的角度，
                    // 自身的x的中心(0.5f)和自身的y的中心(0.5f)旋转
                    Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnimation.setFillAfter(true);
            rotateAnimation.setDuration(duration);

            //此处先添加旋转动画再添加平移动画
            animationSet.addAnimation(rotateAnimation);
            animationSet.addAnimation(translateAnimation);

            childView.startAnimation(animationSet);

        }

        changeMenuStatus();
    }


    /**
     * Author: MrGao
     * CreateTime: 2017/12/23 22:03
     * MethodName:
     * Des：点击某一个菜单项的动画，动画是：
     * 被点击的放大，其余的缩小直到消失
     * Params：
     * Return:
     **/
    private void menuItemClickAnimation(int position) {
        for (int i = 0; i < getChildCount() - 1; i++) {
            View childView = getChildAt(i + 1);
            if (position == i + 1) {
                childView.startAnimation(scaleToBigAnimation(mAnimationDuration));
            } else {
                childView.startAnimation(scaleToSmallAnimation(mAnimationDuration));
            }
        }

    }

    /**
     * Author: MrGao
     * CreateTime: 2017/12/23 22:12
     * MethodName:
     * Des：放大的动画
     * Params：
     * Return:
     **/
    private Animation scaleToBigAnimation(int animationDuration) {
        AnimationSet animationSet = new AnimationSet(true);

        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 4f, 1f, 4f,
                Animation.RELATIVE_TO_SELF, 0.5f,//x轴和y轴分别扩大4倍
                Animation.RELATIVE_TO_SELF, 0.5f);


        AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);

        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);

        animationSet.setFillAfter(true);
        animationSet.setDuration(animationDuration);
        return animationSet;
    }

    /**
     * Author: MrGao
     * CreateTime: 2017/12/23 22:13
     * MethodName:
     * Des：缩小的动画
     * Params：
     * Return:
     **/
    private Animation scaleToSmallAnimation(int animationDuration) {
        AnimationSet animationSet = new AnimationSet(true);

        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 0f, 1f, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f,//x轴和y轴分别扩大4倍
                Animation.RELATIVE_TO_SELF, 0.5f);


        AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);

        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);

        animationSet.setFillAfter(true);
        animationSet.setDuration(animationDuration);
        return animationSet;
    }


    /**
     * Author: MrGao
     * CreateTime: 2017/12/23 21:52
     * MethodName:
     * Des：改变菜单的状态
     * Params：
     * Return:
     **/
    private void changeMenuStatus() {
        mCurrentStatus = (mCurrentStatus == Status.CLOSE ? Status.OPEN : Status.CLOSE);
    }

    /**
     * 设置监听器
     *
     * @param menuItemClickListener
     */
    public void setOnMenuItemClickListener(onMenuItemClickListener menuItemClickListener) {
        mMenuItemClickListener = menuItemClickListener;
    }

    public void setAnimationDuration(int animationDuration) {
        mAnimationDuration = animationDuration;
    }

    /**
     * 点击菜单的回调接口
     */
    public interface onMenuItemClickListener {
        void onClick(View itemView, int position);
    }
}
