package mrgao.com.mrgaoviews.utils;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * Created by mr.gao on 2018/1/9.
 * Package:    mrgao.com.mrgaoviews.utils
 * Create Date:2018/1/9
 * Project Name:SelfDefineViews，
 * Description:使用了三阶贝塞尔曲线
 */

public class ChargeBezierEvaluator implements TypeEvaluator<PointF> {

    private PointF mControllP1;
    private PointF mControllP2;

    public ChargeBezierEvaluator(PointF controllP1, PointF controllP2) {
        mControllP1 = controllP1;
        mControllP2 = controllP2;
    }

    @Override
    public PointF evaluate(float time, PointF startPoint, PointF endPoint) {
        PointF pointF = new PointF();
        float t = 1 - time;
        pointF.x = t * t * t * startPoint.x + 3 * t * t * time * mControllP1.x + 3 * t * time * time * mControllP2.x + time * time * time * endPoint.x;
        pointF.y = t * t * t * startPoint.y + 3 * t * t * time * mControllP1.y + 3 * t * time * time * mControllP2.y + time * time * time * endPoint.y;
        return pointF;
    }
}
