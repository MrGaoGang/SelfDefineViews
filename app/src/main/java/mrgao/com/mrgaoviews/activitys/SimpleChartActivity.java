package mrgao.com.mrgaoviews.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import mrgao.com.mrgaoviews.R;
import mrgao.com.mrgaoviews.model.Point;
import mrgao.com.mrgaoviews.views.SimpleChartView;

public class SimpleChartActivity extends AppCompatActivity {

    private SimpleChartView mSimpleChartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_chart);
        mSimpleChartView = (SimpleChartView) findViewById(R.id.simplechar);

        List<String> xDa = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            xDa.add("x" + (i + 1));
        }
        List<String> yDa = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            yDa.add("y" + (i + 1));
        }

        mSimpleChartView.addAllXDataString(xDa);
        mSimpleChartView.addAllYDataString(yDa);

        Point point = new Point(0, 3);
        Point point1 = new Point(1, 2);
        Point point2 = new Point(2, 4);
        Point point3 = new Point(3, 1);
        Point point4 = new Point(4, 0);
        List<Point> pointList = new ArrayList<>();
        pointList.add(point);
        pointList.add(point1);
        pointList.add(point2);
        pointList.add(point3);
        pointList.add(point4);

        mSimpleChartView.addAllPointList(pointList);


    }
}
