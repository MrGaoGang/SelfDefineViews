package mrgao.com.mrgaoviews;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import mrgao.com.mrgaoviews.activitys.RadarMenuActivity;

public class MainActivity extends AppCompatActivity {


    private Button mRadarBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRadarBtn = (Button) findViewById(R.id.radarViewBtn);
        mRadarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RadarMenuActivity.class));
            }
        });
    }
}
