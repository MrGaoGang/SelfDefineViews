package mrgao.com.mrgaoviews.model;

/**
 * Created by mr.gao on 2017/12/24.
 * Package:    mrgao.com.mrgaoviews.model
 * Create Date:2017/12/24
 * Project Name:SelfDefineViews
 * Description:
 */

public class Point {
    int xPoint;
    int yPoint;

    public Point(int xPoint, int yPoint) {
        this.xPoint = xPoint;
        this.yPoint = yPoint;
    }

    public int getxPoint() {
        return xPoint;
    }

    public void setxPoint(int xPoint) {
        this.xPoint = xPoint;
    }

    public int getyPoint() {
        return yPoint;
    }

    public void setyPoint(int yPoint) {
        this.yPoint = yPoint;
    }
}
