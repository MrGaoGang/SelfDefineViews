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
 * Description:自定义的线性布局
 */

public class MyLinLayout extends ViewGroup {
    public MyLinLayout(Context context) {
        super(context);
    }

    public MyLinLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLinLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean change, int l, int t, int r, int b) {
        int top = 0, left = 0;
        int childWidth, childHeight;

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
            childHeight = child.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
            childWidth = child.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
            child.layout(left, top, childWidth, top + childHeight);
            top += childHeight;

        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        int measureWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int measureHeightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = 0, height = 0;
        int count = getChildCount();
        int childWidth, childHeight;
        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);

            MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();
            childHeight = childView.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
            childWidth = childView.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
            height += childHeight;
            width = Math.max(width, childWidth);
        }
        setMeasuredDimension((measureWidthMode == MeasureSpec.EXACTLY ? measureWidth : width), (measureHeightMode == MeasureSpec.EXACTLY ? measureHeight : height));

    }

    /**
     * 记住要重写以下的三个方法，才可以获取到每个子view的margin的值，
     * 才可以得到MarginLayoutParams
     *
     * @param
     * @return
     */

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
