package mrgao.com.mrgaoviews.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import mrgao.com.mrgaoviews.R;
import mrgao.com.mrgaoviews.views.PanelCircleView;

public class PanelCircleActivity extends AppCompatActivity {

    PanelCircleView mPanelCircleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_circle);
        mPanelCircleView = (PanelCircleView) findViewById(R.id.panelView);

        mPanelCircleView.setPercent(50);
    }
}
