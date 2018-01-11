package mrgao.com.mrgaoviews.group;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by mr.gao on 2018/1/11.
 * Package:    mrgao.com.androidviewtext.group
 * Create Date:2018/1/11
 * Project Name:AndroidViewText
 * Description:用于记录布局参数的值
 */

public class WaterFlowLayoutParams extends ViewGroup.LayoutParams {

    public int left = 0;
    public int righ = 0;
    public int top = 0;
    public int bottom = 0;

    public WaterFlowLayoutParams(Context c, AttributeSet attrs) {
        super(c, attrs);
    }

    public WaterFlowLayoutParams(int width, int height) {
        super(width, height);
    }

    public WaterFlowLayoutParams(ViewGroup.LayoutParams source) {
        super(source);
    }


}
