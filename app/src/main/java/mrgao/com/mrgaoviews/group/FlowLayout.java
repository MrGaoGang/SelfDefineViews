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
 * Description:流式布局
 */

public class FlowLayout extends ViewGroup {
    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean change, int l, int t, int r, int b) {
        int count = getChildCount();

        int lineWidth = 0, lineHeight = 0;
        int top = 0, left = 0;
        int childW, childH;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);

            MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
            childW = child.getMeasuredWidth() + layoutParams.rightMargin + layoutParams.leftMargin;
            childH = child.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;

            if (lineWidth + childW > getMeasuredWidth()) {
                //表示要换行
                top += lineHeight;
                left = 0;

                lineHeight = childH;
                lineWidth = childW;

            } else {
                lineHeight = Math.max(childH, lineHeight);
                lineWidth += childW;

            }
            int lc = left + layoutParams.leftMargin;
            int tc = top + layoutParams.topMargin;
            int rc = lc + child.getMeasuredWidth() + layoutParams.rightMargin;
            int bc = tc + child.getMeasuredHeight() + layoutParams.bottomMargin;

            child.layout(lc, tc, rc, bc);

            left = left + childW;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        int measureWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int measureHeightMode = MeasureSpec.getMode(heightMeasureSpec);

        int count = getChildCount();
        //表示当前行的宽度和高度
        int lineWidth = 0, lineHeight = 0;
        //整个组件的宽度和高度
        int width = 0, height = 0;
        int childW, childH;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
            childW = child.getMeasuredWidth() + layoutParams.rightMargin + layoutParams.leftMargin;
            childH = child.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;

            if (lineWidth + childW > measureWidth) {
                //表示要换行
                //首先将当前行宽lineWidth与目前的最大行宽width比较计算出最新的最大行宽width，作为当前FlowLayout所占的宽度
                width = Math.max(lineWidth, width);//宽度是最宽的那个
                height += lineHeight;//为什么这里不能加childH,那是因为加的一定是那一行最高的，其实可以发现似乎少加了一次

                lineHeight = childH;
                lineWidth = childW;

            } else {
                lineWidth += childW;
                lineHeight = Math.max(lineHeight, childH);
            }

            //最后一行是不会超出width范围的，所以要单独处理
            if (i == count - 1) {
                height += lineHeight;//少加的一次加回来了
                width = Math.max(width, lineWidth);//组件的宽，就是最宽的那个
            }

        }

        setMeasuredDimension((measureWidthMode == MeasureSpec.EXACTLY ? measureWidth : width), (measureHeightMode == MeasureSpec.EXACTLY ? measureHeight : height));
    }

    /**
     * 记住要重写以下的三个方法，才可以获取到每个子view的margin的值，
     * 才可以得到MarginLayoutParams
     *
     * @param attrs
     * @return
     */

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected boolean checkLayoutParams(LayoutParams p) {
        return p instanceof MarginLayoutParams;
    }
}
