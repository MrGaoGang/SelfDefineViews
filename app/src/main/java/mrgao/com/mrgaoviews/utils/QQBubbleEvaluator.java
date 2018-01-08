package mrgao.com.mrgaoviews.utils;

import android.animation.TypeEvaluator;
import android.graphics.Point;

/**
 * Created by mr.gao on 2018/1/8.
 * Package:    mrgao.com.mrgaoviews.utils
 * Create Date:2018/1/8
 * Project Name:SelfDefineViews
 * Description:
 */

public class QQBubbleEvaluator implements TypeEvaluator<Point> {
    @Override
    public Point evaluate(float v, Point startPoint, Point endPoint) {
        Point p = new Point();
        p.x = (int) (startPoint.x + v * (endPoint.x - startPoint.x));
        p.y = (int) (startPoint.y + v * (endPoint.y - startPoint.y));

        return p;
    }
}
