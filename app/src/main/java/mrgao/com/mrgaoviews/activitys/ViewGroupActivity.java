package mrgao.com.mrgaoviews.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import mrgao.com.mrgaoviews.R;

public class ViewGroupActivity extends AppCompatActivity {

    private Button mRadarBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_group);
        mRadarBtn = (Button) findViewById(R.id.radarViewBtn);
        mRadarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewGroupActivity.this, RadarMenuActivity.class));
            }
        });

        ((Button) findViewById(R.id.water)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewGroupActivity.this, WaterFlowActivity.class));
            }
        });
        ((Button) findViewById(R.id.selfDe)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewGroupActivity.this, FlowActivity.class));
            }
        });
        ((Button) findViewById(R.id.selfLin)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewGroupActivity.this, MyLinActivity.class));
            }
        });
    }
}
