package mrgao.com.mrgaoviews.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import mrgao.com.mrgaoviews.R;
import mrgao.com.mrgaoviews.model.Point;
import mrgao.com.mrgaoviews.utils.DensityUtils;

/**
 * Created by mr.gao on 2017/12/24.
 * Package:    mrgao.com.mrgaoviews.views
 * Create Date:2017/12/24
 * Project Name:SelfDefineViews
 * Description:一个简单的折线图
 */


public class SimpleChartView extends View {

    //整个View的宽度和高度
    private int mWidth;
    private int mHeight;

    //存储x轴和y轴的数据
    private List<String> mXDataString;
    private List<String> mYDataString;

    //分别是线段，点的画笔，x轴的画笔，y轴的画笔
    private Paint mLinePaint;
    private Paint mPointPaint;
    private Paint mXPaint;
    private Paint mYPaint;

    //线段的颜色和点的颜色
    private int mLineColor;
    private int mPointColor;

    //x和y轴的偏移量
    private int mXOffest = 40;//X轴位移量
    private int mYOffest = 50;//y轴的位移量

    //用来测量x和y轴的文字大小
    private Rect mYTextRect;
    private Rect mXTextRect;

    //存储x轴的每一个Item的x值
    private int[] xPoint;
    private int[] yPoint;

    private int mYInternalOffest;//Y中两个值之间的间距
    private int mXInternalOffest;//x中两个值之间的间距
    //需要绘制的点
    private List<mrgao.com.mrgaoviews.model.Point> mPointList = null;

    public SimpleChartView(Context context) {
        this(context, null);
    }

    public SimpleChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SimpleChartView,
                defStyleAttr, 0);
        mLineColor = typedArray.getColor(R.styleable.SimpleChartView_lineColor, Color.BLACK);
        mPointColor = typedArray.getColor(R.styleable.SimpleChartView_pointColor, Color.RED);

        typedArray.recycle();
        init();
    }

    private void init() {
        mLinePaint = new Paint();
        mLinePaint.setColor(mLineColor);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(10);

        mPointPaint = new Paint();
        mPointPaint.setAntiAlias(true);
        mPointPaint.setColor(mPointColor);

        mXDataString = new ArrayList<>();
        mYDataString = new ArrayList<>();
        mPointList = new ArrayList<>();

        mYTextRect = new Rect();
        mXTextRect = new Rect();

        mXPaint = new Paint();
        mXPaint.setColor(Color.BLUE);
        mXPaint.setAntiAlias(true);
        mXPaint.setTextSize(DensityUtils.dp2px(getContext(), 18));

        mYPaint = new Paint();
        mYPaint.setColor(Color.BLUE);
        mYPaint.setAntiAlias(true);
        mYPaint.setTextSize(DensityUtils.dp2px(getContext(), 18));
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        } else {
            mWidth = getMeasuredWidth();
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            mHeight = getMeasuredHeight();
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawY(canvas);
        drawX(canvas);
        drawPointLine(canvas);
    }


    /**
     * Author: MrGao
     * CreateTime: 2017/12/24 13:31
     * MethodName: 绘制X轴
     * Des：
     * Params：
     * Return:
     **/
    private void drawX(Canvas canvas) {
        xPoint = new int[mXDataString.size()];
        if (mXDataString.size() != 0) {
            mXPaint.getTextBounds(mXDataString.get(0), 0, mXDataString.get(0).length(), mXTextRect);
            mXInternalOffest = (mWidth - mXOffest - mXDataString.size()
                    * mXTextRect.width()) / (mXDataString.size() + 1);
            //在绘制x轴的时候，需要首先知道y轴的高度
            int y = mYInternalOffest * mYDataString.size() + mYTextRect.height()
                    * mYDataString.size() + mYOffest;

            for (int i = 0; i < mXDataString.size(); i++) {
                String text = mXDataString.get(i);
                mXPaint.getTextBounds(text, 0, text.length(), mXTextRect);
                int x = mXOffest + mXInternalOffest * (i + 1) + mXTextRect.width() * i;
                canvas.drawText(text, x, y, mXPaint);
                xPoint[i] = x + mXTextRect.height() / 2;//坐标放在正中心
            }
        }
    }

    /**
     * Author: MrGao
     * CreateTime: 2017/12/24 13:31
     * MethodName:
     * Des：绘制Y轴,
     * 思路：
     * 两个Y轴值之间的偏移量=(控件的高度-Y轴的偏移量-n个文件的高度)/(n+1)
     * 为什么是n+1呢？
     * 比如是控件的高度-Y轴的偏移量-n个文件的高度=100，n=5;
     * 那么每个Item之间的间距为20，5个点，中间只有4个线段，
     * 那么第一个值得位置就是(xOffest,0)，就直接在布局的最上面了，不好看
     * 如果是n+1的话，
     * 最上面的那个y轴点就不紧贴着上面，而是留了一个mYInternalOffest的高度
     * Params：
     * Return:
     **/
    private void drawY(Canvas canvas) {
        yPoint = new int[mYDataString.size()];
        mYPaint.getTextBounds(mYDataString.get(0), 0, mYDataString.get(0).length(), mYTextRect);
        if (mYDataString.size() != 0) {
            mYInternalOffest = (mHeight - mYOffest - mYDataString.size() * mYTextRect.height())
                    / (mYDataString.size() + 1);
            for (int i = 0; i < mYDataString.size(); i++) {
                String text = mYDataString.get(i);
                mYPaint.getTextBounds(text, 0, text.length(), mYTextRect);
                int y = mYInternalOffest * (i + 1) + mYTextRect.height() * i;
                canvas.drawText(text, mXOffest, y, mYPaint);
                yPoint[i] = y - mYTextRect.height() / 2;//取正中心的点
            }

        }

    }

    /**
     * Author: MrGao
     * CreateTime: 2017/12/24 13:32
     * MethodName:
     * Des：绘制点和线
     * Params：
     * Return:
     **/
    private void drawPointLine(Canvas canvas) {

        if (mPointList.size() != 0) {
            for (int i = 0; i < mPointList.size(); i++) {
                Point point = mPointList.get(i);
                int x = xPoint[point.getxPoint()];
                int y = yPoint[point.getyPoint()];

                canvas.drawCircle(x, y, 10, mPointPaint);
                if (i > 0) {
                    Point point2 = mPointList.get(i - 1);
                    int x2 = xPoint[point2.getxPoint()];
                    int y2 = yPoint[point2.getyPoint()];
                    canvas.drawLine(x2, y2, x, y, mLinePaint);

                }
            }

        }

    }

    public void addAllXDataString(List<String> XDataString) {
        if (mXDataString != null) {
            mXDataString.clear();
        } else {
            mXDataString = new ArrayList<>();

        }
        mXDataString.addAll(XDataString);
        invalidate();
    }

    public void addAllYDataString(List<String> YDataString) {
        if (mYDataString != null) {
            mYDataString.clear();
        } else {
            mYDataString = new ArrayList<>();

        }
        //将数据反向存储
        for (int i = YDataString.size() - 1; i >= 0; i--) {
            mYDataString.add(YDataString.get(i));
        }
        invalidate();
    }

    /**
     * Author: MrGao
     * CreateTime: 2017/12/24 14:55
     * MethodName:
     * Des：设置要绘制的点
     * Params：
     * Return:
     **/
    public void addAllPointList(List<mrgao.com.mrgaoviews.model.Point> pointList) {
        mPointList.addAll(pointList);
        invalidate();
    }

    public void addPoint(mrgao.com.mrgaoviews.model.Point point) {
        if (!mPointList.contains(point)) {
            mPointList.add(point);
            invalidate();
        }


    }


}
