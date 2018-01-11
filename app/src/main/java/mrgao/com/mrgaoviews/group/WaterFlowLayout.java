package mrgao.com.mrgaoviews.group;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mr.gao on 2018/1/11.
 * Package:    mrgao.com.androidviewtext.group
 * Create Date:2018/1/11
 * Project Name:AndroidViewText
 * Description:一个每次都是放在最短位置的布局
 */

public class WaterFlowLayout extends ViewGroup {
    private int mColumns = 3;
    private int hSpace = 20;//纵向的间距
    private int vSpace = 20;//横向的间距
    private int childWidth = 0;//每一个item的宽度
    private int top[];//用来存储最低列,记录那一列最短，

    public WaterFlowLayout(Context context) {
        this(context, null);
    }

    public WaterFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaterFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        top = new int[mColumns];
    }

    /**
     * 其实布局的时候有一个技巧就是：
     * 因为每一个item都是放在最小的那一列，那么首先必须要获取到最小的那一列，然后
     * 距离左端的距离=列数*（item的宽度+间隔）
     * 距离上端=最小列的高度；
     *
     * 此处使用了自定义的LayoutParams，在测量的时候就将对于的数据计算出来，也就以免，在布局的时候再次测量
     * 计算，因为假设item特别多的话，那么遍历就会翻倍，所以此处是一大改进
     * @param change
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean change, int l, int t, int r, int b) {
        int childCount = getChildCount();

        clearTop();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
          /*  int minColum = getMinColunmHeight();//因为每个子的组件每次放的都是在最短的那一列
            int lf = minColum * (childWidth + vSpace);
            int lt = top[minColum];
            int lr = lf + childWidth;
            int lb = lt + childHeight;
            top[minColum] += childHeight;*/
            WaterFlowLayoutParams params = (WaterFlowLayoutParams) child.getLayoutParams();
            child.layout(params.left, params.top, params.righ, params.bottom);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();
        int childHeight;
        //首先计算每一列的宽度，也就是每一个组件的宽度
        childWidth = (sizeWidth - (mColumns - 1) * vSpace) / mColumns;

        //如果子View的个数小于列数的时候，那么父控件的宽度就是count * childWidth + (count - 1) * vSpace;
        int wrapWidth = 0;
        if (count < mColumns) {
            wrapWidth = count * childWidth + (count - 1) * vSpace;
        } else {
            wrapWidth = sizeWidth;
        }

        //清楚上一次存储的数据
        clearTop();
        //因为每一个Item存入进去的时候，都是放在最小的那一列；当放入到最小列之后记得将当前的View高度添加到对应的列上
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            //等比缩放view的高
            childHeight = view.getMeasuredHeight() * childWidth / view.getMeasuredWidth();
            int minColum = getMinColunmHeight();
            //记录下每一个view的位置
            WaterFlowLayoutParams layoutParams = (WaterFlowLayoutParams) view.getLayoutParams();
            layoutParams.left = minColum * (childWidth + vSpace);
            layoutParams.top = top[minColum];
            layoutParams.bottom = layoutParams.top + childHeight;
            layoutParams.righ = layoutParams.left + childWidth;

            top[minColum] += hSpace + childHeight;
        }

        //获取到组件最长的列
        int maxHeight = top[getMaxColumnHeight()];
        setMeasuredDimension((widthMode == MeasureSpec.AT_MOST ? wrapWidth : sizeWidth), maxHeight);

    }


    //要记得清楚，以防添加了一个View只会 数据就是脏数据了
    private void clearTop() {
        for (int i = 0; i < mColumns; i++) {
            top[i] = 0;
        }
    }

    /**
     * 获取最短的那一列
     *
     * @return
     */
    private int getMinColunmHeight() {
        int min = 0;

        for (int i = 0; i < mColumns; i++) {
            if (top[i] < top[min]) {
                min = i;
            }
        }

        return min;
    }

    /**
     * 获取最高的那一列，作为真个组件的高度
     *
     * @return
     */
    private int getMaxColumnHeight() {
        int max = 0;

        for (int i = 0; i < mColumns; i++) {
            if (top[i] > top[max]) {
                max = i;
            }
        }

        return max;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new WaterFlowLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new WaterFlowLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new WaterFlowLayoutParams(getContext(), attrs);
    }

    /**
     * 自己写的LayoutParams必须要复写这个方法
     *
     * @param p
     * @return
     */

    @Override
    protected boolean checkLayoutParams(LayoutParams p) {
        return p instanceof WaterFlowLayoutParams;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int index);
    }

    /**
     * 设置监听事件
     *
     * @param listener
     */
    public void setOnItemClickListener(final OnItemClickListener listener) {
        for (int i = 0; i < getChildCount(); i++) {
            final int index = i;
            View view = getChildAt(i);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, index);
                }
            });
        }
    }
}
