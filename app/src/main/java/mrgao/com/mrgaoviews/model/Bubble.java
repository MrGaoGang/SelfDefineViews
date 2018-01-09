package mrgao.com.mrgaoviews.model;

import mrgao.com.mrgaoviews.views.ChargeBubbleView;

/**
 * Created by mr.gao on 2018/1/9.
 * Package:    mrgao.com.mrgaoviews.model
 * Create Date:2018/1/9
 * Project Name:SelfDefineViews
 * Description:
 */

public class Bubble {
    //气泡的位置
    int x, y;
    //气泡的半径
    int radius;
    //气泡开始的类型
    ChargeBubbleView.BubbleStartType type;
    //气泡开始的时间
    long time;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public ChargeBubbleView.BubbleStartType getType() {
        return type;
    }

    public void setType(ChargeBubbleView.BubbleStartType type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
