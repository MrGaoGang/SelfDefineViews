package mrgao.com.mrgaoviews.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import mrgao.com.mrgaoviews.R;
import mrgao.com.mrgaoviews.views.WaterProgressView;

public class WaterProgressActivity extends AppCompatActivity {

    WaterProgressView mWaterProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_progress);

        mWaterProgressView=(WaterProgressView)findViewById(R.id.waterProgress);

        mWaterProgressView.setProgress(30);

        ((Button)findViewById(R.id.pause)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWaterProgressView.pause();

            }
        });

        ((Button)findViewById(R.id.start)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWaterProgressView.restart();

            }
        });
    }
}
