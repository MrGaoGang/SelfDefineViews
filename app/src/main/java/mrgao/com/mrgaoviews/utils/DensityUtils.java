package mrgao.com.mrgaoviews.utils;

import android.content.Context;

/**
 * Created by mr.gao on 2017/12/23.
 * Package:    mrgao.com.mrgaoviews.utils
 * Create Date:2017/12/23
 * Project Name:SelfDefineViews
 * Description:
 */

public class DensityUtils {

    public static int dp2px(Context context, int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (scale * dp + 0.5f);
    }

    public static int px2dp(Context context, int px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);

    }
}
